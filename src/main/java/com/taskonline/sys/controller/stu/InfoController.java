package com.taskonline.sys.controller.stu;

import com.taskonline.sys.entity.Student;
import com.taskonline.sys.mapper.StudentMapper;
import com.taskonline.sys.pojo.User;
import com.taskonline.sys.service.common.FileServer;
import com.taskonline.sys.service.common.UserServer;
import com.taskonline.sys.service.teacher.StuService;
import com.taskonline.util.InfoString;
import com.taskonline.util.MessageMap;
import com.taskonline.util.PasswordChange;
import com.taskonline.util.UserUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Map;

/**
 * @program: taskonline
 * @description: 学生信息控制器。
 * @author: qwer
 * @create: 2020-04-03 11:11
 */
@RequestMapping("/stu/info")
@Controller("stuInfoController")
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
    StudentMapper studentMapper;

    /*
     *@Description: get获取学生信息页面，如果不是学生用户登录，那么重定向到/login
     *@Author: qwer
     *@date: 2020/4/3
     */
    @GetMapping("")
    public String getInfo(Model model){
        UserUtil.setCurrentUserToModelFromSecurity(model);
        if(!model.containsAttribute("stu")){
            return "redirect:login";
        }
        return "stu/info";
    }

    /*
     *@Description: 修改学生信息。如果当前用户不是学生，那么重定向到。修改信息的主键和学生id由当前用户提供，不依赖提交的数据
     *@Author: qwer
     *@date: 2020/4/3
     */
    @PostMapping("")
    public String changeInfo(@RequestParam(value = "file", required = false) MultipartFile mfile, Student student, Model model){
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
        if ((c == null) || !(c instanceof Student))
            return "redirect:/login";
        Student newStu=new Student();
        BeanUtils.copyProperties(c,newStu);
       /*使用基于复制currentStu生成的newStu作为修改的参考对象，而不是直接通过form映射的student，防止form提交了不能修改的属性
            不直接修改currentStu*，因为这个对象同时被security存储。如果修改这个对象，不再登录前获取的用户信息都可能是修改失败的，而不是真正的用户信息
        */
        newStu.setIcon(fileName);
        newStu.setSname(student.getSname());
        /*-------------------------------------------------------------------------------------------*/
        map=userServer.stuInfoChange(newStu);
        if(map.containsKey(MessageMap.MSG)){
            model.addAttribute(MessageMap.MSG, map.get(MessageMap.MSG));
            model.addAttribute(MessageMap.USER, map.get(MessageMap.MSG));
            return getInfo(model);
        }
        newStu= (Student) map.get(MessageMap.USER);
        if(UserUtil.setCurrentUserToSecurity(newStu)){
            UserUtil.setCurrentUserToModelFromSecurity(model);
        }else
            model.addAttribute(MessageMap.MSG, infoString.UPDATE_USER_FAIL);
        model.addAttribute(MessageMap.MSG, "修改信息成功");
        return getInfo(model);
    }


    @GetMapping("pwd")
    public String getPwd(Model model){
        UserUtil.setCurrentUserToModelFromSecurity(model);
        if(!model.containsAttribute("stu")){
            return "redirect:login";
        }
        return "stu/pwd";
    }

    @PostMapping("pwd")
    public String changePwd(String oldPassword, String newPassword, String newPasswordAgain, Model model){
        UserUtil.setCurrentUserToModelFromSecurity(model);
        if(!model.containsAttribute("stu")){
            return "redirect:login";
        }
        User user=UserUtil.getCurrentUserFromSecurity();
        if(StringUtils.isEmpty(oldPassword) || StringUtils.isEmpty(newPassword) || StringUtils.isEmpty(newPasswordAgain)){
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
        int row=studentMapper.updateByPrimaryKeySelective((Student) user);
        if(row<1){
            model.addAttribute(MessageMap.MSG, "修改密码失败");
            return getPwd(model);
        }
        model.addAttribute(MessageMap.MSG, "修改密码成功");
        return getPwd(model);
    }
}