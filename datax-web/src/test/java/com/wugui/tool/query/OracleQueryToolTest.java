package com.wugui.tool.query;

import com.wugui.dataxweb.entity.JobJdbcDatasource;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

@Slf4j
public class OracleQueryToolTest {

    private BaseQueryTool queryTool;
    private JobJdbcDatasource jdbcDatasource;

    @Before
    public void before() {
        genMysqlDemo();
        queryTool = QueryToolFactory.getByDbType(jdbcDatasource);
    }

    private void genMysqlDemo() {
        jdbcDatasource = new JobJdbcDatasource();
        jdbcDatasource.setDatasourceName("test");
        jdbcDatasource.setJdbcUsername("scott");
        jdbcDatasource.setJdbcPassword("tiger");
        jdbcDatasource.setJdbcUrl("jdbc:oracle:thin:@localhost:1521/orcl");
        jdbcDatasource.setJdbcDriverClass("oracle.jdbc.OracleDriver");
    }

    @Test
    public void getTableNames() {
        List<String> tableNames = queryTool.getTableNames();
        tableNames.forEach(System.out::println);
    }

    @Test
    public void getColumnNames() {
        List<String> columns = queryTool.getColumnNames("EMP");
        log.info(columns.toString());
    }
}