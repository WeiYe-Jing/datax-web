package com.wugui.datax.admin.tool.datax;


import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wugui.datatx.core.datasource.HBaseDataSource;
import com.wugui.datatx.core.datasource.MongoDBDataSource;
import com.wugui.datatx.core.enums.DbType;
import com.wugui.datatx.core.util.Constants;
import com.wugui.datatx.core.util.JSONUtils;
import com.wugui.datax.admin.dto.*;
import com.wugui.datax.admin.entity.JobDatasource;
import com.wugui.datax.admin.tool.datax.reader.*;
import com.wugui.datax.admin.tool.datax.writer.*;
import com.wugui.datax.admin.tool.pojo.*;
import com.wugui.datax.admin.util.StringUtil;
import com.wugui.datax.admin.util.TransformerUtil;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.util.TextUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 构建 com.wugui.datax json的工具类
 *
 * @author jingwk
 * @ClassName DataxJsonHelper
 * @Version 2.1.1
 * @since 2020/03/14 08:24
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
    private JobDatasource readerDatasource;
    /**
     * writer jdbc 数据源
     */
    private JobDatasource writerDatasource;
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

    private HiveReaderDTO hiveReaderDto;

    private HiveWriterDTO hiveWriterDto;

    private HbaseReaderDTO hbaseReaderDto;

    private HbaseWriterDTO hbaseWriterDto;

    private RdbmsReaderDTO rdbmsReaderDto;

    private RdbmsWriterDTO rdbmsWriterDto;

    private MongoDBReaderDTO mongoDBReaderDto;

    private MongoDBWriterDTO mongoDBWriterDto;

    private List<DataXTransformer> transformers = new ArrayList<>();


    //用于保存额外参数
    private Map<String, Object> extraParams = Maps.newHashMap();

    public void initReader(DataXJsonBuildDTO dataxJsonDto, JobDatasource readerDatasource) {

        this.readerDatasource = readerDatasource;
        this.readerTables = dataxJsonDto.getReaderTables();
        this.readerColumns = dataxJsonDto.getReaderColumns();
        this.hiveReaderDto = dataxJsonDto.getHiveReader();
        this.rdbmsReaderDto = dataxJsonDto.getRdbmsReader();
        this.hbaseReaderDto = dataxJsonDto.getHbaseReader();
        DbType dbType = readerDatasource.getType();
        this.readerColumns = convertKeywordsColumns(dbType, this.readerColumns);
        switch (dbType) {
            case MYSQL:
                readerPlugin = new MysqlReader();
                buildReader = buildReader();
            case SQLSERVER:
                readerPlugin = new SqlServerReader();
                buildReader = buildReader();
            case POSTGRESQL:
            case GREENPLUM:
                readerPlugin = new PostgresqlReader();
                buildReader = buildReader();
            case ORACLE:
                readerPlugin = new OracleReader();
                buildReader = buildReader();
            case CLICKHOUSE:
                readerPlugin = new ClickHouseReader();
                buildReader = buildReader();
            case HIVE:
                readerPlugin = new HBaseReader();
                buildReader = buildHBaseReader();
            case MONGODB:
                readerPlugin = new MongoDBReader();
                buildReader = buildMongoDBReader();
            case HBASE:
                readerPlugin = new HBaseReader();
                buildReader = buildHBaseReader();
        }
    }

    public void initWriter(DataXJsonBuildDTO dataxJsonDto, JobDatasource readerDatasource) {
        this.writerDatasource = readerDatasource;
        this.writerTables = dataxJsonDto.getWriterTables();
        this.writerColumns = dataxJsonDto.getWriterColumns();
        this.hiveWriterDto = dataxJsonDto.getHiveWriter();
        this.rdbmsWriterDto = dataxJsonDto.getRdbmsWriter();
        this.hbaseWriterDto = dataxJsonDto.getHbaseWriter();
        this.mongoDBWriterDto = dataxJsonDto.getMongoDBWriter();
        DbType dbType = readerDatasource.getType();
        this.writerColumns = convertKeywordsColumns(dbType, this.writerColumns);
        switch (dbType) {
            case MYSQL:
                writerPlugin = new MysqlWriter();
                buildWriter = this.buildWriter();
            case SQLSERVER:
                writerPlugin = new SqlServerlWriter();
                buildWriter = this.buildWriter();
            case POSTGRESQL:
                writerPlugin = new PostgresqllWriter();
                buildWriter = this.buildWriter();
            case GREENPLUM:
                writerPlugin = new GreenPlumWriter();
                buildWriter = this.buildWriter();
            case ORACLE:
                writerPlugin = new OraclelWriter();
                buildWriter = this.buildWriter();
            case CLICKHOUSE:
                writerPlugin = new ClickHouseWriter();
                buildWriter = buildWriter();
            case HIVE:
                writerPlugin = new HiveWriter();
                buildWriter = this.buildHiveWriter();
            case MONGODB:
                writerPlugin = new MongoDBWriter();
                buildWriter = this.buildMongoDBWriter();
            case HBASE:
                writerPlugin = new HBaseWriter();
                buildWriter = this.buildHBaseWriter();
        }
    }


    /**
     * 初始化脱敏规则
     * 暂时实现
     * 1.对字段进行MD5脱敏
     * 2.对字段替换换行符
     *
     * @param dataXJsonBuildDto
     */
    public void initTransformer(DataXJsonBuildDTO dataXJsonBuildDto) {
        if (null == dataXJsonBuildDto.getTransformer() || dataXJsonBuildDto.getTransformer().size() == 0) {
            return;
        }
        for (int i = 0; i < dataXJsonBuildDto.getTransformer().size(); i++) {
            if (TextUtils.isBlank(TransformerUtil.getTransformerName(dataXJsonBuildDto.getTransformer().get(i)))) {
                continue;
            }
            DataXTransformer t = new DataXTransformer();
            t.setName(TransformerUtil.getTransformerName(dataXJsonBuildDto.getTransformer().get(i)));
            DataXTransformer.Parameter p = new DataXTransformer.Parameter();
            List<String> paras = new ArrayList<>();
            if ("replaceNewLineSymbol".equals(t.getName())) {
                paras.add("");
                p.setColumnIndex(i);

            } else if ("md5".equals(t.getName())) {
                paras.add("");
                p.setColumnIndex(i);

            }
            p.setParas(paras);
            t.setParameter(p);
            transformers.add(t);
        }
    }


    private List<String> convertKeywordsColumns(DbType dbType, List<String> columns) {
        if (columns == null) {
            return null;
        }

        List<String> toColumns = new ArrayList<>();
        columns.forEach(s -> {
            toColumns.add(doConvertKeywordsColumn(dbType, s));
        });
        return toColumns;
    }

    private String doConvertKeywordsColumn(DbType dbType, String column) {
        if (column == null) {
            return null;
        }

        column = column.trim();
        column = column.replace("[", "");
        column = column.replace("]", "");
        column = column.replace("`", "");
        column = column.replace("\"", "");
        column = column.replace("'", "");

        switch (dbType) {
            case MYSQL:
                return String.format("`%s`", column);
            case SQLSERVER:
                return String.format("[%s]", column);
            case POSTGRESQL:
            case ORACLE:
                return String.format("\"%s\"", column);
            default:
                return column;
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
        speedMap.putAll(ImmutableMap.of("channel", 3, "byte", 1048576));
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
        if (transformers.size() > 0) {
            res.put("transformer", transformers);
        }
        return res;
    }

    @Override
    public Map<String, Object> buildReader() {
        DataxRdbmsPojo dataxPluginPojo = new DataxRdbmsPojo();
        dataxPluginPojo.setJobDatasource(readerDatasource);
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
        List<Map<String, Object>> columns = Lists.newArrayList();
        readerColumns.forEach(c -> {
            Map<String, Object> column = Maps.newLinkedHashMap();
            column.put("index", c.split(Constants.SPLIT_SCOLON)[0]);
            column.put("type", c.split(Constants.SPLIT_SCOLON)[2]);
            columns.add(column);
        });
        dataxHivePojo.setColumns(columns);
        dataxHivePojo.setReaderDefaultFS(hiveReaderDto.getReaderDefaultFS());
        dataxHivePojo.setReaderFieldDelimiter(StringUtil.unicode2String(hiveReaderDto.getReaderFieldDelimiter()));
        dataxHivePojo.setReaderFileType(hiveReaderDto.getReaderFileType());
        dataxHivePojo.setReaderPath(hiveReaderDto.getReaderPath());
        dataxHivePojo.setSkipHeader(hiveReaderDto.getReaderSkipHeader());
        return readerPlugin.buildHive(dataxHivePojo);
    }

    @Override
    public Map<String, Object> buildHBaseReader() {
        DataxHbasePojo dataxHbasePojo = new DataxHbasePojo();
        dataxHbasePojo.setJdbcDatasource(readerDatasource);
        List<Map<String, Object>> columns = Lists.newArrayList();
        for (int i = 0; i < readerColumns.size(); i++) {
            Map<String, Object> column = Maps.newLinkedHashMap();
            column.put("name", readerColumns.get(i));
            column.put("type", "string");
            columns.add(column);
        }
        dataxHbasePojo.setColumns(columns);
        HBaseDataSource HBaseDataSource = JSONUtils.parseObject(readerDatasource.getConnectionParams(), HBaseDataSource.class);
        dataxHbasePojo.setReaderHbaseConfig(HBaseDataSource.getZkAddress());
        String readerTable = !CollectionUtils.isEmpty(readerTables) ? readerTables.get(0) : Constants.STRING_BLANK;
        dataxHbasePojo.setReaderTable(readerTable);
        dataxHbasePojo.setReaderMode(hbaseReaderDto.getReaderMode());
        dataxHbasePojo.setReaderRange(hbaseReaderDto.getReaderRange());
        return readerPlugin.buildHbase(dataxHbasePojo);
    }

    @Override
    public Map<String, Object> buildMongoDBReader() {
        DataxMongoDBPojo dataxMongoDBPojo = new DataxMongoDBPojo();

        dataxMongoDBPojo.setJdbcDatasource(readerDatasource);
        List<Map<String, Object>> columns = Lists.newArrayList();
        buildColumns(readerColumns, columns);
        dataxMongoDBPojo.setColumns(columns);
        MongoDBDataSource mongoDBDataSource = JSONUtils.parseObject(readerDatasource.getConnectionParams(), MongoDBDataSource.class);
        dataxMongoDBPojo.setAddress(mongoDBDataSource.getMongoClientURI());
        dataxMongoDBPojo.setDbName(mongoDBDataSource.getDatabase());
        dataxMongoDBPojo.setReaderTable(readerTables.get(0));
        return readerPlugin.buildMongoDB(dataxMongoDBPojo);
    }


    @Override
    public Map<String, Object> buildWriter() {
        DataxRdbmsPojo dataxPluginPojo = new DataxRdbmsPojo();
        dataxPluginPojo.setJobDatasource(writerDatasource);
        dataxPluginPojo.setTables(writerTables);
        dataxPluginPojo.setRdbmsColumns(writerColumns);
        dataxPluginPojo.setPreSql(rdbmsWriterDto.getPreSql());
        dataxPluginPojo.setPostSql(rdbmsWriterDto.getPostSql());
        return writerPlugin.build(dataxPluginPojo);
    }

    @Override
    public Map<String, Object> buildHiveWriter() {
        DataxHivePojo dataxHivePojo = new DataxHivePojo();
        dataxHivePojo.setJdbcDatasource(writerDatasource);
        List<Map<String, Object>> columns = Lists.newArrayList();
        writerColumns.forEach(c -> {
            Map<String, Object> column = Maps.newLinkedHashMap();
            column.put("name", c.split(Constants.SPLIT_SCOLON)[1]);
            column.put("type", c.split(Constants.SPLIT_SCOLON)[2]);
            columns.add(column);
        });
        dataxHivePojo.setColumns(columns);
        dataxHivePojo.setWriterDefaultFS(hiveWriterDto.getWriterDefaultFS());
        dataxHivePojo.setWriteFieldDelimiter(StringUtil.unicode2String(hiveWriterDto.getWriteFieldDelimiter()));
        dataxHivePojo.setWriterFileType(hiveWriterDto.getWriterFileType());
        dataxHivePojo.setWriterPath(hiveWriterDto.getWriterPath());
        dataxHivePojo.setWriteMode(hiveWriterDto.getWriteMode());
        dataxHivePojo.setWriterFileName(hiveWriterDto.getWriterFileName());
        return writerPlugin.buildHive(dataxHivePojo);
    }

    @Override
    public Map<String, Object> buildHBaseWriter() {
        DataxHbasePojo dataxHbasePojo = new DataxHbasePojo();
        dataxHbasePojo.setJdbcDatasource(writerDatasource);
        List<Map<String, Object>> columns = Lists.newArrayList();
        for (int i = 0; i < writerColumns.size(); i++) {
            Map<String, Object> column = Maps.newLinkedHashMap();
            column.put("index", i + 1);
            column.put("name", writerColumns.get(i));
            column.put("type", "string");
            columns.add(column);
        }
        dataxHbasePojo.setColumns(columns);
        HBaseDataSource HBaseDataSource = JSONUtils.parseObject(readerDatasource.getConnectionParams(), HBaseDataSource.class);
        dataxHbasePojo.setWriterHbaseConfig(HBaseDataSource.getZkAddress());
        String writerTable = !CollectionUtils.isEmpty(writerTables) ? writerTables.get(0) : Constants.STRING_BLANK;
        dataxHbasePojo.setWriterTable(writerTable);
        dataxHbasePojo.setWriterVersionColumn(hbaseWriterDto.getWriterVersionColumn());
        dataxHbasePojo.setWriterRowkeyColumn(hbaseWriterDto.getWriterRowkeyColumn());
        dataxHbasePojo.setWriterMode(hbaseWriterDto.getWriterMode());
        return writerPlugin.buildHbase(dataxHbasePojo);
    }


    @Override
    public Map<String, Object> buildMongoDBWriter() {
        DataxMongoDBPojo dataxMongoDBPojo = new DataxMongoDBPojo();
        dataxMongoDBPojo.setJdbcDatasource(writerDatasource);
        List<Map<String, Object>> columns = Lists.newArrayList();
        buildColumns(writerColumns, columns);
        dataxMongoDBPojo.setColumns(columns);
        MongoDBDataSource mongoDBDataSource = JSONUtils.parseObject(readerDatasource.getConnectionParams(), MongoDBDataSource.class);
        dataxMongoDBPojo.setAddress(mongoDBDataSource.getMongoClientURI());
        dataxMongoDBPojo.setDbName(mongoDBDataSource.getDatabase());
        dataxMongoDBPojo.setWriterTable(readerTables.get(0));
        dataxMongoDBPojo.setUpsertInfo(mongoDBWriterDto.getUpsertInfo());
        return writerPlugin.buildMongoDB(dataxMongoDBPojo);
    }

    private void buildColumns(List<String> columns, List<Map<String, Object>> returnColumns) {
        columns.forEach(c -> {
            Map<String, Object> column = Maps.newLinkedHashMap();
            column.put("name", c.split(Constants.SPLIT_SCOLON)[0]);
            column.put("type", c.split(Constants.SPLIT_SCOLON)[1]);
            returnColumns.add(column);
        });
    }
}