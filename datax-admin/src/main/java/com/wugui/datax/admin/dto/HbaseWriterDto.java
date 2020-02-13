package com.wugui.datax.admin.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class HbaseWriterDto implements Serializable {

  private String writerHbaseConfig;

  private List<String> writerTable;

  private String writerEncoding;

  private String writerMode;

  private RowkeyColumn writerRowkeyColumn;

  private VersionColumn writervVersionColumn;
}
