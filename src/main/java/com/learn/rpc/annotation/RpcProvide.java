package com.learn.rpc.annotation;

import org.springframework.stereotype.Service;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * rpc 服务提供注解
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Service
public @interface RpcProvide {

    //接口类型
    Class<?> value();

    //可能存在多个实现，用于获取某个具体实现
    String implCode() default "";
}
