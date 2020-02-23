package com.wugui.datax.transformer.fieldformat;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.datax.common.element.Column;
import com.alibaba.datax.common.element.Record;
import com.alibaba.datax.common.exception.DataXException;
import com.alibaba.datax.transformer.ComplexTransformer;
import com.wugui.datax.transformer.fieldformat.annotations.Formatter;
import com.wugui.datax.transformer.fieldformat.formatter.FieldFormatter;
import org.apache.commons.lang3.StringUtils;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * @author fantasticKe
 * @description 多隆镇楼，bug退散🙏🙏🙏
 * 字段格式化
 * @date 2020/2/23
 */
public class FieldFormatTransformer extends ComplexTransformer {

    @Override
    public Record evaluate(Record record, Map<String, Object> map, Object... objects) {
        if (objects.length != 1 || CollectionUtil.isEmpty(map) || !map.containsKey(KeyConstant.NAME))
            throw DataXException.asDataXException(TransformerErrorCode.ILLEGAL_PARAM,
                    TransformerErrorCode.ILLEGAL_PARAM.getDescription());
        int columnIndex = (int) objects[0];
        String name = (String) map.get(KeyConstant.NAME);
        Column column = record.getColumn(columnIndex);
        Optional<FieldFormatter> formatter = getFieldFormatterByName(name);
        if (!formatter.isPresent())
            throw DataXException.asDataXException(TransformerErrorCode.NO_MATCH_FORMATTER,
                    TransformerErrorCode.NO_MATCH_FORMATTER.getDescription() + name);

        boolean checkParam = formatter.get().checkParam(map);
        if (!checkParam)
            throw DataXException.asDataXException(TransformerErrorCode.ILLEGAL_PARAM,
                    TransformerErrorCode.ILLEGAL_PARAM.getDescription() + map.toString());

        Column formatColumn = formatter.get().format(column, map);
        record.setColumn(columnIndex, formatColumn);
        return record;
    }

    private Optional<FieldFormatter> getFieldFormatterByName(String name) {
        if (StringUtils.isEmpty(name)) return Optional.empty();
        return new Reflections(KeyConstant.PACKAGE_NAME)
                .getTypesAnnotatedWith(Formatter.class).stream()
                .filter(p -> name.equalsIgnoreCase(p.getSimpleName()))
                .map(c -> {
                    try {
                        Constructor constructor = c.getDeclaredConstructor();
                        constructor.setAccessible(true);
                        return (FieldFormatter) c.newInstance();
                    } catch (NoSuchMethodException | IllegalAccessException | InstantiationException ignore) {
                        return null;
                    }
                }).filter(Objects::nonNull).findAny();
    }
}
