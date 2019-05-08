package com.wugui.dataxweb.entity;

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

