package com.taskonline.sys.controller;

import com.taskonline.anno.UserValidatorAnnotation;
import com.taskonline.sys.pojo.User;
import com.taskonline.util.MessageMap;
import com.taskonline.sys.service.common.UserServer;
import com.taskonline.util.InfoString;
import com.taskonline.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Map;

@Controller
@Validated
public class UserController {

    @Autowired
    UserServer userServer;

    @Autowired
    InfoString infoString;

    @Value("${iconDir}")
    String iconDirPath = "";


    @GetMapping(path = "/")
    public String index(@RequestParam(value = "msg", required = false)String msg, Model model){
        if(!StringUtils.isEmpty(msg))
            model.addAttribute(MessageMap.MSG, msg);
        UserUtil.setCurrentUserToModelFromSecurity(model);
        if (model.containsAttribute("stu"))
            return "stu/index";
        else if (model.containsAttribute("tea")){
            return "tea/index";
        }
        return "fail";
    }

    /*管理员登录的GET和POST*/
    @GetMapping(path = {"/admin"})
    public String administratorGet(Model model) {
        return "login";
    }

    @PostMapping(path = {"/admin"})
    public String administratorLogin() {
        return "success";
    }

    /*教师和学生登录的GET和POST*/
    @GetMapping(path = "/login")
    public String loginGet(HttpServletRequest rs, @SessionAttribute(value = "SPRING_SECURITY_LAST_EXCEPTION", required = false) Exception e, Model model) {
        if(e != null){
            rs.getSession().removeAttribute("SPRING_SECURITY_LAST_EXCEPTION");
            String msg =e.getMessage();
            if(!StringUtils.isEmpty(msg))
                model.addAttribute(MessageMap.MSG, msg);
        }
        return "login";
    }


    /*教师和学生注册的GET和POST*/
    @GetMapping(path = "/register")
    public String registerGet(Model model) {
        return "register";
    }

    @PostMapping(path = "/register")
    public String registerPost(@UserValidatorAnnotation(group = UserValidatorAnnotation.LoginGroup.class) User user,
                               Model model) {
        if(!StringUtils.isEmpty(user.getMsg())){
            model.addAttribute(MessageMap.MSG, user.getMsg());
            return registerGet(model);
        }
        Map<String, Object> map;
        if ("stu".equals(user.getType())) {
            map = userServer.studentRegister(user.getId(), user.getName(), user.getPassword());
        } else {
            map = userServer.teacherRegister(user.getId(), user.getName(), user.getPassword());
        }
        if (map == null || map.isEmpty()) {
            model.addAttribute(MessageMap.MSG, infoString.NULL_MAP);
            return registerGet(model);
        }
        if (map.containsKey(MessageMap.MSG)) {
            model.addAttribute(MessageMap.MSG, map.get(MessageMap.MSG));
            return registerGet(model);
        }

        return "redirect:login";
    }



    /*--------------由于rs目录不能用来上传头像，所以使用一个Mapping来获取头像，不传递参数，通过security中存储的信息获取-----------*/
    @GetMapping("/getIcon/{icon}")
    public void getIcon(@PathVariable String icon, HttpServletRequest request, HttpServletResponse response) throws IOException {
        try{
            FileInputStream hFile=new FileInputStream(iconDirPath+ File.separator+icon);
            int i=hFile.available();
            byte[] data =new byte[i];
            hFile.read(data);
            hFile.close();
            response.setContentType("image/*");
            OutputStream toClient=response.getOutputStream();
            toClient.write(data);
            toClient.close();
        }catch (IOException e){
            e.printStackTrace();
            PrintWriter toClient=response.getWriter();
            response.setContentType("text/html;charset=gb2312");
            toClient.write("无法打开图片");
            toClient.close();
        }
    }

}
