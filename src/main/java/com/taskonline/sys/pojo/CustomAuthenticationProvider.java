package com.taskonline.sys.pojo;

import com.taskonline.anno.UserValidatorAnnotation;
import com.taskonline.aop.UserValidated;
import com.taskonline.sys.service.common.UserServer;
import com.taskonline.util.MessageMap;
import com.taskonline.util.MyValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @program: taskonline
 * @description: Authentication实现类。用于自定义处理登录数据，包括自定义获取的登录扩展数据
 * @author: qwer
 * @create: 2020-04-01 15:18
 */
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
    @Resource
    UserValidated validated;
    @Resource
    UserServer userServer;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String inputPassword = authentication.getCredentials().toString();
        //details是springSecurity自动对登录表单进行处理获取到的表单数据对象
        CustomWebAuthenticationDetails details = (CustomWebAuthenticationDetails) authentication.getDetails();
        User user = new User();
        user.setId(details.id);
        user.setPassword(inputPassword);
        user.setType(details.type);
        user.setCaptchaId(details.uuid);
        user.setCaptcha(details.captcha);
        String msg = validated.validatorLogin(user);
        if(msg != null) {
            throw new DisabledException(msg);
        }

        Map<String, Object> map;
        if("stu".equals(user.type)){
            map=userServer.studentLogin(user.getId(), user.getPassword());
        }else{
            map=userServer.teacherLogin(user.getId(), user.getPassword());
        }

        if(map.containsKey("msg")) {
            throw new BadCredentialsException((String) map.get(MessageMap.MSG));
        }
        //返回的principal不是简单的用户名，而是查询出来的Student/Teacher对象
        return new UsernamePasswordAuthenticationToken(map.get(MessageMap.USER), inputPassword, user.getAuthorities());
   }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(UsernamePasswordAuthenticationToken.class);
    }
}