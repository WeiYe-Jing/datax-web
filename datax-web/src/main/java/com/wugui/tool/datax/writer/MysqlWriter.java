package com.wugui.tool.datax.writer;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * TODO
 *
 * @author zhouhongfa@gz-yibo.com
 * @ClassName MysqlWriter
 * @Version 1.0
 * @since 2019/7/30 23:08
 */
public class MysqlWriter implements DataxWriterInterface {
    @Override
    public String getName() {
        return "mysqlwriter";
    }

    @Override
    public Map<String, Object> build() {
        //获取表信息
//        TableInfo tableInfo = buildTableInfo(tableName);

        //构建
//        Map<String, Object> res = Maps.newLinkedHashMap();
        Map<String, Object> writerObj = Maps.newLinkedHashMap();
//
//        writerObj.put("name", getWriterName());
//
//        Map<String, Object> parameterObj = Maps.newLinkedHashMap();
//        parameterObj.put("writeMode", "insert");
//        parameterObj.put("username", jobJdbcDatasource.getJdbcUsername());
//        parameterObj.put("password", jobJdbcDatasource.getJdbcPassword());
//
//        List<String> columns = Lists.newArrayList();
//        //列表
//        tableInfo.getColumns().stream().forEach(e -> columns.add(e.getName()));
//        parameterObj.put("column", columns);
//
//        Map<String, Object> connectionObj = Maps.newLinkedHashMap();
//        connectionObj.put("table", ImmutableList.of(tableInfo.getName()));
//
//        connectionObj.put("jdbcUrl", jobJdbcDatasource.getJdbcUrl());
//
//        parameterObj.put("connection", ImmutableList.of(connectionObj));
//
//        writerObj.put("parameter", parameterObj);

        return writerObj;
    }

    @Override
    public Map<String, Object> sample() {
        return null;
    }
}
