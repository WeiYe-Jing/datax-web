package com.wugui.datax.admin.tool.query;

import com.wugui.datatx.core.datasource.*;
import com.wugui.datatx.core.enums.DbConnectType;
import com.wugui.datatx.core.datasource.BaseDataSource;
import com.wugui.datatx.core.enums.DbType;
import com.wugui.datatx.core.util.Constants;
import com.wugui.datatx.core.util.JSONUtils;
import com.wugui.datax.admin.util.AesUtil;
import com.wugui.datax.admin.util.CommonUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.security.UserGroupInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.LinkedHashMap;
import java.util.Map;

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
            if (DbType.HIVE == dbType) {
                if (CommonUtils.getKerberosStartupState()) {
                    System.setProperty(Constants.JAVA_SECURITY_KRB5_CONF, getString(Constants.JAVA_SECURITY_KRB5_CONF_PATH));
                    Configuration configuration = new Configuration();
                    configuration.set(Constants.HADOOP_SECURITY_AUTHENTICATION, "kerberos");
                    UserGroupInformation.setConfiguration(configuration);
                    UserGroupInformation.loginUserFromKeytab(getString(Constants.LOGIN_USER_KEY_TAB_USERNAME), getString(Constants.LOGIN_USER_KEY_TAB_PATH));
                }
            }
            if (dbType.getDriver() != null) {
                Class.forName(dbType.getDriver());
            }
            datasource = getBaseDataSource(dbType, parameter);
            if (datasource != null) {
                datasource.setUser(AesUtil.decrypt(datasource.getUser()));
                datasource.setPassword(AesUtil.decrypt(datasource.getPassword()));
                connection = DriverManager.getConnection(datasource.getJdbcUrl(), datasource.getUser(), datasource.getPassword());
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return connection;
    }

    public static BaseDataSource getBaseDataSource(DbType dbType, String parameter) {
        BaseDataSource datasource = null;
        try {
            if (dbType.getClazz() != null) {
                datasource = JSONUtils.parseObject(parameter, dbType.getClazz());
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        if (datasource instanceof OracleDataSource) {
            Map<String, String> parameters = JSONUtils.toMap(parameter);
            if (parameters.get("jdbcUrl").indexOf("//") == -1) {
                ((OracleDataSource) datasource).setConnectType(DbConnectType.ORACLE_SID);
            }else {
                ((OracleDataSource) datasource).setConnectType(DbConnectType.ORACLE_SERVICE_NAME);
            }
        }
        return datasource;
    }

    /**
     * build paramters
     *
     * @param userName  user name
     * @param password  password
     * @param type      data source  type
     * @param database  data source database name
     * @param jdbcUrl   jdbcUrl
     * @param principal principal
     * @param comments  comments
     *
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
        parameterMap.put(Constants.USER, AesUtil.encrypt(userName));
        parameterMap.put(Constants.PASSWORD, AesUtil.encrypt(password));
        if (type == DbType.HIVE) {
            parameterMap.put(Constants.PRINCIPAL, principal);
        }

        if (logger.isDebugEnabled()) {
            logger.info("parameters map:{}", JSONUtils.toJsonString(parameterMap));
        }
        return JSONUtils.toJsonString(parameterMap);
    }
}
