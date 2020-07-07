package com.wugui.datax.admin.tool.query;

import com.wugui.datax.admin.entity.JobDatasource;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.List;

@Slf4j
public class MySQLQueryToolTest {

    private BaseQueryTool queryTool;
    private JobDatasource jdbcDatasource;

    @Before
    public void before() {
        genMysqlDemo();
        queryTool = QueryToolFactory.getByDbType(jdbcDatasource);
    }

    private void genMysqlDemo() {
        jdbcDatasource = new JobDatasource();
        jdbcDatasource.setDatasourceName("z01_mysql_3306");
        jdbcDatasource.setJdbcUsername("root");
        jdbcDatasource.setJdbcPassword("root");
        jdbcDatasource.setJdbcUrl("jdbc:mysql://localhost:3306/datax_web?serverTimezone=Asia/Shanghai&useLegacyDatetimeCode=false&useSSL=false&nullNamePatternMatchesAll=true&useUnicode=true&characterEncoding=UTF-8");
        jdbcDatasource.setJdbcDriverClass("com.mysql.jdbc.Driver");
    }

    @Test
    public void getTableNames() {
        List<String> tableNames = queryTool.getTableNames(null);
        tableNames.forEach(System.out::println);
    }

    @Test
    public void getColumnNames() {
        List<String> columns = queryTool.getColumnNames("job_config",jdbcDatasource.getJdbcDriverClass());
        log.info(columns.toString());
        columns = queryTool.getColumnNames("job_log",jdbcDatasource.getJdbcDriverClass());
        log.info(columns.toString());
    }

    @Test
    public void getColumnsByQuerySql() throws SQLException {
        String querySql = "select l.log_file_path, c.name, c.job_group from job_log l left join job_config c on c.id = l.job_id where l.status = 1";
        List<String> columns = queryTool.getColumnsByQuerySql(querySql);
        log.info(columns.toString());
    }
}