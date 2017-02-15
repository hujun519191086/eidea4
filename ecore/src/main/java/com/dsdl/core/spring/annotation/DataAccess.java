package com.dsdl.core.spring.annotation;

import java.lang.annotation.*;

/**
 * Created by 刘大磊 on 2017/2/15 17:08.
 * 数据库持久成注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
@Documented
public @interface DataAccess {
    Class<?> entity();
}