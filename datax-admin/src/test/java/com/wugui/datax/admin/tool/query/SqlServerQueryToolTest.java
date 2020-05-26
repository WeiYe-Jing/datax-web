package com.wugui.datax.admin.tool.query;

import com.wugui.datax.admin.entity.JobDatasource;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

@Slf4j
public class SqlServerQueryToolTest {
    private BaseQueryTool queryTool;
    private JobDatasource jdbcDatasource;

    @Before
    public void before() {
        genMysqlDemo();
        queryTool = QueryToolFactory.getByDbType(jdbcDatasource);
    }

    private void genMysqlDemo() {
        jdbcDatasource = new JobDatasource();
        jdbcDatasource.setDatasourceName("test");
        jdbcDatasource.setJdbcUsername("sa");
        jdbcDatasource.setJdbcPassword("Sa123");
        jdbcDatasource.setJdbcUrl("jdbc:sqlserver://10.20.1.196;databaseName=DataCenter");
        jdbcDatasource.setJdbcDriverClass("com.microsoft.sqlserver.jdbc.SQLServerDriver");
    }

    @Test
    public void getTableNames() {
        List<String> tableNames = queryTool.getTableNames(null);
        tableNames.forEach(System.out::println);
    }

    @Test
    public void getColumnNames() {
        List<String> columns = queryTool.getColumnNames("BD_EMR_TYPE",jdbcDatasource.getJdbcDriverClass());
        log.info(columns.toString());
    }
}