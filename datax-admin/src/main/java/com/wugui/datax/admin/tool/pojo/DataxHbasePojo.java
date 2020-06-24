package com.wugui.datax.admin.tool.pojo;

import com.wugui.datax.admin.dto.Range;
import com.wugui.datax.admin.dto.RowkeyColumn;
import com.wugui.datax.admin.dto.VersionColumn;
import com.wugui.datax.admin.entity.JobDatasource;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class DataxHbasePojo {

  private List<Map<String,Object>> columns;

  /**
   * 数据源信息
   */
  private JobDatasource jdbcDatasource;


  private String readerHbaseConfig;

  private List<String> readerTable;

  private String readerMode;

  private String readerMaxVersion;

  private Range readerRange;

  private String writerHbaseConfig;

  private List<String> writerTable;

  private String writerMode;

  private VersionColumn writerVersionColumn;

  private RowkeyColumn writerRowkeyColumn;
}
