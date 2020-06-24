package com.wugui.datax.admin.tool.datax.writer;

import com.google.common.collect.Maps;
import com.wugui.datatx.core.util.Constants;
import com.wugui.datax.admin.dto.UpsertInfo;
import com.wugui.datax.admin.entity.JobDatasource;
import com.wugui.datax.admin.tool.pojo.DataxMongoDBPojo;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class MongoDBWriter extends BaseWriterPlugin implements DataxWriterInterface {
    @Override
    public String getName() {
        return "mongodbwriter";
    }

    @Override
    public Map<String, Object> sample() {
        return null;
    }


    @Override
    public Map<String, Object> buildMongoDB(DataxMongoDBPojo plugin) {
        //构建
        Map<String, Object> writerObj = Maps.newLinkedHashMap();
        JobDatasource dataSource = plugin.getJdbcDatasource();
        writerObj.put("name", getName());
        Map<String, Object> parameterObj = Maps.newLinkedHashMap();
        String[] addressList;
        if (dataSource.getJdbcUrl().contains(Constants.SPLIT_COMMA)) {
            addressList = dataSource.getJdbcUrl().split(Constants.SPLIT_COMMA);
        } else {
            addressList = new String[]{dataSource.getJdbcUrl()};
        }
        parameterObj.put("address", addressList);
        parameterObj.put("userName", dataSource.getJdbcUsername() == null ? Constants.STRING_BLANK : dataSource.getJdbcUsername());
        parameterObj.put("userPassword", dataSource.getJdbcPassword() == null ? Constants.STRING_BLANK : dataSource.getJdbcPassword());
        parameterObj.put("dbName", dataSource.getDatabaseName());
        parameterObj.put("collectionName", plugin.getWriterTable());
        parameterObj.put("column", plugin.getColumns());
        UpsertInfo upsert = plugin.getUpsertInfo();
        if (upsert != null) {
            parameterObj.put("upsertInfo", upsert);
        }
        writerObj.put("parameter", parameterObj);
        return writerObj;
    }
}
