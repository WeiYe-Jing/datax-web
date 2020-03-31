package com.wugui.datax.admin.tool.query;

import com.wugui.datax.admin.entity.JobDatasource;

import java.sql.SQLException;

/**
 * hive
 *
 * @author wenkaijing
 * @version 2.0
 * @since 2020/01/05
 */
public class HiveQueryTool extends BaseQueryTool implements QueryToolInterface {
    public HiveQueryTool(JobDatasource jobDatasource) throws SQLException {
        super(jobDatasource);
    }
}
