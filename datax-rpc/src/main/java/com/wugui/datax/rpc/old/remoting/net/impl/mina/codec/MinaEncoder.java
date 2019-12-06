package com.wugui.datax.rpc.old.remoting.net.impl.mina.codec;//package com.xxl.rpc.remoting.net.impl.mina.codec;
//
//import org.apache.mina.core.buffer.IoBuffer;
//import org.apache.mina.core.session.IoSession;
//import org.apache.mina.filter.codec.ProtocolEncoder;
//import org.apache.mina.filter.codec.ProtocolEncoderOutput;
//
//import com.xxl.rpc.serialize.Serializer;
//
//public class MinaEncoder implements ProtocolEncoder {
//
//	private Class<?> genericClass;
//    private Serializer serializer;
//
//    public MinaEncoder(Class<?> genericClass, final Serializer serializer) {
//        this.genericClass = genericClass;
//        this.serializer = serializer;
//    }
//
//    @Override
//	public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
//    	if (genericClass.isInstance(message)) {
//            byte[] datas = serializer.serialize(message);
//
//            IoBuffer buffer = IoBuffer.allocate(256);
//    		buffer.setAutoExpand(true);
//    		buffer.setAutoShrink(true);
//
//    		buffer.putInt(datas.length);
//    		buffer.put(datas);
//
//    		buffer.flip();
//    		session.write(buffer);
//        }
//	}
//
//	@Override
//	public void dispose(IoSession session) throws Exception {
//
//	}
//
//}
