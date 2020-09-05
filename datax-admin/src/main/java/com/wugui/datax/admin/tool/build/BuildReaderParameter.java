package com.wugui.datax.admin.tool.build;

import com.wugui.datax.admin.dto.DataXJsonBuildDTO;
import com.wugui.datax.admin.entity.JobDatasource;

import java.util.Map;

public abstract class BuildReaderParameter {
    /**
     * 将前端的参数和数据源信息组合成JdbcReaderParameters
     * @param dataXJsonBuildDto
     * @param readerDatasource
     * @return
     */
    abstract Map<String,Object> getJdbcReaderParameters(DataXJsonBuildDTO dataXJsonBuildDto, JobDatasource readerDatasource) throws Exception;
}
