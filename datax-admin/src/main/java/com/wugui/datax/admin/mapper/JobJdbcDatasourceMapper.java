package com.wugui.datax.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wugui.datax.admin.entity.JobJdbcDatasource;
import org.apache.ibatis.annotations.Mapper;

/**
 * jdbc数据源配置表数据库访问层
 *
 * @author zhouhongfa@gz-yibo.com
 * @version v1.0
 * @since 2019-07-30
 */
@Mapper
public interface JobJdbcDatasourceMapper extends BaseMapper<JobJdbcDatasource> {

    int update(JobJdbcDatasource datasource);

}