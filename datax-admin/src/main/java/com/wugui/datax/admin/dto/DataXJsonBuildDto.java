package com.wugui.datax.admin.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 构建json dto
 *
 * @author jingwk
 * @ClassName DataxJsonDto
 * @Version 2.1.1
 * @since 2020/03/14 07:15
 */
@Data
public class DataXJsonBuildDto implements Serializable {

    private Long readerDatasourceId;

    private List<String> readerTables;

    private List<String> readerColumns;

    private Long writerDatasourceId;

    private List<String> writerTables;

    private List<String> writerColumns;

    private HiveReaderDto hiveReader;

    private HiveWriterDto hiveWriter;

    private HbaseReaderDto hbaseReader;

    private HbaseWriterDto hbaseWriter;

    private RdbmsReaderDto rdbmsReader;

    private RdbmsWriterDto rdbmsWriter;

    private MongoDBReaderDto mongoDBReader;

    private MongoDBWriterDto mongoDBWriter;
}
