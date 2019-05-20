package com.wugui.dataxweb.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

@Data
@TableName(value = "JOB_CONFIG")
public class JobConfig {
    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    @TableField(value = "CONFIG")
    private String config;

    @TableField(value = "CREATE_DATE")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;

    @TableField(value = "CREATE_BY")
    private Integer createBy;

    @TableField(value = "UPDATE_BY")
    private Integer updateBy;

    @TableField(value = "UPDATE_DATE")
    private Date updateDate;

    @TableField(value = "STATUS")
    private Integer status;

    @TableField(value = "USER_ID")
    private Integer userID;
}

