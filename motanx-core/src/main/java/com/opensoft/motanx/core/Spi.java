package com.opensoft.motanx.core;

import java.lang.annotation.*;

/**
 * 扩展接口注解，标识该接口可扩展
 * Created by kangwei on 2016/8/24.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Spi {
    String name() default "";

    Scope scope() default Scope.PROTOTYPE;
}
