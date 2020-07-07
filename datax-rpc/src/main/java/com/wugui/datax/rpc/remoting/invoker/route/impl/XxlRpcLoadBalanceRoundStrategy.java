package com.wugui.datax.rpc.remoting.invoker.route.impl;

import com.wugui.datax.rpc.remoting.invoker.route.AbstractXxlRpcLoadBalance;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * round
 *
 * @author xuxueli 2018-12-04
 */
public class XxlRpcLoadBalanceRoundStrategy extends AbstractXxlRpcLoadBalance {

    private final ConcurrentMap<String, Integer> routeCountEachJob = new ConcurrentHashMap<>();
    private long cacheValidTime = 0;

    /**
     * @param serviceKey key
     * @return int
     */
    private Integer count(String serviceKey) {
        // cache clear
        if (System.currentTimeMillis() > cacheValidTime) {
            routeCountEachJob.clear();
            cacheValidTime = System.currentTimeMillis() + 24 * 60 * 60 * 1000;
        }
        Integer count = routeCountEachJob.get(serviceKey);
        // 初始化时主动Random一次，缓解首次压力
        count = (count == null || count > 1000000) ? (new Random().nextInt(100)) : ++count;
        routeCountEachJob.put(serviceKey, count);
        return count;
    }

    @Override
    public String route(String serviceKey, TreeSet<String> addressSet) {
        String[] addressArr = addressSet.toArray(new String[addressSet.size()]);
        String finalAddress = addressArr[count(serviceKey) % addressArr.length];
        return finalAddress;
    }

}
