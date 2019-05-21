package com.wugui.dataxweb.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;
import lombok.Data;

@Data
@TableName(value = "job_config")
public class JobConfig {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField(value = "config_json")
    private String configJson;

    @TableField(value = "description")
    private String description;

    @TableField(value = "name")
    private String name;

    @TableField(value = "label")
    private String label;


    @TableField(fill =  FieldFill.INSERT)     //mp自动填充
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;

    @TableField(value = "create_by")
    private Integer createBy;

    @TableField(value = "update_by")
    private Integer updateBy;

    @TableField(fill =  FieldFill.INSERT_UPDATE)  //mp自动填充
    private Date updateDate;

    @TableField(value = "status")
    private Integer status;

    @TableField(value = "user_id")
    private Integer userID;
}

