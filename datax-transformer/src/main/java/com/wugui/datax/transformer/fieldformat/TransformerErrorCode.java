package com.wugui.datax.transformer.fieldformat;

import com.alibaba.datax.common.spi.ErrorCode;

/**
 * @author fantasticKe
 * @description 多隆镇楼，bug退散🙏🙏🙏
 *
 * @date 2020/2/23
 */
public enum TransformerErrorCode implements ErrorCode {
    ILLEGAL_PARAM("ILLEGAL_PARAM", "参数错误: "),
    NO_MATCH_FORMATTER("NO_MATCH_FORMATTER", "没有匹配的格式化器: "),
    ILLEGAL_COLUMN_TYPE("ILLEGAL_COLUMN_TYPE", "指定的column类型错误:"),
    FORMAT_FAILED("FORMAT_FAILED", "标准化失败:");

    private final String code;

    private final String description;

    TransformerErrorCode(String code, String description) {
        this.code = code;
        this.description = description;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
