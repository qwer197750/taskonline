package com.taskonline.config;

import com.taskonline.sys.pojo.CustomAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @program: taskonline
 * @description: Security配置类
 * @author: qwer
 * @create: 2020-03-31 20:03
 */
@EnableWebSecurity
public class MySecurityConfig extends WebSecurityConfigurerAdapter {
    @Resource
    private AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> authenticationDetailSource;

    @Resource
    private CustomAuthenticationProvider customAuthenticationProvider;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/login","/register","/rest/captcha").permitAll()
                .antMatchers("/stu/**").hasAnyRole("stu")
                .antMatchers("/tea/**").hasAnyRole("tea")
                .antMatchers("/", "/course/**","/statistic/**").hasAnyRole("stu", "tea")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/")
                .failureUrl("/login")
                .authenticationDetailsSource(authenticationDetailSource)
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login");
        http.csrf().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(customAuthenticationProvider);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers( "/js/**", "/css/**", "/icon/**", "/bootstrap/**", "/images/*");
    }

}