package com.wugui.tool.pojo;

import com.wugui.dataxweb.entity.JobJdbcDatasource;
import lombok.Data;

import java.util.List;

/**
 * 用于传参，构建json
 *
 * @author zhouhongfa@gz-yibo.com
 * @ClassName DataxPluginPojo
 * @Version 1.0
 * @since 2019/7/31 9:26
 */
@Data
public class DataxPluginPojo {

    /**
     * 表名
     */
    private List<String> tables;

    /**
     * 列名
     */
    private List<String> columns;

    /**
     * 数据源信息
     */
    private JobJdbcDatasource jdbcDatasource;

    /**
     * querySql 属性，如果指定了，则优先于columns参数
     */
    private String querySql;

    /**
     * preSql 属性
     */
    private String preSql;
}
