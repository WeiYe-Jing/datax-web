package com.wugui.tool.query;

import com.wugui.dataxweb.entity.JobJdbcDatasource;

import java.sql.SQLException;

/**
 * TODO
 *
 * @author zhouhongfa@gz-yibo.com
 * @ClassName PostgresqlQueryTool
 * @Version 1.0
 * @since 2019/8/2 11:28
 */
public class PostgresqlQueryTool extends BaseQueryTool implements QueryToolInterface {
    public PostgresqlQueryTool(JobJdbcDatasource jobJdbcDatasource) throws SQLException {
        super(jobJdbcDatasource);
    }

}
