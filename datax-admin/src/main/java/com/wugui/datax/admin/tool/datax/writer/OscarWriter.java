package com.wugui.datax.admin.tool.datax.writer;

import java.util.Map;

/**
 * oscar writer构建类
 *
 * @author Locki
 * @version 1.0
 * @since 2021-04-25
 */
public class OscarWriter extends BaseWriterPlugin implements DataxWriterInterface {
    @Override
    public String getName() {
        return "oscarwriter";
    }

    @Override
    public Map<String, Object> sample() {
        return null;
    }
}
