package com.wugui.tool.datax.writer;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.wugui.dataxweb.entity.JobJdbcDatasource;
import com.wugui.tool.datax.BaseDataxPlugin;
import com.wugui.tool.pojo.DataxPluginPojo;

import java.util.Map;

/**
 * TODO
 *
 * @author zhouhongfa@gz-yibo.com
 * @ClassName BaseWriterPlugin
 * @Version 1.0
 * @since 2019/8/2 16:28
 */
public abstract class BaseWriterPlugin extends BaseDataxPlugin {
    @Override
    public Map<String, Object> build(DataxPluginPojo dataxPluginPojo) {
        Map<String, Object> writerObj = Maps.newLinkedHashMap();
        writerObj.put("name", getName());

        Map<String, Object> parameterObj = Maps.newLinkedHashMap();
//        parameterObj.put("writeMode", "insert");

        JobJdbcDatasource jobJdbcDatasource = dataxPluginPojo.getJdbcDatasource();
        parameterObj.put("username", jobJdbcDatasource.getJdbcUsername());
        parameterObj.put("password", jobJdbcDatasource.getJdbcPassword());

        parameterObj.put("column", dataxPluginPojo.getColumns());

        // preSql
        parameterObj.put("preSql", ImmutableList.of(dataxPluginPojo.getPreSql()));

        Map<String, Object> connectionObj = Maps.newLinkedHashMap();
        connectionObj.put("table", dataxPluginPojo.getTables());

        connectionObj.put("jdbcUrl", jobJdbcDatasource.getJdbcUrl());

        parameterObj.put("connection", ImmutableList.of(connectionObj));

        writerObj.put("parameter", parameterObj);

        return writerObj;
    }
}
