package com.wugui.datax.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wugui.datax.admin.entity.JobDatasource;

import java.io.IOException;
import java.util.List;

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
    Boolean dataSourceTest(JobDatasource jdbcDatasource) throws IOException;

    /**
     *更新数据源信息
     * @param datasource
     * @return
     */
    int update(JobDatasource datasource);

    /**
     * 获取所有数据源
     * @return
     */
    List<JobDatasource> selectAllDatasource();
}