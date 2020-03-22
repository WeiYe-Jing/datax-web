package com.wugui.datax.admin.tool.datax.reader;

import com.google.common.collect.Maps;
import com.wugui.datax.admin.tool.pojo.DataxMongoDBPojo;

import java.util.Map;

public class MongoDBReader extends BaseReaderPlugin implements DataxReaderInterface {
  @Override
  public String getName() {
    return "mongodbreader";
  }

  @Override
  public Map<String, Object> sample() {
    return null;
  }

  public Map<String, Object> buildMongoDB(DataxMongoDBPojo plugin) {
    //构建
    Map<String, Object> readerObj = Maps.newLinkedHashMap();
    readerObj.put("name", getName());
    Map<String, Object> parameterObj = Maps.newLinkedHashMap();
    parameterObj.put("address", plugin.getJdbcDatasource().getJdbcUrl());
    parameterObj.put("userName", plugin.getJdbcDatasource().getJdbcUsername());
    parameterObj.put("userPassword", plugin.getJdbcDatasource().getJdbcPassword());
    parameterObj.put("dbName", plugin.getJdbcDatasource());
    parameterObj.put("collectionName", plugin.getReaderTable());
    readerObj.put("parameter", parameterObj);
    return readerObj;
  }
}
