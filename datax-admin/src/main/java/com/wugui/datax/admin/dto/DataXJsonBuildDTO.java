package com.wugui.datax.admin.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 构建json dto
 *
 * @author jingwk
 * @ClassName DataXJsonDto
 * @Version 2.1.1
 * @since 2020/03/14 07:15
 */
@Data
public class DataXJsonBuildDTO implements Serializable {

    private Long readerDatasourceId;

    private List<String> readerTables;

    private List<String> readerColumns;

    private List<String> transformer;

    private Long writerDatasourceId;

    private List<String> writerTables;

    private List<String> writerColumns;

    private HiveReaderDTO hiveReader;

    private HiveWriterDTO hiveWriter;

    private HbaseReaderDTO hbaseReader;

    private HbaseWriterDTO hbaseWriter;

    private RdbmsReaderDTO rdbmsReader;

    private RdbmsWriterDTO rdbmsWriter;

    private MongoDBReaderDTO mongoDBReader;

    private MongoDBWriterDTO mongoDBWriter;
}
