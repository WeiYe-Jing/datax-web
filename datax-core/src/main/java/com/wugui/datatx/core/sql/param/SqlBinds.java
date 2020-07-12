package com.wugui.datatx.core.sql.param;

import com.wugui.datatx.core.sql.Property;

import java.util.Map;

/**
 * Used to contains both prepared sql string and its to-be-bind parameters
 */
public class SqlBinds {
    private final String sql;
    private final Map<Integer, Property> paramsMap;

    public SqlBinds(String sql, Map<Integer, Property> paramsMap) {
        this.sql = sql;
        this.paramsMap = paramsMap;
    }

    public String getSql() {
        return sql;
    }

    public Map<Integer, Property> getParamsMap() {
        return paramsMap;
    }
}
