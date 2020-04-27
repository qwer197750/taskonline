package com.taskonline.anno;

import com.taskonline.aop.UserValidated;
import org.springframework.validation.annotation.Validated;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.CLASS;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/*
* 用户验证注解,用于实现对于User的参数校验
* */
@Target({METHOD, TYPE,FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RUNTIME)
@Validated
@Constraint(validatedBy = UserValidated.class)
public @interface UserValidatorAnnotation {
    String message() default "默认错误";
    Class<?>[] groups() default {};
    Class<?> group();

    Class<? extends Payload>[] payload() default {};

    @interface LoginGroup{};
    @interface RegisterGroup{};
    @interface AdminLoginGroup{};
    @interface AdminRegisterGroup{};
}
