package com.wugui.dataxweb.enums;

import com.google.common.collect.Maps;

import java.util.EnumSet;
import java.util.Map;

/**
 * @author Jingwk
 * @date 2019/11/10
 */
public enum HandleCodeEnum {
    SUCCESS(200, "SUCCESS"),
    FAIL(500, "FAIL");

    private Integer value;
    private String desc;

    HandleCodeEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public Integer getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static final Map<Integer, HandleCodeEnum> lookup = Maps.newHashMap();

    static {
        for (HandleCodeEnum e : EnumSet.allOf(HandleCodeEnum.class)) {
            lookup.put(e.value, e);
        }
    }

    public static HandleCodeEnum find(Integer value) {
        HandleCodeEnum data = lookup.get(value);
        return data;
    }
}
