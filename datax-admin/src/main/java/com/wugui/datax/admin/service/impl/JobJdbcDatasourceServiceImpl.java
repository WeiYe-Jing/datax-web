package com.wugui.datax.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wugui.datax.admin.mapper.JobJdbcDatasourceMapper;
import com.wugui.datax.admin.entity.JobJdbcDatasource;
import com.wugui.datax.admin.service.IJobJdbcDatasourceService;
import com.wugui.datax.admin.tool.query.BaseQueryTool;
import com.wugui.datax.admin.tool.query.QueryToolFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * jdbc数据源配置表服务实现类
 *
 * @author zhouhongfa@gz-yibo.com
 * @version v1.0
 * @since 2019-07-30
 */
@Service
@Transactional(readOnly = true)
public class JobJdbcDatasourceServiceImpl extends ServiceImpl<JobJdbcDatasourceMapper, JobJdbcDatasource> implements IJobJdbcDatasourceService {

    @Override
    public Boolean dataSourceTest(JobJdbcDatasource jdbcDatasource)  {
        BaseQueryTool queryTool = QueryToolFactory.getByDbType(jdbcDatasource);
        return queryTool.dataSourceTest();
    }

}