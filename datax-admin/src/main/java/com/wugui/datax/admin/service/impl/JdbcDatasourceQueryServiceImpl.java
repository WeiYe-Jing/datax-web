package com.wugui.datax.admin.service.impl;

import cn.hutool.core.lang.Pair;
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
    public List<String> getTables(Long datasourceId) {
        Pair<BaseQueryTool, JobJdbcDatasource> pair = getQueryTool(datasourceId);
        return pair == null ? Lists.newArrayList() : pair.getKey().getTableNames();
    }

    @Override
    public List<String> getColumns(Long datasourceId, String tableName) {
        Pair<BaseQueryTool, JobJdbcDatasource> pair = getQueryTool(datasourceId);
        return pair == null ? Lists.newArrayList() : pair.getKey().getColumnNames(tableName, pair.getValue().getDatasource());
    }

    @Override
    public List<String> getColumnsByQuerySql(Long datasourceId, String querySql) {
        Pair<BaseQueryTool, JobJdbcDatasource> pair = getQueryTool(datasourceId);
        return pair == null ? Lists.newArrayList() : pair.getKey().getColumnsByQuerySql(querySql);
    }

    private Pair<BaseQueryTool, JobJdbcDatasource> getQueryTool(Long datasourceId) {
        //获取数据源对象
        JobJdbcDatasource jdbcDatasource = jobJdbcDatasourceService.getById(datasourceId);
        //queryTool组装
        if (ObjectUtil.isNull(jdbcDatasource)) {
            return null;
        }
        return new Pair<>(QueryToolFactory.getByDbType(jdbcDatasource), jdbcDatasource);
    }
}
