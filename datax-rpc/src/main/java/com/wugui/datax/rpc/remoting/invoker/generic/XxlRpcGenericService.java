package com.wugui.datax.rpc.remoting.invoker.generic;

/**
 * @author xuxueli 2018-12-04
 */
public interface XxlRpcGenericService {

    /**
     * generic invoke
     *
     * @param iface          iface name
     * @param version        iface version
     * @param method         method name
     * @param parameterTypes parameter types, limit base type like "int、java.lang.Integer、java.util.List、java.util.Map ..."
     * @param args
     * @return
     */
    Object invoke(String iface, String version, String method, String[] parameterTypes, Object[] args);

}