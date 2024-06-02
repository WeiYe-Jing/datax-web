package com.wugui.datax.admin.tool.datax.reader;

import java.util.Collections;
import java.util.Map;

/**
 * <p>
 * rdbms 构建类
 * </p>
 *
 * @author Listenabe
 * @since 2024/06/02
 */
public class RdbmsReader extends BaseReaderPlugin implements DataxReaderInterface{
    @Override
    public String getName() {
        return "rdbmsreader";
    }

    @Override
    public Map<String, Object> sample() {
        return Collections.emptyMap();
    }
}
