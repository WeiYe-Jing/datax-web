package com.wugui.tool.query;

import com.alibaba.druid.util.MySqlUtils;
import com.google.common.collect.Lists;
import com.wugui.dataxweb.entity.JobJdbcDatasource;

import java.sql.SQLException;
import java.util.List;

/**
 * mysql数据库使用的查询工具
 *
 * @author zhouhongfa@gz-yibo.com
 * @ClassName MySQLQueryTool
 * @Version 1.0
 * @since 2019/7/18 9:31
 */
public class MySQLQueryTool extends BaseQueryTool implements QueryToolInterface {

    public MySQLQueryTool(JobJdbcDatasource codeJdbcDatasource) throws SQLException {
        super(codeJdbcDatasource);
    }

    @Override
    public List<String> getTableNames() {
        List<String> res = Lists.newArrayList();
        //查询
        try {
            res = MySqlUtils.showTables(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }
}
