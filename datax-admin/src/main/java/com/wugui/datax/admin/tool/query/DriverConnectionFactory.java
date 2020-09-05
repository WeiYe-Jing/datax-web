package com.wugui.datax.admin.tool.query;

import com.wugui.datatx.core.datasource.*;
import com.wugui.datatx.core.enums.DbType;
import com.wugui.datatx.core.util.Constants;
import com.wugui.datatx.core.util.JSONUtils;
import com.wugui.datax.admin.util.AesUtil;
import com.wugui.datax.admin.util.CommonUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.security.UserGroupInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import static com.wugui.datatx.core.util.PropertyUtils.getString;

public class DriverConnectionFactory {

    private static final Logger logger = LoggerFactory.getLogger(CommonUtils.class);

    /**
     * get connection
     *
     * @param dbType    datasource type
     * @param parameter parameter
     * @return connection for datasource
     */
    public static Connection getConnection(DbType dbType, String parameter) {
        Connection connection = null;
        BaseDataSource datasource = null;
        try {
            switch (dbType) {
                case POSTGRESQL:
                    datasource = JSONUtils.parseObject(parameter, PostgreDataSource.class);
                    Class.forName(Constants.ORG_POSTGRESQL_DRIVER);
                    break;
                case MYSQL:
                    datasource = JSONUtils.parseObject(parameter, MySQLDataSource.class);
                    Class.forName(Constants.COM_MYSQL_JDBC_DRIVER);
                    break;
                case HIVE:
                    if (CommonUtils.getKerberosStartupState()) {
                        System.setProperty(Constants.JAVA_SECURITY_KRB5_CONF,
                                getString(Constants.JAVA_SECURITY_KRB5_CONF_PATH));
                        Configuration configuration = new Configuration();
                        configuration.set(Constants.HADOOP_SECURITY_AUTHENTICATION, "kerberos");
                        UserGroupInformation.setConfiguration(configuration);
                        UserGroupInformation.loginUserFromKeytab(getString(Constants.LOGIN_USER_KEY_TAB_USERNAME),
                                getString(Constants.LOGIN_USER_KEY_TAB_PATH));
                    }

                    datasource = JSONUtils.parseObject(parameter, HiveDataSource.class);
                    Class.forName(Constants.ORG_APACHE_HIVE_JDBC_HIVE_DRIVER);
                    break;
                case CLICKHOUSE:
                    datasource = JSONUtils.parseObject(parameter, ClickHouseDataSource.class);
                    Class.forName(Constants.COM_CLICKHOUSE_JDBC_DRIVER);
                    break;
                case ORACLE:
                    datasource = JSONUtils.parseObject(parameter, OracleDataSource.class);
                    Class.forName(Constants.COM_ORACLE_JDBC_DRIVER);
                    break;
                case SQLSERVER:
                    datasource = JSONUtils.parseObject(parameter, SQLServerDataSource.class);
                    Class.forName(Constants.COM_SQLSERVER_JDBC_DRIVER);
                    break;
                case DB2:
                    datasource = JSONUtils.parseObject(parameter, DB2ServerDataSource.class);
                    Class.forName(Constants.COM_DB2_JDBC_DRIVER);
                    break;
                case PHOENIX:
                    datasource = JSONUtils.parseObject(parameter, PhoenixDataSource.class);
                    Class.forName(Constants.COM_PHOENIX_JDBC_DRIVER);
                    break;
                default:
                    break;
            }

            if (datasource != null) {
                connection = DriverManager.getConnection(datasource.getJdbcUrl(), datasource.getUser(), datasource.getPassword());
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return connection;
    }

    /**
     * build paramters
     *
     * @param type      data source  type
     * @param database  data source database name
     * @param userName  user name
     * @param password  password
     * @param principal principal
     * @return datasource parameter
     */
    public static String buildParameter(String userName, String password, DbType type, String database, String jdbcUrl, String principal, String comments) {

        String address = jdbcUrl;

        Map<String, Object> parameterMap = new LinkedHashMap<String, Object>(6);

        //TODO k8s config
        if (type == DbType.HIVE) {
            jdbcUrl += ";principal=" + principal;
        }

        parameterMap.put(Constants.ADDRESS, address);
        parameterMap.put(Constants.JDBC_URL, jdbcUrl);
        parameterMap.put(Constants.DATABASE, database);
        parameterMap.put(Constants.USER, decrypt(userName));
        parameterMap.put(Constants.PASSWORD, decrypt(password));
        if (type == DbType.HIVE) {
            parameterMap.put(Constants.PRINCIPAL, principal);
        }

        if (logger.isDebugEnabled()) {
            logger.info("parameters map:{}", JSONUtils.toJsonString(parameterMap));
        }
        return JSONUtils.toJsonString(parameterMap);
    }

    private static String decrypt(String account) {
        String decryptUsername = AesUtil.decrypt(account);
        return decryptUsername == null ? account : decryptUsername;
    }
}
