package com.wugui.datax.admin.tool.query;

import com.wugui.datax.admin.tool.database.ColumnInfo;
import com.wugui.datax.admin.tool.database.TableInfo;

import java.util.List;
import java.util.Map;

/**
 * 基础查询接口
 *
 * @author zhouhongfa@gz-yibo.com
 * @version 1.0
 * @since 2019/7/18
 */
public interface QueryToolInterface {
    /**
     * 构建 tableInfo对象
     *
     * @param tableName 表名
     * @return
     */
    public TableInfo buildTableInfo(String tableName);

    /**
     * 获取指定表信息
     *
     * @return
     */
    public List<Map<String, Object>> getTableInfo(String tableName);

    /**
     * 获取当前schema下的所有表
     *
     * @return
     */
    public List<Map<String, Object>> getTables();

    /**
     * 根据表名获取所有字段
     *
     * @param tableName
     * @return2
     */
    public List<ColumnInfo> getColumns(String tableName);


    /**
     * 根据表名和获取所有字段名称（不包括表名）
     *
     * @param tableName
     * @return2
     */
    public List<String> getColumnNames(String tableName,String datasource);


    /**
     * 获取所有可用表名
     *
     * @return2
     */
    public List<String> getTableNames();

    public List<String> getColumnsByQuerySql(String querySql);

}
