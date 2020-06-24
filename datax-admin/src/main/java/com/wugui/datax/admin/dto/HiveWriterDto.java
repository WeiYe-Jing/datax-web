package com.wugui.datax.admin.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 构建hive write dto
 *
 * @author jingwk
 * @ClassName hive write dto
 * @Version 2.0
 * @since 2020/01/11 17:15
 */
@Data
public class HiveWriterDto implements Serializable {

    private String writerDefaultFS;

    private String writerFileType;

    private String writerPath;

    private String writerFileName;

    private String writeMode;

    private String writeFieldDelimiter;

    private Boolean haveKerberos;// 是否加kerberos 认证

    private String kerberosKeytabFilePath ; //keytab的位置

    private String kerberosPrincipal; // 用户认证
}
