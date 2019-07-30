package com.wugui.tool.meta;

public interface DatabaseInterface {

    /**
     * Returns the minimal SQL to launch in order to determine the layout of the resultset for a given database table
     *
     * @param tableName The name of the table to determine the layout for
     * @return The SQL to launch.
     */
    public String getSQLQueryFields(String tableName);

    /**
     * 获取表和字段注释的sql语句
     *
     * @return The SQL to launch.
     */
    public String getSQLQueryComment(String schemaName, String tableName, String columnName);

    /**
     * 获取主键字段
     *
     * @return
     */
    public String getSQLQueryPrimaryKey();


    /**
     * 根据schemaName获取所有的表名和注释
     *
     * @return
     */
    public String getSQLQueryTablesNameComments();

    /**
     * 根据schemaName tableName 获取所有的表名和注释
     *
     * @return
     */
    public String getSQLQueryTableNameComment();

//    /**
//     * 查询表名所有字段信息
//     * @return
//     */
//    public String getSQLQueryColumnInfos();
}
