package com.wugui.datax.transformer.fieldvalidate;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.datax.common.element.*;
import com.alibaba.datax.common.exception.DataXException;
import com.alibaba.datax.transformer.ComplexTransformer;
import com.wugui.datax.transformer.fieldvalidate.annotations.Validator;
import com.wugui.datax.transformer.fieldvalidate.validator.FieldValidator;
import org.apache.commons.lang3.StringUtils;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * @author fantasticKe
 * @description 多隆镇楼，bug退散🙏🙏🙏
 * 字段校验
 * @date 2020/2/23
 */
public class FieldValidateTransformer extends ComplexTransformer {

    private static final Logger LOGGER = LoggerFactory.getLogger(FieldValidateTransformer.class);

    @Override
    public Record evaluate(Record record, Map<String, Object> map, Object... objects) {
        if (objects.length != 1 || CollectionUtil.isEmpty(map) || !map.containsKey(KeyConstant.NAME))
            throw DataXException.asDataXException(TransformerErrorCode.ILLEGAL_PARAM,
                    TransformerErrorCode.ILLEGAL_PARAM.getDescription());
        int columnIndex = (int) objects[0];
        String name = (String) map.get(KeyConstant.NAME);
        boolean discard = (Boolean) map.getOrDefault(KeyConstant.DISCARD, false);
        Column column = record.getColumn(columnIndex);
        Optional<FieldValidator> validator = getFieldValidatorByName(name);
        if (!validator.isPresent())
            throw DataXException.asDataXException(TransformerErrorCode.NO_MATCH_VALIDATOR,
                    TransformerErrorCode.NO_MATCH_VALIDATOR.getDescription() + name);
        boolean checkParam = validator.get().checkParam(map);
        if (!checkParam)
            throw DataXException.asDataXException(TransformerErrorCode.ILLEGAL_PARAM,
                    TransformerErrorCode.ILLEGAL_PARAM + map.toString());
        boolean validate = validator.get().validate(column, map);
        if (validate) return record;
        if (discard) {
            LOGGER.error("{} validate field failed: {}", name, column);
            return record;
        } else {
            Column nullColumn;
            switch (column.getType()) {
                case BOOL:
                    nullColumn = new BoolColumn();
                    break;
                case INT:
                case LONG:
                    nullColumn = new LongColumn();
                    break;
                case DOUBLE:
                    nullColumn = new DoubleColumn();
                    break;
                case DATE:
                    nullColumn = new DateColumn();
                    break;
                default:
                    nullColumn = new StringColumn();
            }
            record.setColumn(columnIndex, nullColumn);
        }
        return record;
    }

    private Optional<FieldValidator> getFieldValidatorByName(String name) {
        if (StringUtils.isEmpty(name)) return Optional.empty();
        return new Reflections(KeyConstant.PACKAGE_NAME)
                .getTypesAnnotatedWith(Validator.class).stream()
                .filter(p -> name.equalsIgnoreCase(p.getSimpleName()))
                .map(c -> {
                    try {
                        Constructor constructor = c.getDeclaredConstructor();
                        constructor.setAccessible(true);
                        return (FieldValidator) c.newInstance();
                    } catch (NoSuchMethodException | IllegalAccessException | InstantiationException ignore) {
                        return null;
                    }
                }).filter(Objects::nonNull).findAny();
    }
}
