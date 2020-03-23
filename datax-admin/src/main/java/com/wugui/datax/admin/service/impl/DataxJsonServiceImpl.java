package com.wugui.datax.admin.service.impl;

import com.alibaba.fastjson.JSON;
import com.wugui.datax.admin.dto.DataxJsonDto;
import com.wugui.datax.admin.entity.JobDatasource;
import com.wugui.datax.admin.service.DataxJsonService;
import com.wugui.datax.admin.service.JobDatasourceService;
import com.wugui.datax.admin.tool.datax.DataxJsonHelper;
import org.apache.commons.lang.StringUtils;
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

    @Autowired
    private JobDatasourceService jobJdbcDatasourceService;

    @Override
    public String buildJobJson(DataxJsonDto dataxJsonDto) {
        DataxJsonHelper dataxJsonHelper = new DataxJsonHelper();
        // reader
        JobDatasource readerDatasource = jobJdbcDatasourceService.getById(dataxJsonDto.getReaderDatasourceId());
        // reader plugin init
        dataxJsonHelper.initReader(dataxJsonDto,readerDatasource);
        JobDatasource writerDatasource = jobJdbcDatasourceService.getById(dataxJsonDto.getWriterDatasourceId());
        dataxJsonHelper.initWriter(dataxJsonDto,writerDatasource);
        return JSON.toJSONString(dataxJsonHelper.buildJob());
    }
}