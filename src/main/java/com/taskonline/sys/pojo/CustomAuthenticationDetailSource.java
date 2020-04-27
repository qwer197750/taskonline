package com.taskonline.sys.pojo;

import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @program: taskonline
 * @description: 父类：AuthenticationDetailsSource。用于替换掉spring security配置的bena。使用这个类注入，从而将数据写入到自定义的details中，从而获取登录的额外参数
 * @author: qwer
 * @create: 2020-04-01 15:06
 */
@Component("authenticationDetailsSource")
public class CustomAuthenticationDetailSource implements AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> {
    @Override
    public WebAuthenticationDetails buildDetails(HttpServletRequest httpServletRequest) {
        return new CustomWebAuthenticationDetails(httpServletRequest);
    }
}