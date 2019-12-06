package com.wugui.datax.registry.model;

import java.util.List;

/**
 * @author xuxueli 2018-12-03
 */
public class XxlRegistryParamVO {


    private String accessToken;
    private String biz;
    private String env;


    private List<XxlRegistryDataParamVO> registryDataList;
    private List<String> keys;

    public String getBiz() {
        return biz;
    }

    public void setBiz(String biz) {
        this.biz = biz;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public List<XxlRegistryDataParamVO> getRegistryDataList() {
        return registryDataList;
    }

    public void setRegistryDataList(List<XxlRegistryDataParamVO> registryDataList) {
        this.registryDataList = registryDataList;
    }

    public List<String> getKeys() {
        return keys;
    }

    public void setKeys(List<String> keys) {
        this.keys = keys;
    }
}
