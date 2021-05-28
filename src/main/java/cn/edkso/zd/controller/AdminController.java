package cn.edkso.zd.controller;
import cn.edkso.zd.VO.ResultVO;
import cn.edkso.zd.constant.ConfigDefault;
import cn.edkso.zd.entry.Admin;
import cn.edkso.zd.enums.ResultEnum;
import cn.edkso.zd.service.AdminService;
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
import java.util.concurrent.TimeUnit;

@Api(tags = "管理员模块")
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;
    @Autowired
    private RedisTemplate redisTemplate;

    @ApiOperation(value = "管理员登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username" ,value = "管理员账号", required = true),
            @ApiImplicitParam(name = "password" ,value = "管理员密码", required = true),
    })
    @PostMapping("login")
    public ResultVO login(String username, String password, HttpServletRequest request) {
        //1. 通过token，从redis获取用户
        String token = request.getHeader(ConfigDefault.ADMIN_TOKEN_NAME);
        Admin admin;

        if (StringUtils.isNotBlank(token)) {
            admin = (Admin) redisTemplate.opsForValue().get(token);
            if (admin != null) {
                Map<String, Object> map = new HashMap<>();
                map.put(ConfigDefault.ADMIN_TOKEN_NAME, token);
                map.put("admin", admin);
                return ResultVOUtil.success(map);
            }
        }

        //2. 从db数据库获取用户
        admin = adminService.login(username, password);

        if (admin != null) {
            //3. 生成token
            token = TokenUtils.token(username, password);
            if (token == null){
                return ResultVOUtil.error(ResultEnum.LOGIN_ERROR);
            }
            //4. 把（token，用户）存储到redis
            redisTemplate.opsForValue().set(token, admin,8 , TimeUnit.HOURS);

            //5. 返回
            Map<String, Object> map = new HashMap<>();
            map.put(ConfigDefault.ADMIN_TOKEN_NAME, token);
            map.put("admin", admin);
            return ResultVOUtil.success(map);
        }
        return ResultVOUtil.error(ResultEnum.LOGIN_ERROR);
    }


    @ApiOperation(value = "添加管理员") //注册的时候就需要上传身份证号和照片
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username" ,value = "管理员账号", required = true),
            @ApiImplicitParam(name = "password" ,value = "管理员密码", required = true),
            @ApiImplicitParam(name = "name" ,value = "管理员姓名", required = true),
    })
    @ApiOperationSupport(
            ignoreParameters = {"id","state","grade"}
    )
    @PostMapping("add")
    public ResultVO add(Admin admin){

        admin.setState(1);
        admin.setGrade(2);

        try {
            Admin resAdmin = adminService.add(admin);
            if (resAdmin != null){
                return ResultVOUtil.success(resAdmin);
            }
        }catch (Exception e){
            return ResultVOUtil.error(e.getMessage());
        }

        return ResultVOUtil.error(ResultEnum.REGISTER_ERROR);
    }


    @ApiOperation(value = "管理员修改")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "oldPassword" ,value = "旧账号密码", required = false),
            @ApiImplicitParam(name = "password" ,value = "修改的密码", required = false),
            @ApiImplicitParam(name = "name" ,value = "管理员姓名", required = false),
    })
    @ApiOperationSupport(ignoreParameters = {"id","username","state","grade"})
    @PostMapping("update")
    public ResultVO update(Admin admin, String oldPassword){
        //1. 通过token，从redis获取用户
        String access_token = ServletUtils.getRequest().getHeader(ConfigDefault.ADMIN_TOKEN_NAME);
        Admin redisAdmin = (Admin) redisTemplate.opsForValue().get(access_token);
        if(redisAdmin == null){
            return ResultVOUtil.error(ResultEnum.NOT_LOGGED_IN); //没有登录
        }

        if (!MD5Utils.md5(oldPassword).equals(redisAdmin.getPassword())){
            return ResultVOUtil.error(ResultEnum.OLD_PASWORD_ERROR); //原始密码错误
        }

        admin.setId(redisAdmin.getId());

        Admin resAdmin = adminService.update(admin);
        if (resAdmin != null){
            return ResultVOUtil.success(resAdmin);
        }
        return ResultVOUtil.error(ResultEnum.REGISTER_ERROR);
    }
}
