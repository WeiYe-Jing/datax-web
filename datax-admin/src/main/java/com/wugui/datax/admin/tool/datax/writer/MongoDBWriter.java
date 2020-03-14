package com.wugui.datax.admin.tool.datax.writer;

import com.google.common.collect.Maps;
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
        parameterObj.put("address", plugin.getAddress());
        parameterObj.put("userName", plugin.getJdbcDatasource().getJdbcUsername());
        parameterObj.put("userPassword", plugin.getJdbcDatasource().getJdbcPassword());
        parameterObj.put("dbName", plugin.getDbName());
        parameterObj.put("collectionName", plugin.getCollectionName());
        writerObj.put("parameter", parameterObj);

        Map<String, Object> upsertInfo = Maps.newLinkedHashMap();
        upsertInfo.put("isUpsert", plugin.isUpsert());
        parameterObj.put("upsertKey", plugin.getUpsertKey());
        writerObj.put("upsertInfo", upsertInfo);
        return writerObj;
    }
}
