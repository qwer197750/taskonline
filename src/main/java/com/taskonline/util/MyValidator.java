package com.taskonline.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotEmpty;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @program: taskonline
 * @description: 具体验证各个字段的工具类，本项目不在自定义校验器中逐一验证。而采用组的方式对于不同组采取不同的验证
 * @author: qwer
 * @create: 2020-03-30 18:37
 */
@Component
public class MyValidator {
    @Autowired
    CaptchaUtil captchaUtil;

    public boolean isEmpty(String s){
        return StringUtils.isEmpty(s);
    }

    public boolean checkLengthSize(int min, int max, String s){
        if(isEmpty(s))return false;
        return s.length() >= min && s.length() <= max;
    }

    public boolean checkPasswordAgain(String password, String passwordAgain){
        if(isEmpty(password) || isEmpty(passwordAgain))return false;
        return password.equals(passwordAgain);
    }

    /*
     *@Description: 校验验证码，查询redis。如果校验成功了，就删除redis的数据，防止一个验证码多次使用
     *@param uuid：生成的对应验证码的唯一id,text：提交的验证码
     *@return:
     *@Author: qwer
     *@date: 2020/3/30
     */
    public boolean checkCaptcha(String uuid, String text){
        if(isEmpty(uuid) || isEmpty(text))return false;
        return captchaUtil.checkCaptchaText(uuid, text);
    }

    /*
     *@Description: 非法字符检查，用于用户名，密码，学号等输入字符串。限制使用大小写英文字母，数字，简单符号
     *@param null
     *@return:
     *@Author: qwer
     *@date: 2020/3/30
     */
    public boolean checkLegalSymbol(String s){
        String regex = "^.*[A-Za-z\\d$@$!%*#?&]$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(s);
        return m.matches();
    }

    /*
     *@Description: 检查id是否能转为数字类型
     *@param null
     *@return:
     *@Author: qwer
     *@date: 2020/3/30
     */
    public boolean CheckIsIDNumber(String s){
        if(isEmpty(s))return false;
        try{
            Long l=Long.valueOf(s);
        }catch (Exception e){
            return false;
        }
        return true;
    }
}