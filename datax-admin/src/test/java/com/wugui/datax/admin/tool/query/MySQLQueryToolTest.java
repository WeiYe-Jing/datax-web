package com.wugui.datax.admin.tool.query;

import com.wugui.datatx.core.enums.DbType;
import com.wugui.datax.admin.entity.JobDatasource;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.List;

import static com.wugui.datax.admin.tool.query.DriverConnectionFactory.buildParameter;

@Slf4j
public class MySQLQueryToolTest {

    private BaseQueryTool queryTool;
    private JobDatasource jobDatasource;

    @Before
    public void before() {
        genMysqlDemo();
        queryTool = QueryToolFactory.getByDbType(jobDatasource.getType(), jobDatasource.getConnectionParams());
    }

    private void genMysqlDemo() {
        jobDatasource = new JobDatasource();
        jobDatasource.setDatasourceName("z01_mysql_3306");
        String parameter = buildParameter("root", "root", DbType.MYSQL, null, "jdbc:mysql://localhost:3306/datax_web?serverTimezone=Asia/Shanghai&useLegacyDatetimeCode=false&useSSL=false&nullNamePatternMatchesAll=true&useUnicode=true&characterEncoding=UTF-8", null, null);
        jobDatasource.setConnectionParams(parameter);
    }

    @Test
    public void getTableNames() {
        List<String> tableNames = queryTool.getTableNames(null);
        tableNames.forEach(System.out::println);
    }

    @Test
    public void getColumnNames() {
        List<String> columns = queryTool.getColumnNames("job_config", jobDatasource.getType());
        log.info(columns.toString());
        columns = queryTool.getColumnNames("job_log", jobDatasource.getType());
        log.info(columns.toString());
    }

    @Test
    public void getColumnsByQuerySql() throws SQLException {
        String querySql = "select l.log_file_path, c.name, c.job_group from job_log l left join job_config c on c.id = l.job_id where l.status = 1";
        List<String> columns = queryTool.getColumnsByQuerySql(querySql);
        log.info(columns.toString());
    }
}