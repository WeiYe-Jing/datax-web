package com.wugui.datax.admin.util;

import java.util.ArrayList;

import java.util.List;

/**
 * @Author: zhc
 * @Data: 2019/12/30 16:19
 * @Email: ah.zhanghaicheng@aisino.com
 * @Description: mysql、oracle数据库类型和hive类型做适配，先实现功能，后期在考虑优化
 */
public class MysqlAndOracleTypeToHive {
    public static List<String> changeType(List<String> type) {
        List<String> hiveType = new ArrayList<>();
//        System.out.println(type.get(0));
        for (int i = 0; i < type.size(); i++) {
//            System.out.println(type.get(i));
            if ("CHAR".equals(type.get(i)) || "VARCHAR".equals(type.get(i)) || "TINYBLOB".equals(type.get(i))
                    || "TINYTEXT".equals(type.get(i)) || "BLOB".equals(type.get(i)) || "TEXT".equals("type.get(i)")
                    || "MEDIUMBLOB".equals(type.get(i)) || "MEDIUMTEXT".equals(type.get(i)) || "LONGBLOB".equals(type.get(i))
                    || "LONGTEXT".equals(type.get(i)) || "VARCHAR2".equals(type.get(i)) || "NCHAR".equals(type.get(i)) || "NVARCHAR".equals(type.get(i)) || "NVARCHAR2".equals(type.get(i))
                    || "RAW".equals(type.get(i)) || "LONG RAW".equals(type.get(i)) || "CLOB".equals("type.get(i)")
                    || "NCLOB".equals(type.get(i)) || "BFILE".equals(type.get(i))) {
                hiveType.add(i, "string");
            } else if ("TINYINT".equals(type.get(i)) || "SMALLINT".equals(type.get(i)) || "INT".equals(type.get(i)) || "BIGINT".equals(type.get(i))
                    || "FLOAT".equals(type.get(i)) || "DOUBLE".equals(type.get(i)) || "DECIMAL".equals(type.get(i)) || "DATE".equals(type.get(i)) || "TIMESTAMP".equals(type.get(i))) {
                hiveType.add(i, type.get(i));
            } else if ("LONG".equals(type.get(i))) {
                hiveType.add(i, "bigint");
            } else if ("DATETIME".equals(type.get(i))) {
                hiveType.add(i, "timestamp");
            } else if ("NUMBER".equals(type.get(i))) {
                hiveType.add(i, "double");
            } else {
                hiveType.add(i, "string");
            }
        }
        return hiveType;
    }
}
