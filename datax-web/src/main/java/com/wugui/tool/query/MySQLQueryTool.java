package com.wugui.tool.query;

import com.alibaba.druid.util.JdbcUtils;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.wugui.dataxweb.entity.JobJdbcDatasource;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        //获取sql
        String sqlQueryTables = sqlBuilder.getSQLQueryTables();
        logger.info(sqlQueryTables);
        //查询
        try {
            List<Map<String, Object>> maps = JdbcUtils.executeQuery(connection, sqlQueryTables, ImmutableList.of(getSchema()));
//            // 只取value即可
            maps.forEach((k) -> {
                String tName = (String) new ArrayList<>(k.values()).get(0);
                res.add(tName);
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }
}
