package com.wugui.datax.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wugui.datax.admin.entity.JobJdbcDatasource;

/**
 * jdbc数据源配置表服务接口
 *
 * @author zhouhongfa@gz-yibo.com
 * @version v1.0
 * @since 2019-07-30
 */
public interface IJobJdbcDatasourceService extends IService<JobJdbcDatasource> {


    /**
     * 测试数据源
     * @param jdbcDatasource
     * @return
     */
    Boolean dataSourceTest(JobJdbcDatasource jdbcDatasource);

    int update(JobJdbcDatasource datasource);
}