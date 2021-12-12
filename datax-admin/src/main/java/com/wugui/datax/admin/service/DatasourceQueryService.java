package com.wugui.datax.admin.service;

import com.wugui.datatx.core.enums.DbType;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * 数据库查询服务
 *
 * @author zhouhongfa@gz-yibo.com
 * @ClassName JdbcDatasourceQueryService
 * @Version 1.0
 * @since 2019/7/31 20:50
 */
public interface DatasourceQueryService {


    /**
     * 获取db列表
     *
     * @param id
     * @return
     * @throws IOException
     */
    List<String> getDBs(Long id) throws IOException;


    /**
     * 根据数据源表id查询出可用的表
     *
     * @param id
     * @param tableSchema
     * @return
     * @throws IOException
     */
    List<String> getTables(Long id, String tableSchema) throws IOException;


    /**
     * 获取CollectionNames
     *
     * @param id
     * @param dbName
     * @return
     * @throws IOException
     */
    List<String> getCollectionNames(long id, String dbName) throws IOException;


    /**
     * 根据数据源id，表名查询出该表所有字段
     * @param id
     * @param tableName
     * @param tableSchema
     * @return
     * @throws IOException
     */
    List<String> getColumns(Long id, String tableName, String tableSchema) throws IOException;


    /**
     * 根据 sql 语句获取字段
     * @param dataSourceId
     * @param querySql
     * @return
     * @throws SQLException
     */
    List<String> getColumnsByQuerySql(Long dataSourceId, String querySql) throws SQLException;

    /**
     * 获取PG table schema
     *
     * @param id
     * @return
     */
    List<String> getDbSchema(Long id);

    boolean checkConnection(DbType type, String parameter);
}
