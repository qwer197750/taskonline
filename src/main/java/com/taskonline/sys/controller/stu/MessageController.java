package com.taskonline.sys.controller.stu;

import com.taskonline.sys.service.common.UserServer;
import com.taskonline.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @program: taskonline
 * @description: 消息控制器。现用于显示学生申请加入课程的信息
 * @author: qwer
 * @create: 2020-04-04 10:53
 */
@RequestMapping("/stu/message")
@Component("stuMessageController")
public class MessageController {
    @Autowired
    UserServer userServer;

    @GetMapping("")
    public String getMessage(Model model){
        UserUtil.setCurrentUserToModelFromSecurity(model);
        if(!model.containsAttribute("stu")){
            return "redirect:/login";
        }

        return "/stu/message";
    }

}