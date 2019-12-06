package com.wugui.datax.rpc.remoting.invoker.reference.impl;

import com.wugui.datax.rpc.remoting.invoker.reference.XxlRpcReferenceBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * rpc reference bean, use by spring xml and annotation (for spring)
 *
 * @author xuxueli 2015-10-29 20:18:32
 */
public class XxlRpcSpringReferenceBean implements FactoryBean<Object>, InitializingBean {


    // ---------------------- util ----------------------

    private XxlRpcReferenceBean xxlRpcReferenceBean;

    @Override
    public void afterPropertiesSet() {

        // init config
        this.xxlRpcReferenceBean = new XxlRpcReferenceBean();
    }


    @Override
    public Object getObject() throws Exception {
        return xxlRpcReferenceBean.getObject();
    }

    @Override
    public Class<?> getObjectType() {
        return xxlRpcReferenceBean.getIface();
    }

    @Override
    public boolean isSingleton() {
        return false;
    }


    /**
     *	public static <T> ClientProxy ClientProxy<T> getFuture(Class<T> type) {
     *		<T> ClientProxy proxy = (<T>) new ClientProxy();
     *		return proxy;
     *	}
     */


}
