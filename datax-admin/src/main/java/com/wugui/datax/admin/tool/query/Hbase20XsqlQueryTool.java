package com.wugui.datax.admin.tool.query;

import com.google.common.collect.Lists;
import com.wugui.datax.admin.core.util.LocalCacheUtil;
import com.wugui.datax.admin.entity.JobDatasource;
import com.wugui.datax.admin.util.JdbcUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


/**
 * @author jingwk
 */
public class Hbase20XsqlQueryTool extends BaseQueryTool implements QueryToolInterface {
    Connection conn = null;


    /**
     * 构造方法
     *
     * @param jobJdbcDatasource
     */
    public Hbase20XsqlQueryTool(JobDatasource jobJdbcDatasource) throws SQLException {
        super(jobJdbcDatasource);

        if (LocalCacheUtil.get(jobJdbcDatasource.getDatasourceName()) == null) {
            getDataSource(jobJdbcDatasource);
        } else {
            conn = (Connection) LocalCacheUtil.get(jobJdbcDatasource.getDatasourceName());
            if (conn == null) {
                LocalCacheUtil.remove(jobJdbcDatasource.getDatasourceName());
            }
        }
        LocalCacheUtil.set(jobJdbcDatasource.getDatasourceName(), conn, 4 * 60 * 60 * 1000);

    }

    @Override
    public List<String> getTableNames(String tableSchema) {
        DatabaseMetaData metaData;
        List<String> tables = new ArrayList<>();
        ResultSet rs = null;
        try {
            metaData = conn.getMetaData();
            rs = metaData.getTables(conn.getCatalog(), null, "%", new String[]{"TABLE"});
            while (rs.next()) {
                tables.add(rs.getString("TABLE_NAME"));
            }

        } catch (SQLException e) {
            logger.error("[getTableNames Exception] --> the exception message is:{}", e.getMessage());
        } finally {
            JdbcUtils.close(rs);
        }
        return tables;
    }

    @Override
    public List<String> getColumnNames(String tableName, String dataSource) {
        DatabaseMetaData metaData;
        List<String> columnNames = Lists.newArrayList();
        ResultSet rs = null;
        try {
            metaData = conn.getMetaData();
            rs = metaData.getColumns(conn.getCatalog(), null, tableName, "%");
            while (rs.next()) {
                columnNames.add(rs.getString("COLUMN_NAME"));
                // 获取字段的数据类型  rs.getString("TYPE_NAME")
            }

        } catch (SQLException e) {
            logger.error("[getColumnNames Exception] --> the exception message is:{}", e.getMessage());
        } finally {
            JdbcUtils.close(rs);
        }
        return columnNames;
    }


    private static int getSize(ResultSet rs) {
        try {
            if (rs.getType() == ResultSet.TYPE_FORWARD_ONLY) {
                return -1;
            }
            rs.last();
            int total = rs.getRow();
            rs.beforeFirst();
            return total;
        } catch (SQLException e) {
            return -1;
        } catch (AbstractMethodError e) {
            return -1;
        }
    }


    private void getDataSource(JobDatasource jobDatasource) throws SQLException {
        conn = DriverManager.getConnection(jobDatasource.getJdbcUrl());
    }


}
