package com.wugui.datax.rpc.remoting.invoker.route.impl;

import com.wugui.datax.rpc.remoting.invoker.route.AbstractXxlRpcLoadBalance;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author xuxueli 2018-12-04
 */
public class XxlRpcLoadBalanceLRUStrategy extends AbstractXxlRpcLoadBalance {

    private final ConcurrentMap<String, LinkedHashMap<String, String>> jobLruMap = new ConcurrentHashMap<>();
    private long cacheValidTime = 0;

    public String doRoute(String serviceKey, TreeSet<String> addressSet) {

        if (System.currentTimeMillis() > cacheValidTime) {
            jobLruMap.clear();
            cacheValidTime = System.currentTimeMillis() + 1000 * 60 * 60 * 24;
        }
        // init lru
        LinkedHashMap<String, String> lruItem = jobLruMap.get(serviceKey);
        if (lruItem == null) {
            /**
             * LinkedHashMap
             *      a、accessOrder：ture=访问顺序排序（get/put时排序）/ACCESS-LAST；false=插入顺序排期/FIFO；
             *      b、removeEldestEntry：新增元素时将会调用，返回true时会删除最老元素；可封装LinkedHashMap并重写该方法，比如定义最大容量，超出是返回true即可实现固定长度的LRU算法；
             */
            lruItem = new LinkedHashMap<String, String>(16, 0.75f, true) {
                @Override
                protected boolean removeEldestEntry(Map.Entry<String, String> eldest) {
                    if (super.size() > 1000) {
                        return true;
                    } else {
                        return false;
                    }
                }
            };
            jobLruMap.putIfAbsent(serviceKey, lruItem);
        }

        // put new
        for (String address : addressSet) {
            if (!lruItem.containsKey(address)) {
                lruItem.put(address, address);
            }
        }
        // remove old
        List<String> delKeys = new ArrayList<>();
        for (String existKey : lruItem.keySet()) {
            if (!addressSet.contains(existKey)) {
                delKeys.add(existKey);
            }
        }
        if (delKeys.isEmpty()) {
            for (String delKey : delKeys) {
                lruItem.remove(delKey);
            }
        }
        // load
        String eldestKey = lruItem.entrySet().iterator().next().getKey();
        return lruItem.get(eldestKey);
    }

    @Override
    public String route(String serviceKey, TreeSet<String> addressSet) {
        return doRoute(serviceKey, addressSet);
    }

}
