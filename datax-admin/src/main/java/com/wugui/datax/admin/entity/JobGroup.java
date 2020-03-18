package com.wugui.datax.admin.entity;

import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by jingwk on 2019/11/17
 */
public class JobGroup {

    @ApiModelProperty("执行器Id")
    private int id;
    @ApiModelProperty("执行器AppName")
    private String appName;
    @ApiModelProperty("执行器名称")
    private String title;
    @ApiModelProperty("排序")
    private int order;
    @ApiModelProperty("执行器地址类型：0=自动注册、1=手动录入")
    private int addressType;
    @ApiModelProperty("执行器地址列表，多地址逗号分隔(手动录入)")
    private String addressList;

    // registry list
    private List<String> registryList;  // 执行器地址列表(系统注册)
    public List<String> getRegistryList() {
        if (addressList!=null && addressList.trim().length()>0) {
            registryList = new ArrayList<>(Arrays.asList(addressList.split(",")));
        }
        return registryList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
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
