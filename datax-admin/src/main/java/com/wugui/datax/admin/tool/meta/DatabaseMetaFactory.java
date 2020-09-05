package com.wugui.datax.admin.tool.meta;

import com.wugui.datatx.core.enums.DbType;

/**
 * meta信息工厂
 *
 * @author weiye
 */
public class DatabaseMetaFactory {

    /**
     * 根据数据库类型返回对应的接口
     *
     * @param dbType String
     * @return DatabaseInterface
     */
    public static DatabaseInterface getByDbType(DbType dbType) {
        DatabaseInterface databaseInterface = null;
        switch (dbType) {
            case POSTGRESQL:
            case GREENPLUM:
                databaseInterface = PostgresqlDatabaseMeta.getInstance();
                break;
            case MYSQL:
                databaseInterface = MySQLDatabaseMeta.getInstance();
                break;
            case HIVE:
                databaseInterface = HiveDatabaseMeta.getInstance();
                break;
            case CLICKHOUSE:
                databaseInterface = ClickHouseDataBaseMeta.getInstance();
                break;
            case ORACLE:
                databaseInterface = OracleDatabaseMeta.getInstance();
                break;
            case SQLSERVER:
                databaseInterface = SqlServerDatabaseMeta.getInstance();
                break;
            case HBASE20XSQL:
                databaseInterface = Hbase20xsqlMeta.getInstance();
                break;
            case DB2:
                break;
            default:
                break;
        }
        return databaseInterface;
    }
}