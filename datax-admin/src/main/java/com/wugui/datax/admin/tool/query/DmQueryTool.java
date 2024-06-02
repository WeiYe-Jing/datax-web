package com.wugui.datax.admin.tool.query;


import com.wugui.datax.admin.entity.JobDatasource;

import java.sql.SQLException;


/**
 * <p>
 * 达梦数据库使用的查询工具
 * </p>
 *
 * @author Listenabe
 * @since 2024/06/02
 */
public class DmQueryTool extends BaseQueryTool implements QueryToolInterface {


    DmQueryTool(JobDatasource jobDatasource) throws SQLException {
        super(jobDatasource);
    }
}
