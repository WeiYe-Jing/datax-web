package com.wugui.datatx.core.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * increment type
 */
public enum IncrementTypeEnum {
    /**
     * 2 TIME
     * 1 ID
     * 3 PARTITION
     * 4 3 PARTITION AND TIME
     * 5 MONGODB ID
     */
    ID(1, "自增主键"),
    TIME(2, "时间"),
    PARTITION(3, "HIVE分区"),
    PARTITION_TIME(4, "HIVE分区时间增量"),
    MONGODB_ID(5, "mongodb主键增量");

    IncrementTypeEnum(Integer code, String descp) {
        this.code = code;
        this.descp = descp;
    }

    private final Integer code;
    private final String descp;

    public static final List<Integer> partition = new ArrayList<Integer>();
    public static final List<Integer> time = new ArrayList<Integer>();

    public Integer getCode() {
        return code;
    }

    public String getDescp() {
        return descp;
    }

    static {
        partition.add(IncrementTypeEnum.PARTITION.code);
        partition.add(IncrementTypeEnum.PARTITION_TIME.code);
    }

    static {
        time.add(IncrementTypeEnum.TIME.code);
        time.add(IncrementTypeEnum.PARTITION_TIME.code);
    }
}

