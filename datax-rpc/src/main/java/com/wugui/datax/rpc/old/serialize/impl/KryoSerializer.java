package com.wugui.datax.rpc.old.serialize.impl;//package com.xxl.rpc.serialize.impl;
//
//import com.esotericsoftware.kryo.Kryo;
//import com.esotericsoftware.kryo.io.Input;
//import com.esotericsoftware.kryo.io.Output;
//import com.xxl.rpc.serialize.Serializer;
//import com.xxl.rpc.util.XxlRpcException;
//
//import java.io.ByteArrayInputStream;
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//
///**
// * kryo serializer
// *
// *      - Tips：Class Must have no-arg constructor
// *
// *
// *      <!-- kryo (provided) -->
// * 		<dependency>
// * 			<groupId>com.esotericsoftware</groupId>
// * 			<artifactId>kryo</artifactId>
// * 			<version>${kryo.version}</version>
// * 			<scope>provided</scope>
// * 		</dependency>
// *
// * @author xuxueli 2019-02-19
// */
//public class KryoSerializer extends Serializer {
//
//    private final ThreadLocal<Kryo> kryoLocal = new ThreadLocal<Kryo>() {
//        @Override
//        protected Kryo initialValue() {
//
//            Kryo kryo = new Kryo();
//            kryo.setReferences(true);   //支持对象循环引用（否则会栈溢出）
//            kryo.setRegistrationRequired(false);    //不强制要求注册类（注册行为无法保证多个 JVM 内同一个类的注册编号相同；而且业务系统中大量的 Class 也难以一一注册）
//            return kryo;
//        }
//    };
//
//    @Override
//    public <T> byte[] serialize(T obj) {
//        ByteArrayOutputStream os = new ByteArrayOutputStream();
//        Output output = new Output(os);
//        try {
//            kryoLocal.get().writeObject(output, obj);
//            output.flush();
//
//            byte[] result = os.toByteArray();
//            return result;
//        } catch (Exception e) {
//            throw new XxlRpcException(e);
//        } finally {
//            try {
//                output.close();
//            } catch (Exception e) {
//                throw new XxlRpcException(e);
//            }
//            try {
//                os.close();
//            } catch (IOException e) {
//                throw new XxlRpcException(e);
//            }
//        }
//    }
//
//    @Override
//    public <T> Object deserialize(byte[] bytes, Class<T> clazz) {
//        ByteArrayInputStream is = new ByteArrayInputStream(bytes);
//        Input input = new Input(is);
//        try {
//            Object result = kryoLocal.get().readObject(input, clazz);
//            return result;
//        } catch (Exception e) {
//            throw new XxlRpcException(e);
//        } finally {
//            try {
//                input.close();
//            } catch (Exception e) {
//                throw new XxlRpcException(e);
//            }
//            try {
//                is.close();
//            } catch (IOException e) {
//                throw new XxlRpcException(e);
//            }
//        }
//    }
//
//}
