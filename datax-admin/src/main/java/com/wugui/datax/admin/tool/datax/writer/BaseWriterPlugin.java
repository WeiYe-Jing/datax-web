package com.wugui.datax.admin.tool.datax.writer;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.wugui.datax.admin.entity.JobDatasource;
import com.wugui.datax.admin.tool.datax.BaseDataxPlugin;
import com.wugui.datax.admin.tool.pojo.DataxHbasePojo;
import com.wugui.datax.admin.tool.pojo.DataxHivePojo;
import com.wugui.datax.admin.tool.pojo.DataxMongoDBPojo;
import com.wugui.datax.admin.tool.pojo.DataxRdbmsPojo;
import com.wugui.datax.admin.util.AESUtil;

import java.util.Map;

/**
 * datax writer base
 *
 * @author zhouhongfa@gz-yibo.com
 * @ClassName BaseWriterPlugin
 * @Version 1.0
 * @since 2019/8/2 16:28
 */
public abstract class BaseWriterPlugin extends BaseDataxPlugin {
    @Override
    public Map<String, Object> build(DataxRdbmsPojo plugin) {
        Map<String, Object> writerObj = Maps.newLinkedHashMap();
        writerObj.put("name", getName());

        Map<String, Object> parameterObj = Maps.newLinkedHashMap();
//        parameterObj.put("writeMode", "insert");
        JobDatasource jobDatasource = plugin.getJobDatasource();
        parameterObj.put("username", jobDatasource.getJdbcUsername());
        parameterObj.put("password", jobDatasource.getJdbcPassword());
        parameterObj.put("column", plugin.getRdbmsColumns());
        // preSql
        parameterObj.put("preSql", ImmutableList.of(plugin.getPreSql()));

        Map<String, Object> connectionObj = Maps.newLinkedHashMap();
        connectionObj.put("table", plugin.getTables());
        connectionObj.put("jdbcUrl", jobDatasource.getJdbcUrl());

        parameterObj.put("connection", ImmutableList.of(connectionObj));
        writerObj.put("parameter", parameterObj);

        return writerObj;
    }

    @Override
    public Map<String, Object> buildHive(DataxHivePojo dataxHivePojo) {
        return null;
    }


    @Override
    public Map<String, Object> buildHbase(DataxHbasePojo dataxHbasePojo) {
        return null;
    }

    @Override
    public Map<String, Object> buildMongoDB(DataxMongoDBPojo plugin) {
        return null;
    }
}
