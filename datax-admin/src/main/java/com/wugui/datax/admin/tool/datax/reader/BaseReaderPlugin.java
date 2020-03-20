package com.wugui.datax.admin.tool.datax.reader;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.wugui.datax.admin.entity.JobJdbcDatasource;
import com.wugui.datax.admin.tool.datax.BaseDataxPlugin;
import com.wugui.datax.admin.tool.pojo.DataxHivePojo;
import com.wugui.datax.admin.tool.pojo.DataxRdbmsPojo;
import com.wugui.datax.admin.util.AESUtil;
import org.apache.commons.lang3.StringUtils;

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
    public Map<String, Object> build(DataxRdbmsPojo plugin) {
        //构建
        Map<String, Object> readerObj = Maps.newLinkedHashMap();
        readerObj.put("name", getName());
        Map<String, Object> parameterObj = Maps.newLinkedHashMap();
        Map<String, Object> connectionObj = Maps.newLinkedHashMap();

        JobJdbcDatasource jobJdbcDatasource = plugin.getJdbcDatasource();
        parameterObj.put("username", jobJdbcDatasource.getJdbcUsername());
        parameterObj.put("password", jobJdbcDatasource.getJdbcPassword());

        //判断是否是 querySql
        if (StrUtil.isNotBlank(plugin.getQuerySql())) {
            connectionObj.put("querySql", ImmutableList.of(plugin.getQuerySql()));
        } else {
            parameterObj.put("column", plugin.getRdbmsColumns());
            //判断是否有where
            if (StringUtils.isNotBlank(plugin.getWhereParam())) {
                parameterObj.put("where", plugin.getWhereParam());
            }
            connectionObj.put("table", plugin.getTables());
        }
        parameterObj.put("splitPk",plugin.getSplitPk());
        connectionObj.put("jdbcUrl", ImmutableList.of(jobJdbcDatasource.getJdbcUrl()));

        parameterObj.put("connection", ImmutableList.of(connectionObj));

        readerObj.put("parameter", parameterObj);

        return readerObj;
    }

    @Override
    public Map<String, Object> buildHive(DataxHivePojo dataxHivePojo) {
        return null;
    }
}
