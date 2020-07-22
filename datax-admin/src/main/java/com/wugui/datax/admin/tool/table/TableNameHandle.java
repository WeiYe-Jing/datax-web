package com.wugui.datax.admin.tool.table;

/**
 * 表名称处理
 */
public class TableNameHandle {

    /**
     * 添加双引号
     * 解决表名称大小写敏感问题。
     * 适用Oracle和PostgreSQL对表进行查询时使用
     *
     * @param tableName
     * @return
     */
    public static String addDoubleQuotes(String tableName) {
        if (tableName.indexOf(".") >= 0) {
            int idx = tableName.indexOf(".");
            String prefixStr = tableName.substring(0, idx);
            String suffixStr = tableName.substring(idx + 1);
            tableName = prefixStr + ".\"" + suffixStr + "\"";
        } else {
            tableName = "\"" + tableName + "\"";
        }
        return tableName;
    }

}
