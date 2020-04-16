package com.wugui.datax.admin.tool.table;

import com.wugui.datax.admin.tool.database.ColumnInfo;
import com.wugui.datax.admin.tool.query.BaseQueryTool;

import java.util.List;

public class ClickHouse {

    public static String database_name = "";

    public String buildMysql2ClickHouseCreateTbSQL(String tableName, BaseQueryTool queryTool) {
        List<ColumnInfo> columnInfos = queryTool.getColumns(tableName);
        /**
         * 构建建表语句
         */
        StringBuilder stringBuilder = new StringBuilder();
        String preSql = String.format("CREATE TABLE IF NOT EXISTS %s (", database_name + "_" + tableName);
        stringBuilder.append(preSql);
        String primatyKey = "";
        for (int i = 0; i < columnInfos.size(); i++) {
            ColumnInfo c = columnInfos.get(i);
            if (c.getIfPrimaryKey()) {
                primatyKey = c.getName();
            }
            String str = "";
            if (i == columnInfos.size() - 1) {
                switch (c.getType()) {
                    case "VARCHAR":
                    case "CHAR":
                        if (c.getIsnull() == 0) {
                            str = c.getName() + " String COMMENT '" + c.getComment() + "'";

                        } else {
                            str = c.getName() + " Nullable(String) COMMENT '" + c.getComment() + "'";
                        }
                        break;
                    case "INT":
                        if (c.getIsnull() == 0) {
                            str = c.getName() + " UInt16 COMMENT '" + c.getComment() + "'";

                        } else {
                            str = c.getName() + " Nullable(UInt16) COMMENT '" + c.getComment() + "'";
                        }
                        break;
                    case "DATE":
                    case "DATETIME":
                        if (c.getIsnull() == 0) {
                            str = c.getName() + " datetime COMMENT '" + c.getComment() + "'";

                        } else {
                            str = c.getName() + " Nullable(datetime) COMMENT '" + c.getComment() + "'";
                        }
                        break;
                    default:
                        System.out.println(c.getType());
                        break;
                }
            } else {
                switch (c.getType()) {
                    case "VARCHAR":
                    case "CHAR":
                        if (c.getIsnull() == 0) {
                            str = c.getName() + " String COMMENT '" + c.getComment() + "',";

                        } else {
                            str = c.getName() + " Nullable(String) COMMENT '" + c.getComment() + "',";
                        }
                        break;
                    case "INT":
                    case "TINYINT":
                    case "BIGINT":
                        if (c.getIsnull() == 0) {
                            str = c.getName() + " UInt16 COMMENT '" + c.getComment() + "',";

                        } else {
                            str = c.getName() + " Nullable(UInt16) COMMENT '" + c.getComment() + "',";
                        }
                        break;
                    case "DATE":
                    case "DATETIME":
                        if (c.getIsnull() == 0) {
                            str = c.getName() + " datetime COMMENT '" + c.getComment() + "',";

                        } else {
                            str = c.getName() + " Nullable(datetime) COMMENT '" + c.getComment() + "',";
                        }
                        break;
                    default:
                        System.out.println("=============尚未捕获的数据类型" + c.getType());
                        break;
                }
            }
            stringBuilder.append(str);
        }
        String afterSQl = String.format("   ,datacenter_insert_time DateTime DEFAULT now() COMMENT '数据中心数据抽取入库时间' ) ENGINE = MergeTree PARTITION BY id ORDER BY id", primatyKey, preSql);
        stringBuilder.append(afterSQl);
        System.out.println(stringBuilder.toString());
        return stringBuilder.toString();
    }
}
