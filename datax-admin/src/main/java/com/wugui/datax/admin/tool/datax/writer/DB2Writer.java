package com.wugui.datax.admin.tool.datax.writer;

import java.util.Map;

/**
 * db2 writer构建类
 *
 * @author Locki
 * @version 1.0
 * @since 2020-09-12 16:18:17
 */
public class DB2Writer extends BaseWriterPlugin implements DataxWriterInterface {
    @Override
    public String getName() {
        return "db2writer";
    }

    @Override
    public Map<String, Object> sample() {
        return null;
    }
}
