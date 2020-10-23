package com.wugui.datax.admin.tool.datax.reader;

/**
 * sqlserver reader 构建类
 *
 * @author zhouhongfa@gz-yibo.com
 * @version 1.0
 * @since 2019/8/2
 */
public class SqlServerReader extends BaseReaderPlugin implements DataxReaderInterface {

    @Override
    public String getName() {
        return "sqlserverreader";
    }
}
