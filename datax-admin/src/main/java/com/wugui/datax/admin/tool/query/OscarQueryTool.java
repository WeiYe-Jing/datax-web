package com.wugui.datax.admin.tool.query;

import com.wugui.datatx.core.enums.DbType;

/**
 * Oscar数据库使用的查询工具
 *
 * @author Locki
 * @date 2021-04-26
 */
public class OscarQueryTool extends BaseQueryTool implements QueryToolInterface {
    public OscarQueryTool(DbType dbType, String parameter) {
        super(dbType, parameter);
    }
}
