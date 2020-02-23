package com.wugui.datax.admin.tool.query;

import com.wugui.datax.admin.entity.JobJdbcDatasource;

import java.sql.SQLException;

public class HbaseQueryTool extends BaseQueryTool implements QueryToolInterface {
  public HbaseQueryTool(JobJdbcDatasource jdbcDatasource) throws SQLException {
    super(jdbcDatasource);
  }
}
