package cn.edkso.zd.controller;

import cn.edkso.zd.VO.ResultVO;
import cn.edkso.zd.constant.ConfigDefault;
import cn.edkso.zd.entry.User;
import cn.edkso.zd.enums.ResultEnum;
import cn.edkso.zd.service.UserService;
import cn.edkso.zd.utils.MD5Utils;
import cn.edkso.zd.utils.ResultVOUtil;
import cn.edkso.zd.utils.ServletUtils;
import cn.edkso.zd.utils.TokenUtils;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Api(tags = "用户模块")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private RedisTemplate redisTemplate;

    @ApiOperation(value = "学生登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username" ,value = "用户账号", required = true),
            @ApiImplicitParam(name = "password" ,value = "用户密码", required = true),
    })
    @PostMapping("login")
    public ResultVO login(String username, String password, HttpServletRequest request) {
        //1. 通过token，从redis获取用户
        String token = request.getHeader(ConfigDefault.USER_TOKEN_NAME);
        User user;

        if (StringUtils.isNotBlank(token)) {
            user = (User) redisTemplate.opsForValue().get(token);
            if (user != null) {
                Map<String, Object> map = new HashMap<>();
                map.put(ConfigDefault.USER_TOKEN_NAME, token);
                map.put("user", user);
                return ResultVOUtil.success(map);
            }
        }

        //2. 从db数据库获取用户
        user = userService.login(username, password);
        if (user != null) {
            //3. 生成token
            token = TokenUtils.token(username, password);
            if (token == null){
                return ResultVOUtil.error(ResultEnum.LOGIN_ERROR);
            }
            //4. 把（token，用户）存储到redis
            redisTemplate.opsForValue().set(token, user);

            //5. 返回
            Map<String, Object> map = new HashMap<>();
            map.put(ConfigDefault.USER_TOKEN_NAME, token);
            map.put("user", user);
            return ResultVOUtil.success(map);
        }
        return ResultVOUtil.error(ResultEnum.LOGIN_ERROR);
    }


    @ApiOperation(value = "用户注册")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username" ,value = "用户账号", required = true),
            @ApiImplicitParam(name = "password" ,value = "用户密码", required = true),
            @ApiImplicitParam(name = "name" ,value = "用户姓名", required = true)
    })
    @PostMapping("register")
    public ResultVO register(String username, String password, String name){
        User user = userService.register(username,password,name);
        if (user != null){
            return ResultVOUtil.success(user);
        }
        return ResultVOUtil.error(ResultEnum.REGISTER_ERROR);
    }

    @ApiOperation(value = "用户修改")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "oldPassword" ,value = "旧账号密码", required = false),
            @ApiImplicitParam(name = "password" ,value = "修改的密码", required = false),
            @ApiImplicitParam(name = "name" ,value = "用户姓名", required = false),
            @ApiImplicitParam(name = "state" ,value = "用户状态", required = false),
    })
    @ApiOperationSupport(ignoreParameters = {"id","username","createTime","updateTime"})
    @PostMapping("update")
    public ResultVO update(User user, String oldPassword){
        //1. 通过token，从redis获取用户
        String access_token = ServletUtils.getRequest().getHeader(ConfigDefault.USER_TOKEN_NAME);
        User oldUser = (User) redisTemplate.opsForValue().get(access_token);
        if(oldUser == null){
            return ResultVOUtil.error(ResultEnum.NOT_LOGGED_IN); //没有登录
        }

        if (!MD5Utils.md5(oldPassword).equals(oldUser.getPassword())){
            return ResultVOUtil.error(ResultEnum.OLD_PASWORD_ERROR); //原始密码错误
        }

        if (StringUtils.isNotBlank(user.getPassword())){
            oldUser.setPassword(MD5Utils.md5(user.getPassword()));
        }
        if(user.getState() != null){
            oldUser.setState(user.getState());
        }

        if (StringUtils.isNotBlank(user.getName())){
            oldUser.setName(user.getName());
        }

        User resUser = userService.update(oldUser);
        if (resUser != null){
            return ResultVOUtil.success(resUser);
        }
        return ResultVOUtil.error(ResultEnum.REGISTER_ERROR);
    }

    @ApiOperation(value = "用户认证上传")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "idcard" ,value = "旧账号密码", required = false),
            @ApiImplicitParam(name = "" ,value = "修改的密码", required = false),
            @ApiImplicitParam(name = "name" ,value = "用户姓名", required = false),
            @ApiImplicitParam(name = "state" ,value = "用户状态", required = false),
    })
    @ApiOperationSupport(ignoreParameters = {"id","username","createTime","updateTime"})
    @PostMapping("update")
    public ResultVO update(User user, String oldPassword){
        //1. 通过token，从redis获取用户
        String access_token = ServletUtils.getRequest().getHeader(ConfigDefault.USER_TOKEN_NAME);
        User oldUser = (User) redisTemplate.opsForValue().get(access_token);
        if(oldUser == null){
            return ResultVOUtil.error(ResultEnum.NOT_LOGGED_IN); //没有登录
        }

        if (!MD5Utils.md5(oldPassword).equals(oldUser.getPassword())){
            return ResultVOUtil.error(ResultEnum.OLD_PASWORD_ERROR); //原始密码错误
        }

        if (StringUtils.isNotBlank(user.getPassword())){
            oldUser.setPassword(MD5Utils.md5(user.getPassword()));
        }
        if(user.getState() != null){
            oldUser.setState(user.getState());
        }

        if (StringUtils.isNotBlank(user.getName())){
            oldUser.setName(user.getName());
        }

        User resUser = userService.update(oldUser);
        if (resUser != null){
            return ResultVOUtil.success(resUser);
        }
        return ResultVOUtil.error(ResultEnum.REGISTER_ERROR);
    }
}
