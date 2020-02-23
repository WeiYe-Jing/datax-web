package com.wugui.datax.transformer.fieldvalidate.validator;

import com.alibaba.datax.common.element.Column;

import java.util.Map;

/**
 * @author fantasticKe
 * @description 多隆镇楼，bug退散🙏🙏🙏
 * 字段验证接口
 * @date 2020/2/23
 */
public interface FieldValidator {

    /**
     * 验证工具名称(唯一性)通过这个名称来动态找到注解里配置规则类
     * @return
     */
    String name();

    /**
     * 参数校验
     * @param params 参数
     * @return
     */
    boolean checkParam(Map<String, Object> params);

    /**
     * 数据校验
     * @param column 待校验的数据列
     * @param params 校验器参数
     * @return
     */
    boolean validate(Column column, Map<String, Object> params);
}
