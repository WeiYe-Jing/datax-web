package com.wugui.datax.rpc.remoting.invoker.route.impl;

import com.wugui.datax.rpc.remoting.invoker.route.AbstractXxlRpcLoadBalance;

import java.util.Random;
import java.util.TreeSet;

/**
 * random
 *
 * @author xuxueli 2018-12-04
 */
public class XxlRpcLoadBalanceRandomStrategy extends AbstractXxlRpcLoadBalance {

    private Random random = new Random();

    @Override
    public String route(String serviceKey, TreeSet<String> addressSet) {
        // arr
        String[] addressArr = addressSet.toArray(new String[addressSet.size()]);

        // random
        String finalAddress = addressArr[random.nextInt(addressSet.size())];
        return finalAddress;
    }

}
