package com.taskonline.sys.controller.tea;

import com.taskonline.sys.entity.Student;
import com.taskonline.sys.entity.Teacher;
import com.taskonline.sys.mapper.TeacherMapper;
import com.taskonline.sys.pojo.User;
import com.taskonline.sys.service.common.FileServer;
import com.taskonline.sys.service.common.UserServer;
import com.taskonline.util.InfoString;
import com.taskonline.util.MessageMap;
import com.taskonline.util.PasswordChange;
import com.taskonline.util.UserUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Map;

/**
 * @program: taskonline
 * @description: 教师信息controller
 * @author: qwer
 * @create: 2020-04-03 12:24
 */
@RequestMapping("/tea/info")
@Controller("teaInfoController")
public class InfoController {
    @Autowired
    FileServer fileServer;
    @Autowired
    UserServer userServer;
    @Autowired
    InfoString infoString;
    @Autowired
    PasswordChange passwordChange;
    @Autowired
    TeacherMapper teacherMapper;

    /*
     *@Description: get获取教师信息页面，如果不是教师用户登录，那么重定向到/login
     *@Author: qwer
     *@date: 2020/4/3
     */
    @GetMapping("")
    public String getInfo(Model model){
        UserUtil.setCurrentUserToModelFromSecurity(model);
        if(!model.containsAttribute("tea")){
            return "redirect:/login";
        }
        return "tea/info";
    }

    /*
     *@Description: 修改教师信息。如果当前用户不是教师，那么重定向到。修改信息的主键和教师id由当前用户提供，不依赖提交的数据
     *@Author: qwer
     *@date: 2020/4/3
     */
    @PostMapping("")
    public String changeInfo(@RequestParam(value = "file", required = false) MultipartFile mfile, Teacher teacher, Model model){
        Map<String, Object> map=fileServer.iconUpload(mfile);
        if(map.containsKey(MessageMap.MSG)){
            model.addAttribute(MessageMap.MSG, map.get(MessageMap.MSG));
            return getInfo(model);
        }
        File file= (File) map.get(MessageMap.FILE);
        String fileName;
        if (file == null){
            fileName=null;
        }else
            fileName=file.getName();
        User c=UserUtil.getCurrentUserFromSecurity();
        if ((c == null) || !(c instanceof Teacher))
            return "redirect:/login";
        Teacher newTea=new Teacher();
        BeanUtils.copyProperties(c,newTea);
       /*使用基于复制currentTea生成的newTea作为修改的参考对象，而不是直接通过form映射的teacher，防止form提交了不能修改的属性
            不直接修改currentTea，因为这个对象同时被security存储。如果修改这个对象，不再登录前获取的用户信息都可能是修改失败的，而不是真正的用户信息
        */
        newTea.setIcon(fileName);
        newTea.setTname(teacher.getTname());
        /*-------------------------------------------------------------------------------------------*/
        map=userServer.teaInfoChange(newTea);
        if(map.containsKey(MessageMap.MSG)){
            model.addAttribute(MessageMap.MSG, map.get(MessageMap.MSG));
            model.addAttribute(MessageMap.USER, map.get(MessageMap.MSG));
            return getInfo(model);
        }
        newTea= (Teacher) map.get(MessageMap.USER);
        if(UserUtil.setCurrentUserToSecurity(newTea)){
            UserUtil.setCurrentUserToModelFromSecurity(model);
        }else
            model.addAttribute(MessageMap.MSG, infoString.UPDATE_USER_FAIL);
        model.addAttribute(MessageMap.MSG, "修改信息成功");
        return getInfo(model);
    }



    @GetMapping("pwd")
    public String getPwd(Model model){
        UserUtil.setCurrentUserToModelFromSecurity(model);
        if(!model.containsAttribute("tea")){
            return "redirect:/login";
        }
        return "tea/pwd";
    }

    @PostMapping("pwd")
    public String changePwd(String oldPassword, String newPassword, String newPasswordAgain, Model model){
        UserUtil.setCurrentUserToModelFromSecurity(model);
        if(!model.containsAttribute("tea")){
            return "redirect:login";
        }
        User user=UserUtil.getCurrentUserFromSecurity();
        if(oldPassword==null || newPassword == null || newPasswordAgain==null){
            model.addAttribute(MessageMap.MSG, infoString.NULL_PASSWORD);
            return getPwd(model);
        }
        if(!newPassword.equals(newPasswordAgain)){
            model.addAttribute(MessageMap.MSG, infoString.PASSWORD_PA_NOT_SAME);
            return getPwd(model);
        }
        if(!user.getPassword().equals(passwordChange.enPassword(oldPassword))){
            model.addAttribute(MessageMap.MSG, "密码错误");
            return getPwd(model);
        }
        user.setPassword(passwordChange.enPassword(newPassword));
        int row=teacherMapper.updateByPrimaryKeySelective((Teacher) user);
        if(row<1){
            model.addAttribute(MessageMap.MSG, "修改密码失败");
            return getPwd(model);
        }
        model.addAttribute(MessageMap.MSG, "修改密码成功");
        return getPwd(model);
    }
}