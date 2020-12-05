package com.wugui.datax.admin.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.google.common.collect.Lists;
import com.wugui.datatx.core.datasource.BaseDataSource;
import com.wugui.datatx.core.datasource.DataSourceFactory;
import com.wugui.datatx.core.enums.DbType;
import com.wugui.datax.admin.entity.JobDatasource;
import com.wugui.datax.admin.service.DatasourceQueryService;
import com.wugui.datax.admin.service.JobDatasourceService;
import com.wugui.datax.admin.tool.query.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static com.wugui.datax.admin.tool.query.DriverConnectionFactory.getConnection;

/**
 * datasource query
 *
 * @author zhouhongfa@gz-yibo.com
 * @ClassName JdbcDatasourceQueryServiceImpl
 * @Version 1.0
 * @since 2019/7/31 20:51
 */
@Slf4j
@Service
public class DatasourceQueryServiceImpl implements DatasourceQueryService {

    private final JobDatasourceService jobDatasourceService;

    public DatasourceQueryServiceImpl(JobDatasourceService jobDatasourceService) {
        this.jobDatasourceService = jobDatasourceService;
    }

    @Override
    public List<String> getDBs(Long id) throws IOException {
        //获取数据源对象
        JobDatasource datasource = jobDatasourceService.getById(id);
        return new MongoDBQueryTool(datasource.getType(),datasource.getConnectionParams()).getDBNames();
    }


    @Override
    public List<String> getTables(Long id, String tableSchema) throws IOException {
        //获取数据源对象
        JobDatasource datasource = jobDatasourceService.getById(id);
        BaseDataSource datasourceForm=DataSourceFactory.getDatasource(datasource.getType(),datasource.getConnectionParams());
        //queryTool组装
        if (ObjectUtil.isNull(datasource)) {
            return Lists.newArrayList();
        }
        if (DbType.HBASE.equals(datasource.getType())) {
            return new HBaseQueryTool(datasource.getType(),datasource.getConnectionParams()).getTableNames();
        } else if (DbType.MONGODB.equals(datasource.getType())) {
            return new MongoDBQueryTool(datasource.getType(),datasource.getConnectionParams()).getCollectionNames(datasourceForm.getDatabase());
        } else {
            BaseQueryTool qTool = QueryToolFactory.getByDbType(datasource.getType(),datasource.getConnectionParams());
            if(StringUtils.isBlank(tableSchema)){
                return qTool.getTableNames();
            }else{
                return qTool.getTableNames(tableSchema);
            }
        }
    }

    @Override
    public List<String> getDbSchema(Long id) {
        //获取数据源对象
        JobDatasource dataSource = jobDatasourceService.getById(id);
        //queryTool组装
        if (ObjectUtil.isNull(dataSource)) {
            return Lists.newArrayList();
        }
        BaseQueryTool qTool = QueryToolFactory.getByDbType(dataSource.getType(),dataSource.getConnectionParams());
        return qTool.getDbSchema();
    }

    @Override
    public List<String> getCollectionNames(long id, String dbName) throws IOException {
        //获取数据源对象
        JobDatasource datasource = jobDatasourceService.getById(id);
        //queryTool组装
        if (ObjectUtil.isNull(datasource)) {
            return Lists.newArrayList();
        }
        return new MongoDBQueryTool(datasource.getType(),datasource.getConnectionParams()).getCollectionNames(dbName);
    }


    @Override
    public List<String> getColumns(Long id, String tableName) throws IOException {
        //获取数据源对象
        JobDatasource datasource = jobDatasourceService.getById(id);
        //queryTool组装
        if (ObjectUtil.isNull(datasource)) {
            return Lists.newArrayList();
        }
        if (DbType.HBASE.equals(datasource.getType())) {
            return new HBaseQueryTool(datasource.getType(),datasource.getConnectionParams()).getColumns(tableName);
        } else if (DbType.MONGODB.equals(datasource.getType())) {
            return new MongoDBQueryTool(datasource.getType(),datasource.getConnectionParams()).getColumns(tableName);
        } else {
            BaseQueryTool queryTool = QueryToolFactory.getByDbType(datasource.getType(),datasource.getConnectionParams());
            return queryTool.getColumnNames(tableName, datasource.getType());
        }
    }

    @Override
    public List<String> getColumnsByQuerySql(Long datasourceId, String querySql) throws SQLException {
        //获取数据源对象
        JobDatasource datasource = jobDatasourceService.getById(datasourceId);
        //queryTool组装
        if (ObjectUtil.isNull(datasource)) {
            return Lists.newArrayList();
        }
        BaseQueryTool queryTool = QueryToolFactory.getByDbType(datasource.getType(),datasource.getConnectionParams());
        return queryTool.getColumnsByQuerySql(querySql);
    }

    /**
     * check connection
     *
     * @param type      data source type
     * @param parameter data source parameters
     * @return true if connect successfully, otherwise false
     */
    public boolean checkConnection(DbType type, String parameter) {
        Boolean isConnection = false;
        Connection con = getConnection(type, parameter);
        if (con != null) {
            isConnection = true;
            try {
                con.close();
            } catch (SQLException e) {
                log.error("close connection fail at DataSourceService::checkConnection()", e);
            }
        }
        return isConnection;
    }
}
