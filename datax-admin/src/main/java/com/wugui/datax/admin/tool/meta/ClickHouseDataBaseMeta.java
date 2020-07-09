package com.wugui.datax.admin.tool.meta;

public class ClickHouseDataBaseMeta extends BaseDatabaseMeta implements DatabaseInterface {
    private volatile static ClickHouseDataBaseMeta single;
    public static ClickHouseDataBaseMeta getInstance() {
        if (single == null) {
            synchronized (ClickHouseDataBaseMeta.class) {
                if (single == null) {
                    single = new ClickHouseDataBaseMeta();
                }
            }
        }
        return single;
    }
    @Override
    public String getSQLQueryTables() {
        return "show tables";
    }
}
