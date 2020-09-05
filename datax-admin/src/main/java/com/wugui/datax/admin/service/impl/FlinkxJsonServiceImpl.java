package com.wugui.datax.admin.service.impl;

import com.alibaba.fastjson.JSON;
import com.wugui.datax.admin.dto.DataXJsonBuildDTO;
import com.wugui.datax.admin.entity.JobDatasource;
import com.wugui.datax.admin.service.DataxJsonService;
import com.wugui.datax.admin.service.FlinkxJsonService;
import com.wugui.datax.admin.service.JobDatasourceService;
import com.wugui.datax.admin.tool.build.FlinkxBuildHelper;
import com.wugui.datax.admin.tool.datax.DataxJsonHelper;
import com.wugui.datax.admin.tool.table.TableNameHandle;
import com.wugui.datax.admin.util.JdbcConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FlinkxJsonServiceImpl implements FlinkxJsonService {

    @Autowired
    private JobDatasourceService jobDataSourceService;

    @Override
    public String buildJobJson(DataXJsonBuildDTO dataXJsonBuildDto) {
        DataxJsonHelper dataxJsonHelper = new DataxJsonHelper();

        FlinkxBuildHelper flinkxBuildHelper = new FlinkxBuildHelper();

        // reader
        JobDatasource readerDatasource = jobDataSourceService.getById(dataXJsonBuildDto.getReaderDatasourceId());

        // writer
        JobDatasource writerDatasource = jobDataSourceService.getById(dataXJsonBuildDto.getWriterDatasourceId());

        String job = flinkxBuildHelper.build(readerDatasource,writerDatasource,dataXJsonBuildDto);


        return job;
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