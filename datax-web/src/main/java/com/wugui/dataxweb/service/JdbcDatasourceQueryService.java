package com.wugui.dataxweb.service;

import java.util.List;

/**
 * 数据库查询服务层
 *
 * @author zhouhongfa@gz-yibo.com
 * @ClassName JdbcDatasourceQueryService
 * @Version 1.0
 * @since 2019/7/31 20:50
 */
public interface JdbcDatasourceQueryService {

    /**
     * 根据数据源表id查询出可用的表
     *
     * @param id
     * @return
     */
    List<String> getTables(Long id);


    /**
     * 根据数据源id，表名查询出该表所有字段
     *
     * @param id
     * @return
     */
    List<String> getColumns(Long id, String tableName);

}
