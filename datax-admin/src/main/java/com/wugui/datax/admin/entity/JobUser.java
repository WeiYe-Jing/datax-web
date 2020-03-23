package com.wugui.datax.admin.entity;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author xuxueli 2019-05-04 16:43:12
 */
public class JobUser {

    private int id;
    @ApiModelProperty("账号")
    private String username;
    @ApiModelProperty("密码")
    private String password;

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


}
