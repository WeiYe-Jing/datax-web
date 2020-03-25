package com.wugui.datax.admin.entity;

import io.swagger.annotations.ApiModelProperty;
import org.springframework.util.StringUtils;

/**
 * @author xuxueli 2019-05-04 16:43:12
 */
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    // plugin
    public boolean validPermission(int jobGroup){
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
