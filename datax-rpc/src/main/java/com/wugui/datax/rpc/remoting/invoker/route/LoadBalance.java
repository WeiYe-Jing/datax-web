package com.wugui.datax.rpc.remoting.invoker.route;

import com.wugui.datax.rpc.remoting.invoker.route.impl.*;

/**
 * @author xuxueli 2018-12-04
 */
public enum LoadBalance {

    RANDOM(new XxlRpcLoadBalanceRandomStrategy()),
    ROUND(new XxlRpcLoadBalanceRoundStrategy()),
    LRU(new XxlRpcLoadBalanceLRUStrategy()),
    LFU(new XxlRpcLoadBalanceLFUStrategy()),
    CONSISTENT_HASH(new XxlRpcLoadBalanceConsistentHashStrategy());


    public final XxlRpcLoadBalance xxlRpcInvokerRouter;

    private LoadBalance(XxlRpcLoadBalance xxlRpcInvokerRouter) {
        this.xxlRpcInvokerRouter = xxlRpcInvokerRouter;
    }


    public static LoadBalance match(String name, LoadBalance defaultRouter) {
        for (LoadBalance item : LoadBalance.values()) {
            if (item.equals(name)) {
                return item;
            }
        }
        return defaultRouter;
    }



    /*public static void main(String[] args) {
        String serviceKey = "service";
        TreeSet<String> addressSet = new TreeSet<String>(){{
            add("1");
            add("2");
            add("3");
            add("4");
            add("5");
        }};

        for (LoadBalance item : LoadBalance.values()) {
            long start = System.currentTimeMillis();
            for (int i = 0; i < 100000; i++) {
                String address = LoadBalance.LFU.xxlRpcInvokerRouter.route(serviceKey, addressSet);
                //System.out.println(address);;
            }
            long end = System.currentTimeMillis();
            System.out.println(item.name() + " --- " + (end-start));
        }

    }*/


}