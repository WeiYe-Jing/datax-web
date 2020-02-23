package com.wugui.datax.transformer.fieldformat.formatter;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.IdcardUtil;
import com.alibaba.datax.common.element.Column;
import com.alibaba.datax.common.element.StringColumn;
import com.wugui.datax.transformer.fieldformat.annotations.Formatter;
import com.wugui.datax.transformer.fieldvalidate.KeyConstant;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * @author fantasticKe
 * @description 多隆镇楼，bug退散🙏🙏🙏
 * 身份证转化器
 * @date 2020/2/23
 */
@Formatter
public class IdCardFormatter implements FieldFormatter {
    @Override
    public String name() {
        return this.getClass().getSimpleName();
    }

    @Override
    public boolean checkParam(Map<String, Object> params) {
        return CollectionUtil.isNotEmpty(params) && params.containsKey(KeyConstant.NAME);
    }

    @Override
    public Column format(Column column, Map<String, Object> params) {
        boolean discard = (Boolean) params.getOrDefault(KeyConstant.DISCARD, false);
        if (StringUtils.isNotEmpty(column.asString()) && column.asString().length() == 18) return column;
        String convert15To18 = IdcardUtil.convert15To18(column.asString());
        if (StringUtils.isNotEmpty(convert15To18)) return new StringColumn(convert15To18);
        return discard ? new StringColumn() : column;
    }
}
