package com.wugui.datax.admin.tool.datax;

import com.alibaba.druid.util.JdbcConstants;
import com.alibaba.druid.util.JdbcUtils;
import com.beust.jcommander.internal.Lists;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.wugui.datax.admin.dto.*;
import com.wugui.datax.admin.entity.JobJdbcDatasource;
import com.wugui.datax.admin.tool.datax.reader.*;
import com.wugui.datax.admin.tool.datax.writer.*;
import com.wugui.datax.admin.tool.pojo.DataxHivePojo;
import com.wugui.datax.admin.tool.pojo.DataxRdbmsPojo;
import com.wugui.datax.admin.util.Constant;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * 构建 com.wugui.datax json的工具类
 *
 * @author zhouhongfa@gz-yibo.com
 * @ClassName DataxJsonHelper
 * @Version 1.0
 * @since 2019/7/30 22:24
 */
@Data
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

    private Map<String, Object> buildReader;

    private Map<String, Object> buildWriter;

    private BaseDataxPlugin readerPlugin;

    private BaseDataxPlugin writerPlugin;

    private HiveReaderDto hiveReaderDto;

    private HiveWriterDto hiveWriterDto;

    private RdbmsReaderDto rdbmsReaderDto;

    private RdbmsWriterDto rdbmsWriterDto;


    //用于保存额外参数
    private Map<String, Object> extraParams = Maps.newHashMap();

    public void initReader(DataxJsonDto dataxJsonDto, JobJdbcDatasource readerDatasource) {
        this.readerDatasource = readerDatasource;
        this.readerTables = dataxJsonDto.getReaderTables();
        this.readerColumns = dataxJsonDto.getReaderColumns();
        this.hiveReaderDto = dataxJsonDto.getHiveReader();
        this.rdbmsReaderDto = dataxJsonDto.getRdbmsReader();
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
        } else if (JdbcConstants.HIVE.equals(readerDbType)) {
            readerPlugin = new HiveReader();
            buildReader = buildHiveReader();
        }
        if(!JdbcConstants.HIVE.equals(readerDbType)){
            buildReader = this.buildReader();
        }

    }

    public void initWriter(DataxJsonDto dataxJsonDto, JobJdbcDatasource readerDatasource) {
        this.writerDatasource = readerDatasource;
        this.writerTables = dataxJsonDto.getWriterTables();
        this.writerColumns = dataxJsonDto.getWriterColumns();
        this.hiveWriterDto = dataxJsonDto.getHiveWriter();
        this.rdbmsWriterDto = dataxJsonDto.getRdbmsWriter();
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
        } else if (JdbcConstants.HIVE.equals(writerDbType)) {
            writerPlugin = new HiveWriter();
            buildWriter = this.buildHiveWriter();
        }
        if(!JdbcConstants.HIVE.equals(writerDbType)){
            buildWriter = this.buildWriter();
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
        res.put("reader", this.buildReader);
        res.put("writer", this.buildWriter);
        return res;
    }

    @Override
    public Map<String, Object> buildReader() {
        DataxRdbmsPojo dataxPluginPojo = new DataxRdbmsPojo();
        dataxPluginPojo.setJdbcDatasource(readerDatasource);
        dataxPluginPojo.setTables(readerTables);
        dataxPluginPojo.setRdbmsColumns(readerColumns);
        dataxPluginPojo.setSplitPk(rdbmsReaderDto.getReaderSplitPk());
        if (StringUtils.isNotBlank(rdbmsReaderDto.getQuerySql())) {
            dataxPluginPojo.setQuerySql(rdbmsReaderDto.getQuerySql());
        }
        //where
        if (StringUtils.isNotBlank(rdbmsReaderDto.getWhereParams())) {
            dataxPluginPojo.setWhereParam(rdbmsReaderDto.getWhereParams());
        }
        return readerPlugin.build(dataxPluginPojo);
    }

    @Override
    public Map<String, Object> buildHiveReader() {
        DataxHivePojo dataxHivePojo = new DataxHivePojo();
        dataxHivePojo.setJdbcDatasource(readerDatasource);
        List<Map<String, String>> columns = Lists.newArrayList();
        readerColumns.forEach(c -> {
            Map<String, String> column = Maps.newLinkedHashMap();
            column.put("name", c.split(Constant.SPLIT_SCOLON)[0]);
            column.put("type", c.split(Constant.SPLIT_SCOLON)[1]);
            columns.add(column);
        });
        dataxHivePojo.setColumns(columns);
        dataxHivePojo.setReaderDefaultFS(hiveReaderDto.getReaderDefaultFS());
        dataxHivePojo.setReaderFieldDelimiter(hiveReaderDto.getReaderFieldDelimiter());
        dataxHivePojo.setReaderFileType(hiveReaderDto.getReaderFileType());
        dataxHivePojo.setReaderPath(hiveReaderDto.getReaderPath());
        return readerPlugin.buildHive(dataxHivePojo);
    }

    @Override
    public Map<String, Object> buildWriter() {
        DataxRdbmsPojo dataxPluginPojo = new DataxRdbmsPojo();
        dataxPluginPojo.setJdbcDatasource(writerDatasource);
        dataxPluginPojo.setTables(writerTables);
        dataxPluginPojo.setRdbmsColumns(writerColumns);
        dataxPluginPojo.setPreSql(rdbmsWriterDto.getPreSql());
        return writerPlugin.build(dataxPluginPojo);
    }

    @Override
    public Map<String, Object> buildHiveWriter() {
        DataxHivePojo dataxHivePojo = new DataxHivePojo();
        dataxHivePojo.setJdbcDatasource(writerDatasource);
        List<Map<String, String>> columns = Lists.newArrayList();
        writerColumns.forEach(c -> {
            Map<String, String> column = Maps.newLinkedHashMap();
            column.put("name", c.split(Constant.SPLIT_SCOLON)[0]);
            column.put("type", c.split(Constant.SPLIT_SCOLON)[1]);
            columns.add(column);
        });
        dataxHivePojo.setColumns(columns);
        dataxHivePojo.setWriterDefaultFS(hiveWriterDto.getWriterDefaultFS());
        dataxHivePojo.setWriteFieldDelimiter(hiveWriterDto.getWriteFieldDelimiter());
        dataxHivePojo.setWriterFileType(hiveWriterDto.getWriterFileType());
        dataxHivePojo.setWriterPath(hiveWriterDto.getWriterPath());
        dataxHivePojo.setWriteMode(hiveWriterDto.getWriteMode());
        dataxHivePojo.setWriterFileName(hiveWriterDto.getWriterFileName());
        return writerPlugin.buildHive(dataxHivePojo);
    }
}
