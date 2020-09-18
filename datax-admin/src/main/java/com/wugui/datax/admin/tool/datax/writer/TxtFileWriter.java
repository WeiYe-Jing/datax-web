package com.wugui.datax.admin.tool.datax.writer;

import com.google.common.collect.Maps;
import com.wugui.datax.admin.tool.pojo.DataxHivePojo;

import java.util.Map;

/**
 * txtfile writer构建类
 *
 * @author liu.siqi
 * @Version 1.0
 * @since 2020/7/11
 */
public class TxtFileWriter extends BaseWriterPlugin implements DataxWriterInterface {
    @Override
    public String getName() {
        return "txtfilewriter";
    }

    @Override
    public Map<String, Object> sample() {
        return null;
    }

    @Override
    public Map<String, Object> buildTxtFile(DataxHivePojo plugin) {

        Map<String, Object> writerObj = Maps.newLinkedHashMap();
        writerObj.put("name", getName());
        Map<String, Object> parameterObj = Maps.newLinkedHashMap();
        parameterObj.put("path", plugin.getWriterPath());
        parameterObj.put("fileName", plugin.getWriterFileName());
        parameterObj.put("writeMode", plugin.getWriteMode());
        parameterObj.put("fieldDelimiter", plugin.getWriteFieldDelimiter());
        parameterObj.put("dateFormat", "yyyy-MM-dd");
        parameterObj.put("fileFormat", plugin.getWriterFileType());
        writerObj.put("parameter", parameterObj);
        System.out.println("writerObj"+writerObj);
        return writerObj;
    }

}
