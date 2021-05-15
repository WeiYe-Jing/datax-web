package com.wugui.datax.admin.tool.query;

import com.alibaba.druid.util.JdbcConstants;
import com.wugui.datatx.core.enums.DbType;
import com.wugui.datax.admin.entity.JobDatasource;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.wugui.datax.admin.tool.query.DriverConnectionFactory.buildParameter;

@Slf4j
public class Hbase20xsqlQueryToolTest {

    private BaseQueryTool queryTool;
    private JobDatasource jdbcDatasource;

    @Before
    public void before() {
        genMysqlDemo();
        queryTool = QueryToolFactory.getByDbType(jdbcDatasource.getType(),jdbcDatasource.getConnectionParams());
    }

    private void genMysqlDemo() {
        jdbcDatasource = new JobDatasource();
        String parameter = buildParameter("", "", DbType.HBASE20XSQL, null, "jdbc:phoenix:hadoop1,hadoop2,hadoop3:2181", null, null);
        jdbcDatasource.setConnectionParams(parameter);
        jdbcDatasource.setDatasourceName(DbType.HBASE20XSQL.getDescp());

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