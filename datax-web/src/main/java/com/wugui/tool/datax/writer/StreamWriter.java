package com.wugui.tool.datax.writer;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.wugui.tool.pojo.DataxPluginPojo;

import java.util.Map;

/**
 * streamwriter
 *
 * @author zhouhongfa@gz-yibo.com
 * @ClassName StreaWriter
 * @Version 1.0
 * @since 2019/7/31 19:44
 */
public class StreamWriter extends BaseWriterPlugin implements DataxWriterInterface {

    @Override
    public String getName() {
        return "streamwriter";
    }

    @Override
    public Map<String, Object> build(DataxPluginPojo dataxPluginPojo) {
        Map<String, Object> writerObj = Maps.newLinkedHashMap();

        writerObj.putAll(ImmutableMap.of("name", getName(), "parameter", ImmutableMap.of("print", true)));

        return writerObj;
    }

    @Override
    public Map<String, Object> sample() {
        return null;
    }
}



