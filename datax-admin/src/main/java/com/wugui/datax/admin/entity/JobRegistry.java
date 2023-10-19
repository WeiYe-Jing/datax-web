package com.wugui.datax.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * Created by xuxueli on 16/9/30.
 */
@KeySequence(value = "SEQ_ID")
@Data
public class JobRegistry {
    @TableId(type = IdType.INPUT)
    private int id;
    private String registryGroup;
    private String registryKey;
    private String registryValue;
    private double cpuUsage;
    private double memoryUsage;
    private double loadAverage;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
}
