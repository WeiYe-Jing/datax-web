package com.wugui.datax.admin.tool.database;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 原始jdbc字段对象
 *
 * @author zhouhongfa@gz-yibo.com
 * @ClassName DasColumn
 * @Version 1.0
 * @since 2019/7/17 16:29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DasColumn {

    private String columnName;

    private String columnTypeName;

    private String columnClassName;

    private String columnComment;
    private int isNull;
    private boolean isprimaryKey;
}
