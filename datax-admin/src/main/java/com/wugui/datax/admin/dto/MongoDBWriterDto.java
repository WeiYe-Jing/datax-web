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

    private UpsertInfo upsertInfo;

}
