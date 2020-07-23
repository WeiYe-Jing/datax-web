package com.wugui.datax.admin.service.impl;

import com.alibaba.fastjson.JSON;
import com.wugui.datax.admin.dto.DataXBatchJsonBuildDto;
import com.wugui.datax.admin.dto.DataXJsonBuildDto;
import com.wugui.datax.admin.entity.JobDatasource;
import com.wugui.datax.admin.entity.JobInfo;
import com.wugui.datax.admin.entity.JobTemplate;
import com.wugui.datax.admin.mapper.JobInfoMapper;
import com.wugui.datax.admin.mapper.JobTemplateMapper;
import com.wugui.datax.admin.service.DatasourceQueryService;
import com.wugui.datax.admin.service.DataxJsonService;
import com.wugui.datax.admin.service.JobDatasourceService;
import com.wugui.datax.admin.tool.datax.DataxJsonHelper;
import com.wugui.datax.admin.tool.table.TableNameHandle;
import com.wugui.datax.admin.util.JdbcConstants;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    public String buildJobJson(DataXJsonBuildDto dataXJsonBuildDto) {
        DataxJsonHelper dataxJsonHelper = new DataxJsonHelper();
        // reader
        JobDatasource readerDatasource = jobJdbcDatasourceService.getById(dataXJsonBuildDto.getReaderDatasourceId());
        //处理reader表名
        processingTableName(readerDatasource, dataXJsonBuildDto.getReaderTables());
        // reader plugin init
        dataxJsonHelper.initReader(dataXJsonBuildDto, readerDatasource);

        JobDatasource writerDatasource = jobJdbcDatasourceService.getById(dataXJsonBuildDto.getWriterDatasourceId());
        //处理writer表名
        processingTableName(writerDatasource, dataXJsonBuildDto.getWriterTables());
        dataxJsonHelper.initWriter(dataXJsonBuildDto, writerDatasource);

        return JSON.toJSONString(dataxJsonHelper.buildJob());
    }

    /**
     * 处理表名称
     * 解决生成json中的表名称大小写敏感问题
     * 目前针对Oracle和postgreSQL
     * @param jobDatasource
     * @param tables
     */
    private void processingTableName(JobDatasource jobDatasource, List<String> tables) {
        if (JdbcConstants.ORACLE.equals(jobDatasource.getDatasource()) || JdbcConstants.POSTGRESQL.equals(jobDatasource.getDatasource())) {
            for (int i = 0; i < tables.size(); i++) {
                tables.set(i, TableNameHandle.addDoubleQuotes(tables.get(i)));
            }
        }
    }


}