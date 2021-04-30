package com.wugui.datax.admin.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class JobInfoDto implements Serializable {

    private String id;

    private String type = "task";

    private String name;

    private String ico = "el-icon-user-solid";
}
