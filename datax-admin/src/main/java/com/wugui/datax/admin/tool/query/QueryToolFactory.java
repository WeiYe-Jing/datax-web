package com.wugui.datax.admin.tool.query;

import com.wugui.datatx.core.enums.DbType;
import com.wugui.datax.admin.tool.enums.DbTypePlugin;

import java.lang.reflect.Constructor;

/**
 * 获取单例实体
 *
 * @author weiye
 * @ClassName QueryToolFactory
 * @Version 2.1.3
 * @since 2020/08/02 9:36
 */
public class QueryToolFactory {

    public static BaseQueryTool getByDbType(DbType dbType, String parameter) {
        Class clazz = DbTypePlugin.getDbTypePlugin(dbType).getClazz();
        BaseQueryTool baseQueryTool = null;
        if (clazz != null) {
            try {
                Constructor<BaseQueryTool> c = clazz.getConstructor(DbType.class, String.class);
                baseQueryTool = c.newInstance(dbType, parameter);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
        return baseQueryTool;
    }
}
