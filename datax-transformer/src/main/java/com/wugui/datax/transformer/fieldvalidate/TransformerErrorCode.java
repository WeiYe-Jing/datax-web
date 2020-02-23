package com.wugui.datax.transformer.fieldvalidate;

import com.alibaba.datax.common.spi.ErrorCode;

/**
 * @author fantasticKe
 * @description 多隆镇楼，bug退散🙏🙏🙏
 *
 * @date 2020/2/23
 */
public enum TransformerErrorCode implements ErrorCode {
    ILLEGAL_PARAM("ILLEGAL_PARAM", "参数错误: "),
    NO_MATCH_VALIDATOR("NO_MATCH_VALIDATOR", "没有匹配的验证器:");

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
