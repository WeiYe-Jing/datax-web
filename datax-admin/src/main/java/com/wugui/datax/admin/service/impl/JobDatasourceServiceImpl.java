package com.wugui.datax.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wugui.datax.admin.mapper.JobDatasourceMapper;
import com.wugui.datax.admin.entity.JobDatasource;
import com.wugui.datax.admin.service.JobDatasourceService;
import com.wugui.datax.admin.tool.query.BaseQueryTool;
import com.wugui.datax.admin.tool.query.HBaseQueryTool;
import com.wugui.datax.admin.tool.query.MongoDBQueryTool;
import com.wugui.datax.admin.tool.query.QueryToolFactory;
import com.wugui.datax.admin.util.AESUtil;
import com.wugui.datax.admin.util.JdbcConstants;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * Created by jingwk on 2020/01/30
 */
@Service
@Transactional(readOnly = true)
public class JobDatasourceServiceImpl extends ServiceImpl<JobDatasourceMapper, JobDatasource> implements JobDatasourceService {

    @Resource
    private JobDatasourceMapper datasourceMapper;

    @Override
    public Boolean  dataSourceTest(JobDatasource jobDatasource) throws IOException {
        if (JdbcConstants.HBASE.equals(jobDatasource.getDatasource())) {
            return new HBaseQueryTool(jobDatasource).dataSourceTest();
        }
        String userName = AESUtil.decrypt(jobDatasource.getJdbcUsername());
        //  判断账密是否为密文
        if (userName == null) {
            jobDatasource.setJdbcUsername(AESUtil.encrypt(jobDatasource.getJdbcUsername()));
        }
        String pwd = AESUtil.decrypt(jobDatasource.getJdbcPassword());
        if (pwd == null) {
            jobDatasource.setJdbcPassword(AESUtil.encrypt(jobDatasource.getJdbcPassword()));
        }
        if (JdbcConstants.MONGODB.equals(jobDatasource.getDatasource())) {
            return new MongoDBQueryTool(jobDatasource).dataSourceTest(jobDatasource.getDatabaseName());
        }
        BaseQueryTool queryTool = QueryToolFactory.getByDbType(jobDatasource);
        return queryTool.dataSourceTest();
    }

    @Override
    public int update(JobDatasource datasource) {
        return datasourceMapper.update(datasource);
    }

    @Override
    public List<JobDatasource> selectAllDatasource() {
        return datasourceMapper.selectList(null);
    }

}