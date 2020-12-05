package com.wugui.datatx.core.datasource;

import com.wugui.datatx.core.enums.DbType;
import com.wugui.datatx.core.util.Constants;
import com.wugui.datatx.core.util.JSONUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * produce datasource in this custom defined datasource factory.
 */
public class DataSourceFactory {

    private static final Logger logger = LoggerFactory.getLogger(DataSourceFactory.class);

    /**
     * getDatasource
     *
     * @param dbType    dbType
     * @param parameter parameter
     * @return getDatasource
     */
    public static BaseDataSource getDatasource(DbType dbType, String parameter) {
        BaseDataSource baseDataSource = null;
        try {

            switch (dbType) {
                case MYSQL:
                    baseDataSource = JSONUtils.parseObject(parameter, MySQLDataSource.class);
                    break;
                case POSTGRESQL:
                    baseDataSource = JSONUtils.parseObject(parameter, PostgreDataSource.class);
                    break;
                case HIVE:
                    baseDataSource = JSONUtils.parseObject(parameter, HiveDataSource.class);
                    break;
                case SPARK:
                    baseDataSource = JSONUtils.parseObject(parameter, SparkDataSource.class);
                    break;
                case CLICKHOUSE:
                    baseDataSource = JSONUtils.parseObject(parameter, ClickHouseDataSource.class);
                    break;
                case ORACLE:
                    baseDataSource = JSONUtils.parseObject(parameter, OracleDataSource.class);
                    break;
                case SQLSERVER:
                    baseDataSource = JSONUtils.parseObject(parameter, SQLServerDataSource.class);
                    break;
                case DB2:
                    baseDataSource = JSONUtils.parseObject(parameter, DB2ServerDataSource.class);
                    break;
                case PHOENIX:
                    baseDataSource = JSONUtils.parseObject(parameter, PhoenixDataSource.class);
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            logger.error("get datasource object error", e);
        }
        return baseDataSource;
    }

    /**
     * load class
     *
     * @param dbType
     * @throws Exception
     */
    public static void loadClass(DbType dbType) throws Exception {
        switch (dbType) {
            case MYSQL:
                Class.forName(Constants.COM_MYSQL_JDBC_DRIVER);
                break;
            case POSTGRESQL:
                Class.forName(Constants.ORG_POSTGRESQL_DRIVER);
                break;
            case HIVE:
            case SPARK:
                Class.forName(Constants.ORG_APACHE_HIVE_JDBC_HIVE_DRIVER);
                break;
            case CLICKHOUSE:
                Class.forName(Constants.COM_CLICKHOUSE_JDBC_DRIVER);
                break;
            case ORACLE:
                Class.forName(Constants.COM_ORACLE_JDBC_DRIVER);
                break;
            case SQLSERVER:
                Class.forName(Constants.COM_SQLSERVER_JDBC_DRIVER);
                break;
            case DB2:
                Class.forName(Constants.COM_DB2_JDBC_DRIVER);
                break;
            default:
                logger.error("not support sql type: {},can't load class", dbType);
                throw new IllegalArgumentException("not support sql type,can't load class");
        }
    }
}
