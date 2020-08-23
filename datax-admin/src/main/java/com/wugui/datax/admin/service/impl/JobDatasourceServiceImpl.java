package com.wugui.datax.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wugui.datatx.core.datasource.*;
import com.wugui.datatx.core.enums.DbType;
import com.wugui.datatx.core.util.Constants;
import com.wugui.datatx.core.util.JSONUtils;
import com.wugui.datax.admin.mapper.JobDatasourceMapper;
import com.wugui.datax.admin.entity.JobDatasource;
import com.wugui.datax.admin.service.JobDatasourceService;
import com.wugui.datax.admin.tool.query.BaseQueryTool;
import com.wugui.datax.admin.tool.query.HBaseQueryTool;
import com.wugui.datax.admin.tool.query.MongoDBQueryTool;
import com.wugui.datax.admin.tool.query.QueryToolFactory;
import com.wugui.datax.admin.util.AesUtil;
import com.wugui.datax.admin.util.CommonUtils;
import com.wugui.datax.admin.util.JdbcConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.security.UserGroupInformation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.wugui.datatx.core.util.PropertyUtils.getString;

/**
 * @author  jingwk on 2020/01/30
 */
@Service
@Transactional(readOnly = true)
@Slf4j
public class JobDatasourceServiceImpl extends ServiceImpl<JobDatasourceMapper, JobDatasource> implements JobDatasourceService {

    @Resource
    private JobDatasourceMapper datasourceMapper;

    private static final String TYPE = "type";

    @Override
    public Boolean  dataSourceTest(JobDatasource jobDatasource) throws IOException {
        if (JdbcConstants.HBASE.equals(jobDatasource.getDatasource())) {
            return new HBaseQueryTool(jobDatasource).dataSourceTest();
        }
        String userName = AesUtil.decrypt(jobDatasource.getJdbcUsername());
        //  判断账密是否为密文
        if (userName == null) {
            jobDatasource.setJdbcUsername(AesUtil.encrypt(jobDatasource.getJdbcUsername()));
        }
        String pwd = AesUtil.decrypt(jobDatasource.getJdbcPassword());
        if (pwd == null) {
            jobDatasource.setJdbcPassword(AesUtil.encrypt(jobDatasource.getJdbcPassword()));
        }
        if (JdbcConstants.MONGODB.equals(jobDatasource.getDatasource())) {
            return new MongoDBQueryTool(jobDatasource).dataSourceTest(jobDatasource.getDatabaseName());
        }
        BaseQueryTool queryTool = QueryToolFactory.getByDbType(jobDatasource);
        return queryTool.dataSourceTest();
    }


    @Override
    public int update(JobDatasource datasource) {
        return datasourceMapper.update(datasource);
    }

    @Override
    public List<JobDatasource> selectAllDatasource() {
        return datasourceMapper.selectList(null);
    }


    /**
     * check connection
     *
     * @param type data source type
     * @param parameter data source parameters
     * @return true if connect successfully, otherwise false
     */
    public boolean checkConnection(DbType type, String parameter) {
        Boolean isConnection = false;
        Connection con = getConnection(type, parameter);
        if (con != null) {
            isConnection = true;
            try {
                con.close();
            } catch (SQLException e) {
                log.error("close connection fail at DataSourceService::checkConnection()", e);
            }
        }
        return isConnection;
    }

    /**
     * get connection
     *
     * @param dbType datasource type
     * @param parameter parameter
     * @return connection for datasource
     */
    private Connection getConnection(DbType dbType, String parameter) {
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
                case SPARK:
                    if (CommonUtils.getKerberosStartupState())  {
                        System.setProperty(Constants.JAVA_SECURITY_KRB5_CONF,
                                getString(Constants.JAVA_SECURITY_KRB5_CONF_PATH));
                        Configuration configuration = new Configuration();
                        configuration.set(Constants.HADOOP_SECURITY_AUTHENTICATION, "kerberos");
                        UserGroupInformation.setConfiguration(configuration);
                        UserGroupInformation.loginUserFromKeytab(getString(Constants.LOGIN_USER_KEY_TAB_USERNAME),
                                getString(Constants.LOGIN_USER_KEY_TAB_PATH));
                    }
                    if (dbType == DbType.HIVE){
                        datasource = JSONUtils.parseObject(parameter, HiveDataSource.class);
                    }else if (dbType == DbType.SPARK){
                        datasource = JSONUtils.parseObject(parameter, SparkDataSource.class);
                    }
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
                default:
                    break;
            }

            if(datasource != null){
                connection = DriverManager.getConnection(datasource.getJdbcUrl(), datasource.getUser(), datasource.getPassword());
            }
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        return connection;
    }

    /**
     * build paramters
     *
     * @param type data source  type
     * @param database data source database name
     * @param userName user name
     * @param password password
     * @param principal principal
     * @return datasource parameter
     */
    public String buildParameter(String userName, String password, DbType type, String database,String jdbcUrl,String principal,String comments) {

        Map<String, Object> parameterMap = new LinkedHashMap<String, Object>(6);

        if (CommonUtils.getKerberosStartupState() &&
                (type == DbType.HIVE || type == DbType.SPARK)){
            jdbcUrl += ";principal=" + principal;
        }

        parameterMap.put(Constants.JDBC_URL, jdbcUrl);
        parameterMap.put(Constants.USER, userName);
        parameterMap.put(Constants.PASSWORD, password);
        if (CommonUtils.getKerberosStartupState() &&
                (type == DbType.HIVE || type == DbType.SPARK)){
            parameterMap.put(Constants.PRINCIPAL,principal);
        }

        if(log.isDebugEnabled()){
            log.info("parameters map:{}", JSONUtils.toJsonString(parameterMap));
        }
        return JSONUtils.toJsonString(parameterMap);
    }

}