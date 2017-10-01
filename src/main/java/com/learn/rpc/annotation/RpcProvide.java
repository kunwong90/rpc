package com.learn.rpc.annotation;

/**
 * rpc 服务提供注解
 */
public @interface RpcProvide {

    //接口类型
    Class<?> value();

    //可能存在多个实现，用于获取某个具体实现
    String implCode() default "";
}
