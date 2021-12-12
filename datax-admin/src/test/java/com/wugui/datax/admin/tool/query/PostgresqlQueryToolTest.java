package com.wugui.datax.admin.tool.query;

import com.wugui.datatx.core.enums.DbType;
import com.wugui.datax.admin.entity.JobDatasource;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.wugui.datax.admin.tool.query.DriverConnectionFactory.buildParameter;

public class PostgresqlQueryToolTest {

    private BaseQueryTool queryTool;
    private JobDatasource jdbcDatasource;

    @Before
    public void before() {
        genDs();
        queryTool = QueryToolFactory.getByDbType(jdbcDatasource.getType(),jdbcDatasource.getConnectionParams());
    }

    private void genDs() {
        jdbcDatasource = new JobDatasource();
        jdbcDatasource.setDatasourceName("test");
        String parameter = buildParameter("postgres", "postgres", DbType.POSTGRESQL, null, "jdbc:postgresql://localhost:5432/data", null, null);
        jdbcDatasource.setConnectionParams(parameter);
    }

    @Test
    public void getTableNames() {
        List<String> tableNames = queryTool.getTableNames(null);
        tableNames.forEach(System.out::println);
    }

    @Test
    public void getTableColumns() {
        List<String> tableNames = queryTool.getColumnNames("BD_EMR_TYPE", null, jdbcDatasource.getType());
        tableNames.forEach(System.out::println);
    }

}