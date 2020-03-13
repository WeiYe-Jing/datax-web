package com.wugui.datax.rpc.remoting.net.params;

import com.wugui.datax.rpc.remoting.invoker.XxlRpcInvokerFactory;
import com.wugui.datax.rpc.remoting.invoker.call.XxlRpcInvokeCallback;
import com.wugui.datax.rpc.util.XxlRpcException;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * call back future
 *
 * @author xuxueli 2015-11-5 14:26:37
 */
public class XxlRpcFutureResponse implements Future<XxlRpcResponse> {

    private XxlRpcInvokerFactory invokerFactory;

    // net data
    private XxlRpcRequest request;
    private XxlRpcResponse response;

    // future lock
    private boolean done = false;
    private Object lock = new Object();

    // callback, can be null
    private XxlRpcInvokeCallback invokeCallback;


    public XxlRpcFutureResponse(final XxlRpcInvokerFactory invokerFactory, XxlRpcRequest request, XxlRpcInvokeCallback invokeCallback) {
        this.invokerFactory = invokerFactory;
        this.request = request;
        this.invokeCallback = invokeCallback;

        // set-InvokerFuture
        setInvokerFuture();
    }


    // ---------------------- response pool ----------------------

    public void setInvokerFuture() {
        this.invokerFactory.setInvokerFuture(request.getRequestId(), this);
    }

    public void removeInvokerFuture() {
        this.invokerFactory.removeInvokerFuture(request.getRequestId());
    }


    // ---------------------- get ----------------------

    public XxlRpcRequest getRequest() {
        return request;
    }

    public XxlRpcInvokeCallback getInvokeCallback() {
        return invokeCallback;
    }


    // ---------------------- for invoke back ----------------------

    public void setResponse(XxlRpcResponse response) {
        this.response = response;
        synchronized (lock) {
            done = true;
            lock.notifyAll();
        }
    }


    // ---------------------- for invoke ----------------------

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        // TODO
        return false;
    }

    @Override
    public boolean isCancelled() {
        // TODO
        return false;
    }

    @Override
    public boolean isDone() {
        return done;
    }

    @Override
    public XxlRpcResponse get() throws InterruptedException {
        try {
            return get(-1, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            throw new XxlRpcException(e);
        }
    }

    @Override
    public XxlRpcResponse get(long timeout, TimeUnit unit) throws InterruptedException, TimeoutException {
        if (!done) {
            synchronized (lock) {
                try {
                    if (timeout < 0) {
                        lock.wait();
                    } else {
                        long timeoutMillis = (TimeUnit.MILLISECONDS == unit) ? timeout : TimeUnit.MILLISECONDS.convert(timeout, unit);
                        lock.wait(timeoutMillis);
                    }
                } catch (InterruptedException e) {
                    throw e;
                }
            }
        }

        if (!done) {
            throw new XxlRpcException("xxl-rpc, request timeout at:" + System.currentTimeMillis() + ", request:" + request.toString());
        }
        return response;
    }


}
