package com.wugui.datax.admin.tool.query;

import com.wugui.datax.admin.entity.JobDatasource;

import java.sql.SQLException;

/**
 * ClickHouse
 */

public class ClickHouseQueryTool extends BaseQueryTool implements QueryToolInterface {
    /**
     * 构造方法
     *
     * @param jobJdbcDatasource
     */
  public ClickHouseQueryTool(JobDatasource jobJdbcDatasource) throws SQLException {
        super(jobJdbcDatasource);
    }
}
