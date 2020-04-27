package com.taskonline.util;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @program: taskonline
 * @description: 常用字符串，springBoot会在yaml文件中自动注入
 * @author: qwer
 * @create: 2020-03-26 11:47
 */
@ConfigurationProperties(prefix = "string")
@Component
@Getter
@Setter
public class InfoString {
    //error系列
    public String ERROR_LOGIN_TYPE;
    public String ERROR_REGISTER_TYPE;
    public String ERROR_NAME_OR_PASSWORD;
    public String ERROR_TYPE;

    //null系列
    public String NULL_MAP;
    public String NULL_USER_NAME;
    public String NULL_PASSWORD;
    public String NULL_TEACHER_ID;
    public String NULL_STUDENT_ID;
    public String NULL_SUBMIT_TASK_ID;
    public String NULL_COURSE_ID;
    public String NULL_TEACHER_PRIMARY_KEY;
    public String NULL_STUDENT_PRIMARY_KEY;
    public String NULL_TASK_ID;

    //uesd系列
    public String TEACHER_ID_USED;
    public String STUDENT_ID_USED;
    public String ADMINISTRATOR_ID_USED;

    //fail系列
    public String ADD_TEACHER_FAIL;
    public String ADD_STUDENT_FAIL;
    public String ADD_ADMINISTRATOR_FAIL;
    public String UPDATE_USER_FAIL;
    public String TRANSACTION_FILE_FAIL;

    //unknown系列
    public String UNKNOWN_MESSAGE_KEY;

    //信息提示类
    public String FILE_UPLOAD;
    public String ILLEGAL_USERNAME;
    public String ILLEGAL_PASSWORD;
    public String PASSWORD_PA_NOT_SAME;
    public String NAME_LENGTH_MUST;
    public String PASSWORD_LENGTH_MUST;
    public String CAPTCHA_CHECK_FAIL;

    //异常系列
    public String ILLEGAL_STRING_FOR_NUMBER;
    public String TYPE_CHANGE_EXCEPTION;
    public String UUID_FOR_REDIS_FAIL_OVER_TEN;
    public String NOT_ANNOTATION_FOR_GROUP;
    public String NOT_SUITABLE_ANNOTATION_FOR_GROUP;
}