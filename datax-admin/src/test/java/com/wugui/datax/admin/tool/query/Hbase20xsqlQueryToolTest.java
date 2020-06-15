package com.wugui.datax.admin.tool.query;

import com.wugui.datax.admin.entity.JobDatasource;
import com.wugui.datax.admin.util.JdbcConstants;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

@Slf4j
public class Hbase20xsqlQueryToolTest {

    private BaseQueryTool queryTool;
    private JobDatasource jdbcDatasource;

    @Before
    public void before() {
        genMysqlDemo();
        queryTool = QueryToolFactory.getByDbType(jdbcDatasource);
    }

    private void genMysqlDemo() {
        jdbcDatasource = new JobDatasource();
        jdbcDatasource.setDatasource(JdbcConstants.HBASE20XSQL);
        jdbcDatasource.setDatasourceName(JdbcConstants.HBASE20XSQL);
        jdbcDatasource.setJdbcDriverClass(JdbcConstants.HBASE20XSQL_DRIVER);
        jdbcDatasource.setJdbcUrl("jdbc:phoenix:hadoop1,hadoop2,hadoop3:2181");
    }

    @Test
    public void getTableNames() {
        List<String> tableNames = queryTool.getTableNames(null);
        for (String table : tableNames) {
            System.out.println(table);
        }
    }

    @Test
    public void getColumnNames() {
        List<String> columns = queryTool.getColumnNames("STOCK_SYMBOL", null);

        for (String column : columns) {
            System.out.println(column);
        }
    }


}