package com.wugui.datax.admin.tool.query;

import com.wugui.datatx.core.enums.DbType;

/**
 * ClickHouse
 */

public class ClickHouseQueryTool extends BaseQueryTool implements QueryToolInterface {

    public ClickHouseQueryTool(DbType dbType, String parameter) {
        super(dbType, parameter);
    }
}
