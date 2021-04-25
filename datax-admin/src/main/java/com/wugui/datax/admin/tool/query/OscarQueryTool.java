package com.wugui.datax.admin.tool.query;

import com.wugui.datax.admin.entity.JobDatasource;

import java.sql.SQLException;

/**
 * Oscar数据库使用的查询工具
 *
 * @author Locki
 * @date 2021-04-25
 */
public class OscarQueryTool extends BaseQueryTool implements QueryToolInterface {

    public OscarQueryTool(JobDatasource jobDatasource) throws SQLException {
        super(jobDatasource);
    }
}
