package com.wugui.datax.admin.tool.datax;

import com.wugui.datax.admin.entity.JobDatasource;

public class DataxJsonHelperTest {

    private JobDatasource getReaderDatasource() {
        JobDatasource readerDatasource = new JobDatasource();
        readerDatasource.setDatasourceName("z01_mysql_3306");
        readerDatasource.setJdbcUsername("root");
        readerDatasource.setJdbcPassword("root");
        readerDatasource.setJdbcUrl("jdbc:mysql://localhost:3306/datax_web?serverTimezone=Asia/Shanghai&useLegacyDatetimeCode=false&useSSL=false&nullNamePatternMatchesAll=true&useUnicode=true&characterEncoding=UTF-8");
        readerDatasource.setJdbcDriverClass("com.mysql.jdbc.Driver");
        return readerDatasource;
    }

    private JobDatasource getWriterDatasource() {
        JobDatasource writerDatasource = new JobDatasource();
        writerDatasource.setDatasourceName("z01_mysql_3306");
        writerDatasource.setJdbcUsername("root");
        writerDatasource.setJdbcPassword("root");
        writerDatasource.setJdbcUrl("jdbc:mysql://localhost:3306/datax_web_demo?serverTimezone=Asia/Shanghai&useLegacyDatetimeCode=false&useSSL=false&nullNamePatternMatchesAll=true&useUnicode=true&characterEncoding=UTF-8");
        writerDatasource.setJdbcDriverClass("com.mysql.jdbc.Driver");
        return writerDatasource;
    }

}
