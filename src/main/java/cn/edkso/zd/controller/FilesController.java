package cn.edkso.zd.controller;

import cn.edkso.zd.VO.ResultVO;
import cn.edkso.zd.constant.ConfigDefault;
import cn.edkso.zd.entry.User;
import cn.edkso.zd.enums.ResultEnum;
import cn.edkso.zd.utils.ResultVOUtil;
import cn.edkso.zd.utils.SaveFileUtil;
import cn.edkso.zd.utils.ServletUtils;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Api(tags = "文件模块")
@RestController
@RequestMapping("/files")
public class FilesController {

    @Autowired
    private RedisTemplate redisTemplate;

    @PostMapping("upload")
    public ResultVO upload(MultipartFile file){
        //1. 通过token，从redis获取用户
        String access_token = ServletUtils.getRequest().getHeader(ConfigDefault.USER_TOKEN_NAME);
        User oldUser = (User) redisTemplate.opsForValue().get(access_token);
        if(oldUser == null){
            return ResultVOUtil.error(ResultEnum.NOT_LOGGED_IN); //没有登录
        }

        try {
            String filePath = SaveFileUtil.saveFile("/upload/img", file);
            Map<String, String> map = new HashMap<>();
            map.put("filePath",filePath);
            return ResultVOUtil.success(map);
        } catch (IOException e) {
            return ResultVOUtil.error(ResultEnum.UPLOAD_ERROR);
        }
    }
}
