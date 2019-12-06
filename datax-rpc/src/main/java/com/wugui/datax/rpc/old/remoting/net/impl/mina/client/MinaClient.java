package com.wugui.datax.rpc.old.remoting.net.impl.mina.client;//package com.xxl.rpc.remoting.net.impl.mina.client;
//
//import com.xxl.rpc.remoting.net.Client;
//import com.xxl.rpc.remoting.net.params.XxlRpcRequest;
//import com.xxl.rpc.remoting.net.common.ConnectClient;
//
///**
// * mina client
// *
// * @author xuxueli 2015-11-24 22:25:15
// */
//public class MinaClient extends Client {
//
//	private Class<? extends ConnectClient> connectClientImpl = MinaConnectClient.class;
//
//	@Override
//	public void asyncSend(String address, XxlRpcRequest xxlRpcRequest) throws Exception {
//		ConnectClient.asyncSend(xxlRpcRequest, address, connectClientImpl, xxlRpcReferenceBean);
//	}
//
//}
