package com.wugui.dataxweb.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.wugui.dataxweb.dto.DataxJsonDto;
import com.wugui.dataxweb.entity.JobJdbcDatasource;
import com.wugui.dataxweb.service.DataxJsonService;
import com.wugui.dataxweb.service.IJobJdbcDatasourceService;
import com.wugui.tool.datax.DataxJsonHelper;
import com.wugui.tool.datax.writer.StreamWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * datax json构建实现类
 *
 * @author zhouhongfa@gz-yibo.com
 * @ClassName DataxJsonServiceImpl
 * @Version 1.0
 * @since 2019/8/1 16:40
 */
@Service
public class DataxJsonServiceImpl implements DataxJsonService {

    @Autowired
    private IJobJdbcDatasourceService jobJdbcDatasourceService;

    @Override
    public String buildJobJson(DataxJsonDto dataxJsonDto) {

        // datax json helper
        DataxJsonHelper dataxJsonHelper = new DataxJsonHelper();

        // reader
        JobJdbcDatasource readerDatasource = jobJdbcDatasourceService.getById(dataxJsonDto.getReaderDatasourceId());

        //querySql
        dataxJsonHelper.setQuerySql(dataxJsonDto.getQuerySql());
        //where
        if (StrUtil.isNotBlank(dataxJsonDto.getWhereParams())) {
            dataxJsonHelper.addWhereParams(dataxJsonDto.getWhereParams());
        }

        // reader plugin init
        dataxJsonHelper.initReader(readerDatasource, dataxJsonDto.getReaderTables(), dataxJsonDto.getReaderColumns());
        //
        //如果是streamwriter
        if (dataxJsonDto.getIfStreamWriter()) {
            dataxJsonHelper.setWriterPlugin(new StreamWriter());
        } else {
            JobJdbcDatasource writerDatasource = jobJdbcDatasourceService.getById(dataxJsonDto.getWriterDatasourceId());
            dataxJsonHelper.initWriter(writerDatasource, dataxJsonDto.getWriterTables(), dataxJsonDto.getWriterColumns());
            dataxJsonHelper.setPreSql(dataxJsonDto.getPreSql());
        }

        return JSON.toJSONString(dataxJsonHelper.buildJob());
    }
}
