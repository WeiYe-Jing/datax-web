package com.wugui.datax.admin.tool.pojo;

import com.wugui.datax.admin.entity.JobDatasource;
import lombok.Data;

import java.util.List;

/**
 * 用于传参，构建json
 *
 * @author jingwk
 * @ClassName DataxRdbmsPojo
 * @Version 2.0
 * @since 2020/01/11 15:19
 */
@Data
public class DataxRdbmsPojo {

    /**
     * 表名
     */
    private List<String> tables;

    /**
     * 列名
     */
    private List<String> rdbmsColumns;

    /**
     * 数据源信息
     */
    private JobDatasource jobDatasource;

    /**
     * querySql 属性，如果指定了，则优先于columns参数
     */
    private String querySql;

    /**
     * preSql 属性
     */
    private String preSql;

    /**
     * postSql 属性
     */
    private String postSql;

    /**
     * 切分主键
     */
    private String splitPk;

    /**
     * where
     */
    private String whereParam;
}
