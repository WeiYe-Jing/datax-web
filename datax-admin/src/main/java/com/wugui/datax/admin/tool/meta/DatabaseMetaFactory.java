package com.wugui.datax.admin.tool.meta;

import com.wugui.datatx.core.enums.DbType;
import com.wugui.datax.admin.tool.enums.DbTypePlugin;

/**
 * meta信息工厂
 *
 * @author weiye
 */
public class DatabaseMetaFactory {

    /**
     * 根据数据库类型返回对应的接口
     *
     * @param dbType String
     * @return DatabaseInterface
     */
    public static DatabaseInterface getByDbType(DbType dbType) {
        return DbTypePlugin.getDbTypePlugin(dbType).getDatabaseInterface();
    }
}