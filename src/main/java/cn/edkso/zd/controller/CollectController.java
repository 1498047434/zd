package cn.edkso.zd.controller;

import cn.edkso.zd.VO.ResultVO;
import cn.edkso.zd.entry.Collect;
import cn.edkso.zd.enums.ResultEnum;
import cn.edkso.zd.service.CollectService;
import cn.edkso.zd.utils.ResultVOUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "收藏模块")
@RestController
@RequestMapping("/collect")
public class CollectController {

    @Autowired
    private CollectService collectService;

    @ApiOperation(value = "增加收藏")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "landId" ,value = "土地id", required = true),
            @ApiImplicitParam(name = "userId" ,value = "用户id", required = true),
    })
    @PostMapping("add")
    public ResultVO add(Collect collect){

        Collect resCollect = collectService.add(collect);
        if (resCollect != null){
            return ResultVOUtil.success(resCollect);
        }
        return ResultVOUtil.error(ResultEnum.PARAMS_ERROR_OR_SYSTEM_EXCEPTION);
    }

    @ApiOperation(value = "删除收藏-通过土地id")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "landId" ,value = "土地id", required = true),
    })
    @PostMapping("delByLandId")
    public ResultVO delByLandId(Integer landId){
        try {
            collectService.delByLandId(landId);
        }catch (Exception e){
            return ResultVOUtil.error(ResultEnum.PARAMS_ERROR_OR_SYSTEM_EXCEPTION);
        }
        return ResultVOUtil.success();
    }

    @ApiOperation(value = "删除收藏-id")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id" ,value = "id", required = true),
    })
    @PostMapping("delById")
    public ResultVO delById(Integer id){
        try {
            collectService.delById(id);
        }catch (Exception e){
            return ResultVOUtil.error(ResultEnum.PARAMS_ERROR_OR_SYSTEM_EXCEPTION);
        }
        return ResultVOUtil.success();
    }
}
