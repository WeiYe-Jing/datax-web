package com.wugui.datax.admin.tool.datax.writer;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.wugui.datatx.core.util.Constants;
import com.wugui.datax.admin.entity.JobDatasource;
import com.wugui.datax.admin.tool.pojo.DataxRabbitmqPojo;

public class RabbitmqWriter extends BaseWriterPlugin implements DataxWriterInterface {
    @Override
    public String getName() {
        return "rabbitmqwriter";
    }

    @Override
    public Map<String, Object> sample() {
        return null;
    }

    @Override
    public Map<String, Object> buildRabbitmq(DataxRabbitmqPojo plugin) {
        //构建
        Map<String, Object> writerObj = Maps.newLinkedHashMap();
        JobDatasource datasource = plugin.getJdbcDatasource();
        writerObj.put("name", getName());
        Map<String, Object> parameterObj = Maps.newLinkedHashMap();
        parameterObj.put("host", datasource.getJdbcUrl());
        String port = "5672";
        if (StringUtils.isNotBlank(datasource.getExtra())) {
        	JSONObject extra = JSONObject.parseObject(datasource.getExtra());
        	port = extra.getString("port");
        }
        parameterObj.put("port", port);
        parameterObj.put("username", datasource.getJdbcUsername() == null ? Constants.STRING_BLANK : datasource.getJdbcUsername());
        parameterObj.put("password", datasource.getJdbcPassword() == null ? Constants.STRING_BLANK : datasource.getJdbcPassword());
        parameterObj.put("exchange", plugin.getExchange());
        parameterObj.put("vhost", plugin.getVhost());
        parameterObj.put("queue", plugin.getQueue());
        parameterObj.put("routingKey", plugin.getRoutingKey());
        parameterObj.put("batchSize", plugin.getBatchSize());
        parameterObj.put("jointColumn", plugin.getJointColumn());
        parameterObj.put("messagePrefix", plugin.getMessagePrefix());
        parameterObj.put("messageSuffix", plugin.getMessageSuffix());
        parameterObj.put("fieldDelimiter", plugin.getFieldDelimiter());
        parameterObj.put("column", plugin.getColumns());
        writerObj.put("parameter", parameterObj);
        return writerObj;
    }
}
