package com.wugui.datax.admin.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 构建hive reader dto
 *
 * @author jingwk
 * @ClassName hive reader
 * @Version 2.0
 * @since 2020/01/11 17:15
 */
@Data
public class HiveReaderDto implements Serializable {

    private String readerPath;

    private String readerDefaultFS;

    private String readerFileType;

    private String readerFieldDelimiter;

    private Boolean haveKerberos;// 是否加kerberos 认证

    private String kerberosKeytabFilePath ; //keytab的位置

    private String kerberosPrincipal; // 用户认证

}
