package com.wugui.tool.datax;

import com.wugui.dataxweb.entity.JobJdbcDatasource;

import java.util.List;
import java.util.Map;

/**
 * 构建 datax json的工具类
 *
 * @author zhouhongfa@gz-yibo.com
 * @ClassName DataxJsonHelper
 * @Version 1.0
 * @since 2019/7/30 22:24
 */
public class DataxJsonHelper implements DataxJsonInterface {

    /**
     * 读取的表，根据datax示例，支持多个表（先不考虑，后面再去实现， 这里先用list保存吧）
     * <p>
     * 目的表的表名称。支持写入一个或者多个表。当配置为多张表时，必须确保所有表结构保持一致
     */
    private List<String> readerTables;

    /**
     * reader jdbc 数据源
     */
    private JobJdbcDatasource readerDatasource;

    /**
     * writer jdbc 数据源
     */
    private JobJdbcDatasource writerDatasource;

    /**
     * 写入的表
     */
    private List<String> writerTables;

    @Override
    public Map<String, Object> buildJob() {
        return null;
    }

    @Override
    public Map<String, Object> builSetting() {
        return null;
    }

    @Override
    public Map<String, Object> buildContent() {
        return null;
    }

    @Override
    public Map<String, Object> buildReader() {
        return null;
    }

    @Override
    public Map<String, Object> buildWriter() {
        return null;
    }
}
