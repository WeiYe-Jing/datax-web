package com.wugui.datax.admin.tool.datax.reader;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Maps;
import com.wugui.datax.admin.tool.pojo.DataxParquetFilePojo;

public class ParquetFileReader extends BaseReaderPlugin implements DataxReaderInterface {
  @Override
  public String getName() {
    return "parquetfilereader";
  }

  @Override
  public Map<String, Object> sample() {
    return null;
  }

  public Map<String, Object> buildParquetFile(DataxParquetFilePojo plugin) {
    //构建
    Map<String, Object> readerObj = Maps.newLinkedHashMap();
    readerObj.put("name", getName());
    Map<String, Object> parameterObj = Maps.newLinkedHashMap();
    
    parameterObj.put("path", plugin.getPath());
    parameterObj.put("encoding", StringUtils.isNotBlank(plugin.getEncoding()) ? plugin.getEncoding() : "UTF-8");
    parameterObj.put("column", plugin.getColumns());
    readerObj.put("parameter", parameterObj);
    return readerObj;
  }
}
