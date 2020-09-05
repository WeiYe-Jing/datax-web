package com.wugui.datax.admin.util;

import com.wugui.datax.admin.entity.JobDatasource;
import com.wugui.datax.admin.tool.table.TableNameHandle;

import java.util.List;

/**
 * 构建DataX/FLinkX相关的工具类
 */
public class BuildJobUtils {

    /**
     * 处理表名称
     * 解决生成json中的表名称大小写敏感问题
     * 目前针对Oracle和postgreSQL
     * @param jobDatasource
     * @param tables
     */
    public static void processingTableName(JobDatasource jobDatasource, List<String> tables) {
        if (JdbcConstants.ORACLE.equals(jobDatasource.getDatasource()) || JdbcConstants.POSTGRESQL.equals(jobDatasource.getDatasource())) {
            for (int i = 0; i < tables.size(); i++) {
                tables.set(i, TableNameHandle.addDoubleQuotes(tables.get(i)));
            }
        }
    }
}
