package com.wugui.datax.admin.tool.database;

import lombok.Data;

/**
 * 字段信息
 *
 * @author zhouhongfa@gz-yibo.com
 * @version 1.0
 * @since 2019/7/30
 */
@Data
public class ColumnInfo {
    /**
     * 字段名称
     */
    private String name;
    /**
     * 注释
     */
    private String comment;
    /**
     * 字段类型
     */
    private String type;

    /**
     * 是否是主键列
     */
    private Boolean ifPrimaryKey;
    /**
     * 是否可为null   0 不可为空  1 可以为null
     */
    private int isnull;
}