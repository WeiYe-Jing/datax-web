package com.wugui.datax.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wugui.datatx.core.enums.DbType;
import com.wugui.datax.admin.entity.JobDatasource;
import com.wugui.datax.admin.mapper.JobDatasourceMapper;
import com.wugui.datax.admin.service.JobDatasourceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.List;

/**
 * @author jingwk on 2020/01/30
 */
@Service
@Slf4j
public class JobDatasourceServiceImpl extends ServiceImpl<JobDatasourceMapper, JobDatasource> implements JobDatasourceService {

    @Resource
    private JobDatasourceMapper datasourceMapper;

    @Override
    public int update(JobDatasource datasource) {
        return datasourceMapper.update(datasource);
    }

    @Override
    public List<JobDatasource> selectAllDatasource() {
        return datasourceMapper.selectList(null);
    }


    @Override
    public int createDataSource(String datasourceName, String datasourceGroup, DbType type, int status, String comments, String parameter) {
        JobDatasource datasource = new JobDatasource();
        datasource.setDatasourceName(datasourceName);
        datasource.setDatasourceGroup(datasourceGroup);
        datasource.setConnectionParams(parameter);
        datasource.setComments(comments);
        datasource.setType(type);
        datasource.setStatus(status);
        return datasourceMapper.insert(datasource);
    }

    @Override
    public int updateDataSource(long id, String datasourceName, String datasourceGroup, DbType type, int status, String comments, String parameter) {
        JobDatasource datasource = new JobDatasource();
        datasource.setDatasourceName(datasourceName);
        datasource.setDatasourceGroup(datasourceGroup);
        datasource.setConnectionParams(parameter);
        datasource.setComments(comments);
        datasource.setId(id);
        datasource.setType(type);
        datasource.setStatus(status);
        return datasourceMapper.updateById(datasource);
    }
}