package com.taskonline.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.util.HashMap;

/**
 * @program: taskonline
 * @description: HashMap的子类，用于在MVC各层之间更方便的处理消息传递，约定了一些消息规范，嵌入了日志系统，使得传递的消息也会被写入日志
 * @author: qwer
 * @create: 2020-03-27 08:41
 */

public class MessageMap extends HashMap<String, Object> {

    Logger logger;

    public static final String MSG= "msg";
    public static final String ERROR= "error";
    public static final String WARN= "warn";
    public static final String EXCEPTION= "exception";
    public static final String USER= "user";
    public static final String FILE= "FILE";
    public static final String OBJECT= "object"; //特殊key，表示不需要记录日志

    /*以下boolean变量为是否写入相应的日志*/
    public boolean loggerUser=false;
    public boolean loggerMsg=false;
    public boolean loggerError=true;
    public boolean loggerException=true;
    public boolean loggerFile=true;
    public boolean ignoreKeyWarn=false;
    /*
     *@Description:限制使用有参构造方法，用于开启消息日志
     *@param clazz 类
     *@return:
     *@Author: qwer
     *@date: 2020/3/27
     */
    public MessageMap(Class<?> clazz){
        logger = LoggerFactory.getLogger(clazz);
    }

    /*
     *@Description: 设置日志需要的原类，也就是日志里写的“谁发生了啥”中的谁
     *@param clazz
     *@return: void
     *@Author: qwer
     *@date: 2020/3/27
     */
    public void setUserClassForLogger(Class<?> clazz){
        logger=LoggerFactory.getLogger(clazz);
    }

    /*
     *@Description: 覆写了HashMap中的put，使得put时可以根据key来书写不同的日志
     *@param key, Object
     *@return: object
     *@Author: qwer
     *@date: 2020/3/27
     */
    @Override
    public Object put(String key, Object obejct){
        switch (key){
            case MSG: loggerMsg(obejct.toString());break;
            case ERROR: loggerError(obejct.toString());break;
            case EXCEPTION: loggerException(obejct.toString());break;
            case USER: loggerUser(obejct.toString());break;
            case FILE: loggerFile(obejct);break;
            case OBJECT:break;
            default:
                if(!ignoreKeyWarn){
                    Logger newLogger = LoggerFactory.getLogger(getClass());
                    newLogger.warn("未知的key："+key);
                }
        }
        super.put(key, obejct);
        return obejct;
    }

    public boolean isKeyUser(){
        return containsKey(USER);
    }

    public boolean isKeyMsg(){
        return containsKey(MSG);
    }

    public boolean isKeyError(){
        return containsKey(ERROR);
    }

    public boolean isKeyException(){
        return containsKey(EXCEPTION);
    }

    public Object getUser(){
        return isKeyUser()?get(USER):null;
    }

    public String getMsgException(){
        return (isKeyMsg()&&isKeyException())?(((String)get(MSG))+"\n"+getMsgException()):(isKeyMsg()? (String) get(MSG) :(isKeyException()? (String) get(EXCEPTION) :null));
    }

    private void loggerMsg(String msg){
        if (loggerMsg){
            logger.info(msg);
        }
    }
    private void loggerError(String error){if (loggerError){
        logger.error(error);
    }}
    private void loggerException(String exception){
        if (loggerException){
            logger.warn(exception);
        }
    }
    private void loggerUser(String user){
        if (loggerUser){
            logger.info(user);
        }}
    private void loggerFile(Object o){
        if (loggerFile){
            try {
                File file =(File)o;
                logger.info("上传文件："+file.getName());
            }catch (Exception e){
                Logger newLogger = LoggerFactory.getLogger(getClass());
                newLogger.warn("文件类型转换异常："+e.toString());
            }
        }
    }


}