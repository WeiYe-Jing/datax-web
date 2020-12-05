package com.wugui.datax.admin.tool.datax.reader;

import com.google.common.collect.Maps;
import com.wugui.datax.admin.tool.pojo.DataxHivePojo;

import java.util.Map;

/**
 * hive reader 构建类
 *
 * @author jingwk
 * @version 2.0
 * @since 2020/01/05
 */
public class HiveReader extends BaseReaderPlugin implements DataxReaderInterface {

    @Override
    public String getName() {
        return "hdfsreader";
    }

    @Override
    public Map<String, Object> buildHive(DataxHivePojo plugin) {


        Map<String, Object> parameter = Maps.newLinkedHashMap();
        parameter.put("path", plugin.getReaderPath());
        parameter.put("defaultFS", plugin.getReaderDefaultFS());
        parameter.put("fileType", plugin.getReaderFileType());
        parameter.put("fieldDelimiter", plugin.getReaderFieldDelimiter());
        parameter.put("skipHeader", plugin.getSkipHeader());
        parameter.put("column", plugin.getColumns());

        Map<String, Object> reader = Maps.newLinkedHashMap();
        reader.put("name", getName());
        reader.put("parameter", parameter);
        return reader;
    }
}
