package com.wugui.tool.datax.reader;

import cn.hutool.core.util.StrUtil;
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
 * @ClassName BaseReaderPlugin
 * @Version 1.0
 * @since 2019/8/2 16:27
 */
public abstract class BaseReaderPlugin extends BaseDataxPlugin {

    @Override
    public Map<String, Object> build(DataxPluginPojo dataxPluginPojo) {
        //构建
        Map<String, Object> readerObj = Maps.newLinkedHashMap();

        readerObj.put("name", getName());
//
        Map<String, Object> parameterObj = Maps.newLinkedHashMap();
        Map<String, Object> connectionObj = Maps.newLinkedHashMap();

        JobJdbcDatasource jobJdbcDatasource = dataxPluginPojo.getJdbcDatasource();
        parameterObj.put("username", jobJdbcDatasource.getJdbcUsername());
        parameterObj.put("password", jobJdbcDatasource.getJdbcPassword());

        //判断是否是 querySql
        if (StrUtil.isNotBlank(dataxPluginPojo.getQuerySql())) {
            connectionObj.put("querySql", ImmutableList.of(dataxPluginPojo.getQuerySql()));
        } else {
            //列表
            parameterObj.put("column", dataxPluginPojo.getColumns());
            //判断是否有where
            if (extraParams.containsKey("where")) {
                parameterObj.put("where", extraParams.get("where"));
            }
            connectionObj.put("table", dataxPluginPojo.getTables());
        }

//        logger.info(extraParams.toString());
        connectionObj.put("jdbcUrl", ImmutableList.of(jobJdbcDatasource.getJdbcUrl()));

        parameterObj.put("connection", ImmutableList.of(connectionObj));

        readerObj.put("parameter", parameterObj);

        return readerObj;
    }
}
