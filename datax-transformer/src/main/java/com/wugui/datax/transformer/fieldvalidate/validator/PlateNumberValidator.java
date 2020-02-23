package com.wugui.datax.transformer.fieldvalidate.validator;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.datax.common.element.Column;
import com.wugui.datax.transformer.fieldvalidate.KeyConstant;
import com.wugui.datax.transformer.fieldvalidate.annotations.Validator;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * @author fantasticKe
 * @description 多隆镇楼，bug退散🙏🙏🙏
 * 中国车牌号验证器
 * @date 2020/2/23
 */
@Validator
public class PlateNumberValidator implements FieldValidator {
    @Override
    public String name() {
        return this.getClass().getSimpleName();
    }

    @Override
    public boolean checkParam(Map<String, Object> params) {
        return CollectionUtil.isNotEmpty(params) && params.containsKey(KeyConstant.NAME);
    }

    @Override
    public boolean validate(Column column, Map<String, Object> params) {
        return StringUtils.isNotEmpty(column.asString()) &&
                cn.hutool.core.lang.Validator.isPlateNumber(column.asString());
    }
}
