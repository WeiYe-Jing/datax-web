package com.wugui.datax.admin.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class HbaseWriterDto implements Serializable {

  private String writeNullMode;

  private String writerMode;

  private RowkeyColumn writerRowkeyColumn;

  private VersionColumn writerVersionColumn;
}
