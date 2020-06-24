package com.wugui.datax.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wugui.datax.admin.entity.JobDatasource;
import com.wugui.datax.admin.entity.JobDatasourceEntity;

import java.io.IOException;
import java.sql.SQLException;

/**
 * jdbc数据源配置表服务接口
 *
 * @author jingwk
 * @version v2.0
 * @since 2020-01-10
 */
public interface JobDatasourceService extends IService<JobDatasource> {
    /**
     * 测试数据源
     * @param jdbcDatasource
     * @return
     */
    Boolean dataSourceTest(JobDatasourceEntity jdbcDatasource) throws IOException, SQLException, ClassNotFoundException;

    int update(JobDatasource datasource);
}