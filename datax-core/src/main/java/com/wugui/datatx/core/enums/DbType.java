package com.wugui.datatx.core.enums;


import com.wugui.datatx.core.datasource.*;
import com.wugui.datatx.core.util.Constants;
import lombok.Getter;

import java.util.HashMap;

/**
 * data base types
 */
@Getter
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
     * 8 greenplum
     * 9 hbase20xsql
     */
    MYSQL(0, Constants.COM_MYSQL_JDBC_DRIVER, MySQLDataSource.class, "mysql"),
    POSTGRESQL(1, Constants.ORG_POSTGRESQL_DRIVER, PostgreDataSource.class, "postgresql"),
    HIVE(2, Constants.ORG_APACHE_HIVE_JDBC_HIVE_DRIVER, HiveDataSource.class, "hive"),
    SPARK(3, null, SparkDataSource.class, "spark"),
    CLICKHOUSE(4, Constants.COM_CLICKHOUSE_JDBC_DRIVER, ClickHouseDataSource.class, "clickhouse"),
    ORACLE(5, Constants.COM_ORACLE_JDBC_DRIVER, OracleDataSource.class, "oracle"),
    SQLSERVER(6, Constants.COM_SQLSERVER_JDBC_DRIVER, SQLServerDataSource.class, "sqlserver"),
    DB2(7, Constants.COM_DB2_JDBC_DRIVER, DB2ServerDataSource.class, "db2"),
    GREENPLUM(8, null, null, "greenplum"),
    HBASE20XSQL(9, null, null, "hbase20xsql"),
    HBASE(10, null, null, "hbase"),
    MONGODB(11, null, null, "mongodb"),
    PHOENIX(12, Constants.COM_PHOENIX_JDBC_DRIVER, PhoenixDataSource.class, "phoenix"),
    OSCAR(13, Constants.COM_OSCAR_JDBC_DRIVER, OscarServerDataSource.class, "oscar");

    DbType(int code, String driver, Class<? extends BaseDataSource> clazz, String descp) {
        this.code = code;
        this.driver = driver;
        this.clazz = clazz;
        this.descp = descp;
    }


    private final int code;
    private final String driver;
    private final Class<? extends BaseDataSource> clazz;
    private final String descp;

    private static HashMap<Integer, DbType> DB_TYPE_MAP = new HashMap<>();

    static {
        for (DbType dbType : DbType.values()) {
            DB_TYPE_MAP.put(dbType.getCode(), dbType);
        }
    }

    public static DbType of(int type) {
        if (DB_TYPE_MAP.containsKey(type)) {
            return DB_TYPE_MAP.get(type);
        }
        throw new IllegalArgumentException("invalid type : " + type);
    }
}
