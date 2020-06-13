package com.wugui.datax.admin.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 构建json dto
 *
 * @author jingwk
 * @ClassName DataXJsonDto
 * @Version 2.1.2
 * @since 2020/05/05 17:15
 */
@Data
public class DataXBatchJsonBuildDto implements Serializable {

    private Long readerDatasourceId;

    private List<String> readerTables;

    private Long writerDatasourceId;

    private List<String> writerTables;

    private int templateId;

    private RdbmsReaderDto rdbmsReader;

    private RdbmsWriterDto rdbmsWriter;
}
