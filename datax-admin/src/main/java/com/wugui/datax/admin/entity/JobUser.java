package com.wugui.datax.admin.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
import org.springframework.util.StringUtils;

/**
 * @author xuxueli 2019-05-04 16:43:12
 */
@Data
public class JobUser {

    private int id;
    @ApiModelProperty("账号")
    private String username;
    @ApiModelProperty("密码")
    private String password;
    @ApiModelProperty("角色：0-普通用户、1-管理员")
    private String role;
    @ApiModelProperty("权限：执行器ID列表，多个逗号分割")
    private String permission;

    @ApiModelProperty("邮件地址")
    @Required
    private String email;

    @ApiModelProperty("手机号码")
    @Required
    private String phone;

    @ApiModelProperty("昵称")
    @Required
    private String nickname;


    // plugin
    public boolean validPermission(int jobGroup) {
        if ("1".equals(this.role)) {
            return true;
        } else {
            if (StringUtils.hasText(this.permission)) {
                for (String permissionItem : this.permission.split(",")) {
                    if (String.valueOf(jobGroup).equals(permissionItem)) {
                        return true;
                    }
                }
            }
            return false;
        }

    }

}
