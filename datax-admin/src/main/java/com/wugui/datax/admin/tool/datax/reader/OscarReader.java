package com.wugui.datax.admin.tool.datax.reader;

import java.util.Map;

/**
 * oscar reader 构建类
 *
 * @author Locki
 * @version 1.0
 * @since 2021-04-25
 */
public class OscarReader extends BaseReaderPlugin implements DataxReaderInterface {
    @Override
    public String getName() {
        return "oscarreader";
    }

    @Override
    public Map<String, Object> sample() {
        return null;
    }
}
