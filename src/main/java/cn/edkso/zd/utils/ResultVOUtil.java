package cn.edkso.zd.utils;


import cn.edkso.zd.VO.ResultVO;
import cn.edkso.zd.enums.ResultEnum;
import org.springframework.data.domain.Page;

import java.util.Date;


public class ResultVOUtil {

    /**
     * 返回失败消息【自定义消息】
     */
    public static ResultVO error(String msg){
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(-1);
        resultVO.setMsg(msg);
        resultVO.setTimestamp(new Date().getTime());
        return resultVO;
    }

    /**
     * 返回失败消息【自定义状态，自定义消息】
     */
    public static ResultVO error(Integer status, String msg){
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(-1);
        resultVO.setMsg(msg);
        resultVO.setTimestamp(new Date().getTime());
        return resultVO;
    }

    /**
     * 返回失败消息【根据ResultEnum 自定义状态，自定义消息】
     */
    public static ResultVO error(ResultEnum resultEnum){
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(-1);
        resultVO.setMsg(resultEnum.getMessage());
        resultVO.setTimestamp(new Date().getTime());
        return resultVO;
    }

    /**
     * 返回成功消息【自定义消息】
     */
    public static ResultVO success(String msg){
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(0);
        resultVO.setMsg(msg);
        resultVO.setTimestamp(new Date().getTime());
        return resultVO;
    }


    /**
     * 返回成功消息【success】
     * @param teacher
     * @param data
     */
    public static ResultVO success(){
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(0);
        resultVO.setMsg("success");
        resultVO.setTimestamp(new Date().getTime());
        return resultVO;
    }



    /**
     * 返回成功的消息【自定义消息，[data]】
     */
    public static ResultVO success(String msg,Object data){
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(0);
        resultVO.setMsg(msg);
        resultVO.setData(data);
        resultVO.setTimestamp(new Date().getTime());
        return resultVO;
    }



    /**
     * 返回成功的消息【success, [data]】
     */
    public static ResultVO success(Object data){
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(0);
        resultVO.setMsg("success");
        resultVO.setData(data);
        resultVO.setTimestamp(new Date().getTime());
        return resultVO;
    }

    /**
     * 返回成功的消息【success, [page ->(count, data)]】
     */
    public static ResultVO success(Page page){
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(0);
        resultVO.setMsg("success");
        resultVO.setCount(page.getTotalElements());
        resultVO.setData(page.getContent());
        resultVO.setTimestamp(new Date().getTime());
        return resultVO;
    }


}