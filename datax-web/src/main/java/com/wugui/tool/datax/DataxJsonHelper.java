package com.wugui.tool.datax;

import com.alibaba.druid.util.JdbcConstants;
import com.alibaba.druid.util.JdbcUtils;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.wugui.dataxweb.entity.JobJdbcDatasource;
import com.wugui.tool.datax.reader.MysqlReader;
import com.wugui.tool.datax.reader.OracleReader;
import com.wugui.tool.datax.reader.PostgresqlReader;
import com.wugui.tool.datax.reader.SqlServerReader;
import com.wugui.tool.datax.writer.MysqlWriter;
import com.wugui.tool.datax.writer.OraclelWriter;
import com.wugui.tool.datax.writer.PostgresqllWriter;
import com.wugui.tool.datax.writer.SqlServerlWriter;
import com.wugui.tool.pojo.DataxPluginPojo;

import java.util.List;
import java.util.Map;

/**
 * 构建 datax json的工具类
 *
 * @author zhouhongfa@gz-yibo.com
 * @ClassName DataxJsonHelper
 * @Version 1.0
 * @since 2019/7/30 22:24
 */
public class DataxJsonHelper implements DataxJsonInterface {

    /**
     * 读取的表，根据datax示例，支持多个表（先不考虑，后面再去实现， 这里先用list保存吧）
     * <p>
     * 目的表的表名称。支持写入一个或者多个表。当配置为多张表时，必须确保所有表结构保持一致
     */
    private List<String> readerTables;

    /**
     * 读取的字段列表
     */
    private List<String> readerColumns;

    /**
     * reader jdbc 数据源
     */
    private JobJdbcDatasource readerDatasource;

    /**
     * writer jdbc 数据源
     */
    private JobJdbcDatasource writerDatasource;

    /**
     * 写入的表
     */
    private List<String> writerTables;

    /**
     * 写入的字段列表
     */
    private List<String> writerColumns;

    private String querySql;

    private String preSql;

    private BaseDataxPlugin readerPlugin;

    private BaseDataxPlugin writerPlugin;

    //用于保存额外参数
    private Map<String, Object> extraParams = Maps.newHashMap();

    public void initReader(JobJdbcDatasource readerDatasource, List<String> readerTables, List<String> readerColumns) {
        this.readerTables = readerTables;
        this.readerColumns = readerColumns;
        this.readerDatasource = readerDatasource;
        // reader 插件
        String readerDbType = JdbcUtils.getDbType(readerDatasource.getJdbcUrl(), readerDatasource.getJdbcDriverClass());
        if (JdbcConstants.MYSQL.equals(readerDbType)) {
            readerPlugin = new MysqlReader();
        } else if (JdbcConstants.ORACLE.equals(readerDbType)) {
            readerPlugin = new OracleReader();
        } else if (JdbcConstants.SQL_SERVER.equals(readerDbType)) {
            readerPlugin = new SqlServerReader();
        } else if (JdbcConstants.POSTGRESQL.equals(readerDbType)) {
            readerPlugin = new PostgresqlReader();
        }
        readerPlugin.setExtraParams(extraParams);
    }

    public void initWriter(JobJdbcDatasource writerDatasource, List<String> writerTables, List<String> writerColumns) {
        this.writerDatasource = writerDatasource;
        this.writerTables = writerTables;
        this.writerColumns = writerColumns;
        // writer
        String writerDbType = JdbcUtils.getDbType(writerDatasource.getJdbcUrl(), writerDatasource.getJdbcDriverClass());
        if (JdbcConstants.MYSQL.equals(writerDbType)) {
            writerPlugin = new MysqlWriter();
        } else if (JdbcConstants.ORACLE.equals(writerDbType)) {
            writerPlugin = new OraclelWriter();
        } else if (JdbcConstants.SQL_SERVER.equals(writerDbType)) {
            writerPlugin = new SqlServerlWriter();
        } else if (JdbcConstants.POSTGRESQL.equals(writerDbType)) {
            writerPlugin = new PostgresqllWriter();
        }
    }

    @Override
    public Map<String, Object> buildJob() {

        Map<String, Object> res = Maps.newLinkedHashMap();

        Map<String, Object> jobMap = Maps.newLinkedHashMap();
        jobMap.put("setting", buildSetting());
        jobMap.put("content", ImmutableList.of(buildContent()));

        res.put("job", jobMap);
        return res;
    }

    @Override
    public Map<String, Object> buildSetting() {
        Map<String, Object> res = Maps.newLinkedHashMap();
        Map<String, Object> speedMap = Maps.newLinkedHashMap();
        Map<String, Object> errorLimitMap = Maps.newLinkedHashMap();
        speedMap.putAll(ImmutableMap.of("channel", 3));
        errorLimitMap.putAll(ImmutableMap.of("record", 0, "percentage", 0.02));
        res.put("speed", speedMap);
        res.put("errorLimit", errorLimitMap);
        return res;
    }

    @Override
    public Map<String, Object> buildContent() {
        Map<String, Object> res = Maps.newLinkedHashMap();
        res.put("reader", buildReader());
        res.put("writer", buildWriter());
        return res;
    }

    @Override
    public Map<String, Object> buildReader() {

        DataxPluginPojo dataxPluginPojo = new DataxPluginPojo();
        dataxPluginPojo.setJdbcDatasource(readerDatasource);
        dataxPluginPojo.setTables(readerTables);
        dataxPluginPojo.setColumns(readerColumns);
        dataxPluginPojo.setQuerySql(querySql);

        return readerPlugin.build(dataxPluginPojo);
    }

    @Override
    public Map<String, Object> buildWriter() {
        DataxPluginPojo dataxPluginPojo = new DataxPluginPojo();
        dataxPluginPojo.setJdbcDatasource(writerDatasource);
        dataxPluginPojo.setTables(writerTables);
        dataxPluginPojo.setColumns(writerColumns);
        dataxPluginPojo.setPreSql(preSql);

        return writerPlugin.build(dataxPluginPojo);
    }

    public void setWriterPlugin(BaseDataxPlugin writerPlugin) {
        this.writerPlugin = writerPlugin;
    }

    public void addWhereParams(String params) {
        extraParams.put("where", params);
    }

    public void setQuerySql(String querySql) {
        this.querySql = querySql;
    }

    public void setPreSql(String preSql) {
        this.preSql = preSql;
    }
}
