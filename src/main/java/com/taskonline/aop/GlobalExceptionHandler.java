package com.taskonline.aop;

import com.taskonline.util.MessageMap;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @program: taskonline
 * @description: 全局异常处理
 * @author: qwer
 * @create: 2020-03-30 10:16
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /*
     *@Description: 全局异常处理，针对在方法中使用Validated直接对参数进行验证失败抛出的异常
     *@param 验证参数错误抛出的异常
     *@return: Map
     *@Author: qwer
     *@date: 2020/3/30
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public  Map<String, Object> resolveConstraintViolationException(ConstraintViolationException ex){
        Map<String, Object> errorMap = new MessageMap(getClass());
        Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();
        if(!CollectionUtils.isEmpty(constraintViolations)){
            StringBuilder msgBuilder = new StringBuilder();
            for(ConstraintViolation constraintViolation :constraintViolations){
                msgBuilder.append(constraintViolation.getMessage()).append(",");
            }
            String errorMessage = msgBuilder.toString();
            if(errorMessage.length()>1){
                errorMessage = errorMessage.substring(0,errorMessage.length()-1);
            }
            errorMap.put(MessageMap.MSG,errorMessage);
            return errorMap;
        }
        System.out.println("全局异常1");
        errorMap.put(MessageMap.MSG,ex.getMessage());
        return errorMap;
    }

    /*
     *@Description: 针对在Bean中的参数验证失败抛出的异常进行处理
     *@param 抛出的异常
     *@return: Map
     *@Author: qwer
     *@date: 2020/3/30
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public Map<String, Object> resolveMethodArgumentNotValidException(MethodArgumentNotValidException ex){
        Map<String, Object> errorMap = new MessageMap(getClass());
        List<ObjectError> objectErrors = ex.getBindingResult().getAllErrors();
        if(!CollectionUtils.isEmpty(objectErrors)) {
            StringBuilder msgBuilder = new StringBuilder();
            for (ObjectError objectError : objectErrors) {
                msgBuilder.append(objectError.getDefaultMessage()).append(",");
            }
            String errorMessage = msgBuilder.toString();
            if (errorMessage.length() > 1) {
                errorMessage = errorMessage.substring(0, errorMessage.length() - 1);
            }
            errorMap.put(MessageMap.MSG, errorMessage);
            System.out.println("全局异常");
            return errorMap;
        }
        errorMap.put(MessageMap.MSG,ex.getMessage());
        return errorMap;
    }


}