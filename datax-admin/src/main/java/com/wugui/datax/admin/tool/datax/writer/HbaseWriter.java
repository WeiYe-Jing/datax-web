package com.wugui.datax.admin.tool.datax.writer;

import com.google.common.collect.Maps;
import com.wugui.datax.admin.tool.pojo.DataxHbasePojo;

import java.util.Map;

public class HbaseWriter extends BaseWriterPlugin implements DataxWriterInterface {
  @Override
  public String getName() {
    return "hbase11xwriter";
  }

  @Override
  public Map<String, Object> sample() {
    return null;
  }

  public Map<String, Object> buildHbase(DataxHbasePojo plugin) {
    //构建
    Map<String, Object> readerObj = Maps.newLinkedHashMap();
    readerObj.put("name", getName());
    Map<String, Object> parameterObj = Maps.newLinkedHashMap();
    Map<String, Object> confige = Maps.newLinkedHashMap();
    confige.put("hbase.zookeeper.quorum",plugin.getWriterHbaseConfig());
    parameterObj.put("hbaseConfig", confige);
    parameterObj.put("table", plugin.getWriterTable());
    parameterObj.put("encoding", plugin.getWriterEncoding());
    parameterObj.put("mode", plugin.getWriterMode());
    parameterObj.put("column", plugin.getColumns());
    parameterObj.put("range", plugin.getWriterRange());
    parameterObj.put("rowkeyColumn", plugin.getWriterRowkeyColumn());
    parameterObj.put("versionColumn",plugin.getWritervVersionColumn());
    readerObj.put("parameter", parameterObj);
    return readerObj;
  }
}
