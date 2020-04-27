package com.taskonline.sys.pojo;

import com.taskonline.sys.entity.Student;
import com.taskonline.sys.entity.Teacher;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.Collection;

/*
* 配合controller使用，和登录、注册等form表单配合
* */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString
public class User extends PojoSuper implements UserDetails {
    String name;
    String id;
    String password;
    String passwordAgain;
    String captchaId;
    String captcha;
    String type;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<SimpleGrantedAuthority> c= new ArrayList<SimpleGrantedAuthority>();
        if("stu".equals(type)){
            c.add(new SimpleGrantedAuthority("ROLE_stu"));
        }else if("tea".equals(type)){
            c.add(new SimpleGrantedAuthority("ROLE_tea"));
        }
        return c;
    }


    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
