package com.taskonline.sys.pojo;

import org.springframework.util.StringUtils;

/**
 * @program: taskonline
 * @description: 参数Bean的父对象。用于自定义参数校验后，不通过全局异常处理而是写入到这个父类的属性里面交由Controller处理
 * @author: qwer
 * @create: 2020-03-31 16:59
 */
public class PojoSuper {
    String msg;
    public boolean isValida(){
        return StringUtils.isEmpty(msg);
    }

    public String getMsg(){
        return msg;
    }
    public void setMsg(String msg){
        this.msg=msg;
    }
}