package com.wugui.tool.query;

import com.wugui.dataxweb.entity.JobJdbcDatasource;

import java.sql.SQLException;

/**
 * sql server
 *
 * @author zhouhongfa@gz-yibo.com
 * @version 1.0
 * @since 2019/8/2
 */
public class SqlServerQueryTool extends BaseQueryTool implements QueryToolInterface {
    public SqlServerQueryTool(JobJdbcDatasource jobJdbcDatasource) throws SQLException {
        super(jobJdbcDatasource);
    }
}
