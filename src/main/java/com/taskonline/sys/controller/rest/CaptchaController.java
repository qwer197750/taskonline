package com.taskonline.sys.controller.rest;

import com.taskonline.util.CaptchaUtil;
import com.wf.captcha.base.Captcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: taskonline
 * @description: controller：用于ajax获取验证码，验证交由各个Controller处理。
 * @author: qwer
 * @create: 2020-03-29 21:02
 */
@RestController
public class CaptchaController {
    @Autowired
    private CaptchaUtil captchaUtil;

    @GetMapping(path = {"/rest/captcha"})
    public Map<String, Object> getCaptcha(){
        Captcha captcha=captchaUtil.getAriCaptcha();
        Map<String, Object> map=new HashMap<String, Object>();
        map.put("captchaId", captchaUtil.saveCaptchaTextInRedis(captcha.text()));
        map.put("captchaImage", captcha.toBase64());
        return map;
    }
}