package com.wugui.datax.admin.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 构建json dto
 *
 * @author jingwk
 * @ClassName RdbmsReaderDto
 * @Version 2.0
 * @since 2020/01/11 17:15
 */
@Data
public class RdbmsReaderDto implements Serializable {

    private String readerSplitPk;

    private String whereParams;

    private String querySql;
}
