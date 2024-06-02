package com.wugui.datax.admin.tool.meta;

import cn.hutool.core.util.StrUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * <p>
 * dm数据库meta信息查询
 * </p>
 *
 * @author Listenabe
 * @since 2024/06/02
 */
public class DmDatabaseMeta extends BaseDatabaseMeta implements DatabaseInterface{

    private volatile static DmDatabaseMeta single;

    public static DmDatabaseMeta getInstance() {
        if (single == null) {
            synchronized (DmDatabaseMeta.class) {
                if (single == null) {
                    single = new DmDatabaseMeta();
                }
            }
        }
        return single;
    }

    /**
     * 根据条件返回表字段注释
     * @param schemaName schemaName
     * @param tableName 达梦数据库目前返回的是schema.tableName， 所以需要截取tableName
     * @param columnName 表字段名
     * @return
     */
    @Override
    public String getSQLQueryComment(String schemaName, String tableName, String columnName) {
        tableName = StringUtils.substringAfterLast(tableName, StrUtil.DOT).replace("\"", "");
        return String.format("SELECT comments FROM USER_COL_COMMENTS where OWNER = '%s' and TABLE_NAME = '%s' and COLUMN_NAME = '%s'", schemaName, tableName, columnName);
    }

    public static void main(String[] args) {
        String tableName = "TEST_SYNC.aaaa.TEST_USER";
        System.out.println(StringUtils.substringAfterLast(tableName, StrUtil.DOT).replace("\"", ""));
    }

    @Override
    public String getSQLQueryTables() {
        return "select CONCAT(OWNER, '.\"', table_name, '\"') from all_tables";
    }

    @Override
    public String getSQLQueryTables(String... tableSchema) {
        return "select CONCAT(OWNER, '.\"', table_name, '\"') from all_tables where OWNER = '%s'";
    }
}
