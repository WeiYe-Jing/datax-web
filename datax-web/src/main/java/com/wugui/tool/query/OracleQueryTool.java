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
 * Oracle数据库使用的查询工具
 *
 * @author zhouhongfa@gz-yibo.com
 * @ClassName MySQLQueryTool
 * @Version 1.0
 * @since 2019/7/18 9:31
 */
public class OracleQueryTool extends BaseQueryTool implements QueryToolInterface {

    public OracleQueryTool(JobJdbcDatasource jobJdbcDatasource) throws SQLException {
        super(jobJdbcDatasource);
    }

    @Override
    public List<Map<String, Object>> getTableInfo(String tableName) {
        String sqlQueryTableNameComment = sqlBuilder.getSQLQueryTableNameComment();
        logger.info(sqlQueryTableNameComment);
        List<Map<String, Object>> res = null;
        try {
            res = JdbcUtils.executeQuery(connection, sqlQueryTableNameComment, ImmutableList.of(tableName));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    @Override
    public List<Map<String, Object>> getTables() {
        String sqlQueryTablesNameComments = sqlBuilder.getSQLQueryTablesNameComments();
        logger.info(sqlQueryTablesNameComments);
        List<Map<String, Object>> res = null;
        try {
            res = JdbcUtils.executeQuery(connection, sqlQueryTablesNameComments, Lists.newArrayList());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    @Override
    public List<String> getTableNames() {
        {
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
}
