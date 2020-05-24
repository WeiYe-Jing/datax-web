package com.wugui.datatx.core.enums;

/**
 * increment type
 */
public enum IncrementTypeEnum {
    /**
     * 2 TIME
     * 1 ID
     * 3 PARTITION
     */
    TIME(2, "时间"),
    ID(1, "自增主键"),
    PARTITION(3, "HIVE分区");

    IncrementTypeEnum(int code, String descp){
        this.code = code;
        this.descp = descp;
    }

    private final int code;
    private final String descp;

    public int getCode() {
        return code;
    }

    public String getDescp() {
        return descp;
    }
}

