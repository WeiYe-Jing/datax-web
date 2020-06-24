package com.wugui.datax.registry;

import com.wugui.datax.registry.model.XxlRegistryDataParamVO;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

public class XxlRegistryClientTest {

    public static void main(String[] args) throws InterruptedException {
        XxlRegistryClient registryClient = new XxlRegistryClient("http://localhost:8080/xxl-registry-admin/", null, "xxl-rpc", "test");

        // registry test
        List<XxlRegistryDataParamVO> registryDataList = new ArrayList<>();
        registryDataList.add(new XxlRegistryDataParamVO("service01", "address01"));
        registryDataList.add(new XxlRegistryDataParamVO("service02", "address02"));
        System.out.println("registry:" + registryClient.registry(registryDataList));
        TimeUnit.SECONDS.sleep(2);

        // discovery test
        Set<String> keys = new TreeSet<>();
        keys.add("service01");
        keys.add("service02");
        System.out.println("discovery:" + registryClient.discovery(keys));

        while (true) {
            TimeUnit.SECONDS.sleep(1);
        }

    }

}
