package com.wugui.dataxweb.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 构建json dto
 *
 * @author zhouhongfa@gz-yibo.com
 * @ClassName DataxJsonDto
 * @Version 1.0
 * @since 2019/8/1 17:15
 */
@Data
public class DataxJsonDto implements Serializable {

    private Long readerDatasourceId;

    private List<String> readerTables;

    private List<String> readerColumns;

    private Boolean ifStreamWriter;

    private Long writerDatasourceId;

    private List<String> writerTables;

    private List<String> writerColumns;

    private String whereParams;

    private String querySql;

    private String preSql;
}
