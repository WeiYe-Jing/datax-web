package com.wugui.datax.admin.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.google.common.collect.Lists;
import com.wugui.datax.admin.tool.query.QueryToolFactory;
import com.wugui.datax.admin.entity.JobJdbcDatasource;
import com.wugui.datax.admin.service.IJobJdbcDatasourceService;
import com.wugui.datax.admin.service.JdbcDatasourceQueryService;
import com.wugui.datax.admin.tool.query.BaseQueryTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * TODO
 *
 * @author zhouhongfa@gz-yibo.com
 * @ClassName JdbcDatasourceQueryServiceImpl
 * @Version 1.0
 * @since 2019/7/31 20:51
 */
@Service
public class JdbcDatasourceQueryServiceImpl implements JdbcDatasourceQueryService {

    @Autowired
    private IJobJdbcDatasourceService jobJdbcDatasourceService;

    @Override
    public List<String> getTables(Long id) {
        //获取数据源对象
        JobJdbcDatasource jdbcDatasource = jobJdbcDatasourceService.getById(id);
        //queryTool组装
        if (ObjectUtil.isNull(jdbcDatasource)) {
            return Lists.newArrayList();
        }
        BaseQueryTool queryTool = QueryToolFactory.getByDbType(jdbcDatasource);
        return queryTool.getTableNames();
    }

    @Override
    public List<String> getColumns(Long id, String tableName) {
        //获取数据源对象
        JobJdbcDatasource jdbcDatasource = jobJdbcDatasourceService.getById(id);
        //queryTool组装
        if (ObjectUtil.isNull(jdbcDatasource)) {
            return Lists.newArrayList();
        }
        BaseQueryTool queryTool = QueryToolFactory.getByDbType(jdbcDatasource);
        return queryTool.getColumnNames(tableName,jdbcDatasource.getJdbcDriverClass());
    }

    @Override
    public List<String> getColumnsByQuerySql(Long datasourceId, String querySql) {
        //获取数据源对象
        JobJdbcDatasource jdbcDatasource = jobJdbcDatasourceService.getById(datasourceId);
        //queryTool组装
        if (ObjectUtil.isNull(jdbcDatasource)) {
            return Lists.newArrayList();
        }
        BaseQueryTool queryTool = QueryToolFactory.getByDbType(jdbcDatasource);
        return queryTool.getColumnsByQuerySql(querySql);
    }
}
