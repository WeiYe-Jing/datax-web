package com.wugui.tool.query;

import com.wugui.dataxweb.entity.JobJdbcDatasource;
import com.wugui.tool.database.TableInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

@Slf4j
public class MySQLQueryToolTest {

    private BaseQueryTool queryTool;
    private JobJdbcDatasource jdbcDatasource;

    @Before
    public void before() {
        genMysqlDemo();
        queryTool = QueryToolFactory.getByDbType(jdbcDatasource);
    }

    private void genMysqlDemo() {
        jdbcDatasource = new JobJdbcDatasource();
        jdbcDatasource.setDatasourceName("z01_mysql_3306");
        jdbcDatasource.setJdbcUsername("root");
        jdbcDatasource.setJdbcPassword("root");
        jdbcDatasource.setJdbcUrl("jdbc:mysql://z01:3306/datax_web?serverTimezone=Asia/Shanghai&useLegacyDatetimeCode=false&useSSL=false&nullNamePatternMatchesAll=true&useUnicode=true&characterEncoding=UTF-8");
        jdbcDatasource.setJdbcDriverClass("com.mysql.jdbc.Driver");
    }

    @Test
    public void getTableInfo() {
        List<Map<String, Object>> tableInfo = queryTool.getTableInfo("datax_plugin");
        tableInfo.forEach(e -> {
            log.info(e.toString());
        });
    }

    @Test
    public void buildTableInfo() {
        TableInfo tableInfo = queryTool.buildTableInfo("datax_plugin");
        log.info(tableInfo.toString());
    }

    @Test
    public void getTables() {
        List<Map<String, Object>> tables = queryTool.getTables();
        tables.forEach(e -> log.info(e.toString()));
    }

    @Test
    public void getColumns() {
        List<String> columns = queryTool.getColumnNames("datax_plugin");
        log.info(columns.toString());
    }

    @Test
    public void getTableNames() {
        List<String> tableNames = queryTool.getTableNames();
        tableNames.forEach(System.out::println);
    }
}