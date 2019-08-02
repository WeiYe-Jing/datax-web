package com.wugui.tool.query;

import com.alibaba.druid.util.JdbcUtils;
import com.google.common.collect.Lists;
import com.wugui.dataxweb.entity.JobJdbcDatasource;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    @Override
    public List<String> getTableNames() {
        List<String> res = Lists.newArrayList();
        //获取sql
        String sqlQueryTables = sqlBuilder.getSQLQueryTables();
        logger.info(sqlQueryTables);
        //查询
        try {
            List<Map<String, Object>> maps = JdbcUtils.executeQuery(connection, sqlQueryTables, new ArrayList<>());
//            logger.info(maps.toString());
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
