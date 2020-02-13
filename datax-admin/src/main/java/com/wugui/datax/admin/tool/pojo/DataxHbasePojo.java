package com.wugui.datax.admin.tool.pojo;

import com.wugui.datax.admin.dto.Range;
import com.wugui.datax.admin.dto.RowkeyColumn;
import com.wugui.datax.admin.dto.VersionColumn;
import com.wugui.datax.admin.entity.JobJdbcDatasource;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class DataxHbasePojo {

  private List<Map<String,Object>> columns;

  /**
   * 数据源信息
   */
  private JobJdbcDatasource jdbcDatasource;


  private String readerHbaseConfig;

  private List<String> readerTable;

  private String readerEncoding;

  private String readerMode;

  private String readmaxVersion;

  private Range readerRange;

  private String writerHbaseConfig;

  private List<String> writerTable;

  private String writerEncoding;

  private String writerMode;

  private Range writerRange;

  private VersionColumn writervVersionColumn;

  private RowkeyColumn writerRowkeyColumn;
}
