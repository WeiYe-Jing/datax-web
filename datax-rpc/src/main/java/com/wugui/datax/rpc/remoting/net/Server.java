package com.wugui.datax.rpc.remoting.net;

import com.wugui.datax.rpc.remoting.net.params.BaseCallback;
import com.wugui.datax.rpc.remoting.provider.XxlRpcProviderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * server
 *
 * @author xuxueli 2015-11-24 20:59:49
 */
public abstract class Server {
	protected static final Logger logger = LoggerFactory.getLogger(Server.class);


	private BaseCallback startedCallback;
	private BaseCallback stopedCallback;

	public void setStartedCallback(BaseCallback startedCallback) {
		this.startedCallback = startedCallback;
	}

	public void setStopedCallback(BaseCallback stopedCallback) {
		this.stopedCallback = stopedCallback;
	}


	/**
	 * start server
	 *
	 * @param xxlRpcProviderFactory
	 * @throws Exception
	 */
	public abstract void start(final XxlRpcProviderFactory xxlRpcProviderFactory) throws Exception;

	/**
	 * callback when started
	 */
	public void onStarted() {
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
	public abstract void stop() throws Exception;

	/**
	 * callback when stoped
	 */
	public void onStopped() {
		if (stopedCallback != null) {
			try {
				stopedCallback.run();
			} catch (Exception e) {
				logger.error(">>>>>>>>>>> xxl-rpc, server stopedCallback error.", e);
			}
		}
	}

}
