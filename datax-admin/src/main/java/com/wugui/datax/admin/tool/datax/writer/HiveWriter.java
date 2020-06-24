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
    public Map<String, Object> sample() {
        return null;
    }

    @Override
    public Map<String, Object> buildHive(DataxHivePojo plugin) {
        Map<String, Object> writerObj = Maps.newLinkedHashMap();
        writerObj.put("name", getName());

        Map<String, Object> parameterObj = Maps.newLinkedHashMap();
        parameterObj.put("defaultFS", plugin.getWriterDefaultFS());
        parameterObj.put("fileType", plugin.getWriterFileType());
        parameterObj.put("path", plugin.getWriterPath());
        parameterObj.put("fileName", plugin.getWriterFileName());
        parameterObj.put("writeMode", plugin.getWriteMode());
        parameterObj.put("fieldDelimiter", plugin.getWriteFieldDelimiter());
        parameterObj.put("column", plugin.getColumns());
        writerObj.put("parameter", parameterObj);
        if (Boolean.parseBoolean(plugin.getHaveKerberos())) {
            parameterObj.put("haveKerberos",plugin.getHaveKerberos());
//            parameterObj.setKerberosKeytabFilePath(plugin.getKerberosKeytabFilePath());
            parameterObj.put("kerberosKeytabFilePath",plugin.getKerberosKeytabFilePath());
//            parameterObj.setKerberosPrincipal(plugin.getKerberosPrincipal());
            parameterObj.put("kerberosPrincipal",plugin.getKerberosPrincipal());
        }

        return writerObj;
    }
}
