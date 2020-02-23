package com.wugui.datax.transformer.fieldformat.formatter;

import com.alibaba.datax.common.element.Column;

import java.util.Map;

/**
 * @author fantasticKe
 * @description 多隆镇楼，bug退散🙏🙏🙏
 * 字段格式化接口
 * @date 2020/2/23
 */

public interface FieldFormatter {

    /**
     * 格式化工具名称(唯一性)通过这个名称来动态找到注解里配置规则类
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
     * 数据格式化
     * @param column 待格式化的数据
     * @param params 格式化参数
     * @return
     */
    Column format(Column column, Map<String, Object> params);
}
