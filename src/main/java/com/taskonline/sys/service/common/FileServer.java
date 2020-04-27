package com.taskonline.sys.service.common;

import com.taskonline.util.MessageMap;
import com.taskonline.util.InfoString;
import com.taskonline.util.RandomFileName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;

@Service
public class FileServer {
    @Value("${iconDir}")
    String iconDirPath = "";

    @Autowired
    RandomFileName randomFileName;

    @Autowired
    InfoString infoString;

    public Map<String, Object> iconUpload(MultipartFile file){
        Map<String,Object> map=new MessageMap(getClass());
        if(file==null || file.isEmpty()) {
            map.put(MessageMap.FILE, null);//没有上传
            return map;
        }
        //检查文件大小
        if(file.getSize() > 1024*1024*2) {
            map.put(MessageMap.MSG,"请上传文件小于2M的图片" );
            return map;
        }
        //检查是否是图片
        try{
            BufferedImage bi = ImageIO.read(file.getInputStream());
            if(bi == null){
                map.put(MessageMap.MSG,"请选择图片类型的文件上传");
                return map;
            }
        }catch (IOException ie){
            ie.printStackTrace();
            map.put(MessageMap.EXCEPTION, ie);
            return map;
        }
        return fileUpload(file, iconDirPath);
    }

    private Map<String, Object> fileUpload(MultipartFile file, String path){
        Map<String, Object> map = new MessageMap(getClass());
        if(file == null){
            map.put(MessageMap.MSG, "null file");
            return map;
        }
        String fileName = file.getOriginalFilename();
        String names[] = fileName.split("\\.");
        String suffix = "";
        if (names.length>1){
            suffix=names[names.length-1];
        }
        fileName = randomFileName.randomName(names[0]);
        fileName = path+"/"+fileName+ (suffix.equals("")?"" : ("."+suffix) );

        File newFile = new File(fileName);
        if(newFile.exists()){
            return this.fileUpload(file, path);
        }
        try {
            file.transferTo(newFile);
        } catch (IOException e) {
            e.printStackTrace();
            map.put(MessageMap.MSG, infoString.TRANSACTION_FILE_FAIL);
        }
        map.put(MessageMap.FILE, newFile);
        return map;
    }

}

