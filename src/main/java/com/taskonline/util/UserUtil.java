package com.taskonline.util;

import com.taskonline.sys.entity.Student;
import com.taskonline.sys.entity.Teacher;
import com.taskonline.sys.pojo.User;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;

/**
 * @program: taskonline
 * @description: 用户工具类
 * @author: qwer
 * @create: 2020-04-03 12:14
 */
public class UserUtil {
    /*
     *@Description: 获取通过spring security登录的当前用户，请在Controller中调用
     *@param null
     *@return: User(实际是子类Student或Teacher的对象。可通过user.getType()区分)
     *@Author: qwer
     *@date: 2020/4/2
     */
    public static void setCurrentUserToModelFromSecurity(Model model){
        User user=getCurrentUserFromSecurity();
        if(user != null){
            if(user instanceof Student)
                model.addAttribute("stu", user);
            else if(user instanceof Teacher)
                model.addAttribute("tea", user);
        }
    }

    public static User getCurrentUserFromSecurity(){
        SecurityContext context= SecurityContextHolder.getContext();
        if(context==null)
            return null;
        Authentication authentication=context.getAuthentication();
        if (authentication==null)
            return null;
        Object user = authentication.getPrincipal();
        if(user instanceof User)
            return (User)user;
        else
            return null;
    }

    public static boolean setCurrentUserToSecurity(User user){
        User currentUser=getCurrentUserFromSecurity();
        if(user == null){
            return false;
        }
        if(currentUser instanceof Student){
            BeanUtils.copyProperties((Student)user, (Student)currentUser);
        }else if(currentUser instanceof Teacher){
            BeanUtils.copyProperties((Teacher)user, (Teacher)currentUser);
        }else
            return false;
        return true;
    }

}