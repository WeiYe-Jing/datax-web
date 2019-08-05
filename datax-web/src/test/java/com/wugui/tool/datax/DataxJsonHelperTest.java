package com.wugui.tool.datax;

import com.google.common.collect.ImmutableList;
import com.wugui.dataxweb.entity.JobJdbcDatasource;
import com.wugui.tool.datax.writer.StreamWriter;
import com.wugui.tool.util.JSONUtils;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class DataxJsonHelperTest {

    private JobJdbcDatasource getReaderDatasource() {
        JobJdbcDatasource readerDatasource = new JobJdbcDatasource();
        readerDatasource.setDatasourceName("z01_mysql_3306");
        readerDatasource.setJdbcUsername("root");
        readerDatasource.setJdbcPassword("root");
        readerDatasource.setJdbcUrl("jdbc:mysql://localhost:3306/datax_web?serverTimezone=Asia/Shanghai&useLegacyDatetimeCode=false&useSSL=false&nullNamePatternMatchesAll=true&useUnicode=true&characterEncoding=UTF-8");
        readerDatasource.setJdbcDriverClass("com.mysql.jdbc.Driver");
        return readerDatasource;
    }

    private JobJdbcDatasource getWriterDatasource() {
        JobJdbcDatasource writerDatasource = new JobJdbcDatasource();
        writerDatasource.setDatasourceName("z01_mysql_3306");
        writerDatasource.setJdbcUsername("root");
        writerDatasource.setJdbcPassword("root");
        writerDatasource.setJdbcUrl("jdbc:mysql://localhost:3306/datax_web_demo?serverTimezone=Asia/Shanghai&useLegacyDatetimeCode=false&useSSL=false&nullNamePatternMatchesAll=true&useUnicode=true&characterEncoding=UTF-8");
        writerDatasource.setJdbcDriverClass("com.mysql.jdbc.Driver");
        return writerDatasource;
    }

    @Test
    public void buildJob() {
        DataxJsonHelper dataxJsonHelper = new DataxJsonHelper();

        //表名
        List<String> readerTables = ImmutableList.of("datax_plugin");
        List<String> writerTables = ImmutableList.of("datax_plugin");

        //抽取的字段
        List<String> columns = ImmutableList.of("id");

        dataxJsonHelper.initReader(getReaderDatasource(), readerTables, columns);
        dataxJsonHelper.initWriter(getWriterDatasource(), writerTables, columns);
        Map<String, Object> map = dataxJsonHelper.buildJob();
        System.out.println(JSONUtils.formatJson(map));
    }

    @Test
    public void builSetting() {
        DataxJsonHelper dataxJsonHelper = new DataxJsonHelper();
        Map<String, Object> map = dataxJsonHelper.buildSetting();
        System.out.println(JSONUtils.formatJson(map));
    }

    @Test
    public void buildContent() {
        DataxJsonHelper dataxJsonHelper = new DataxJsonHelper();
        dataxJsonHelper.initReader(getReaderDatasource(), ImmutableList.of("datax_plugin"), ImmutableList.of("id"));
        dataxJsonHelper.initWriter(getWriterDatasource(), ImmutableList.of("datax_plugin"), ImmutableList.of("id"));
        Map<String, Object> map = dataxJsonHelper.buildContent();
        System.out.println(JSONUtils.formatJson(map));
    }

    @Test
    public void buildReader() {
        DataxJsonHelper dataxJsonHelper = new DataxJsonHelper();
        dataxJsonHelper.initReader(getReaderDatasource(), ImmutableList.of("datax_plugin"), ImmutableList.of("id"));
//        dataxJsonHelper.addWhereParams("1=1");
        dataxJsonHelper.setQuerySql("select 1");
        Map<String, Object> reader = dataxJsonHelper.buildReader();
        System.out.println(JSONUtils.formatJson(reader));
    }

    @Test
    public void buildWriter() {
        DataxJsonHelper dataxJsonHelper = new DataxJsonHelper();
        dataxJsonHelper.initWriter(getWriterDatasource(), ImmutableList.of("datax_plugin"), ImmutableList.of("id"));
        dataxJsonHelper.setPreSql("delete from datax_prubin");
        Map<String, Object> writer = dataxJsonHelper.buildWriter();
        System.out.println(JSONUtils.formatJson(writer));
    }

    @Test
    public void buildJobWithStreamWriter() {
        DataxJsonHelper dataxJsonHelper = new DataxJsonHelper();
        dataxJsonHelper.setWriterPlugin(new StreamWriter());
        //表名
        List<String> readerTables = ImmutableList.of("datax_plugin");

        //抽取的字段
        List<String> columns = ImmutableList.of("id");

        dataxJsonHelper.initReader(getReaderDatasource(), readerTables, columns);

        Map<String, Object> map = dataxJsonHelper.buildJob();
        System.out.println(JSONUtils.formatJson(map));
    }
}