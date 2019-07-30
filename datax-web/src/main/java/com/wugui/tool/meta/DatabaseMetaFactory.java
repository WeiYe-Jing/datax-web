package com.wugui.tool.meta;

import com.alibaba.druid.util.JdbcConstants;

/**
 * TODO
 *
 * @author zhouhongfa@gz-yibo.com
 * @ClassName DatabaseMetaFactory
 * @Version 1.0
 * @since 2019/7/17 15:55
 */
public class DatabaseMetaFactory {

    //根据数据库类型返回对应的接口
    public static DatabaseInterface getByDbType(String dbType) {
        if (JdbcConstants.MYSQL.equals(dbType.toLowerCase()) || JdbcConstants.MYSQL.equals(dbType.toUpperCase())) {
            return MySQLDatabaseMeta.getInstance();
        } else if (JdbcConstants.ORACLE.equals(dbType.toLowerCase()) || JdbcConstants.ORACLE.equals(dbType.toUpperCase())) {
            return OracleDatabaseMeta.getInstance();
        } else {
            throw new UnsupportedOperationException("暂不支持的类型：".concat(dbType));
        }
    }
}
