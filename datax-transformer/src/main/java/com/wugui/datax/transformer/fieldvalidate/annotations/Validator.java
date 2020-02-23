package com.wugui.datax.transformer.fieldvalidate.annotations;

import java.lang.annotation.*;

/**
 * @author fantasticKe
 * @description 多隆镇楼，bug退散🙏🙏🙏
 * 校验规则注解
 * @date 2020/2/23
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Validator {

    String name() default "";
}
