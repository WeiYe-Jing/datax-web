package com.wugui.datax.rpc.old.remoting.net.impl.mina.client;//package com.xxl.rpc.remoting.net.impl.mina.client;
//
//import com.xxl.rpc.remoting.invoker.XxlRpcInvokerFactory;
//import com.xxl.rpc.remoting.net.common.ConnectClient;
//import com.xxl.rpc.remoting.net.impl.mina.codec.MinaDecoder;
//import com.xxl.rpc.remoting.net.impl.mina.codec.MinaEncoder;
//import com.xxl.rpc.remoting.net.params.XxlRpcRequest;
//import com.xxl.rpc.remoting.net.params.XxlRpcResponse;
//import com.xxl.rpc.serialize.Serializer;
//import com.xxl.rpc.util.IpUtil;
//import org.apache.mina.core.future.ConnectFuture;
//import org.apache.mina.core.session.IdleStatus;
//import org.apache.mina.core.session.IoSession;
//import org.apache.mina.filter.codec.ProtocolCodecFactory;
//import org.apache.mina.filter.codec.ProtocolCodecFilter;
//import org.apache.mina.filter.codec.ProtocolDecoder;
//import org.apache.mina.filter.codec.ProtocolEncoder;
//import org.apache.mina.transport.socket.SocketSessionConfig;
//import org.apache.mina.transport.socket.nio.NioSocketConnector;
//
//import java.net.InetSocketAddress;
//import java.util.concurrent.TimeUnit;
//
///**
// * mina pooled client
// *
// * @author xuxueli
// */
//public class MinaConnectClient extends ConnectClient {
//
//
//	private NioSocketConnector connector;
//	private IoSession ioSession;
//
//
//	@Override
//	public void init(String address, final Serializer serializer, final XxlRpcInvokerFactory xxlRpcInvokerFactory) {
//
//		// host port
//		Object[] array = IpUtil.parseIpPort(address);
//		String host = (String) array[0];
//		int port = (int) array[1];
//
//
//		/*KeepAliveFilter heartBeat = new KeepAliveFilter(new KeepAliveMessageFactory() {
//			@Override
//			public boolean isRequest(IoSession ioSession, Object message) {
//				return Beat.BEAT_ID.equalsIgnoreCase(((XxlRpcResponse) message).getRequestId());	// beat request
//			}
//			@Override
//			public boolean isResponse(IoSession ioSession, Object message) {
//				return Beat.BEAT_ID.equalsIgnoreCase(((XxlRpcRequest) message).getRequestId());		// beat response
//			}
//			@Override
//			public Object getRequest(IoSession ioSession) {
//				return Beat.BEAT_PONG;
//			}
//			@Override
//			public Object getResponse(IoSession ioSession, Object request) {
//				return Beat.BEAT_PING;
//			}
//		}, IdleStatus.BOTH_IDLE, KeepAliveRequestTimeoutHandler.CLOSE);
//		heartBeat.setForwardEvent(true);
//		heartBeat.setRequestInterval(10);
//		heartBeat.setRequestTimeout(10);*/	// TODO，mina beat
//
//		// init
//		connector = new NioSocketConnector();
//		//connector.getFilterChain().addLast("heartbeat", heartBeat);
//		connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new ProtocolCodecFactory() {
//			@Override
//			public ProtocolEncoder getEncoder(IoSession session) throws Exception {
//				return new MinaEncoder(XxlRpcRequest.class, serializer);
//			}
//			@Override
//			public ProtocolDecoder getDecoder(IoSession session) throws Exception {
//				return new MinaDecoder(XxlRpcResponse.class, serializer);
//			}
//		}));
//		connector.setHandler(new MinaClientHandler(xxlRpcInvokerFactory));
//		connector.setConnectTimeoutMillis(10000);
//
//		SocketSessionConfig socketSessionConfig = (SocketSessionConfig) connector.getSessionConfig();
//		socketSessionConfig.setTcpNoDelay(true);
//		socketSessionConfig.setKeepAlive(true);
//		//socketSessionConfig.setReuseAddress(true);
//		socketSessionConfig.setSoLinger(-1);
//		socketSessionConfig.setIdleTime(IdleStatus.BOTH_IDLE, 60);
//
//		ConnectFuture future = connector.connect(new InetSocketAddress(host, port));
//		future.awaitUninterruptibly(5, TimeUnit.SECONDS);
//		this.ioSession = future.getSession();
//
//		// valid
//		if (!isValidate()) {
//			close();
//			return;
//		}
//
//		logger.debug(">>>>>>>>>>> xxl-rpc mina client proxy, connect to server success at host:{}, port:{}", host, port);
//	}
//
//
//	@Override
//	public boolean isValidate() {
//		if (this.connector != null && this.ioSession != null) {
//			return this.connector.isActive() && this.ioSession.isConnected();
//		}
//		return false;
//	}
//
//
//	@Override
//	public void close() {
//		if (this.ioSession!=null && this.ioSession.isConnected()) {
//			//this.ioSession.getCloseFuture().awaitUninterruptibly();
//			this.ioSession.closeOnFlush();
//		}
//		if (this.connector!=null && this.connector.isActive()) {
//			this.connector.dispose();
//		}
//		logger.debug(">>>>>>>>>>> xxl-rpc mina client close.");
//	}
//
//	@Override
//	public void send(XxlRpcRequest xxlRpcRequest) {
//		this.ioSession.write(xxlRpcRequest);
//    }
//
//}
