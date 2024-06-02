package com.wugui.datax.admin.tool.datax.writer;

import java.util.Collections;
import java.util.Map;

/**
 * <p>
 * rdbms构建类
 * </p>
 *
 * @author Listenabe
 * @since 2024/06/02
 */
public class RdbmsWriter extends BaseWriterPlugin implements DataxWriterInterface{
    @Override
    public String getName() {
        return "rdbmswriter";
    }

    @Override
    public Map<String, Object> sample() {
        return Collections.emptyMap();
    }
}
