package com.wugui.datax.admin.entity;/**
 * ClassName: JobDatasourceEntity
 * Description:
 * date: 2020/6/17 17:13
 *
 * @author 吴迪
 * @Version 1.0
 */

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * ClassName: JobDatasourceEntity 
 * Description: 
 * date: 2020/6/17 17:13
 * @author 吴迪
 * @Version 1.0
 */
@Data
@ApiModel
public class JobDatasourceEntity  extends JobDatasource{
    //格式 XXX@XXX.XXX
    @ApiModelProperty(value = "kerberos认证的用户", hidden = true)
    private String user;

    @ApiModelProperty(value = "krb5.ini 文件路径", hidden = true)
    private String iniPath;

    @ApiModelProperty(value = "keytab存放位置", hidden = true)
    private String keytabPath;

    @ApiModelProperty(value = "是否是kerberos认证", hidden = true)
    private Boolean isKerberos = false;

    @ApiModelProperty(value = "用户名字", hidden = true)
    private String nameStr;

}
