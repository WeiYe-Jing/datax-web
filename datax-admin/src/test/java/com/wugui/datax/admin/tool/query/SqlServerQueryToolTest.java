package com.wugui.datax.admin.tool.query;

import com.wugui.datatx.core.enums.DbType;
import com.wugui.datax.admin.entity.JobDatasource;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.wugui.datax.admin.tool.query.DriverConnectionFactory.buildParameter;

@Slf4j
public class SqlServerQueryToolTest {
    private BaseQueryTool queryTool;
    private JobDatasource jdbcDatasource;

    @Before
    public void before() {
        genMysqlDemo();
        queryTool = QueryToolFactory.getByDbType(jdbcDatasource.getType(),jdbcDatasource.getConnectionParams());
    }

    private void genMysqlDemo() {
        jdbcDatasource = new JobDatasource();
        jdbcDatasource.setDatasourceName("test");
        String parameter = buildParameter("sa", "Sa123", DbType.POSTGRESQL, null, "jdbc:sqlserver://10.20.1.196;databaseName=DataCenter", null, null);
        jdbcDatasource.setConnectionParams(parameter);
    }

    @Test
    public void getTableNames() {
        List<String> tableNames = queryTool.getTableNames(null);
        tableNames.forEach(System.out::println);
    }

    @Test
    public void getColumnNames() {
        List<String> columns = queryTool.getColumnNames("BD_EMR_TYPE",jdbcDatasource.getType());
        log.info(columns.toString());
    }
}