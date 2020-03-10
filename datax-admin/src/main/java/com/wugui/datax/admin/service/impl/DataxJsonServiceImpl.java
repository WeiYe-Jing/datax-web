package com.wugui.datax.admin.service.impl;

import com.alibaba.fastjson.JSON;
import com.wugui.datax.admin.dto.DataxJsonDto;
import com.wugui.datax.admin.entity.JobJdbcDatasource;
import com.wugui.datax.admin.service.DataxJsonService;
import com.wugui.datax.admin.service.IJobJdbcDatasourceService;
import com.wugui.datax.admin.tool.datax.DataxJsonHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Autowired
    private IJobJdbcDatasourceService jdbcDatasourceService;

    @Override
    public String buildJobJson(DataxJsonDto dto) {
        DataxJsonHelper dataxJsonHelper = new DataxJsonHelper();
        // reader
        JobJdbcDatasource readerDatasource = jdbcDatasourceService.getById(dto.getReaderDatasourceId());
        // reader plugin init
        dataxJsonHelper.initReader(dto, readerDatasource);
        JobJdbcDatasource writerDatasource = jdbcDatasourceService.getById(dto.getWriterDatasourceId());
        dataxJsonHelper.initWriter(dto, writerDatasource);
        return JSON.toJSONString(dataxJsonHelper.buildJob());
    }
}
