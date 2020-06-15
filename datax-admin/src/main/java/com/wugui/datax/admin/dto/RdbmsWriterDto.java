package com.wugui.datax.admin.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 构建json dto
 *
 * @author jingwk
 * @ClassName RdbmsWriteDto
 * @Version 2.0
 * @since 2020/01/11 17:15
 */
@Data
public class RdbmsWriterDto implements Serializable {

    private String preSql;

    private String postSql;
}
