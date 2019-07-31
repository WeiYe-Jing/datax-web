package com.wugui.tool.datax.reader;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.wugui.dataxweb.entity.JobJdbcDatasource;
import com.wugui.tool.datax.BaseDataxPlugin;
import com.wugui.tool.pojo.DataxPluginPojo;

import java.util.Map;

/**
 * mysql reader 构建类
 *
 * @author zhouhongfa@gz-yibo.com
 * @ClassName MysqlReader
 * @Version 1.0
 * @since 2019/7/30 23:07
 */
public class MysqlReader extends BaseDataxPlugin implements DataxReaderInterface {
    @Override
    public String getName() {
        return "mysqlreader";
    }

    @Override
    public Map<String, Object> build(DataxPluginPojo dataxPluginPojo) {
        //构建
        Map<String, Object> readerObj = Maps.newLinkedHashMap();

        readerObj.put("name", getName());
//
        Map<String, Object> parameterObj = Maps.newLinkedHashMap();

        JobJdbcDatasource jobJdbcDatasource = dataxPluginPojo.getJdbcDatasource();
        parameterObj.put("username", jobJdbcDatasource.getJdbcUsername());
        parameterObj.put("password", jobJdbcDatasource.getJdbcPassword());
        //列表
        parameterObj.put("column", dataxPluginPojo.getColumns());
//
        Map<String, Object> connectionObj = Maps.newLinkedHashMap();
        connectionObj.put("table", dataxPluginPojo.getTables());
        //where
//        connectionObj.put("where", "1=2");
        connectionObj.put("jdbcUrl", ImmutableList.of(jobJdbcDatasource.getJdbcUrl()));

        parameterObj.put("connection", ImmutableList.of(connectionObj));

        readerObj.put("parameter", parameterObj);

        return readerObj;
    }

    @Override
    public Map<String, Object> sample() {
        return null;
    }
}
