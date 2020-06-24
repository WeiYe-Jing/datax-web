package com.wugui.datax.admin.tool.meta;

/**
 * Postgresql数据库 meta信息查询
 *
 * @author zhouhongfa@gz-yibo.com
 * @ClassName PostgresqlDatabaseMeta
 * @Version 1.0
 * @since 2019/8/2 11:02
 */
public class PostgresqlDatabaseMeta extends BaseDatabaseMeta implements DatabaseInterface {

    private volatile static PostgresqlDatabaseMeta single;

    public static PostgresqlDatabaseMeta getInstance() {
        if (single == null) {
            synchronized (PostgresqlDatabaseMeta.class) {
                if (single == null) {
                    single = new PostgresqlDatabaseMeta();
                }
            }
        }
        return single;
    }

    @Override
    public String getSQLQueryPrimaryKey() {
        return "select column_name from information_schema.columns where table_schema='public' and table_name='tb_cis_patient_info' and is_identity = 'YES'";
    }

    @Override
    public String getSQLQueryTables(String... args) {
        return "select relname as tabname from pg_class c \n" +
                "where  relkind = 'r' and relname not like 'pg_%' and relname not like 'sql_%' order by relname";
    }

    @Override
    public String getSQLQueryColumns(String... args) {
        return "SELECT a.attname as name \n" +
                "FROM pg_class as c,pg_attribute as a where c.relname = ? and a.attrelid = c.oid and a.attnum>0";
    }

    @Override
    public String getSQLQueryComment(String schemaName, String tableName, String columnName) {
        return null;
    }
}
