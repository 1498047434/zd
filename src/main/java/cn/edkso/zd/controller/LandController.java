package cn.edkso.zd.controller;

import cn.edkso.zd.VO.ResultVO;
import cn.edkso.zd.constant.ConfigDefault;
import cn.edkso.zd.entry.Admin;
import cn.edkso.zd.entry.Land;
import cn.edkso.zd.entry.User;
import cn.edkso.zd.enums.ResultEnum;
import cn.edkso.zd.service.LandService;
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

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Api(tags = "土地模块")
@RestController
@RequestMapping("/land")
public class LandController {

    @Autowired
    private LandService landService;
    @Autowired
    private RedisTemplate redisTemplate;

    @ApiOperation(value = "获取土地列表-分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page" ,value = "当前页数", required = true),
            @ApiImplicitParam(name = "limit" ,value = "每页限制数", required = true),
            @ApiImplicitParam(name = "address" ,value = "土地地址", required = false),
            @ApiImplicitParam(name = "classify" ,value = "土地分类", required = false),
            @ApiImplicitParam(name = "contractPeriodStart" ,value = "土地承包年限最低", required = false),
            @ApiImplicitParam(name = "contractPeriodEnd" ,value = "土地承包年限最高", required = false),
            @ApiImplicitParam(name = "isBidding" ,value = "是否开始竞价", required = false),
            @ApiImplicitParam(name = "isCollect" ,value = "是否收藏", required = false),
    })
    @ApiOperationSupport(
            ignoreParameters = {"id","state","area","price", "contractPeriod","introduction",
                    "adminId","adminName", "userId","userName","bidPrice","agreementImg"}
    )
    @GetMapping("listByPageForUser")
    public ResultVO listByPageForUser(Integer page, Integer limit, Land land,
                               Integer contractPeriodStart, Integer contractPeriodEnd){

        //1. 通过token，从redis获取用户
        String access_token = ServletUtils.getRequest().getHeader(ConfigDefault.USER_TOKEN_NAME);
        User redisUser = (User) redisTemplate.opsForValue().get(access_token);
        if(redisUser == null){
            return ResultVOUtil.error(ResultEnum.NOT_LOGGED_IN); //没有登录
        }

        ThreadLocal<Map<String, Object>> tl = new ThreadLocal();
        Map<String, Object> map = new HashMap<>();
        map.put("contractPeriodStart", contractPeriodStart);
        map.put("contractPeriodEnd", contractPeriodEnd);
        map.put("user", redisUser);
        tl.set(map);

        Page<Land> landPage = landService.listByPage(page, limit,land,tl);
        return ResultVOUtil.success(landPage);
    }


    @ApiOperation(value = "获取土地列表-分页(管理员获取自己的)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page" ,value = "当前页数", required = true),
            @ApiImplicitParam(name = "limit" ,value = "每页限制数", required = true),
            @ApiImplicitParam(name = "address" ,value = "土地地址", required = false),
            @ApiImplicitParam(name = "classify" ,value = "土地分类", required = false),
            @ApiImplicitParam(name = "contractPeriodStart" ,value = "土地承包年限最低", required = false),
            @ApiImplicitParam(name = "contractPeriodEnd" ,value = "土地承包年限最高", required = false),
            @ApiImplicitParam(name = "isBidding" ,value = "是否开始竞价", required = false),
            @ApiImplicitParam(name = "0.." ,value = "是否收藏", required = false),
    })
    @ApiOperationSupport(
            ignoreParameters = {"id","state","area","price", "contractPeriod","introduction",
                    "adminId","adminName", "userId","userName","bidPrice","agreementImg","isCollect"}
    )
    @GetMapping("listByPageForAdmin")
    public ResultVO listByPageForAdmin(Integer page, Integer limit, Land land,
                                      Integer contractPeriodStart, Integer contractPeriodEnd){

        //1. 通过token，从redis获取用户
        String access_token = ServletUtils.getRequest().getHeader(ConfigDefault.ADMIN_TOKEN_NAME);
        Admin redisAdmin = (Admin) redisTemplate.opsForValue().get(access_token);
        if(redisAdmin == null){
            return ResultVOUtil.error(ResultEnum.NOT_LOGGED_IN); //没有登录
        }

        ThreadLocal<Map<String, Object>> tl = new ThreadLocal();
        Map<String, Object> map = new HashMap<>();
        map.put("contractPeriodStart", contractPeriodStart);
        map.put("contractPeriodEnd", contractPeriodEnd);
        map.put("admin", redisAdmin);
        tl.set(map);

        Page<Land> landPage = landService.listByPage(page, limit,land,tl);
        return ResultVOUtil.success(landPage);
    }


    @ApiOperation(value = "增加土地记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "address" ,value = "土地地址", required = true),
            @ApiImplicitParam(name = "classify" ,value = "土地分类", required = true),
            @ApiImplicitParam(name = "area" ,value = "土地面积", required = true),
            @ApiImplicitParam(name = "price" ,value = "土地价格（单位亩）", required = true),
            @ApiImplicitParam(name = "contractPeriod" ,value = "土地承包年限", required = true),
            @ApiImplicitParam(name = "introduction" ,value = "土地简洁", required = true),
    })
    @ApiOperationSupport(
            ignoreParameters = {"id","userId","bidPrice","bidUserId", "bidUserName","userName",
                    "adminId","adminName", "isBidding","state","agreementImg","isCollect"}
    )
    @GetMapping("add")
    public ResultVO add(Land land){

        //1. 通过token，从redis获取用户
        String access_token = ServletUtils.getRequest().getHeader(ConfigDefault.ADMIN_TOKEN_NAME);
        Admin redisAdmin = (Admin) redisTemplate.opsForValue().get(access_token);
        if(redisAdmin == null){
            return ResultVOUtil.error(ResultEnum.NOT_LOGGED_IN); //没有登录
        }

        land.setState(1);
        land.setAdminId(redisAdmin.getId());
        land.setAdminName(redisAdmin.getName());
        land.setIsBidding(0);

        Land resLand = landService.add(land);
        if (resLand != null){
            return ResultVOUtil.success(resLand);
        }
        return ResultVOUtil.error(ResultEnum.PARAMS_ERROR_OR_SYSTEM_EXCEPTION);
    }

    @ApiOperation(value = "修改土地记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id" ,value = "土地id", required = true),
            @ApiImplicitParam(name = "address" ,value = "土地地址", required = true),
            @ApiImplicitParam(name = "classify" ,value = "土地分类", required = true),
            @ApiImplicitParam(name = "area" ,value = "土地面积", required = true),
            @ApiImplicitParam(name = "price" ,value = "土地价格（单位亩）", required = true),
            @ApiImplicitParam(name = "contractPeriod" ,value = "土地承包年限", required = true),
            @ApiImplicitParam(name = "introduction" ,value = "土地简洁", required = true),
            @ApiImplicitParam(name = "isBidding" ,value = "土地竞价（1竞价中,0未竟价，2竞价结束）", required = true),
            @ApiImplicitParam(name = "state" ,value = "土地状态（1正常,2已承包完）", required = true),

    })
    @ApiOperationSupport(
            ignoreParameters = {"userId","bidPrice","bidUserId", "bidUserName","userName",
                    "adminId","adminName","agreementImg","isCollect"}
    )
    @GetMapping("update")
    public ResultVO update(Land land){

        //1. 通过token，从redis获取用户
        String access_token = ServletUtils.getRequest().getHeader(ConfigDefault.ADMIN_TOKEN_NAME);
        Admin redisAdmin = (Admin) redisTemplate.opsForValue().get(access_token);
        if(redisAdmin == null){
            return ResultVOUtil.error(ResultEnum.NOT_LOGGED_IN); //没有登录
        }

        Land resLand = landService.update(land);
        if (resLand != null){
            return ResultVOUtil.success(resLand);
        }
        return ResultVOUtil.error(ResultEnum.PARAMS_ERROR_OR_SYSTEM_EXCEPTION);
    }

    @ApiOperation(value = "修改土地记录（参与竞价）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id" ,value = "土地id", required = true),
            @ApiImplicitParam(name = "bidPrice" ,value = "土地竞价（单位亩）", required = true),
    })
    @ApiOperationSupport(
            ignoreParameters = {"address","classify","area","price", "contractPeriod","isBidding",
                    "state","userId","userName","introduction","bidUserId","bidUserName",
                    "adminId","adminName","agreementImg","isCollect"}
    )
    @GetMapping("bidding")
    public ResultVO bidding(Land land){

        //1. 通过token，从redis获取用户
        String access_token = ServletUtils.getRequest().getHeader(ConfigDefault.USER_TOKEN_NAME);
        User redisUser = (User) redisTemplate.opsForValue().get(access_token);
        if(redisUser == null){
            return ResultVOUtil.error(ResultEnum.NOT_LOGGED_IN); //没有登录
        }

        land.setBidUserName(redisUser.getName());
        land.setBidUserId(redisUser.getId());
        Land resLand = landService.update(land);
        if (resLand != null){
            return ResultVOUtil.success(resLand);
        }
        return ResultVOUtil.error(ResultEnum.PARAMS_ERROR_OR_SYSTEM_EXCEPTION);
    }

    @ApiOperation(value = "修改土地记录（竞价结束）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id" ,value = "土地id", required = true),
    })
    @ApiOperationSupport(
            ignoreParameters = {"address","classify","area","price", "contractPeriod","isBidding",
                    "state","userId","userName","introduction","bidUserId","bidUserName",
                    "adminId","adminName","bidPrice","agreementImg","isCollect"}
    )
    @GetMapping("isBided")
    public ResultVO isBided(Land land){
        land.setIsBidding(2);
        Land resLand = landService.update(land);
        if (resLand != null){
            return ResultVOUtil.success(resLand);
        }
        return ResultVOUtil.error(ResultEnum.PARAMS_ERROR_OR_SYSTEM_EXCEPTION);
    }

    @ApiOperation(value = "修改土地记录（上传合同）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id" ,value = "土地id", required = true),
            @ApiImplicitParam(name = "filePath" ,value = "合同图片路径", required = true),
    })
    @ApiOperationSupport(
            ignoreParameters = {"address","classify","area","price", "contractPeriod","isBidding",
                    "state","userId","userName","introduction","bidUserId","bidUserName",
                    "adminId","adminName","bidPrice","agreementImg","isCollect"}
    )
    @GetMapping("isBided")
    public ResultVO isBided(Land land, String filePath){
        land.setAgreementImg(filePath);
        Land resLand = landService.update(land);
        if (resLand != null){
            return ResultVOUtil.success(resLand);
        }
        return ResultVOUtil.error(ResultEnum.PARAMS_ERROR_OR_SYSTEM_EXCEPTION);
    }


    @ApiOperation(value = "修改土地记录（确定购买）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id" ,value = "土地id", required = true),
    })
    @ApiOperationSupport(
            ignoreParameters = {"address","classify","area","price", "contractPeriod","isBidding",
                    "state","userId","userName","introduction","bidUserId","bidUserName",
                    "adminId","adminName","bidPrice","agreementImg","isCollect"}
    )
    @GetMapping("buy")
    public ResultVO buy(Land land){

        //1. 通过token，从redis获取用户
        String access_token = ServletUtils.getRequest().getHeader(ConfigDefault.USER_TOKEN_NAME);
        User redisUser = (User) redisTemplate.opsForValue().get(access_token);
        if(redisUser == null){
            return ResultVOUtil.error(ResultEnum.NOT_LOGGED_IN); //没有登录
        }

        if (land.getIsBidding() != 2){
            return ResultVOUtil.error(ResultEnum.BIDDING_IS_NOT_END); //竞价未结束
        }

        if (land.getBidUserId() != redisUser.getId()){
            return ResultVOUtil.error(ResultEnum.BIDDING_IS_NOT_FOR_YOU); //竞价最高者不是您！
        }

        land.setIsBidding(2);
        land.setUserId(redisUser.getId());
        land.setUserName(redisUser.getName());

        Land resLand = landService.update(land);
        if (resLand != null){
            return ResultVOUtil.success(resLand);
        }
        return ResultVOUtil.error(ResultEnum.PARAMS_ERROR_OR_SYSTEM_EXCEPTION);
    }

    @ApiOperation(value = "删除土地记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id" ,value = "id", required = true),
    })
    @PostMapping("delById")
    public ResultVO delById(Integer id){
        try {
            landService.delById(id);
        }catch (Exception e){
            return ResultVOUtil.error(ResultEnum.PARAMS_ERROR_OR_SYSTEM_EXCEPTION);
        }
        return ResultVOUtil.success();
    }
}
