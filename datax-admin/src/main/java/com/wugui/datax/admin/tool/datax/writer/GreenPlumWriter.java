package com.wugui.datax.admin.tool.datax.writer;

/**
 * greenplum writer构建类
 *
 * @author zhouhongfa@gz-yibo.com
 * @version 1.0
 * @since 2019/8/2
 */
public class GreenPlumWriter extends BaseWriterPlugin implements DataxWriterInterface {

    @Override
    public String getName() {
        return "gpdbwriter";
    }
}
