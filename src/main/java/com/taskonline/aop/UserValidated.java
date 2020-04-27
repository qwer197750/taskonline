package com.taskonline.aop;

import com.taskonline.anno.UserValidatorAnnotation;
import com.taskonline.sys.pojo.User;
import com.taskonline.util.InfoString;
import com.taskonline.util.MyValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @program: taskonline
 * @description: 参数验证器，用于管理员、学生、教师登录注册验证
 * @author: qwer
 * @create: 2020-03-30 10:53
 */
@Component
public class UserValidated implements ConstraintValidator<UserValidatorAnnotation, User> {
    @Autowired
    InfoString infoString;

    @Autowired
    MyValidator myValidator;

    public Class<?> group;

    @Override
    public boolean isValid(User user, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        String msg="";
        if(group ==null || !group.isAnnotation()){
            msg=infoString.NOT_ANNOTATION_FOR_GROUP;
        }
        if(group == UserValidatorAnnotation.LoginGroup.class){
            msg=validatorLogin(user);
        }else if(group == UserValidatorAnnotation.RegisterGroup.class) {
            msg = validatorRegister(user);
        }else if(group == UserValidatorAnnotation.AdminLoginGroup.class){
            msg=validatorAdminLogin(user);
        }else if(group == UserValidatorAnnotation.AdminRegisterGroup.class){
            msg=validatorAdminRegister(user);
        }else{
            msg=infoString.NOT_SUITABLE_ANNOTATION_FOR_GROUP;
        }
        if(StringUtils.isEmpty(msg)){ //如果没有校验不通过的错误
            return true;
        }else { //由校验不通过的错误
//            context.buildConstraintViolationWithTemplate(msg)
//                    .addConstraintViolation();
            user.setMsg(msg);
            return true;  //本应该是false,但是这里不进行全局异常处理，而是把错误信息写入user.msg交由Controller处理
        }
    }

    @Override
    public void initialize(UserValidatorAnnotation constraintAnnotation) {
        group = constraintAnnotation.group();
    }

    /*
     *@Description: 校验学生登录
     *@param User
     *@return: 如果验证不通过，返回信息，如果通过返回null
     *@Author: qwer
     *@date: 2020/3/30
     */
    public String validatorLogin(User user){
        String msg=null;
        msg=checkUserName(user.getId());
        if(msg!=null)return msg;
        msg=checkPassword(user.getPassword());
        if(msg!=null)return msg;
        msg=checkFormType(user.getType());
        if(msg!=null)return msg;
        msg=checkCaptcha(user.getCaptchaId(), user.getCaptcha());
        return msg;
    }
    public String validatorRegister(User user){
        String msg=null;
        msg=checkUserName(user.getId());
        if(msg!=null)return msg;
        msg=checkPassword(user.getPassword());
        if(msg!=null)return msg;
        msg=checkUserPasswordAgain(user.getPassword(),user.getPasswordAgain());
        if(msg!=null)return msg;
        msg=checkFormType(user.getType());
        if(msg!=null)return msg;
        msg=checkCaptcha(user.getCaptchaId(), user.getCaptcha());
        return msg;
    }
    public String validatorAdminLogin(User user){
        String msg=null;
        msg=checkUserName(user.getId());
        if(msg!=null)return msg;
        msg=checkPassword(user.getPassword());
        if(msg!=null)return msg;
        msg=checkCaptcha(user.getCaptchaId(), user.getCaptcha());
        return msg;
    }
    public String validatorAdminRegister(User user){
        String msg=null;
        msg=checkUserName(user.getId());
        if(msg!=null)return msg;
        msg=checkPassword(user.getPassword());
        if(msg!=null)return msg;
        msg=checkUserPasswordAgain(user.getPassword(),user.getPasswordAgain());
        if(msg!=null)return msg;
        msg=checkCaptcha(user.getCaptchaId(), user.getCaptcha());
        return msg;
    }

    String checkFormType(String type){
        if(myValidator.isEmpty(type)){
            return infoString.ERROR_TYPE;
        }else if("stu".equals(type) || "tea".equals(type)){
            return null;
        }else
            return infoString.ERROR_TYPE;
    }

    String checkUserName(String name){
        if(myValidator.isEmpty(name)) {
            return infoString.NULL_USER_NAME;
        }else if(!myValidator.checkLengthSize(2,10, name)){
            return String.format(infoString.NAME_LENGTH_MUST, 2, 10);
        } else
            return null;
    }

    String checkPassword(String password){
        if(myValidator.isEmpty(password)){
            return infoString.NULL_PASSWORD;
        }else if(!myValidator.checkLengthSize(6,12, password)){
            return String.format(infoString.PASSWORD_LENGTH_MUST, 6,12);
        }else if(!myValidator.checkLegalSymbol(password)){
            return infoString.ILLEGAL_PASSWORD;
        }
        return null;
    }

    String checkUserPasswordAgain(String password, String passwordAgain){
        if(myValidator.isEmpty(password) || myValidator.isEmpty(passwordAgain)){
            return infoString.NULL_PASSWORD;
        }else if(!myValidator.checkPasswordAgain(password,passwordAgain)){
            return infoString.PASSWORD_PA_NOT_SAME;
        }return null;
    }

    String checkCaptcha(String uuid, String text){
        if (myValidator.checkCaptcha(uuid, text))return null;
        else
            return infoString.CAPTCHA_CHECK_FAIL;
    }

    String checkIsIDNumber(String id){
        if (myValidator.CheckIsIDNumber(id))return null;
        else return infoString.ILLEGAL_STRING_FOR_NUMBER;
    }
}