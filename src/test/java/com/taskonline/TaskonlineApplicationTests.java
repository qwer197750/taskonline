package com.taskonline;

import com.taskonline.sys.entity.Administrator;
import com.taskonline.sys.mapper.expand.ExAdministratorMapper;
import com.taskonline.util.CaptchaUtil;
import com.taskonline.util.DatabaseString;
import com.taskonline.sys.service.common.FileServer;
import com.taskonline.sys.service.common.UserServer;
import com.taskonline.util.PasswordChange;
import com.wf.captcha.ArithmeticCaptcha;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=TaskonlineApplication.class)
public class TaskonlineApplicationTests {

    @Autowired
    private PasswordChange passwordChange;
    @Autowired
    private ExAdministratorMapper administratorMapper;
    @Autowired
    private DatabaseString databaseString;

    @Autowired
    private UserServer userServer;

    @Autowired
    private FileServer fileServer;

    @Autowired
    private CaptchaUtil captchaUtil;

    @Test
    public void mytest() {
        System.out.println(1);
        System.out.println(passwordChange.enPassword("1977509942"));
    }

    @Test
    public void testLogin(){
        String name = "a";
        String password = "123456";
        Administrator administrator = administratorMapper.selectByNamePassword(name, password);
        System.out.println(administrator.getAname());
    }

    @Test
    public void testDateForInsert(){
        String teacherID="45646";
        String tname = "zhangsan";
        String password = "qwer1977";
        Map map = userServer.studentRegister(teacherID,tname, password);
        if(map.containsKey("msg")){
            System.out.println(map.get("msg"));
        }else {
            System.out.println(map.get("user").toString());
        }
    }

    @Test
    public void testFileUpload(){
        fileServer.iconUpload(null);
    }

    @Test
    public void testCaptcha(){
        ArithmeticCaptcha ariCaptcha= captchaUtil.getAriCaptcha();
        System.out.println(ariCaptcha.getArithmeticString());
        System.out.println(ariCaptcha.text());
        String key = captchaUtil.saveCaptchaTextInRedis(ariCaptcha.text());
        System.out.println(key);
        boolean b= captchaUtil.checkCaptchaText(key, ariCaptcha.text());
        System.out.println(b);
    }
}
