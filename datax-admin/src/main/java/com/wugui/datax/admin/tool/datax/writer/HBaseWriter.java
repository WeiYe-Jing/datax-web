package com.wugui.datax.admin.tool.datax.writer;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.wugui.datax.admin.tool.pojo.DataxHbasePojo;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * @author jingwk
 */
public class HBaseWriter extends BaseWriterPlugin implements DataxWriterInterface {

    @Override
    public String getName() {
        return "hbase11xwriter";
    }

    @Override
    public Map<String, Object> buildHbase(DataxHbasePojo plugin) {

        Map<String, Object> config = Maps.newLinkedHashMap();
        config.put("hbase.zookeeper.quorum", plugin.getWriterHbaseConfig());

        Map<String, Object> parameter = Maps.newLinkedHashMap();
        parameter.put("hbaseConfig", config);
        parameter.put("table", plugin.getWriterTable());
        parameter.put("mode", plugin.getWriterMode());
        parameter.put("column", plugin.getColumns());
        parameter.put("rowkeyColumn", JSON.parseArray(plugin.getWriterRowkeyColumn()));
        if (StringUtils.isNotBlank(plugin.getWriterVersionColumn().getValue())) {
            parameter.put("versionColumn", plugin.getWriterVersionColumn());
        }

        Map<String, Object> writer = Maps.newLinkedHashMap();
        writer.put("name", getName());
        writer.put("parameter", parameter);
        return writer;
    }
}
