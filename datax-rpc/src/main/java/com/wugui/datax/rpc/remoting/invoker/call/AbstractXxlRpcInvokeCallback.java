package com.wugui.datax.rpc.remoting.invoker.call;


/**
 * @author xuxueli 2018-10-23
 */
public abstract class AbstractXxlRpcInvokeCallback<T> {

    /**
     * onSuccess
     *
     * @param result
     */
    public abstract void onSuccess(T result);

    /**
     * onFailure
     *
     * @param exception
     */
    public abstract void onFailure(Throwable exception);


    // ---------------------- thread invoke callback ----------------------

    private static ThreadLocal<AbstractXxlRpcInvokeCallback> threadInvokerFuture = new ThreadLocal<AbstractXxlRpcInvokeCallback>();

    /**
     * get callback
     *
     * @return
     */
    public static AbstractXxlRpcInvokeCallback getCallback() {
        AbstractXxlRpcInvokeCallback invokeCallback = threadInvokerFuture.get();
        threadInvokerFuture.remove();
        return invokeCallback;
    }

    /**
     * set future
     *
     * @param invokeCallback
     */
    public static void setCallback(AbstractXxlRpcInvokeCallback invokeCallback) {
        threadInvokerFuture.set(invokeCallback);
    }

    /**
     * remove future
     */
    public static void removeCallback() {
        threadInvokerFuture.remove();
    }


}
