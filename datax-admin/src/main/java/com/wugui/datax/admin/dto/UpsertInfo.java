package com.wugui.datax.admin.dto;

import lombok.Data;

/**
 * Created by mac on 2020/3/16.
 */
@Data
public class UpsertInfo {
    /**
     * 当设置为true时，表示针对相同的upsertKey做更新操作
     */
    private Boolean isUpsert;
    /**
     * upsertKey指定了没行记录的业务主键。用来做更新时使用。
     */
    private String upsertKey;
}
