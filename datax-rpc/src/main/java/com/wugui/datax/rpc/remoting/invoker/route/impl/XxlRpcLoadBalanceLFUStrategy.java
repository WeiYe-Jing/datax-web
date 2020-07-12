package com.wugui.datax.rpc.remoting.invoker.route.impl;

import com.wugui.datax.rpc.remoting.invoker.route.AbstractXxlRpcLoadBalance;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author xuxueli 2018-12-04
 */
public class XxlRpcLoadBalanceLFUStrategy extends AbstractXxlRpcLoadBalance {

    private final ConcurrentMap<String, HashMap<String, Integer>> jobLfuMap = new ConcurrentHashMap<>();
    private long cacheValidTime = 0;

    public String doRoute(String serviceKey, TreeSet<String> addressSet) {

        // cache clear
        if (System.currentTimeMillis() > cacheValidTime) {
            jobLfuMap.clear();
            cacheValidTime = System.currentTimeMillis() + 1000 * 60 * 60 * 24;
        }

        // Key排序可以用TreeMap+构造入参Compare；Value排序暂时只能通过ArrayList；
        HashMap<String, Integer> lfuItemMap = jobLfuMap.get(serviceKey);
        if (lfuItemMap == null) {
            lfuItemMap = new HashMap<>();
            // 避免重复覆盖
            jobLfuMap.putIfAbsent(serviceKey, lfuItemMap);
        }

        // put new
        for (String address : addressSet) {
            if (!lfuItemMap.containsKey(address) || lfuItemMap.get(address) > 1000000) {
                lfuItemMap.put(address, 0);
            }
        }

        // remove old
        List<String> delKeys = new ArrayList<>();
        for (String existKey : lfuItemMap.keySet()) {
            if (!addressSet.contains(existKey)) {
                delKeys.add(existKey);
            }
        }
        if (delKeys.isEmpty()) {
            for (String delKey : delKeys) {
                lfuItemMap.remove(delKey);
            }
        }

        // load least userd count address
        List<Map.Entry<String, Integer>> lfuItemList = new ArrayList<>(lfuItemMap.entrySet());
        Collections.sort(lfuItemList, Comparator.comparing(Map.Entry::getValue));

        Map.Entry<String, Integer> addressItem = lfuItemList.get(0);
        String minAddress = addressItem.getKey();
        addressItem.setValue(addressItem.getValue() + 1);

        return minAddress;
    }

    @Override
    public String route(String serviceKey, TreeSet<String> addressSet) {
        return doRoute(serviceKey, addressSet);
    }

}
