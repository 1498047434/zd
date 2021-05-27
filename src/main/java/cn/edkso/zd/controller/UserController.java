package cn.edkso.zd.controller;

import cn.edkso.zd.VO.ResultVO;
import cn.edkso.zd.constant.ConfigDefault;
import cn.edkso.zd.entry.User;
import cn.edkso.zd.enums.ResultEnum;
import cn.edkso.zd.service.UserService;
import cn.edkso.zd.utils.*;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
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

    @ApiOperation(value = "用户登录")
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
        if (user.getState() == 0){
            return ResultVOUtil.error(ResultEnum.LOGIN_ERROR);
        }
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


    @ApiOperation(value = "用户注册") //注册的时候就需要上传身份证号和照片
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username" ,value = "用户账号", required = true),
            @ApiImplicitParam(name = "password" ,value = "用户密码", required = true),
            @ApiImplicitParam(name = "name" ,value = "用户姓名", required = true),
            @ApiImplicitParam(name = "idcard" ,value = "身份证号", required = true),
            @ApiImplicitParam(name = "idcardImg" ,value = "身份证图片", required = true),
    })
    @ApiOperationSupport(
            ignoreParameters = {"id","state"}
    )
    @PostMapping("register")
    public ResultVO register(User user){

        try {
            User resUser = userService.register(user);
            if (resUser != null){
                return ResultVOUtil.success(resUser);
            }
        }catch (Exception e){
            return ResultVOUtil.error(e.getMessage());
        }

        return ResultVOUtil.error(ResultEnum.REGISTER_ERROR);
    }

    @ApiOperation(value = "用户修改")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "oldPassword" ,value = "旧账号密码", required = false),
            @ApiImplicitParam(name = "password" ,value = "修改的密码", required = false),
            @ApiImplicitParam(name = "name" ,value = "用户姓名", required = false),
    })
    @ApiOperationSupport(ignoreParameters = {"id","username","state","idcard","idcardImg"})
    @PostMapping("update")
    public ResultVO update(User user, String oldPassword){
        //1. 通过token，从redis获取用户
        String access_token = ServletUtils.getRequest().getHeader(ConfigDefault.USER_TOKEN_NAME);
        User redisUser = (User) redisTemplate.opsForValue().get(access_token);
        if(redisUser == null){
            return ResultVOUtil.error(ResultEnum.NOT_LOGGED_IN); //没有登录
        }

        if (!MD5Utils.md5(oldPassword).equals(redisUser.getPassword())){
            return ResultVOUtil.error(ResultEnum.OLD_PASWORD_ERROR); //原始密码错误
        }

        user.setId(redisUser.getId());

        User resUser = userService.update(user);
        if (resUser != null){
            return ResultVOUtil.success(resUser);
        }
        return ResultVOUtil.error(ResultEnum.REGISTER_ERROR);
    }


}
