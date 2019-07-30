package com.wugui.tool.datax.reader;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * TODO
 *
 * @author zhouhongfa@gz-yibo.com
 * @ClassName MysqlReader
 * @Version 1.0
 * @since 2019/7/30 23:07
 */
public class MysqlReader implements DataxReaderInterface {
    @Override
    public String getName() {
        return "mysqlreader";
    }

    @Override
    public Map<String, Object> build() {
        //获取表信息
//        TableInfo tableInfo = buildTableInfo(tableName);

        //构建
        Map<String, Object> readerObj = Maps.newLinkedHashMap();

//        readerObj.put("name", getReaderName());
//
//        Map<String, Object> parameterObj = Maps.newLinkedHashMap();
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
//        //where
////        connectionObj.put("where", "1=2");
//        connectionObj.put("jdbcUrl", ImmutableList.of(jobJdbcDatasource.getJdbcUrl()));
//
//        parameterObj.put("connection", ImmutableList.of(connectionObj));
//
//        readerObj.put("parameter", parameterObj);

        return readerObj;
    }

    @Override
    public Map<String, Object> sample() {
        return null;
    }
}
