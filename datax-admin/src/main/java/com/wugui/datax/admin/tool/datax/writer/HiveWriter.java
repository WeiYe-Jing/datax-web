package com.wugui.datax.admin.tool.datax.writer;

import com.google.common.collect.Maps;
import com.wugui.datax.admin.tool.pojo.DataxHivePojo;

import java.util.Map;

/**
 * hive writer构建类
 *
 * @author jingwk
 * @version 2.0
 * @since 2020/01/05
 */
public class HiveWriter extends BaseWriterPlugin implements DataxWriterInterface {

    @Override
    public String getName() {
        return "hdfswriter";
    }

    @Override
    public Map<String, Object> buildHive(DataxHivePojo plugin) {

        Map<String, Object> parameter = Maps.newLinkedHashMap();
        parameter.put("defaultFS", plugin.getWriterDefaultFS());
        parameter.put("fileType", plugin.getWriterFileType());
        parameter.put("path", plugin.getWriterPath());
        parameter.put("fileName", plugin.getWriterFileName());
        parameter.put("writeMode", plugin.getWriteMode());
        parameter.put("fieldDelimiter", plugin.getWriteFieldDelimiter());
        parameter.put("column", plugin.getColumns());

        Map<String, Object> writer = Maps.newLinkedHashMap();
        writer.put("name", getName());
        writer.put("parameter", parameter);
        return writer;
    }
}
