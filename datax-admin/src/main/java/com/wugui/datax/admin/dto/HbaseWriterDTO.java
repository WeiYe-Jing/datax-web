package com.wugui.datax.admin.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author jingwk
 */
@Data
public class HbaseWriterDTO implements Serializable {

    private String writeNullMode;

    private String writerMode;

    private String writerRowkeyColumn;

    private VersionColumn writerVersionColumn;
}
