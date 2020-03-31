package com.wugui.datatx.core.biz.model;

import java.io.Serializable;

/**
 * Created by xuxueli on 2017-05-10 20:22:42
 */
public class RegistryParam implements Serializable {
    private static final long serialVersionUID = 42L;

    private String registryGroup;
    private String registryKey;
    private String registryValue;
    private double cpuUsage;
    private double memoryUsage;
    private double loadAverage;

    public RegistryParam() {
    }

    public RegistryParam(String registryGroup, String registryKey, String registryValue) {
        this.registryGroup = registryGroup;
        this.registryKey = registryKey;
        this.registryValue = registryValue;
    }

    public RegistryParam(String registryGroup, String registryKey, String registryValue, double cpuUsage, double memoryUsage, double loadAverage) {
        this.registryGroup = registryGroup;
        this.registryKey = registryKey;
        this.registryValue = registryValue;
        this.cpuUsage = cpuUsage;
        this.memoryUsage = memoryUsage;
        this.loadAverage = loadAverage;
    }

    public String getRegistryGroup() {
        return registryGroup;
    }

    public void setRegistryGroup(String registryGroup) {
        this.registryGroup = registryGroup;
    }

    public String getRegistryKey() {
        return registryKey;
    }

    public void setRegistryKey(String registryKey) {
        this.registryKey = registryKey;
    }

    public String getRegistryValue() {
        return registryValue;
    }

    public void setRegistryValue(String registryValue) {
        this.registryValue = registryValue;
    }

    public double getCpuUsage() {
        return cpuUsage;
    }

    public void setCpuUsage(double cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    public double getMemoryUsage() {
        return memoryUsage;
    }

    public void setMemoryUsage(double memoryUsage) {
        this.memoryUsage = memoryUsage;
    }

    public double getLoadAverage() {
        return loadAverage;
    }

    public void setLoadAverage(double loadAverage) {
        this.loadAverage = loadAverage;
    }

    @Override
    public String toString() {
        return "RegistryParam{" +
                "registryGroup='" + registryGroup + '\'' +
                ", registryKey='" + registryKey + '\'' +
                ", registryValue='" + registryValue + '\'' +
                ", cpuUsage='" + cpuUsage + '\'' +
                ", memoryUsage='" + memoryUsage + '\'' +
                ", loadAverage='" + loadAverage + '\'' +
                '}';
    }
}
