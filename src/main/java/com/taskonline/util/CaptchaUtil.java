package com.taskonline.util;

import com.wf.captcha.ArithmeticCaptcha;
import com.wf.captcha.base.Captcha;
import io.netty.util.internal.StringUtil;
import net.bytebuddy.implementation.bind.annotation.Default;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.awt.*;
import java.io.IOException;
import java.util.UUID;

/**
 * @program: taskonline
 * @description: 验证码，采用ExasyCaptcha库，此处只封装功能
 * @author: qwer
 * @create: 2020-03-29 20:01
 */
@Component
public class CaptchaUtil {

    @Resource
    RedisUtil redisUtil;

    @Resource
    InfoString infoString;

    int seconds=300;
    /*
     *@Description: 获取一个算术验证码的对象
     *@param
     *@return: 返回一个算术验证码
     *@Author: qwer
     *@date: 2020/3/29
     */
    public ArithmeticCaptcha getAriCaptcha(){
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(130, 40);
        captcha.setLen(2);  // 几位数运算，默认是两位
        try {
            captcha.setFont(Captcha.FONT_5);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FontFormatException e) {
            e.printStackTrace();
        }
        captcha.getArithmeticString();  // 获取运算的公式：3+2=?
        return captcha;
    }

    /*
     *@Description: 把验证码的答案存到redis中 ，key由uuid生成，过期时间默认300s
     *@param text：验证码答案
     *@return:
     *@Author: qwer
     *@date: 2020/3/29
     */
    public String saveCaptchaTextInRedis(String text){
        String key = UUID.randomUUID().toString();
        int i=10;
        while((!redisUtil.set(key, text, seconds) && i-->0)){
          key= UUID.randomUUID().toString();
          Logger logger= LoggerFactory.getLogger(getClass());
          logger.error(infoString.UUID_FOR_REDIS_FAIL_OVER_TEN+"请检查redis是否启动");
          return null;
        };
        return key;
    }

    /*
     *@Description: 检查输入的验证码是否正确，验证成功后删除相应的数据
     *@param key-键，之前生成的uuid。text-用户输入的验证码
     *@return: true：验证成功，false：验证失败
     *@Author: qwer
     *@date: 2020/3/29
     */
    public boolean checkCaptchaText(String key, String text){
        String rText;
        try{
            rText= (String) redisUtil.get(key);
        }catch (Exception e){
            return false;
        }
        if(StringUtils.isEmpty(text) || StringUtils.isEmpty(rText)){
            return false;
        }
        if(text.equals(rText)){
            redisUtil.del(key);
            return true;
        }else
            redisUtil.del(key);
        return false;
    }
}