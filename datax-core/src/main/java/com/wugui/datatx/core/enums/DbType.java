package com.wugui.datatx.core.enums;


import java.util.HashMap;

/**
 * data base types
 */
public enum DbType {
    /**
     * 0 mysql
     * 1 postgresql
     * 2 hive
     * 3 spark
     * 4 clickhouse
     * 5 oracle
     * 6 sqlserver
     * 7 db2
     */
    MYSQL(0, "mysql"),
    POSTGRESQL(1, "postgresql"),
    HIVE(2, "hive"),
    SPARK(3, "spark"),
    CLICKHOUSE(4, "clickhouse"),
    ORACLE(5, "oracle"),
    SQLSERVER(6, "sqlserver"),
    DB2(7, "db2");

    DbType(int code, String descp) {
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


    private static HashMap<Integer, DbType> DB_TYPE_MAP =new HashMap<>();

    static {
        for (DbType dbType:DbType.values()){
            DB_TYPE_MAP.put(dbType.getCode(),dbType);
        }
    }

    public static DbType of(int type){
        if(DB_TYPE_MAP.containsKey(type)){
            return DB_TYPE_MAP.get(type);
        }
        throw new IllegalArgumentException("invalid type : " + type);
    }
}
