package com.wugui.datax.admin.tool.datax.reader;

import com.google.common.collect.Maps;
import com.wugui.datax.admin.tool.pojo.DataxHbasePojo;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public class HBaseReader extends BaseReaderPlugin implements DataxReaderInterface {
  @Override
  public String getName() {
    return "hbase11xreader";
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
    confige.put("hbase.zookeeper.quorum",plugin.getReaderHbaseConfig());
    parameterObj.put("hbaseConfig", confige);
    parameterObj.put("table", plugin.getReaderTable());
    parameterObj.put("mode", plugin.getReaderMode());
    parameterObj.put("column", plugin.getColumns());
    if(StringUtils.isNotBlank(plugin.getReaderRange().getStartRowkey()) && StringUtils.isNotBlank(plugin.getReaderRange().getEndRowkey())){
      parameterObj.put("range", plugin.getReaderRange());
    }
    parameterObj.put("maxVersion", plugin.getReaderMaxVersion());
    readerObj.put("parameter", parameterObj);
    return readerObj;
  }
}
