package com.wugui.datax.admin.service.impl;

import com.alibaba.fastjson.JSON;
import com.wugui.datax.admin.dto.DataxJsonDto;
import com.wugui.datax.admin.entity.JobJdbcDatasource;
import com.wugui.datax.admin.service.DataxJsonService;
import com.wugui.datax.admin.service.IJobJdbcDatasourceService;
import com.wugui.datax.admin.tool.datax.DataxJsonHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * com.wugui.datax json构建实现类
 *
 * @author jingwk
 * @ClassName DataxJsonServiceImpl
 * @Version 2.0
 * @since 2020/01/11 17:15
 */
@Service
public class DataxJsonServiceImpl implements DataxJsonService {

    private static final String[] sqlDataSource = new String[]{"mysql", "oracle", "postgresql", "sqlserver"};

    @Autowired
    private IJobJdbcDatasourceService jobJdbcDatasourceService;

    @Override
    public String buildJobJson(DataxJsonDto dataxJsonDto) {
        DataxJsonHelper dataxJsonHelper = new DataxJsonHelper();
        // reader
        JobJdbcDatasource readerDatasource = jobJdbcDatasourceService.getById(dataxJsonDto.getReaderDatasourceId());
        if (Arrays.stream(sqlDataSource).anyMatch(db -> db.equals(readerDatasource.getDatasource()))) {
            List<String> readerColumns = dataxJsonDto.getReaderColumns().stream()
                    .map(column -> "`" + column + "`")
                    .collect(Collectors.toList());
            dataxJsonDto.setReaderColumns(readerColumns);
        }
        // reader plugin init
        dataxJsonHelper.initReader(dataxJsonDto,readerDatasource);
        JobJdbcDatasource writerDatasource = jobJdbcDatasourceService.getById(dataxJsonDto.getWriterDatasourceId());
        if (Arrays.stream(sqlDataSource).anyMatch(db -> db.equals(writerDatasource.getDatasource()))) {
            List<String> writerColumns = dataxJsonDto.getWriterColumns().stream()
                    .map(column -> "`" + column + "`")
                    .collect(Collectors.toList());
            dataxJsonDto.setWriterColumns(writerColumns);
        }
        dataxJsonHelper.initWriter(dataxJsonDto,writerDatasource);
        return JSON.toJSONString(dataxJsonHelper.buildJob());
    }
}
