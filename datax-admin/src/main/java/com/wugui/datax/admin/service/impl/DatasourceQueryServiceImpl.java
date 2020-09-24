package com.wugui.datax.admin.service.impl;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.wugui.datax.admin.entity.JobDatasource;
import com.wugui.datax.admin.service.DatasourceQueryService;
import com.wugui.datax.admin.service.JobDatasourceService;
import com.wugui.datax.admin.tool.query.BaseQueryTool;
import com.wugui.datax.admin.tool.query.HBaseQueryTool;
import com.wugui.datax.admin.tool.query.MongoDBQueryTool;
import com.wugui.datax.admin.tool.query.QueryToolFactory;
import com.wugui.datax.admin.util.JdbcConstants;

import cn.hutool.core.util.ObjectUtil;

/**
 * datasource query
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
    public List<String> getDBs(Long id) throws IOException {
        //获取数据源对象
        JobDatasource datasource = jobDatasourceService.getById(id);
        return new MongoDBQueryTool(datasource).getDBNames();
    }


    @Override
    public List<String> getTables(Long id, String tableSchema) throws IOException {
        //获取数据源对象
        JobDatasource datasource = jobDatasourceService.getById(id);
        //queryTool组装
        if (ObjectUtil.isNull(datasource)) {
            return Lists.newArrayList();
        }
        // 数据库类型：hbase，mysql，mongodb，rabbitmq
        String datasourceType = datasource.getDatasource();
        if (JdbcConstants.HBASE.equalsIgnoreCase(datasourceType)) {
            return new HBaseQueryTool(datasource).getTableNames();
        } else if (JdbcConstants.MONGODB.equalsIgnoreCase(datasourceType)) {
            return new MongoDBQueryTool(datasource).getCollectionNames(datasource.getDatabaseName());
        } else if (JdbcConstants.PARQUET_FILE.equalsIgnoreCase(datasourceType)
        		|| JdbcConstants.RABBITMQ.equalsIgnoreCase(datasourceType)
        		|| JdbcConstants.TEXT_FILE.equalsIgnoreCase(datasourceType)) {
        	return Lists.newArrayList("column");
    	} else {
            BaseQueryTool qTool = QueryToolFactory.getByDbType(datasource);
            if(StringUtils.isBlank(tableSchema)){
                return qTool.getTableNames();
            }else{
                return qTool.getTableNames(tableSchema);
            }
        }
    }

    @Override
    public List<String> getTableSchema(Long id) {
        //获取数据源对象
        JobDatasource datasource = jobDatasourceService.getById(id);
        //queryTool组装
        if (ObjectUtil.isNull(datasource)) {
            return Lists.newArrayList();
        }
        BaseQueryTool qTool = QueryToolFactory.getByDbType(datasource);
        return qTool.getTableSchema();
    }

    @Override
    public List<String> getCollectionNames(long id, String dbName) throws IOException {
        //获取数据源对象
        JobDatasource datasource = jobDatasourceService.getById(id);
        //queryTool组装
        if (ObjectUtil.isNull(datasource)) {
            return Lists.newArrayList();
        }
        return new MongoDBQueryTool(datasource).getCollectionNames(dbName);
    }


    @Override
    public List<String> getColumns(Long id, String tableName) throws IOException {
        //获取数据源对象
        JobDatasource datasource = jobDatasourceService.getById(id);
        //queryTool组装
        if (ObjectUtil.isNull(datasource)) {
            return Lists.newArrayList();
        }
        String datasourceType = datasource.getDatasource();
        if (JdbcConstants.HBASE.equalsIgnoreCase(datasourceType)) {
            return new HBaseQueryTool(datasource).getColumns(tableName);
        } else if (JdbcConstants.MONGODB.equalsIgnoreCase(datasourceType)) {
            return new MongoDBQueryTool(datasource).getColumns(tableName);
        } else if (JdbcConstants.PARQUET_FILE.equalsIgnoreCase(datasourceType)
        		|| JdbcConstants.RABBITMQ.equalsIgnoreCase(datasourceType)
        		|| JdbcConstants.TEXT_FILE.equalsIgnoreCase(datasourceType)) {
        	if (StringUtils.isNotBlank(datasource.getColumnx())) {
        		List<JSONObject> columnMapList = datasource.getColumnxList();
        		List<String> columnList = new ArrayList<>();
        		for (JSONObject json : columnMapList) {
        			columnList.add((String)json.get("name"));
				}
        		return columnList;
        	}
        	return Lists.newArrayList("*");
    	} else {
            BaseQueryTool queryTool = QueryToolFactory.getByDbType(datasource);
            return queryTool.getColumnNames(tableName, datasourceType);
        }
    }

    @Override
    public List<String> getColumnsByQuerySql(Long datasourceId, String querySql) throws SQLException {
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
