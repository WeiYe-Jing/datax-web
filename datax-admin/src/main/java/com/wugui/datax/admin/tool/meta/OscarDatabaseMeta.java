package com.wugui.datax.admin.tool.meta;

/**
 * Oscar数据库 meta信息查询
 *
 * @author Locki
 * @date 2021-04-25
 */
public class OscarDatabaseMeta extends BaseDatabaseMeta implements DatabaseInterface {

    private volatile static OscarDatabaseMeta single;

    public static OscarDatabaseMeta getInstance() {
        if (single == null) {
            synchronized (OscarDatabaseMeta.class) {
                if (single == null) {
                    single = new OscarDatabaseMeta();
                }
            }
        }
        return single;
    }


    @Override
    public String getSQLQueryComment(String schemaName, String tableName, String columnName) {
        return String.format("select B.comments \n" +
                "  from user_tab_columns A, user_col_comments B\n" +
                " where a.COLUMN_NAME = b.column_name\n" +
                "   and A.Table_Name = B.Table_Name\n" +
                "   and A.Table_Name = upper('%s')\n" +
                "   AND A.column_name  = '%s'", tableName, columnName);
    }

    @Override
    public String getSQLQueryPrimaryKey() {
        return "SELECT CU.COLUMN_NAME FROM USER_CONS_COLUMNS CU INNER JOIN USER_CONSTRAINTS AU ON CU.CONSTRAINT_NAME = AU.CONSTRAINT_NAME AND CU.TABLE_NAME = AU.TABLE_NAME WHERE AU.OWNER = ? AND AU.CONSTRAINT_TYPE = 'P' AND AU.TABLE_NAME = ?";
    }

    @Override
    public String getSQLQueryTablesNameComments() {
        return "select table_name,comments from user_tab_comments";
    }

    @Override
    public String getSQLQueryTableNameComment() {
        return "select table_name,comments from user_tab_comments where table_name = ?";
    }

    @Override
    public String getSQLQueryTables(String... tableSchema) {
        return "select table_name from dba_tables where owner='" + tableSchema[0] + "'";
    }

    @Override
    public String getSQLQueryTableSchema(String... args) {
        return "select username from dba_users";
    }


    @Override
    public String getSQLQueryTables() {
        return "select table_name from user_tab_comments";
    }

    @Override
    public String getSQLQueryColumns(String... args) {
        return "select table_name,comments from user_tab_comments where table_name = ?";
    }
}