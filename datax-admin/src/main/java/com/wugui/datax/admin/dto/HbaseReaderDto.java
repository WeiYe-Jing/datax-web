package com.wugui.datax.admin.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class HbaseReaderDto implements Serializable {

  private String readerHbaseConfig;

  private List<String> readerTable;

  private String readerEncoding;

  private String readerMode;

  private Range readerRange;

}

