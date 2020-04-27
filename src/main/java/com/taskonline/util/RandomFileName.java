package com.taskonline.util;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RandomFileName {
    @Value("${key}")
    private String key;
    @Autowired
    private PasswordChange passwordChange;

    public String randomName(String base){
        String uuid=UUID.randomUUID().toString().replace("-", "");
        return uuid;
    }
}
