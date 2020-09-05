package com.wugui.datax.admin.tool.build;

import static com.wugui.datax.admin.util.JdbcConstants.MYSQL;
import static com.wugui.datax.admin.util.JdbcConstants.ORACLE;
import static com.wugui.datax.admin.util.JdbcConstants.POSTGRESQL;
import static com.wugui.datax.admin.util.JdbcConstants.SQL_SERVER;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.wugui.datax.admin.dto.DataXJsonBuildDTO;
import com.wugui.datax.admin.entity.JobDatasource;
import com.wugui.datax.admin.tool.table.TableNameHandle;
import com.wugui.datax.admin.util.JdbcConstants;


public abstract class BaseBuildHelper {
    protected  Configuration task= Configuration.from("{}");


    /**
     * 构建任务参数，并返回构建的json或者yaml
     * @param readerDatasource
     * @param writerDatasource
     * @param dataXJsonBuildDto
     * @return 返回构建的json或者yaml
     */
    abstract String build(JobDatasource readerDatasource, JobDatasource writerDatasource, DataXJsonBuildDTO dataXJsonBuildDto);



    /**
     * 构建任务参数的Reader部分
     * @param jsonObject
     */
    abstract void buildReader(JSONObject jsonObject);

    /**
     * 构建任务参数的Writer部分
     * @param jsonObject
     */
    abstract void buildWriter(JSONObject jsonObject);


    /**
     * 构建任务参数的Transformer部分
     * @param jsonObject
     */
    abstract void buildTransformer(JSONObject jsonObject);


    /**
     * 构建任务参数的Filter部分
     * @param jsonObject
     */
    abstract void buildFilter(JSONObject jsonObject);


    /**
     * 处理表名称
     * 解决生成json中的表名称大小写敏感问题
     * 目前针对Oracle和postgreSQL
     * @param jobDatasource
     * @param tables
     */
    protected void processingTableName(JobDatasource jobDatasource, List<String> tables) {
        if (JdbcConstants.ORACLE.equals(jobDatasource.getDatasource()) || JdbcConstants.POSTGRESQL.equals(jobDatasource.getDatasource())) {
            for (int i = 0; i < tables.size(); i++) {
                tables.set(i, TableNameHandle.addDoubleQuotes(tables.get(i)));
            }
        }
    }



    protected List<Object> convertKeywordsColumns(String datasource, List<String> columns) {
        if (columns == null) {
            return null;
        }

        List<Object> toColumns = new ArrayList<Object>();
        columns.forEach(s -> {
            toColumns.add(doConvertKeywordsColumn(datasource, s));
        });
        return toColumns;
    }



    protected String doConvertKeywordsColumn(String dbType, String column) {
        if (column == null) {
            return null;
        }

        column = column.trim();
        column = column.replace("[", "");
        column = column.replace("]", "");
        column = column.replace("`", "");
        column = column.replace("\"", "");
        column = column.replace("'", "");
        // TODO
        // 这个地方如果进行了转义，flinkx会执行失败，还没有排查原因，所以暂时注释
//        switch (dbType) {
//            case MYSQL:
//                return String.format("`%s`", column);
//            case SQL_SERVER:
//                return String.format("[%s]", column);
//            case POSTGRESQL:
//            case ORACLE:
//                return String.format("\"%s\"", column);
//            default:
//                return column;
//        }

        return column;
    }

    /**
     * 构建全局设置部分
     * @param jsonObject
     */
    protected void buildSetting(JSONObject jsonObject) {
        JSONObject errorLimit = new JSONObject();
        errorLimit.put("record",0);
        JSONObject speed = new JSONObject();
        speed.put("channel",1);
        this.task.set("job.setting.errorLimit",errorLimit);
        this.task.set("job.setting.speed",speed);

    }




}
