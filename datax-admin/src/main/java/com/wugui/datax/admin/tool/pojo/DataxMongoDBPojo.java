package com.wugui.datax.admin.tool.pojo;

import com.wugui.datax.admin.entity.JobDatasource;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 用于传参，构建json
 *
 * @author jingwk
 * @ClassName DataxMongoDBPojo
 * @Version 2.0
 * @since 2020/03/14 11:15
 */
@Data
public class DataxMongoDBPojo {

    /**
     * hive列名
     */
    private List<Map<String,Object>> columns;

    /**
     * 数据源信息
     */
    private JobDatasource jdbcDatasource;

    private String address;

    private String dbName;

    private String collectionName;

    /**
     * 当设置为true时，表示针对相同的upsertKey做更新操作
     */
    private boolean isUpsert;
    /**
     *  upsertKey指定了没行记录的业务主键。用来做更新时使用。
     */
    private String upsertKey;
}

