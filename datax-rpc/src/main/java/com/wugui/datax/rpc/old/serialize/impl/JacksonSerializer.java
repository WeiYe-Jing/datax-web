package com.wugui.datax.rpc.old.serialize.impl;//package com.xxl.rpc.serialize.impl;
//
//import com.fasterxml.jackson.core.JsonParseException;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.JsonMappingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.xxl.rpc.serialize.Serializer;
//import com.xxl.rpc.util.XxlRpcException;
//
//import java.io.IOException;
//
///**
// * Jackson serializer
// *
// * 		1、obj need private and set/get；
// * 		2、do not support inner class；
// *
// * 		<!-- jackson (provided) -->
// * 		<dependency>
// * 		    <groupId>com.fasterxml.jackson.core</groupId>
// * 		    <artifactId>jackson-databind</artifactId>
// * 		    <version>${jackson.version}</version>
// * 		    <scope>provided</scope>
// * 		</dependency>
// *
// * @author xuxueli 2015-9-25 18:02:56
// */
//public class JacksonSerializer extends Serializer {
//    private final static ObjectMapper objectMapper = new ObjectMapper();
//
//    /** bean、array、List、Map --> json
//     * @param <T>*/
//    @Override
//	public <T> byte[] serialize(T obj) {
//		try {
//			return objectMapper.writeValueAsBytes(obj);
//		} catch (JsonProcessingException e) {
//			throw new XxlRpcException(e);
//		}
//	}
//
//    /** string --> bean、Map、List(array) */
//    @Override
//	public <T> Object deserialize(byte[] bytes, Class<T> clazz)  {
//		try {
//			return objectMapper.readValue(bytes, clazz);
//		} catch (JsonParseException e) {
//			throw new XxlRpcException(e);
//		} catch (JsonMappingException e) {
//			throw new XxlRpcException(e);
//		} catch (IOException e) {
//			throw new XxlRpcException(e);
//		}
//	}
//
//}
