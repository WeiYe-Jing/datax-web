package com.wugui.datax.admin.enums;

import com.wugui.datax.admin.tool.query.*;
import com.wugui.datax.admin.util.JdbcConstants;

/**
 * 数据库类型枚举类
 *
 * @author Locki
 * @date 2020/9/9
 */
public enum QueryToolEnum {
    /**
     * 查询工具
     */
    //MYSQL(JdbcConstants.MYSQL, MySQLQueryTool.class),
    //TODO 目前JdbcConstants常量类中的mysql是小写，但是前端传值为大写
    MYSQL("MYSQL", MySQLQueryTool.class),
    ORACLE(JdbcConstants.ORACLE, OracleQueryTool.class),
    POSTGRESQL(JdbcConstants.POSTGRESQL, PostgresqlQueryTool.class),
    SPARK(JdbcConstants.GREENPLUM, PostgresqlQueryTool.class),
    SQLSERVER(JdbcConstants.SQL_SERVER, SqlServerQueryTool.class),
    HIVE(JdbcConstants.HIVE, HiveQueryTool.class),
    CLICKHOUSE(JdbcConstants.CLICKHOUSE, ClickHouseQueryTool.class),
    HBASE20XSQL(JdbcConstants.HBASE20XSQL, Hbase20XsqlQueryTool.class),
    DB2(JdbcConstants.DB2, null);

    private final String name;
    private final Class clazz;

    public Class getClazz() {
        return clazz;
    }

    QueryToolEnum(String name, Class clazz) {
        this.name = name;
        this.clazz = clazz;
    }

    public static QueryToolEnum getQueryToolEnum(String name) {
        for (QueryToolEnum typeEnum : QueryToolEnum.values()) {
            if (typeEnum.name.equals(name)) {
                return typeEnum;
            }
        }
        throw new UnsupportedOperationException("找不到该类型: ".concat(name));
    }
}
