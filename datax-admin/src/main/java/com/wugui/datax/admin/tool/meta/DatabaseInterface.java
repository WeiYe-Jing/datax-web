package com.wugui.datax.admin.tool.meta;

public interface DatabaseInterface {

    /**
     * Returns the minimal SQL to launch in order to determine the layout of the resultset for a given com.com.wugui.datax.admin.tool.database table
     *
     * @param tableName The name of the table to determine the layout for
     * @return The SQL to launch.
     */
    public String getSQLQueryFields(String tableName);

    /**
     * 获取主键字段
     *
     * @return
     */
    public String getSQLQueryPrimaryKey();

    public String getSQLQueryTableNameComment();

    public String getSQLQueryTablesNameComments();

    /**
     * 获取所有表名的sql
     *
     * @return
     */
    public String getSQLQueryTables(String... args);


    /**
     * 获取所有的字段的sql
     *
     * @return
     */
    public String getSQLQueryColumns(String... args);

    /**
     * 获取表和字段注释的sql语句
     *
     * @return The SQL to launch.
     */
    public String getSQLQueryComment(String schemaName, String tableName, String columnName);

//    /**
//     * 查询表名所有字段信息
//     * @return
//     */
//    public String getSQLQueryColumnInfos();
}
