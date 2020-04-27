package com.taskonline.sys.controller;

import com.taskonline.sys.service.common.FileServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Controller
public class UploadController {

    @Autowired
    FileServer fileServer;

    @RequestMapping(path = {"/image/upload"}, method = {RequestMethod.POST})
    public String imageUpload(@RequestParam("file") MultipartFile file, Model model){
        Map map = fileServer.iconUpload(file);
        if(map.containsKey("msg")){
            model.addAttribute("msg", map.get("msg"));
            return "fail";
        }
        String newFileName = (String) map.get("fileName");
        return "success";
    }

    @RequestMapping(path = {"/image"}, method = {RequestMethod.GET})
    public String image(){
        return "iconUpload";
    }
}
