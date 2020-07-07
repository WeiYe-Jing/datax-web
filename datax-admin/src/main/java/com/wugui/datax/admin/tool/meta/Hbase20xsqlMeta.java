package com.wugui.datax.admin.tool.meta;

/**
 * MySQL数据库 meta信息查询
 *
 * @author zhouhongfa@gz-yibo.com
 * @ClassName MySQLDatabaseMeta
 * @Version 1.0
 * @since 2019/7/17 15:48
 */
public class Hbase20xsqlMeta extends BaseDatabaseMeta implements DatabaseInterface {

    private volatile static Hbase20xsqlMeta single;

    public static Hbase20xsqlMeta getInstance() {
        if (single == null) {
            synchronized (Hbase20xsqlMeta.class) {
                if (single == null) {
                    single = new Hbase20xsqlMeta();
                }
            }
        }
        return single;
    }


    @Override
    public String getSQLQueryTables(String... tableSchema) {
        return null;
    }
}
