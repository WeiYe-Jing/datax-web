package com.wugui.datax.admin.tool.query;

import com.wugui.datax.admin.entity.JobDatasource;

import java.sql.SQLException;

/**
 * DB2数据库使用的查询工具
 *
 * @author Locki
 * @ClassName DB2SQLQueryTool
 * @Version 1.0
 * @since 2020-09-12 16:18:17
 */
public class DB2SQLQueryTool extends BaseQueryTool implements QueryToolInterface {

    public DB2SQLQueryTool(JobDatasource jobDatasource) throws SQLException {
        super(jobDatasource);
    }

}
