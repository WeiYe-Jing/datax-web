package com.wugui.datax.rpc.remoting.invoker.call;

import com.wugui.datax.rpc.remoting.net.params.XxlRpcFutureResponse;
import com.wugui.datax.rpc.remoting.net.params.XxlRpcResponse;
import com.wugui.datax.rpc.util.XxlRpcException;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author xuxueli 2018-10-22 18:31:42
 */
public class XxlRpcInvokeFuture implements Future {


    private XxlRpcFutureResponse futureResponse;

    public XxlRpcInvokeFuture(XxlRpcFutureResponse futureResponse) {
        this.futureResponse = futureResponse;
    }

    public void stop() {
        // remove-InvokerFuture
        futureResponse.removeInvokerFuture();
    }


    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return futureResponse.cancel(mayInterruptIfRunning);
    }

    @Override
    public boolean isCancelled() {
        return futureResponse.isCancelled();
    }

    @Override
    public boolean isDone() {
        return futureResponse.isDone();
    }

    @Override
    public Object get() throws ExecutionException, InterruptedException {
        try {
            return get(-1, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            throw new XxlRpcException(e);
        }
    }

    @Override
    public Object get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        try {
            // future get
            XxlRpcResponse xxlRpcResponse = futureResponse.get(timeout, unit);
            if (xxlRpcResponse.getErrorMsg() != null) {
                throw new XxlRpcException(xxlRpcResponse.getErrorMsg());
            }
            return xxlRpcResponse.getResult();
        } finally {
            stop();
        }
    }


    // ---------------------- thread invoke future ----------------------

    private static ThreadLocal<XxlRpcInvokeFuture> threadInvokerFuture = new ThreadLocal<XxlRpcInvokeFuture>();

    /**
     * get future
     *
     * @param type
     * @param <T>
     * @return
     */
    public static <T> Future<T> getFuture(Class<T> type) {
        Future<T> future = (Future<T>) threadInvokerFuture.get();
        threadInvokerFuture.remove();
        return future;
    }

    /**
     * set future
     *
     * @param future
     */
    public static void setFuture(XxlRpcInvokeFuture future) {
        threadInvokerFuture.set(future);
    }

    /**
     * remove future
     */
    public static void removeFuture() {
        threadInvokerFuture.remove();
    }

}

