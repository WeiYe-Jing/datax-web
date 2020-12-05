package com.wugui.datax.admin.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author jingwk
 */
@Data
public class HbaseReaderDTO implements Serializable {

  private String readerMaxVersion;

  private String readerMode;

  private Range readerRange;

}

