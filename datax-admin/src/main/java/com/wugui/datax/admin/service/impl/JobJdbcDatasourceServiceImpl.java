package com.wugui.datax.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wugui.datax.admin.mapper.JobJdbcDatasourceMapper;
import com.wugui.datax.admin.entity.JobJdbcDatasource;
import com.wugui.datax.admin.service.IJobJdbcDatasourceService;
import com.wugui.datax.admin.tool.query.BaseQueryTool;
import com.wugui.datax.admin.tool.query.QueryToolFactory;
import com.wugui.datax.admin.util.AESUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

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

    @Resource
    private JobJdbcDatasourceMapper datasourceMapper;

    @Override
    public Boolean dataSourceTest(JobJdbcDatasource jdbcDatasource) {

        String userName = AESUtil.decrypt(jdbcDatasource.getJdbcUsername());
        //  判断账密是否为密文
        if (userName == null) {
            jdbcDatasource.setJdbcUsername(AESUtil.encrypt(jdbcDatasource.getJdbcUsername()));
        }
        String pwd = AESUtil.decrypt(jdbcDatasource.getJdbcPassword());
        if (pwd == null) {
            jdbcDatasource.setJdbcPassword(AESUtil.encrypt(jdbcDatasource.getJdbcPassword()));
        }
        BaseQueryTool queryTool = QueryToolFactory.getByDbType(jdbcDatasource);
        return queryTool.dataSourceTest();
    }

    @Override
    public int update(JobJdbcDatasource datasource) {
        return datasourceMapper.update(datasource);
    }

}