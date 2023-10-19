package com.wugui.datax.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by jingwk on 2019/11/17
 */
@KeySequence(value = "SEQ_ID")
public class JobGroup {

    @TableId(type = IdType.INPUT)
    @ApiModelProperty("执行器Id")
    private Integer id;
    @ApiModelProperty("执行器AppName")
    private String appName;
    @ApiModelProperty("执行器名称")
    private String title;
    @ApiModelProperty("排序")
    private int orderSn;
    @ApiModelProperty("执行器地址类型：0=自动注册、1=手动录入")
    private int addressType;
    @ApiModelProperty("执行器地址列表，多地址逗号分隔(手动录入)")
    private String addressList;

    // registry list
    @TableField(exist = false)
    private List<String> registryList;  // 执行器地址列表(系统注册)
    public List<String> getRegistryList() {
        if (addressList!=null && addressList.trim().length()>0) {
            registryList = new ArrayList<>(Arrays.asList(addressList.split(",")));
        }
        return registryList;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(int orderSn) {
        this.orderSn = orderSn;
    }

    public int getAddressType() {
        return addressType;
    }

    public void setAddressType(int addressType) {
        this.addressType = addressType;
    }

    public String getAddressList() {
        return addressList;
    }

    public void setAddressList(String addressList) {
        this.addressList = addressList;
    }

}
