package com.wugui.datax.admin.entity;

import lombok.Data;

/**
 * @author  jingwk on 2019/11/17
 */
@Data
public class LoginUser {

    private String username;
    private String password;
    private Integer rememberMe;

}
