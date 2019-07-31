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

    private BaseQueryTool readQueryTool;
    private BaseQueryTool writerQueryTool;
    private JobJdbcDatasource readerDatasource;
    private JobJdbcDatasource writerDatasource;

    @Before
    public void before() {
        genMysqlDemo();
        readQueryTool = QueryToolFactory.getByDbType(readerDatasource);
        writerQueryTool = QueryToolFactory.getByDbType(writerDatasource);
    }

    private void genMysqlDemo() {
        readerDatasource = new JobJdbcDatasource();
        readerDatasource.setDatasourceName("z01_mysql_3306");
        readerDatasource.setJdbcUsername("root");
        readerDatasource.setJdbcPassword("root");
        readerDatasource.setJdbcUrl("jdbc:mysql://z01:3306/datax_web?serverTimezone=Asia/Shanghai&useLegacyDatetimeCode=false&useSSL=false&nullNamePatternMatchesAll=true&useUnicode=true&characterEncoding=UTF-8");
        readerDatasource.setJdbcDriverClass("com.mysql.jdbc.Driver");

        writerDatasource = new JobJdbcDatasource();
        writerDatasource.setDatasourceName("z01_mysql_3306");
        writerDatasource.setJdbcUsername("root");
        writerDatasource.setJdbcPassword("root");
        writerDatasource.setJdbcUrl("jdbc:mysql://z01:3306/datax_web_demo?serverTimezone=Asia/Shanghai&useLegacyDatetimeCode=false&useSSL=false&nullNamePatternMatchesAll=true&useUnicode=true&characterEncoding=UTF-8");
        writerDatasource.setJdbcDriverClass("com.mysql.jdbc.Driver");
    }

    @Test
    public void getTableInfo() {
        List<Map<String, Object>> tableInfo = readQueryTool.getTableInfo("datax_plugin");
        tableInfo.forEach(e -> {
            log.info(e.toString());
        });
    }

    @Test
    public void buildTableInfo() {
        TableInfo tableInfo = readQueryTool.buildTableInfo("datax_plugin");
        log.info(tableInfo.toString());
    }

    @Test
    public void getTables() {
        List<Map<String, Object>> tables = readQueryTool.getTables();
        tables.forEach(e -> log.info(e.toString()));
    }
}