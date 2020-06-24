package com.wugui.datax.admin.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class RowkeyColumn implements Serializable {

  private Integer index;

  private String type;

  private String value;
}
