package com.wugui.datax.admin.tool.query;

import com.wugui.datatx.core.enums.DbType;

/**
 * 获取单例实体
 *
 * @author weiye
 * @ClassName QueryToolFactory
 * @Version 2.1.3
 * @since 2020/08/02 9:36
 */
public class QueryToolFactory {

    public static BaseQueryTool getByDbType(DbType dbType, String parameter) {

        BaseQueryTool baseQueryTool = null;

        switch (dbType) {
            case POSTGRESQL:
            case GREENPLUM:
                baseQueryTool = new PostgresqlQueryTool(dbType, parameter);
                break;
            case MYSQL:
                baseQueryTool = new MySQLQueryTool(dbType, parameter);
                break;
            case HIVE:
                baseQueryTool = new HiveQueryTool(dbType, parameter);
                break;
            case CLICKHOUSE:
                baseQueryTool = new ClickHouseQueryTool(dbType, parameter);
                break;
            case ORACLE:
                baseQueryTool = new OracleQueryTool(dbType, parameter);
                break;
            case SQLSERVER:
                baseQueryTool = new SqlServerQueryTool(dbType, parameter);
                break;
            case HBASE20XSQL:
                baseQueryTool = new Hbase20XsqlQueryTool(dbType, parameter);
                break;
            case DB2:
                break;
            default:
                break;
        }
        return baseQueryTool;
    }
}
