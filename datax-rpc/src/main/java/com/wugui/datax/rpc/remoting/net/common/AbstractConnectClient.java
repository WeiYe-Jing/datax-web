package com.wugui.datax.rpc.remoting.net.common;

import com.wugui.datax.rpc.remoting.invoker.XxlRpcInvokerFactory;
import com.wugui.datax.rpc.remoting.invoker.reference.XxlRpcReferenceBean;
import com.wugui.datax.rpc.remoting.net.params.BaseCallback;
import com.wugui.datax.rpc.remoting.net.params.XxlRpcRequest;
import com.wugui.datax.rpc.serialize.AbstractSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.text.html.parser.Entity;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author xuxueli 2018-10-19
 */
public abstract class AbstractConnectClient {
    protected static transient Logger logger = LoggerFactory.getLogger(AbstractConnectClient.class);

    /**
     * 初始化连接
     *
     * @param address              地址信息
     * @param serializer           序列化方式
     * @param xxlRpcInvokerFactory rpc工厂
     * @throws Exception 异常
     */
    public abstract void init(String address, final AbstractSerializer serializer, final XxlRpcInvokerFactory xxlRpcInvokerFactory) throws Exception;

    /**
     * 关闭连接
     */
    public abstract void close();

    /**
     * 是否验证
     *
     * @return boolean
     */
    public abstract boolean isValidate();

    /**
     * 发送请求
     *
     * @param xxlRpcRequest rpc请求
     * @throws Exception
     */
    public abstract void send(XxlRpcRequest xxlRpcRequest) throws Exception;


    /**
     * @param xxlRpcRequest       rpc请求
     * @param address             地址
     * @param connectClientImpl   连接实现类
     * @param xxlRpcReferenceBean rpc bean
     * @throws Exception 异常
     */
    public static void asyncSend(XxlRpcRequest xxlRpcRequest, String address,
                                 Class<? extends AbstractConnectClient> connectClientImpl,
                                 final XxlRpcReferenceBean xxlRpcReferenceBean) throws Exception {

        // client pool	[tips03 : may save 35ms/100invoke if move it to constructor, but it is necessary. cause by ConcurrentHashMap.get]
        AbstractConnectClient clientPool = AbstractConnectClient.getPool(address, connectClientImpl, xxlRpcReferenceBean);
        try {
            // do invoke
            clientPool.send(xxlRpcRequest);
        } catch (Exception e) {
            throw e;
        }
    }

    private static volatile ConcurrentMap<String, AbstractConnectClient> connectClientMap;
    private static volatile ConcurrentMap<String, Object> connectClientLockMap = new ConcurrentHashMap<>();

    private static AbstractConnectClient getPool(String address, Class<? extends AbstractConnectClient> connectClientImpl,
                                                 final XxlRpcReferenceBean xxlRpcReferenceBean) throws Exception {

        // init base compont, avoid repeat init
        if (connectClientMap == null) {
            synchronized (AbstractConnectClient.class) {
                if (connectClientMap == null) {
                    connectClientMap = new ConcurrentHashMap<>();
                    // stop callback
                    xxlRpcReferenceBean.getInvokerFactory().addStopCallBack(new BaseCallback() {
                        @Override
                        public void run() {
                            if (connectClientMap.size() > 0) {
                                connectClientMap.keySet().stream().map(key -> connectClientMap.get(key)).forEach(AbstractConnectClient::close);
                                connectClientMap.clear();
                            }
                        }
                    });
                }
            }
        }

        // get-valid client
        AbstractConnectClient connectClient = connectClientMap.get(address);
        if (connectClient != null && connectClient.isValidate()) {
            return connectClient;
        }

        // lock
        Object clientLock = connectClientLockMap.get(address);
        if (clientLock == null) {
            connectClientLockMap.putIfAbsent(address, new Object());
            clientLock = connectClientLockMap.get(address);
        }
        // remove-create new client
        synchronized (clientLock) {
            // get-valid client, avlid repeat
            connectClient = connectClientMap.get(address);
            if (connectClient != null && connectClient.isValidate()) {
                return connectClient;
            }
            // remove old
            if (connectClient != null) {
                connectClient.close();
                connectClientMap.remove(address);
            }
            // set pool
            AbstractConnectClient connectClientNew = connectClientImpl.newInstance();
            try {
                connectClientNew.init(address, xxlRpcReferenceBean.getSerializerInstance(), xxlRpcReferenceBean.getInvokerFactory());
                connectClientMap.put(address, connectClientNew);
            } catch (Exception e) {
                connectClientNew.close();
                throw e;
            }

            return connectClientNew;
        }

    }

}
