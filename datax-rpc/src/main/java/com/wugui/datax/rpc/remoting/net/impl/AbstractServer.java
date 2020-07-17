package com.wugui.datax.rpc.remoting.net.impl;

import com.wugui.datax.rpc.remoting.net.Server;
import com.wugui.datax.rpc.remoting.net.params.BaseCallback;
import com.wugui.datax.rpc.remoting.provider.XxlRpcProviderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author yangzhuangqiu
 * @date 2020/7/9 12:27
 */
public abstract class AbstractServer implements Server {

    protected static final Logger logger = LoggerFactory.getLogger(AbstractServer.class);

    private BaseCallback startedCallback;
    private BaseCallback stopedCallback;

    private Thread thread;

    private AtomicBoolean started = new AtomicBoolean(false);
    private AtomicBoolean stopped = new AtomicBoolean(true);

    @Override
    public void setStartedCallback(BaseCallback startedCallback) {
        this.startedCallback = startedCallback;
    }

    @Override
    public void setStopedCallback(BaseCallback stopedCallback) {
        this.stopedCallback = stopedCallback;
    }


    /**
     * start server
     *
     * @param xxlRpcProviderFactory
     * @throws Exception
     */
    @Override
    public void start(final XxlRpcProviderFactory xxlRpcProviderFactory) throws Exception {
        boolean success = started.compareAndSet(false, true);
        if (!success) {
            logger.info("Server is already start....");
            return;
        }

        thread = new Thread(() -> {
            startServer(xxlRpcProviderFactory);
        });
        thread.start();

        Runtime.getRuntime().addShutdownHook(
                new Thread(() -> {
                    try {
                        stop();
                    } catch (Exception e) {
                    }
                }));
        stopped.set(false);
    }

    protected abstract void startServer(XxlRpcProviderFactory xxlRpcProviderFactory);

    /**
     * callback when started
     */
    protected void onStarted() {
        if (startedCallback != null) {
            try {
                startedCallback.run();
            } catch (Exception e) {
                logger.error(">>>>>>>>>>> xxl-rpc, server startedCallback error.", e);
            }
        }
    }

    /**
     * stop server
     *
     * @throws Exception
     */
    @Override
    public void stop() throws Exception {
        boolean success = stopped.compareAndSet(false, true);
        if (!success) {
            logger.info("Server is already stop....");
            return;
        }
        // destroy server thread
        if (thread != null && thread.isAlive()) {
            thread.interrupt();
        }

        // on stop
        onStopped();
        logger.info(">>>>>>>>>>> xxl-rpc remoting server destroy success.");

        started.set(false);
    }

    /**
     * callback when stoped
     */
    protected void onStopped() {
        if (stopedCallback != null) {
            try {
                stopedCallback.run();
            } catch (Exception e) {
                logger.error(">>>>>>>>>>> xxl-rpc, server stopedCallback error.", e);
            }
        }
    }

}
