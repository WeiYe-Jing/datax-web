package com.wugui.datax.transformer.fieldformat.annotations;

import java.lang.annotation.*;

/**
 * @author fantasticKe
 * @description 多隆镇楼，bug退散🙏🙏🙏
 * 格式化规则注解
 * @date 2020/2/23
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Formatter {
    //格式化规则名称
    String name() default "";
}
