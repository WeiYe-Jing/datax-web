package com.wugui.datax.admin.tool.database;

import lombok.Data;

import java.util.List;

/**
 * 表信息
 *
 * @author zhouhongfa@gz-yibo.com
 * @version 1.0
 * @since 2019/7/30
 */
@Data
public class TableInfo {
    /**
     * 表名
     */
    private String name;

    /**
     * 注释
     */
    private String comment;
    /**
     * 所有列
     */
    private List<ColumnInfo> columns;
}
