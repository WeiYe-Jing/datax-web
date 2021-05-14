package com.wugui.datax.admin.tool.meta;
/**
 * Oracle数据库 meta信息查询
 *
 * @author zhouhongfa@gz-yibo.com
 * @ClassName MySQLDatabaseMeta
 * @Version 1.0
 * @since 2019/7/17 15:48
 */
public class OracleDatabaseMeta extends BaseDatabaseMeta{

    private volatile static OracleDatabaseMeta single;

    public static OracleDatabaseMeta getInstance() {
        if (single == null) {
            synchronized (OracleDatabaseMeta.class) {
                if (single == null) {
                    single = new OracleDatabaseMeta();
                }
            }
        }
        return single;
    }


    @Override
    public String getSQLQueryComment(String schemaName, String tableName, String columnName) {
        return String.format("select B.comments \n" +
                "  from all_tab_columns A, all_col_comments B\n" +
                " where a.OWNER = b.OWNER\n" +
                "   and a.COLUMN_NAME = b.column_name\n" +
                "   and A.Table_Name = B.Table_Name\n" +
                "    and A.OWNER = upper('%s')\n" +
                "   and A.Table_Name = upper('%s')\n" +
                "   AND A.column_name  = '%s'", schemaName, tableName, columnName);
    }

    @Override
    public String getSQLQueryPrimaryKey() {
        return "select cu.column_name from all_cons_columns cu, all_constraints au where cu.constraint_name = au.constraint_name and au.owner = ? and au.constraint_type = 'P' and au.table_name = ?";
    }

    @Override
    public String getSQLQueryTablesNameComments() {
        return "select table_name,comments from all_tab_comments";
    }

    @Override
    public String getSQLQueryTableNameComment() {
        return "select table_name,comments from all_tab_comments where table_name = ?";
    }

    @Override
    public String getSQLQueryTables(String... tableSchema) {
        return "select owner||'.'||table_name as table_name from all_tables where owner='" + tableSchema[0] + "' " +
                "union " +
                "select (owner||'.'||view_name) as table_name from all_views where owner='" + tableSchema[0] + "'" +
                "order by table_name";
    }

    @Override
    public String getSQLQueryTableSchema(String... args) {
        return "select username from all_users order by username";
    }


    @Override
    public String getSQLQueryTables() {
        return "select table_name from all_tab_comments";
    }

    @Override
    public String getSQLQueryColumns(String... args) {
        return "select column_name from all_tab_columns where owner = ? and table_name = ?";
    }
}