package com.taskonline.sys.pojo;

import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @program: taskonline
 * @description: 继承spring security中的登录获取登录信息的类，使得不再只获得username和password。而能够获得更多的登录参数，如验证码、登录用户类型
 * @author: qwer
 * @create: 2020-04-01 14:54
 */
public class CustomWebAuthenticationDetails extends WebAuthenticationDetails {
    private static final long serialVersionUID = 6975601077710753878L;
    //扩展登录参数-验证码
    public String captcha;
    public String uuid;
    //扩展登录参数-登录用户类型
    public String type;
    public String id;
    public CustomWebAuthenticationDetails(HttpServletRequest request) {
        super(request);
        captcha=request.getParameter("captcha");
        id=request.getParameter("id");
        type=request.getParameter("type");
        uuid=request.getParameter("captchaId");
    }

}