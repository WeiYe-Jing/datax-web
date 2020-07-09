package com.wugui.datatx.core.sql;



import com.wugui.datatx.core.datasource.BaseDataSource;
import com.wugui.datatx.core.enums.DbType;
import com.wugui.datatx.core.util.CollectionUtils;
import com.wugui.datatx.core.util.JSONUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.*;

import static com.wugui.datatx.core.enums.DbType.HIVE;
import static com.wugui.datatx.core.util.Constants.*;


/**
 * sql task
 */
public class AbstractSqlTask {

    private static Logger logger = LoggerFactory.getLogger(AbstractSqlTask.class);

    /**
     *  sql parameters
     */
    private SqlParameters sqlParameters;

    /**
     * base datasource
     */
    private BaseDataSource baseDataSource;



    public AbstractSqlTask(String param) {

        logger.info("sql task params {}", param);
        this.sqlParameters = JSONUtils.parseObject(param, SqlParameters.class);

        if (!sqlParameters.checkParameters()) {
            throw new RuntimeException("sql task params is not valid");
        }
    }

    /**
     * create connection
     *
     * @return connection
     * @throws Exception
     */
    private Connection createConnection() throws Exception{
        // if hive , load connection params if exists
        Connection connection = null;
        if (HIVE == DbType.valueOf(sqlParameters.getType())) {
            Properties paramProp = new Properties();
            paramProp.setProperty(USER, baseDataSource.getUser());
            paramProp.setProperty(PASSWORD, baseDataSource.getPassword());
            Map<String, String> connParamMap = CollectionUtils.stringToMap(sqlParameters.getConnParams(),
                    SPLIT_COLON,
                    HIVE_CONF);
            paramProp.putAll(connParamMap);

            connection = DriverManager.getConnection(baseDataSource.getJdbcUrl(),
                    paramProp);
        }else{
            connection = DriverManager.getConnection(baseDataSource.getJdbcUrl(),
                    baseDataSource.getUser(),
                    baseDataSource.getPassword());
        }
        return connection;
    }

    /**
     *  close jdbc resource
     *
     * @param resultSet resultSet
     * @param pstmt pstmt
     * @param connection connection
     */
    private void close(ResultSet resultSet,
                       PreparedStatement pstmt,
                       Connection connection){
        if (resultSet != null){
            try {
                connection.close();
            } catch (SQLException e) {

            }
        }

        if (pstmt != null){
            try {
                connection.close();
            } catch (SQLException e) {

            }
        }

        if (connection != null){
            try {
                connection.close();
            } catch (SQLException e) {

            }
        }
    }
}
