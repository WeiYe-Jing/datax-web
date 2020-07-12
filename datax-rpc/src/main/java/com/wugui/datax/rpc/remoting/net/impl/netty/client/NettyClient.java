package com.wugui.datax.rpc.remoting.net.impl.netty.client;

import com.wugui.datax.rpc.remoting.net.AbstractClient;
import com.wugui.datax.rpc.remoting.net.common.AbstractConnectClient;
import com.wugui.datax.rpc.remoting.net.params.XxlRpcRequest;

/**
 * netty client
 *
 * @author xuxueli 2015-11-24 22:25:15
 */
public class NettyClient extends AbstractClient {

	private Class<? extends AbstractConnectClient> connectClientImpl = NettyConnectClient.class;

	@Override
	public void asyncSend(String address, XxlRpcRequest xxlRpcRequest) throws Exception {
		AbstractConnectClient.asyncSend(xxlRpcRequest, address, connectClientImpl, xxlRpcReferenceBean);
	}

}
