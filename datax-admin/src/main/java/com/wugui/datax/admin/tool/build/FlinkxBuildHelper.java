package com.wugui.datax.admin.tool.build;

import static com.wugui.datax.admin.util.JdbcConstants.CLICKHOUSE;
import static com.wugui.datax.admin.util.JdbcConstants.HIVE;
import static com.wugui.datax.admin.util.JdbcConstants.KAFKA;
import static com.wugui.datax.admin.util.JdbcConstants.MONGODB;
import static com.wugui.datax.admin.util.JdbcConstants.MYSQL;
import static com.wugui.datax.admin.util.JdbcConstants.ORACLE;
import static com.wugui.datax.admin.util.JdbcConstants.POSTGRESQL;
import static com.wugui.datax.admin.util.JdbcConstants.SQL_SERVER;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wugui.datatx.core.util.CollectionUtils;
import com.wugui.datatx.core.util.Constants;
import com.wugui.datax.admin.dto.DataXJsonBuildDTO;
import com.wugui.datax.admin.dto.HiveWriterDTO;
import com.wugui.datax.admin.dto.RdbmsReaderDTO;
import com.wugui.datax.admin.entity.JobDatasource;
import com.wugui.datax.admin.util.JdbcConstants;

/**
 * 继承DataxBuildHelper，增加独有的方法。
 * 可以重写DataxBuildHelper的逻辑
 */
public class FlinkxBuildHelper extends BaseBuildHelper {


    public static void main(String[] args) {
        DataxBuildHelper helper = new DataxBuildHelper();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("reader", new RdbmsReaderDTO());
        jsonObject.put("writer", new HiveWriterDTO());
        jsonObject.put("transformer", new JSONObject());
        jsonObject.put("speed", new JSONObject());
        jsonObject.put("errorLimit", new JSONObject());
//        String build = helper.build(jsonObject);
        // {"job":{"content":[{"reader":{},"transformer":{},"writer":{}}],"setting":{"errorLimit":{},"speed":{}}}}


    }


    @Override
    public String build(JobDatasource readerDatasource, JobDatasource writerDatasource, DataXJsonBuildDTO dataXJsonBuildDto) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("readerDatasource", readerDatasource);
        jsonObject.put("writerDatasource", writerDatasource);
        jsonObject.put("dataXJsonBuildDto", dataXJsonBuildDto);

        buildReader(jsonObject);
        buildWriter(jsonObject);
        buildSetting(jsonObject);
        String beautify = this.task.beautify();
        System.out.println(beautify);
        return beautify;
    }


    /**
     * 构建全局设置部分
     */
    @Override
    public void buildSetting(JSONObject jsonObject) {
        JSONObject errorLimit = new JSONObject();
        errorLimit.put("record", 0);
        JSONObject speed = new JSONObject();
        speed.put("channel", 1);

        JSONObject restore = new JSONObject();
        restore.put("maxRowNumForCheckpoint", 0);
        restore.put("isRestore", false);
        restore.put("restoreColumnName", "");
        restore.put("restoreColumnIndex", 0);
        JSONObject log = new JSONObject();
        log.put("path", "");
        log.put("level", "debug");
        log.put("pattern", "");
        log.put("isLogger", false);

        this.task.set("job.setting.errorLimit", errorLimit);
        this.task.set("job.setting.speed", speed);
        this.task.set("job.setting.restore", restore);
        this.task.set("job.setting.log", log);
    }


    @Override
    public void buildReader(JSONObject jsonObject) {
        String readerName = "";
        JobDatasource readerDatasource = (JobDatasource) jsonObject.get("readerDatasource");
        DataXJsonBuildDTO dataXJsonBuildDto = (DataXJsonBuildDTO) jsonObject.get("dataXJsonBuildDto");

        processingTableName(readerDatasource, dataXJsonBuildDto.getReaderTables());
        String datasource = readerDatasource.getDatasource();
        List<String> cols = dataXJsonBuildDto.getReaderColumns();
        List<Object> column = convertKeywordsColumns(datasource, cols);


        String password = null;
        String username = null;
        String where = null;
        ArrayList<Object> jdbcUrl = null;
        List<String> table = null;

        String path = null;
        String defaultFS = null;
        String fieldDelimiter = null;
        String fileType = null;
        String collectionName = null;
        String filter = null;
        String mongodburl = null;
        String topic = null;
        String groupId = null;
        String codec = null;
        String zookeeperConnect = null;
        String bootstrapServers = null;
        String autoCommitIntervalMs = null;
        String autoOffsetReset = null;
        boolean blankIgnore = false;
        JSONObject parameter = new JSONObject();
        // 一些普通jdbc类型
        switch (datasource) {
            case MYSQL:
            case ORACLE:
            case SQL_SERVER:
            case POSTGRESQL:
            case CLICKHOUSE:
                readerName = datasource + "reader";
                password = readerDatasource.getJdbcPassword();
                username = readerDatasource.getJdbcUsername();
                String url = readerDatasource.getJdbcUrl();

                if (StringUtils.isNotBlank(url)) {
                    jdbcUrl = Lists.newArrayList();
                    jdbcUrl.add(url);
                }
                table = dataXJsonBuildDto.getReaderTables();
                where = dataXJsonBuildDto.getRdbmsReader().getWhereParams();
                break;
            case HIVE: // HIVE类型
                readerName = "hdfs" + "reader";
                path = dataXJsonBuildDto.getHiveReader().getReaderPath();
                defaultFS = dataXJsonBuildDto.getHiveReader().getReaderDefaultFS();
                fieldDelimiter = dataXJsonBuildDto.getHiveReader().getReaderFieldDelimiter();
                fileType = dataXJsonBuildDto.getHiveReader().getReaderFileType();
                // TODO
                // hadoopConfig这个参数前端也需要传
                ArrayList<Object> list = Lists.newArrayList();
                column.forEach(c -> {
                    Map<String, Object> col = Maps.newLinkedHashMap();
                    String colStr = c.toString();
                    col.put("index", colStr.split(Constants.SPLIT_SCOLON)[0]);
                    col.put("type", colStr.split(Constants.SPLIT_SCOLON)[2]);
                    list.add(col);
                });
                column = list;
                break;
            case MONGODB:
                mongodburl = readerDatasource.getJdbcUrl();
                collectionName = dataXJsonBuildDto.getReaderTables().get(0);
                filter = dataXJsonBuildDto.getRdbmsReader().getWhereParams();
                break;
            case KAFKA:
                // TODO
                // 目前没有kafka数据源，所以这里写死
                topic = "kafka";
                groupId = "default";
                codec = "plain";
                blankIgnore = false;
                zookeeperConnect = "0.0.0.1:2182/kafka";
                bootstrapServers = "0.0.0.1:9092";
                autoCommitIntervalMs = "1000";
                autoOffsetReset = "latest";
                JSONObject consumerSettings = new JSONObject();
                consumerSettings.put("zookeeper.connect", zookeeperConnect);
                consumerSettings.put("bootstrap.servers", bootstrapServers);
                consumerSettings.put("auto.commit.interval.ms", autoCommitIntervalMs);
                consumerSettings.put("auto.offset.reset", autoOffsetReset);
                parameter.put("topic", topic);
                parameter.put("groupId", groupId);
                parameter.put("codec", codec);
                parameter.put("blankIgnore", blankIgnore);
                parameter.put("consumerSettings", consumerSettings);
                break;
            default:
                break;
        }


        if (StringUtils.isNotBlank(password)) {
            parameter.put("password", password);
        }
        if (CollectionUtils.isNotEmpty(column)) {
            parameter.put("column", column);
        }

        if (StringUtils.isNotBlank(where)) {
            parameter.put("where", where);
        }

        if (StringUtils.isNotBlank(username)) {
            parameter.put("username", username);
        }


        if (StringUtils.isNotBlank(path)) {
            parameter.put("path", path);
        }
        if (StringUtils.isNotBlank(defaultFS)) {
            parameter.put("defaultFS", defaultFS);
        }
        if (StringUtils.isNotBlank(fieldDelimiter)) {
            parameter.put("fieldDelimiter", fieldDelimiter);
        }
        if (StringUtils.isNotBlank(fileType)) {
            parameter.put("fileType", fileType);
        }

        if (StringUtils.isNotBlank(mongodburl)) {
            parameter.put("url", mongodburl);
        }

        if (StringUtils.isNotBlank(collectionName)) {
            parameter.put("collectionName", collectionName);
        }

        if (StringUtils.isNotBlank(filter)) {
            parameter.put("filter", filter);
        }


        this.task.set("job.content[0].reader.name", readerName);
        this.task.set("job.content[0].reader.parameter", parameter);
        if (CollectionUtils.isNotEmpty(jdbcUrl)) {
            this.task.set("job.content[0].reader.parameter.connection[0].jdbcUrl", jdbcUrl);
        }

        if (CollectionUtils.isNotEmpty(table)) {
            this.task.set("job.content[0].reader.parameter.connection[0].table", table);
        }

    }

    @Override
    public void buildWriter(JSONObject jsonObject) {
        String writerName = "";
        JobDatasource writerDatasource = (JobDatasource) jsonObject.get("writerDatasource");
        DataXJsonBuildDTO dataXJsonBuildDto = (DataXJsonBuildDTO) jsonObject.get("dataXJsonBuildDto");

        //处理writer表名
        processingTableName(writerDatasource, dataXJsonBuildDto.getWriterTables());
        String datasource = writerDatasource.getDatasource();
        List<String> cols = dataXJsonBuildDto.getWriterColumns();
        List<Object> column = convertKeywordsColumns(datasource, cols);


        writerName = datasource + "writer";


        String password = null;
        String username = null;
        String jdbcUrl = null;
        // TODO
        // 这里的insert可能需要从前端传入
        String writeMode = "insert";
        List<String> table = null;
        List<String> preSql = null;
        List<String> postSql = null;

        String path = null;
        String defaultFS = null;
        String fieldDelimiter = null;
        String fileType = null;

        String collectionName = null;
        String mongodburl = null;

        String topic  = null;
        String timezone  = null;
        String encoding  = null;
        String brokerList  = null;
        String zookeeperConnect  = null;
        String bootstrapServers  = null;

        JSONObject parameter = new JSONObject();

        // 一些普通jdbc类型
        switch (datasource) {
            case MYSQL:
            case ORACLE:
            case SQL_SERVER:
            case POSTGRESQL:
            case CLICKHOUSE:
                password = writerDatasource.getJdbcPassword();
                username = writerDatasource.getJdbcUsername();
                jdbcUrl = writerDatasource.getJdbcUrl();
                table = dataXJsonBuildDto.getWriterTables();
                String preSqlStr = dataXJsonBuildDto.getRdbmsWriter().getPreSql();

                if (StringUtils.isNotBlank(preSqlStr)) {
                    preSql = Lists.newArrayList();
                    preSql.add(preSqlStr);
                }
                String postSqlStr = dataXJsonBuildDto.getRdbmsWriter().getPostSql();

                if (StringUtils.isNotBlank(postSqlStr)) {
                    postSql = Lists.newArrayList();
                    postSql.add(postSqlStr);
                }
                break;
            case HIVE:
                writerName = "hdfs" + "writer";
                path = dataXJsonBuildDto.getHiveWriter().getWriterPath();
                defaultFS = dataXJsonBuildDto.getHiveWriter().getWriterDefaultFS();
                fieldDelimiter = dataXJsonBuildDto.getHiveWriter().getWriteFieldDelimiter();
                fileType = dataXJsonBuildDto.getHiveWriter().getWriterFileType();
                // TODO
                // hadoopConfig这个参数前端也需要传
                ArrayList<Object> list = Lists.newArrayList();
                column.forEach(c -> {
                    Map<String, Object> col = Maps.newLinkedHashMap();
                    String colStr = c.toString();
                    col.put("index", colStr.split(Constants.SPLIT_SCOLON)[0]);
                    col.put("type", colStr.split(Constants.SPLIT_SCOLON)[2]);
                    list.add(col);
                });
                column = list;
                break;
            case MONGODB:
                mongodburl = writerDatasource.getJdbcUrl();
                collectionName = dataXJsonBuildDto.getWriterTables().get(0);
                break;
            case KAFKA:    // 目前没有kafka数据源，所以这里写死
                // TODO
                topic = "kafka";
                timezone = "UTC";
                encoding = "UTF_8";
                brokerList = "0.0.0.1:9092";
                zookeeperConnect = "0.0.0.1:2182";
                bootstrapServers = "0.0.0.1:9092";


                JSONObject producerSettings = new JSONObject();
                producerSettings.put("zookeeper.connect", zookeeperConnect);
                producerSettings.put("bootstrap.servers", bootstrapServers);
                parameter.put("topic", topic);
                parameter.put("timezone", timezone);
                parameter.put("encoding", encoding);
                parameter.put("brokerList", brokerList);
                parameter.put("producerSettings", producerSettings);
                break;
            default:
                break;
        }


        if (StringUtils.isNotBlank(password)) {
            parameter.put("password", password);
        }
        if (CollectionUtils.isNotEmpty(column)) {
            parameter.put("column", column);
        }


        if (StringUtils.isNotBlank(writeMode)) {
            parameter.put("writeMode", writeMode);
        }

        if (StringUtils.isNotBlank(username)) {
            parameter.put("username", username);
        }


        if (CollectionUtils.isNotEmpty(preSql)) {
            parameter.put("preSql", preSql);
        }

        if (CollectionUtils.isNotEmpty(postSql)) {
            parameter.put("postSql", postSql);
        }


        if (StringUtils.isNotBlank(path)) {
            parameter.put("path", path);
        }
        if (StringUtils.isNotBlank(defaultFS)) {
            parameter.put("defaultFS", defaultFS);
        }
        if (StringUtils.isNotBlank(fieldDelimiter)) {
            parameter.put("fieldDelimiter", fieldDelimiter);
        }
        if (StringUtils.isNotBlank(fileType)) {
            parameter.put("fileType", fileType);
        }


        if (StringUtils.isNotBlank(mongodburl)) {
            parameter.put("url", mongodburl);
        }

        if (StringUtils.isNotBlank(collectionName)) {
            parameter.put("collectionName", collectionName);
        }


        this.task.set("job.content[0].writer.name", writerName);
        this.task.set("job.content[0].writer.parameter", parameter);
        if (StringUtils.isNotBlank(jdbcUrl)) {
            this.task.set("job.content[0].writer.parameter.connection[0].jdbcUrl", jdbcUrl);
        }

        if (CollectionUtils.isNotEmpty(table)) {
            this.task.set("job.content[0].writer.parameter.connection[0].table", table);
        }

    }

    @Override
    public void buildTransformer(JSONObject jsonObject) {

    }

    @Override
    public void buildFilter(JSONObject jsonObject) {

    }
}
