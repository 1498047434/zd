package cn.edkso.zd.controller;

import cn.edkso.zd.VO.ResultVO;
import cn.edkso.zd.constant.ConfigDefault;
import cn.edkso.zd.entry.Admin;
import cn.edkso.zd.entry.Report;
import cn.edkso.zd.entry.User;
import cn.edkso.zd.enums.ResultEnum;
import cn.edkso.zd.service.ReportService;
import cn.edkso.zd.utils.ResultVOUtil;
import cn.edkso.zd.utils.ServletUtils;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Api(tags = "举报模块")
@RestController
@RequestMapping("/report")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @Autowired
    private RedisTemplate redisTemplate;

    @ApiOperation(value = "获取举报列表-分页-User")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page" ,value = "当前页数", required = true),
            @ApiImplicitParam(name = "limit" ,value = "每页限制数", required = true),
    })
    @GetMapping("listByPageForUser")
    public ResultVO listByPageForUser(Integer page, Integer limit){
        //1. 通过token，从redis获取用户
        String access_token = ServletUtils.getRequest().getHeader(ConfigDefault.USER_TOKEN_NAME);
        User redisUser = (User) redisTemplate.opsForValue().get(access_token);
        if(redisUser == null){
            return ResultVOUtil.error(ResultEnum.NOT_LOGGED_IN); //没有登录
        }

        ThreadLocal<Map<String, Object>> tl = new ThreadLocal();
        Map<String, Object> map = new HashMap<>();
        map.put("user", redisUser);
        tl.set(map);

        Page<Report> reportPage = reportService.listByPage(page, limit,tl);
        return ResultVOUtil.success(reportPage);
    }

    @ApiOperation(value = "获取举报列表-分页-Admin")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page" ,value = "当前页数", required = true),
            @ApiImplicitParam(name = "limit" ,value = "每页限制数", required = true),
    })
    @GetMapping("listByPageForAdmin")
    public ResultVO listByPageForAdmin(Integer page, Integer limit){
        //1. 通过token，从redis获取用户
        String access_token = ServletUtils.getRequest().getHeader(ConfigDefault.ADMIN_TOKEN_NAME);
        Admin redisAdmin = (Admin) redisTemplate.opsForValue().get(access_token);
        if(redisAdmin == null){
            return ResultVOUtil.error(ResultEnum.NOT_LOGGED_IN); //没有登录
        }

        ThreadLocal<Map<String, Object>> tl = new ThreadLocal();
        Map<String, Object> map = new HashMap<>();
        map.put("admin", redisAdmin);
        tl.set(map);

        Page<Report> reportPage = reportService.listByPage(page, limit,tl);
        return ResultVOUtil.success(reportPage);
    }

    @ApiOperation(value = "增加举报记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "landId" ,value = "土地Id", required = true),
            @ApiImplicitParam(name = "landAddress" ,value = "土地地址", required = true),
            @ApiImplicitParam(name = "landClassify" ,value = "土地分类", required = true),
            @ApiImplicitParam(name = "adminId" ,value = "管理员id", required = true),
            @ApiImplicitParam(name = "adminName" ,value = "管理员名字", required = true),
    })
    @ApiOperationSupport(
            ignoreParameters = {"id","userId","userName","state"}
    )
    @PostMapping("add")
    public ResultVO add(Report report){
        //1. 通过token，从redis获取用户
        String access_token = ServletUtils.getRequest().getHeader(ConfigDefault.USER_TOKEN_NAME);
        User redisUser = (User) redisTemplate.opsForValue().get(access_token);
        if(redisUser == null){
            return ResultVOUtil.error(ResultEnum.NOT_LOGGED_IN); //没有登录
        }

        report.setUserId(redisUser.getId());
        report.setUserName(redisUser.getUsername());
        report.setState(0); //0,未处理，1已经处理

        Report resReport = reportService.add(report);
        if (resReport != null){
            return ResultVOUtil.success(resReport);
        }
        return ResultVOUtil.error(ResultEnum.PARAMS_ERROR_OR_SYSTEM_EXCEPTION);
    }

    @ApiOperation(value = "修改举报记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id" ,value = "举报id", required = true),
    })
    @ApiOperationSupport(
            ignoreParameters = {"landId","landAddress","landClassify", "userId","userName",
                    "adminId","adminName","state","isCollect"}
    )

    @GetMapping("update")
    public ResultVO update(Report report){
        //1. 通过token，从redis获取用户
        String access_token = ServletUtils.getRequest().getHeader(ConfigDefault.ADMIN_TOKEN_NAME);
        Admin redisAdmin = (Admin) redisTemplate.opsForValue().get(access_token);
        if(redisAdmin == null){
            return ResultVOUtil.error(ResultEnum.NOT_LOGGED_IN); //没有登录
        }

        report.setState(1);//举报状态（0未处理,1已经处理）

        Report resReport = reportService.update(report);
        if (resReport != null){
            return ResultVOUtil.success(resReport);
        }
        return ResultVOUtil.error(ResultEnum.PARAMS_ERROR_OR_SYSTEM_EXCEPTION);
    }
}
