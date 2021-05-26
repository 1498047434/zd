package cn.edkso.zd.utils;

import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class SaveFileUtil {

    /**
     *
     * @param file 表单上传的文件
     * @return uploadFileName 文件名按照日期加随机数重命名后的文件名
     */
    public static String rename(MultipartFile file){
        String uploadFileName = file.getOriginalFilename();
        String suffix = uploadFileName.substring(uploadFileName.lastIndexOf("."));
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
        uploadFileName = sf.format(new Date()) + (new Random().nextInt(99999)) + suffix;
        return uploadFileName;
    }

    /**
     *
     * @param path 文件要保存的相对路径
     * @param file 表单上传的文件
     * @return filePath 访问文件的相对路径
     * @throws IOException
     */
    public static String saveFile(String path, MultipartFile file) throws IOException {
        //保存文件
        File classpath = new File(ResourceUtils.getURL("classpath:").getPath());
        if(!classpath.exists()) {
            classpath.mkdirs();
        }
        File upload = new File(classpath.getAbsolutePath(),"static" + path);
        if(!upload.exists()) {
            upload.mkdirs();
        }

        String uploadFileName = rename(file);
        File save = new File(upload,uploadFileName);

        file.transferTo(save);
        //FileCopyUtils.copy(file.getBytes(),save);   //和上面那个方法选一个用

        return path + "/" + uploadFileName;
    }
}
