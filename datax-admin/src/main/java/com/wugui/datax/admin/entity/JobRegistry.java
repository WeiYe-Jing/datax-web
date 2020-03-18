package com.wugui.datax.admin.entity;

import lombok.Data;

import java.util.Date;

/**
 * Created by xuxueli on 16/9/30.
 */
@Data
public class JobRegistry {

    private int id;
    private String registryGroup;
    private String registryKey;
    private String registryValue;
    private double cpuUsage;
    private double memoryUsage;
    private double loadAverage;
    private Date updateTime;
}
