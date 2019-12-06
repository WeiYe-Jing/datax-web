package com.wugui.datax.rpc.remoting.net.impl.netty_http.server;

import com.wugui.datax.rpc.remoting.net.params.Beat;
import com.wugui.datax.rpc.remoting.net.params.XxlRpcRequest;
import com.wugui.datax.rpc.remoting.net.params.XxlRpcResponse;
import com.wugui.datax.rpc.remoting.provider.XxlRpcProviderFactory;
import com.wugui.datax.rpc.util.ThrowableUtil;
import com.wugui.datax.rpc.util.XxlRpcException;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadPoolExecutor;


/**
 * netty_http
 *
 * @author xuxueli 2015-11-24 22:25:15
 */
public class NettyHttpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    private static final Logger logger = LoggerFactory.getLogger(NettyHttpServerHandler.class);


    private XxlRpcProviderFactory xxlRpcProviderFactory;
    private ThreadPoolExecutor serverHandlerPool;

    public NettyHttpServerHandler(final XxlRpcProviderFactory xxlRpcProviderFactory, final ThreadPoolExecutor serverHandlerPool) {
        this.xxlRpcProviderFactory = xxlRpcProviderFactory;
        this.serverHandlerPool = serverHandlerPool;
    }

    @Override
    protected void channelRead0(final ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {

        // request parse
        final byte[] requestBytes = ByteBufUtil.getBytes(msg.content());    // byteBuf.toString(io.netty.util.CharsetUtil.UTF_8);
        final String uri = msg.uri();
        final boolean keepAlive = HttpUtil.isKeepAlive(msg);

        // do invoke
        serverHandlerPool.execute(new Runnable() {
            @Override
            public void run() {
                process(ctx, uri, requestBytes, keepAlive);
            }
        });
    }

    private void process(ChannelHandlerContext ctx, String uri, byte[] requestBytes, boolean keepAlive){
        String requestId = null;
        try {
            if ("/services".equals(uri)) {	// services mapping

                // request
                StringBuffer stringBuffer = new StringBuffer("<ui>");
                for (String serviceKey: xxlRpcProviderFactory.getServiceData().keySet()) {
                    stringBuffer.append("<li>").append(serviceKey).append(": ").append(xxlRpcProviderFactory.getServiceData().get(serviceKey)).append("</li>");
                }
                stringBuffer.append("</ui>");

                // response serialize
                byte[] responseBytes = stringBuffer.toString().getBytes("UTF-8");

                // response-write
                writeResponse(ctx, keepAlive, responseBytes);

            } else {

                // valid
                if (requestBytes.length == 0) {
                    throw new XxlRpcException("xxl-rpc request data empty.");
                }

                // request deserialize
                XxlRpcRequest xxlRpcRequest = (XxlRpcRequest) xxlRpcProviderFactory.getSerializerInstance().deserialize(requestBytes, XxlRpcRequest.class);
                requestId = xxlRpcRequest.getRequestId();

                // filter beat
                if (Beat.BEAT_ID.equalsIgnoreCase(xxlRpcRequest.getRequestId())){
                    logger.debug(">>>>>>>>>>> xxl-rpc provider netty_http server read beat-ping.");
                    return;
                }

                // invoke + response
                XxlRpcResponse xxlRpcResponse = xxlRpcProviderFactory.invokeService(xxlRpcRequest);

                // response serialize
                byte[] responseBytes = xxlRpcProviderFactory.getSerializerInstance().serialize(xxlRpcResponse);

                // response-write
                writeResponse(ctx, keepAlive, responseBytes);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);

            // response error
            XxlRpcResponse xxlRpcResponse = new XxlRpcResponse();
            xxlRpcResponse.setRequestId(requestId);
            xxlRpcResponse.setErrorMsg(ThrowableUtil.toString(e));

            // response serialize
            byte[] responseBytes = xxlRpcProviderFactory.getSerializerInstance().serialize(xxlRpcResponse);

            // response-write
            writeResponse(ctx, keepAlive, responseBytes);
        }

    }

    /**
     * write response
     */
    private void writeResponse(ChannelHandlerContext ctx, boolean keepAlive, byte[] responseBytes){
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.wrappedBuffer(responseBytes));
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html;charset=UTF-8");       // HttpHeaderValues.TEXT_PLAIN.toString()
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
        if (keepAlive) {
            response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        }
        ctx.writeAndFlush(response);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.error(">>>>>>>>>>> xxl-rpc provider netty_http server caught exception", cause);
        ctx.close();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent){
            ctx.channel().close();      // beat 3N, close if idle
            logger.debug(">>>>>>>>>>> xxl-rpc provider netty_http server close an idle channel.");
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

}