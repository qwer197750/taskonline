package com.taskonline.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class PasswordChange {

    private static String md5(String src) {
        return DigestUtils.md5Hex(src);
    }

    @Value("${salt}")
    private  String salt;

    public String enPassword(String password) {
        String str = "";
        for(int i=0;i<password.length();i++){
            int j=i;
            j=j<salt.length()?j:salt.length()-1;
            str += ""+salt.charAt(j)+password.charAt(i);
        }
        return md5(str);
    }

}