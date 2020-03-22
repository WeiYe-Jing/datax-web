package com.wugui.datax.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wugui.datax.admin.mapper.JobJdbcDatasourceMapper;
import com.wugui.datax.admin.entity.JobDatasource;
import com.wugui.datax.admin.service.JobDatasourceService;
import com.wugui.datax.admin.tool.query.BaseQueryTool;
import com.wugui.datax.admin.tool.query.HBaseQueryTool;
import com.wugui.datax.admin.tool.query.MongoDBQueryTool;
import com.wugui.datax.admin.tool.query.QueryToolFactory;
import com.wugui.datax.admin.util.JdbcConstants;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

/**
 * Created by jingwk on 2020/01/30
 */
@Service
@Transactional(readOnly = true)
public class JobDatasourceServiceImpl extends ServiceImpl<JobJdbcDatasourceMapper, JobDatasource> implements JobDatasourceService {

    @Override
    public Boolean dataSourceTest(JobDatasource jobDatasource) throws IOException {
        if (JdbcConstants.HBASE.equals(jobDatasource.getDatasource())) {
            return HBaseQueryTool.getInstance(jobDatasource).dataSourceTest();
        }
        if (JdbcConstants.MONGODB.equals(jobDatasource.getDatasource())) {
            return MongoDBQueryTool.getInstance(jobDatasource).dataSourceTest(jobDatasource.getDatabaseName());
        }
        BaseQueryTool queryTool = QueryToolFactory.getByDbType(jobDatasource);
        return queryTool.dataSourceTest();
    }

}