package com.wugui.datax.admin.tool.datax.writer;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.wugui.datax.admin.tool.pojo.DataxHbasePojo;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public class HBaseWriter extends BaseWriterPlugin implements DataxWriterInterface {
    @Override
    public String getName() {
        return "hbase11xwriter";
    }

    @Override
    public Map<String, Object> sample() {
        return null;
    }

    public Map<String, Object> buildHbase(DataxHbasePojo plugin) {
        //构建
        Map<String, Object> writerObj = Maps.newLinkedHashMap();
        writerObj.put("name", getName());
        Map<String, Object> parameterObj = Maps.newLinkedHashMap();
        Map<String, Object> confige = Maps.newLinkedHashMap();
        confige.put("hbase.zookeeper.quorum", plugin.getWriterHbaseConfig());
        parameterObj.put("hbaseConfig", confige);
        parameterObj.put("table", plugin.getWriterTable());
        parameterObj.put("mode", plugin.getWriterMode());
        parameterObj.put("column", plugin.getColumns());
        parameterObj.put("rowkeyColumn", JSON.parseArray(plugin.getWriterRowkeyColumn()));
        if (StringUtils.isNotBlank(plugin.getWriterVersionColumn().getValue())) {
            parameterObj.put("versionColumn", plugin.getWriterVersionColumn());
        }
        writerObj.put("parameter", parameterObj);
        return writerObj;
    }
}
