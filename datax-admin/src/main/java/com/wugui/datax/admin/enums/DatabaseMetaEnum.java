package com.wugui.datax.admin.enums;

import com.wugui.datax.admin.tool.meta.*;
import com.wugui.datax.admin.util.JdbcConstants;

/**
 * 数据库meta信息枚举类
 *
 * @author Locki
 * @date 2020/9/10
 */
public enum DatabaseMetaEnum {
    /**
     * 查询工具
     */
    //MYSQL(JdbcConstants.MYSQL, MySQLQueryTool.class),
    //TODO 目前JdbcConstants常量类中的mysql是小写，但是前端传值为大写
    MYSQL("MYSQL", MySQLDatabaseMeta.getInstance()),
    ORACLE(JdbcConstants.ORACLE, OracleDatabaseMeta.getInstance()),
    POSTGRESQL(JdbcConstants.POSTGRESQL, PostgresqlDatabaseMeta.getInstance()),
    SPARK(JdbcConstants.GREENPLUM, PostgresqlDatabaseMeta.getInstance()),
    SQLSERVER(JdbcConstants.SQL_SERVER, SqlServerDatabaseMeta.getInstance()),
    HIVE(JdbcConstants.HIVE, HiveDatabaseMeta.getInstance()),
    CLICKHOUSE(JdbcConstants.CLICKHOUSE, ClickHouseDataBaseMeta.getInstance()),
    HBASE20XSQL(JdbcConstants.HBASE20XSQL, Hbase20xsqlMeta.getInstance()),
    DB2(JdbcConstants.DB2, null);

    private final String name;
    private final DatabaseInterface databaseInterface;

    public DatabaseInterface getDatabaseInterface() {
        return databaseInterface;
    }

    DatabaseMetaEnum(String name, DatabaseInterface databaseInterface) {
        this.name = name;
        this.databaseInterface = databaseInterface;
    }

    public static DatabaseMetaEnum getQueryToolEnum(String name) {
        for (DatabaseMetaEnum typeEnum : DatabaseMetaEnum.values()) {
            if (typeEnum.name.equals(name)) {
                return typeEnum;
            }
        }
        throw new UnsupportedOperationException("找不到该类型: ".concat(name));
    }
}
