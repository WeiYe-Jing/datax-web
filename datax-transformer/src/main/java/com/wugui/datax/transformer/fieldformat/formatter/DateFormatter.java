package com.wugui.datax.transformer.fieldformat.formatter;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.datax.common.element.Column;
import com.alibaba.datax.common.element.StringColumn;
import com.wugui.datax.transformer.fieldformat.annotations.Formatter;
import com.wugui.datax.transformer.fieldvalidate.KeyConstant;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

/**
 * @author fantasticKe
 * @description 多隆镇楼，bug退散🙏🙏🙏
 * 时间格式化
 * @date 2020/2/23
 */
@Formatter
public class DateFormatter implements FieldFormatter {

    private static final Logger LOGGER = LoggerFactory.getLogger(DateFormatter.class);

    private static final String[] ACCEPT_PATTERNS = new String[]{
            "yyyy-MM-dd'T'HH:mm:ss.SSS+08:00", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm:ss.S", "yyyy/MM/dd",
            "yyyy年MM月dd日", "yyyy-MM-dd", "yyyy年M月d日", "yyyy年M月dd日", "yyyy年MM月d日", "yyyy年MM月dd日HH:mm:ss",
            "yyyy年MM月dd日HH:mm", "yyyy年MM月dd日HH", "yyyy-M-d", "yyyy-M-dd", "yyyy-MM-d", "yyyy-MM-dd HH:mm",
            "yyyy-MM-dd HH", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM/dd HH", "yyyy.MM.dd HH:mm:ss",
            "yyyy.MM.dd HH:mm", "yyyy.MM.dd HH", "yyyy.MM.dd", "yyyy-MM-dd'T'HH:mm:ss", "yyyyMMdd", "MM/dd/yyyy HH:mm:ss",
            "MM/dd/yyyy HH:mm", "MM/dd/yyyy HH", "MM/dd/yyyy"};

    @Override
    public String name() {
        return this.getClass().getSimpleName();
    }

    @Override
    public boolean checkParam(Map<String, Object> params) {
        return CollectionUtil.isNotEmpty(params) || !params.containsKey(KeyConstant.NAME) || !params.containsKey(KeyConstant.PATTERN);
    }

    @Override
    public Column format(Column column, Map<String, Object> params) {
        String pattern = (String) params.get(KeyConstant.PATTERN);
        boolean discard = (Boolean) params.getOrDefault(KeyConstant.DISCARD, false);
        DateTimeFormatter ftf = DateTimeFormatter.ofPattern(pattern);
        Column formatColumn;
        try {
            formatColumn = dateFormat(column, ftf);
        } catch (Exception e) {
            LOGGER.error("format date failed: {}, {}, {}", column.toString(), pattern, e.getMessage());
            formatColumn = discard ? new StringColumn() : column;
        }
        return formatColumn;
    }

    public static Column dateFormat(Column column, DateTimeFormatter ftf) throws RuntimeException, ParseException {
        if (Column.Type.LONG.equals(column.getType())) {
            Long time = column.asLong();
            if (Objects.nonNull(time)) {
                String format = ftf.format(LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault()));
                return new StringColumn(format);
            }
        } else if (Column.Type.DATE.equals(column.getType())) {
            Date date = column.asDate();
            if (Objects.nonNull(date)) {
                String format = ftf.format(LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()));
                return new StringColumn(format);
            }
        } else if (Column.Type.STRING.equals(column.getType())) {
            String string = column.asString();
            Date date = DateUtils.parseDate(string, ACCEPT_PATTERNS);
            String format = ftf.format(LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()));
            return new StringColumn(format);
        } else {
            throw new RuntimeException("Unknown date data type:" + column.getType().name());
        }
        return null;
    }
}
