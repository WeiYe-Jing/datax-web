package com.wugui.datax.admin.tool.datax.writer;

import com.google.common.collect.Maps;
import com.wugui.datax.admin.dto.UpsertInfo;
import com.wugui.datax.admin.tool.pojo.DataxMongoDBPojo;

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
        writerObj.put("name", getName());
        Map<String, Object> parameterObj = Maps.newLinkedHashMap();
        parameterObj.put("address", plugin.getJdbcDatasource().getJdbcUrl());
        parameterObj.put("userName", plugin.getJdbcDatasource().getJdbcUsername());
        parameterObj.put("userPassword", plugin.getJdbcDatasource().getJdbcPassword());
        parameterObj.put("dbName", plugin.getJdbcDatasource().getDatabaseName());
        parameterObj.put("collectionName", plugin.getCollectionName());
        UpsertInfo upsert = plugin.getUpsertInfo();
        if (upsert != null) {
            parameterObj.put("upsertInfo", upsert);
        }
        writerObj.put("parameter", parameterObj);
        return writerObj;
    }
}
