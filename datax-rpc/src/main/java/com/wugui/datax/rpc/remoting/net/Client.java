package com.wugui.datax.rpc.remoting.net;

import com.wugui.datax.rpc.remoting.invoker.reference.XxlRpcReferenceBean;
import com.wugui.datax.rpc.remoting.net.params.XxlRpcRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * i client
 * @author xuxueli 2015-11-24 22:18:10
 */
public abstract class Client {
	protected static final Logger logger = LoggerFactory.getLogger(Client.class);


	// ---------------------- init ----------------------

	protected volatile XxlRpcReferenceBean xxlRpcReferenceBean;

	public void init(XxlRpcReferenceBean xxlRpcReferenceBean) {
		this.xxlRpcReferenceBean = xxlRpcReferenceBean;
	}


    // ---------------------- send ----------------------

	/**
	 * async send, bind requestId and future-response
	 *
	 * @param address
	 * @param xxlRpcRequest
	 * @return
	 * @throws Exception
	 */
	public abstract void asyncSend(String address, XxlRpcRequest xxlRpcRequest) throws Exception;

}
