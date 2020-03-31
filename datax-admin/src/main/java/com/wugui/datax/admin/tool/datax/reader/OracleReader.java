package com.wugui.datax.admin.tool.datax.reader;

import com.wugui.datax.admin.tool.pojo.DataxMongoDBPojo;

import java.util.Map;

/**
 * oracle reader 构建类
 *
 * @author zhouhongfa@gz-yibo.com
 * @version 1.0
 * @since 2019/8/2
 */
public class OracleReader extends BaseReaderPlugin implements DataxReaderInterface {
    @Override
    public String getName() {
        return "oraclereader";
    }

    @Override
    public Map<String, Object> sample() {
        return null;
    }
}
