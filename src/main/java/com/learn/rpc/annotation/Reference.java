
package com.learn.rpc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface Reference {

    /**
     * 服务版本号
     * @return
     */
    String version() default "";

    //可能存在多个实现，用于获取某个具体实现
    String implCode() default "";
}
