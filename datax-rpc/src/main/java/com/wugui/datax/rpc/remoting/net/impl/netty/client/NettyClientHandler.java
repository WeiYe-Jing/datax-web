package com.wugui.datax.rpc.remoting.net.impl.netty.client;

import com.wugui.datax.rpc.remoting.invoker.XxlRpcInvokerFactory;
import com.wugui.datax.rpc.remoting.net.params.Beat;
import com.wugui.datax.rpc.remoting.net.params.XxlRpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * rpc netty client handler
 *
 * @author xuxueli 2015-10-31 18:00:27
 */
public class NettyClientHandler extends SimpleChannelInboundHandler<XxlRpcResponse> {
	private static final Logger logger = LoggerFactory.getLogger(NettyClientHandler.class);


	private XxlRpcInvokerFactory xxlRpcInvokerFactory;
	private NettyConnectClient nettyConnectClient;
	public NettyClientHandler(final XxlRpcInvokerFactory xxlRpcInvokerFactory, NettyConnectClient nettyConnectClient) {
		this.xxlRpcInvokerFactory = xxlRpcInvokerFactory;
		this.nettyConnectClient = nettyConnectClient;
	}


	@Override
	protected void channelRead0(ChannelHandlerContext ctx, XxlRpcResponse xxlRpcResponse) throws Exception {

		// notify response
		xxlRpcInvokerFactory.notifyInvokerFuture(xxlRpcResponse.getRequestId(), xxlRpcResponse);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.error(">>>>>>>>>>> xxl-rpc netty client caught exception", cause);
		ctx.close();
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (evt instanceof IdleStateEvent){
			/*ctx.channel().close();      // close idle channel
			logger.debug(">>>>>>>>>>> xxl-rpc netty client close an idle channel.");*/

			nettyConnectClient.send(Beat.BEAT_PING);	// beat N, close if fail(may throw error)
			logger.debug(">>>>>>>>>>> xxl-rpc netty client send beat-ping.");

		} else {
			super.userEventTriggered(ctx, evt);
		}
	}

}
