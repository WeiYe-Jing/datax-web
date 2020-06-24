package com.wugui.datax.registry;

import com.wugui.datax.registry.model.XxlRegistryDataParamVO;
import com.wugui.datax.registry.util.json.BasicJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

/**
 * registry client, auto heatbeat registry info, auto monitor discovery info
 *
 * @author xuxueli 2018-12-01 21:48:05
 */
public class XxlRegistryClient {
    private static Logger logger = LoggerFactory.getLogger(XxlRegistryClient.class);


    private volatile Set<XxlRegistryDataParamVO> registryData = new HashSet<>();
    private volatile ConcurrentMap<String, TreeSet<String>> discoveryData = new ConcurrentHashMap<>();

    private Thread registryThread;
    private Thread discoveryThread;
    private volatile boolean registryThreadStop = false;


    private XxlRegistryBaseClient registryBaseClient;

    public XxlRegistryClient(String adminAddress, String accessToken, String biz, String env) {
        registryBaseClient = new XxlRegistryBaseClient(adminAddress, accessToken, biz, env);
        logger.info(">>>>>>>>>>> xxl-registry, XxlRegistryClient init .... [adminAddress={}, accessToken={}, biz={}, env={}]", adminAddress, accessToken, biz, env);

        // registry thread
        registryThread = new Thread(() -> {
            while (!registryThreadStop) {
                try {
                    if (registryData.size() > 0) {

                        boolean ret = registryBaseClient.registry(new ArrayList<>(registryData));
                        logger.debug(">>>>>>>>>>> xxl-registry, refresh registry data {}, registryData = {}", ret ? "success" : "fail", registryData);
                    }
                } catch (Exception e) {
                    if (!registryThreadStop) {
                        logger.error(">>>>>>>>>>> xxl-registry, registryThread error.", e);
                    }
                }
                try {
                    TimeUnit.SECONDS.sleep(10);
                } catch (Exception e) {
                    if (!registryThreadStop) {
                        logger.error(">>>>>>>>>>> xxl-registry, registryThread error.", e);
                    }
                }
            }
            logger.info(">>>>>>>>>>> xxl-registry, registryThread stoped.");
        });
        registryThread.setName("xxl-registry, XxlRegistryClient registryThread.");
        registryThread.setDaemon(true);
        registryThread.start();

        // discovery thread
        discoveryThread = new Thread(() -> {
            while (!registryThreadStop) {

                if (discoveryData.size() == 0) {
                    try {
                        TimeUnit.SECONDS.sleep(3);
                    } catch (Exception e) {
                        if (!registryThreadStop) {
                            logger.error(">>>>>>>>>>> xxl-registry, discoveryThread error.", e);
                        }
                    }
                } else {
                    try {
                        // monitor
                        boolean monitorRet = registryBaseClient.monitor(discoveryData.keySet());

                        // avoid fail-retry request too quick
                        if (!monitorRet) {
                            TimeUnit.SECONDS.sleep(10);
                        }

                        // refreshDiscoveryData, all
                        refreshDiscoveryData(discoveryData.keySet());
                    } catch (Exception e) {
                        if (!registryThreadStop) {
                            logger.error(">>>>>>>>>>> xxl-registry, discoveryThread error.", e);
                        }
                    }
                }

            }
            logger.info(">>>>>>>>>>> xxl-registry, discoveryThread stoped.");
        });
        discoveryThread.setName("xxl-registry, XxlRegistryClient discoveryThread.");
        discoveryThread.setDaemon(true);
        discoveryThread.start();

        logger.info(">>>>>>>>>>> xxl-registry, XxlRegistryClient init success.");
    }


    public void stop() {
        registryThreadStop = true;
        if (registryThread != null) {
            registryThread.interrupt();
        }
        if (discoveryThread != null) {
            discoveryThread.interrupt();
        }
    }


    /**
     * registry
     *
     * @param registryDataList
     * @return
     */
    public boolean registry(List<XxlRegistryDataParamVO> registryDataList) {

        // valid
        if (registryDataList == null || registryDataList.size() == 0) {
            throw new RuntimeException("xxl-registry registryDataList empty");
        }
        for (XxlRegistryDataParamVO registryParam : registryDataList) {
            if (registryParam.getKey() == null || registryParam.getKey().trim().length() < 4 || registryParam.getKey().trim().length() > 255) {
                throw new RuntimeException("xxl-registry registryDataList#key Invalid[4~255]");
            }
            if (registryParam.getValue() == null || registryParam.getValue().trim().length() < 4 || registryParam.getValue().trim().length() > 255) {
                throw new RuntimeException("xxl-registry registryDataList#value Invalid[4~255]");
            }
        }

        // cache
        registryData.addAll(registryDataList);

        // remote
        registryBaseClient.registry(registryDataList);

        return true;
    }


    /**
     * remove
     *
     * @param registryDataList
     * @return
     */
    public boolean remove(List<XxlRegistryDataParamVO> registryDataList) {
        // valid
        if (registryDataList == null || registryDataList.size() == 0) {
            throw new RuntimeException("xxl-registry registryDataList empty");
        }
        for (XxlRegistryDataParamVO registryParam : registryDataList) {
            if (registryParam.getKey() == null || registryParam.getKey().trim().length() < 4 || registryParam.getKey().trim().length() > 255) {
                throw new RuntimeException("xxl-registry registryDataList#key Invalid[4~255]");
            }
            if (registryParam.getValue() == null || registryParam.getValue().trim().length() < 4 || registryParam.getValue().trim().length() > 255) {
                throw new RuntimeException("xxl-registry registryDataList#value Invalid[4~255]");
            }
        }

        // cache
        registryData.removeAll(registryDataList);

        // remote
        registryBaseClient.remove(registryDataList);

        return true;
    }


    /**
     * discovery
     *
     * @param keys
     * @return
     */
    public Map<String, TreeSet<String>> discovery(Set<String> keys) {
        if (keys == null || keys.size() == 0) {
            return null;
        }

        // find from local
        Map<String, TreeSet<String>> registryDataTmp = new HashMap<String, TreeSet<String>>();
        for (String key : keys) {
            TreeSet<String> valueSet = discoveryData.get(key);
            if (valueSet != null) {
                registryDataTmp.put(key, valueSet);
            }
        }

        // not find all, find from remote
        if (keys.size() != registryDataTmp.size()) {

            // refreshDiscoveryData, some, first use
            refreshDiscoveryData(keys);

            // find from local
            for (String key : keys) {
                TreeSet<String> valueSet = discoveryData.get(key);
                if (valueSet != null) {
                    registryDataTmp.put(key, valueSet);
                }
            }

        }

        return registryDataTmp;
    }

    /**
     * refreshDiscoveryData, some or all
     */
    private void refreshDiscoveryData(Set<String> keys) {
        if (keys == null || keys.size() == 0) {
            return;
        }

        // discovery mult
        Map<String, TreeSet<String>> updatedData = new HashMap<>();

        Map<String, TreeSet<String>> keyValueListData = registryBaseClient.discovery(keys);
        if (keyValueListData != null) {
            for (String keyItem : keyValueListData.keySet()) {

                // list > set
                TreeSet<String> valueSet = new TreeSet<>();
                valueSet.addAll(keyValueListData.get(keyItem));

                // valid if updated
                boolean updated = true;
                TreeSet<String> oldValSet = discoveryData.get(keyItem);
                if (oldValSet != null && BasicJson.toJson(oldValSet).equals(BasicJson.toJson(valueSet))) {
                    updated = false;
                }

                // set
                if (updated) {
                    discoveryData.put(keyItem, valueSet);
                    updatedData.put(keyItem, valueSet);
                }

            }
        }

        if (updatedData.size() > 0) {
            logger.info(">>>>>>>>>>> xxl-registry, refresh discovery data finish, discoveryData(updated) = {}", updatedData);
        }
        logger.debug(">>>>>>>>>>> xxl-registry, refresh discovery data finish, discoveryData = {}", discoveryData);
    }


    public TreeSet<String> discovery(String key) {
        if (key == null) {
            return null;
        }

        Map<String, TreeSet<String>> keyValueSetTmp = discovery(new HashSet<>(Arrays.asList(key)));
        if (keyValueSetTmp != null) {
            return keyValueSetTmp.get(key);
        }
        return null;
    }


}
