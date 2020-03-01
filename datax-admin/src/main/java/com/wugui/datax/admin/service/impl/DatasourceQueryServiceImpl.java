package com.wugui.datax.admin.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.google.common.collect.Lists;
import com.wugui.datax.admin.entity.JobDatasource;
import com.wugui.datax.admin.service.DatasourceQueryService;
import com.wugui.datax.admin.service.JobDatasourceService;
import com.wugui.datax.admin.tool.query.BaseQueryTool;
import com.wugui.datax.admin.tool.query.HBaseQueryTool;
import com.wugui.datax.admin.tool.query.QueryToolFactory;
import com.wugui.datax.admin.util.DataSourceConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
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
public class DatasourceQueryServiceImpl implements DatasourceQueryService {

    @Autowired
    private JobDatasourceService jobDatasourceService;

    @Override
    public List<String> getTables(Long id) throws IOException {
        //获取数据源对象
        JobDatasource jobDatasource = jobDatasourceService.getById(id);
        //queryTool组装
        if (ObjectUtil.isNull(jobDatasource)) {
            return Lists.newArrayList();
        }
        if (DataSourceConstants.HBASE.equals(jobDatasource.getDatasource())) {
            return HBaseQueryTool.getInstance(jobDatasource).getTableNames();
        } else {
            BaseQueryTool qTool = QueryToolFactory.getByDbType(jobDatasource);
            return qTool.getTableNames();
        }
    }

    @Override
    public List<String> getColumns(Long id, String tableName) throws IOException {
        //获取数据源对象
        JobDatasource datasource = jobDatasourceService.getById(id);
        //queryTool组装
        if (ObjectUtil.isNull(datasource)) {
            return Lists.newArrayList();
        }
        if (DataSourceConstants.HBASE.equals(datasource.getDatasource())) {
            return HBaseQueryTool.getInstance(datasource).getColumns(tableName);
        } else {
            BaseQueryTool queryTool = QueryToolFactory.getByDbType(datasource);
            return queryTool.getColumnNames(tableName, datasource.getDatasource());
        }
    }

    @Override
    public List<String> getColumnsByQuerySql(Long datasourceId, String querySql) {
        //获取数据源对象
        JobDatasource jdbcDatasource = jobDatasourceService.getById(datasourceId);
        //queryTool组装
        if (ObjectUtil.isNull(jdbcDatasource)) {
            return Lists.newArrayList();
        }
        BaseQueryTool queryTool = QueryToolFactory.getByDbType(jdbcDatasource);
        return queryTool.getColumnsByQuerySql(querySql);
    }
}
