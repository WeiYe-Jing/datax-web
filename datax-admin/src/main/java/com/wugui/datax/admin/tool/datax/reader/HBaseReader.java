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
  public Map<String, Object> buildHbase(DataxHbasePojo plugin) {

    Map<String, Object> config = Maps.newLinkedHashMap();
    config.put("hbase.zookeeper.quorum",plugin.getReaderHbaseConfig());
    Map<String, Object> parameter = Maps.newLinkedHashMap();
    parameter.put("hbaseConfig", config);
    parameter.put("table", plugin.getReaderTable());
    parameter.put("mode", plugin.getReaderMode());
    parameter.put("column", plugin.getColumns());
    if(StringUtils.isNotBlank(plugin.getReaderRange().getStartRowkey()) && StringUtils.isNotBlank(plugin.getReaderRange().getEndRowkey())){
      parameter.put("range", plugin.getReaderRange());
    }
    parameter.put("maxVersion", plugin.getReaderMaxVersion());

    Map<String, Object> reader = Maps.newLinkedHashMap();
    reader.put("name", getName());
    reader.put("parameter", parameter);
    return reader;
  }
}
