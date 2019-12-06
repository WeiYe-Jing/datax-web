package com.wugui.datax.rpc.old.remoting.net.impl.servlet.server;//package com.xxl.rpc.remoting.net.impl.servlet.server;
//
//import com.xxl.rpc.remoting.net.params.XxlRpcRequest;
//import com.xxl.rpc.remoting.net.params.XxlRpcResponse;
//import com.xxl.rpc.remoting.provider.XxlRpcProviderFactory;
//import com.xxl.rpc.util.ThrowableUtil;
//import com.xxl.rpc.util.XxlRpcException;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//
///**
// * servlet
// *
// * @author xuxueli 2015-11-24 22:25:15
// */
//public class ServletServerHandler {
//    private static Logger logger = LoggerFactory.getLogger(ServletServerHandler.class);
//
//    private XxlRpcProviderFactory xxlRpcProviderFactory;
//    public ServletServerHandler(XxlRpcProviderFactory xxlRpcProviderFactory) {
//        this.xxlRpcProviderFactory = xxlRpcProviderFactory;
//    }
//
//    /**
//     * handle servlet request
//     */
//    public void handle(String target, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
//
//        if ("/services".equals(target)) {	// services mapping
//
//            StringBuffer stringBuffer = new StringBuffer("<ui>");
//            for (String serviceKey: xxlRpcProviderFactory.getServiceData().keySet()) {
//                stringBuffer.append("<li>").append(serviceKey).append(": ").append(xxlRpcProviderFactory.getServiceData().get(serviceKey)).append("</li>");
//            }
//            stringBuffer.append("</ui>");
//
//            writeResponse(response, stringBuffer.toString().getBytes());
//            return;
//        } else {	// default remoting mapping
//
//            // request parse
//            XxlRpcRequest xxlRpcRequest = null;
//            try {
//
//                xxlRpcRequest = parseRequest(request);
//            } catch (Exception e) {
//                writeResponse(response, ThrowableUtil.toString(e).getBytes());
//                return;
//            }
//
//            // invoke
//            XxlRpcResponse xxlRpcResponse = xxlRpcProviderFactory.invokeService(xxlRpcRequest);
//
//            // response-serialize + response-write
//            byte[] responseBytes = xxlRpcProviderFactory.getSerializer().serialize(xxlRpcResponse);
//            writeResponse(response, responseBytes);
//        }
//
//    }
//
//    /**
//     * write response
//     */
//    private void writeResponse(HttpServletResponse response, byte[] responseBytes) throws IOException {
//
//        response.setContentType("text/html;charset=UTF-8");
//        response.setStatus(HttpServletResponse.SC_OK);
//
//        OutputStream out = response.getOutputStream();
//        out.write(responseBytes);
//        out.flush();
//    }
//
//    /**
//     * parse request
//     */
//    private XxlRpcRequest parseRequest(HttpServletRequest request) throws Exception {
//        // deserialize request
//        byte[] requestBytes = readBytes(request);
//        if (requestBytes == null || requestBytes.length==0) {
//            throw new XxlRpcException("xxl-rpc request data is empty.");
//        }
//        XxlRpcRequest rpcXxlRpcRequest = (XxlRpcRequest) xxlRpcProviderFactory.getSerializer().deserialize(requestBytes, XxlRpcRequest.class);
//        return rpcXxlRpcRequest;
//    }
//
//    /**
//     * read bytes from http request
//     *
//     * @param request
//     * @return
//     * @throws IOException
//     */
//    public static final byte[] readBytes(HttpServletRequest request) throws IOException {
//        request.setCharacterEncoding("UTF-8");
//        int contentLen = request.getContentLength();
//        InputStream is = request.getInputStream();
//        if (contentLen > 0) {
//            int readLen = 0;
//            int readLengthThisTime = 0;
//            byte[] message = new byte[contentLen];
//            try {
//                while (readLen != contentLen) {
//                    readLengthThisTime = is.read(message, readLen, contentLen - readLen);
//                    if (readLengthThisTime == -1) {
//                        break;
//                    }
//                    readLen += readLengthThisTime;
//                }
//                return message;
//            } catch (IOException e) {
//                logger.error(e.getMessage(), e);
//            }
//        }
//        return new byte[] {};
//    }
//
//
//
//}
