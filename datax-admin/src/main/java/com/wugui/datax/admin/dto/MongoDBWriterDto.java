package com.wugui.datax.admin.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 构建mongodb write dto
 *
 * @author jingwk
 * @ClassName mongodb write dto
 * @Version 2.1.1
 * @since 2020/03/14 07:15
 */
@Data
public class MongoDBWriterDto implements Serializable {

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
