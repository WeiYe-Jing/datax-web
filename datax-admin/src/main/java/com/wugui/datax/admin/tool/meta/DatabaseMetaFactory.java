package com.wugui.datax.admin.tool.meta;

import com.wugui.datax.admin.enums.DatabaseMetaEnum;
import com.wugui.datax.admin.util.JdbcConstants;

/**
 * meta信息工厂
 *
 * @author zhouhongfa@gz-yibo.com
 */
public class DatabaseMetaFactory {

    /**
     * 根据数据库类型返回对应的接口
     *
     * @param dbType String
     * @return DatabaseInterface
     */
    public static DatabaseInterface getByDbType(String dbType) {
        return DatabaseMetaEnum.getQueryToolEnum(dbType).getDatabaseInterface();
    }
}
