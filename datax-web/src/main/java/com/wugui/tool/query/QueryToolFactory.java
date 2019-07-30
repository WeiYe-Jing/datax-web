package com.wugui.tool.query;

import com.alibaba.druid.util.JdbcConstants;
import com.alibaba.druid.util.JdbcUtils;
import com.wugui.dataxweb.entity.JobJdbcDatasource;

import java.sql.SQLException;

/**
 * 工具类，获取单例实体
 *
 * @author zhouhongfa@gz-yibo.com
 * @ClassName QueryToolFactory
 * @Version 1.0
 * @since 2019/7/18 9:36
 */
public class QueryToolFactory {
    public static final BaseQueryTool getByDbType(JobJdbcDatasource jobJdbcDatasource) {
        //获取dbType
        String dbType = JdbcUtils.getDbType(jobJdbcDatasource.getJdbcUrl(), jobJdbcDatasource.getJdbcDriverClass());
        if (JdbcConstants.MYSQL.equals(dbType.toLowerCase()) || JdbcConstants.MYSQL.equals(dbType.toUpperCase())) {
            return getMySQLQueryToolInstance(jobJdbcDatasource);
        } else if (JdbcConstants.ORACLE.equals(dbType.toLowerCase()) || JdbcConstants.ORACLE.equals(dbType.toUpperCase())) {
            return getOracleQueryToolInstance(jobJdbcDatasource);
        }
        throw new UnsupportedOperationException("找不到该类型: ".concat(dbType));
    }

    private static BaseQueryTool getMySQLQueryToolInstance(JobJdbcDatasource codeJdbcDatasource) {
        try {
            return new MySQLQueryTool(codeJdbcDatasource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new UnsupportedOperationException();
    }

    private static BaseQueryTool getOracleQueryToolInstance(JobJdbcDatasource jobJdbcDatasource) {
        try {
            return new OracleQueryTool(jobJdbcDatasource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new UnsupportedOperationException();
    }
}
