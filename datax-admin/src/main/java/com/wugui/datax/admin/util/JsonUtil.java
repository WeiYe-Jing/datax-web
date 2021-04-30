package com.wugui.datax.admin.util;


import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.wugui.datax.admin.service.impl.JobServiceImpl;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * josn处理类
 */
public class JsonUtil {
    private static Logger logger = LoggerFactory.getLogger(JsonUtil.class);
    private final static ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
    }

    /**
     * 格式化对象为json字符串
     *
     * @param obj
     * @return
     * @throws Exception
     * @throws JsonProcessingException
     * @author julong
     * @date 2016-6-23 上午11:23:56
     */
    public static String formatObjectToJson(Object obj) throws JsonProcessingException {
        String result = null;
        logger.debug("【JsonUtil】格式化Object为string字符串操作");
        result = objectMapper.writeValueAsString(obj);
        return result;
    }

    /**
     * 格式化对象为byte[]
     *
     * @param obj
     * @return
     * @throws Exception
     * @throws JsonProcessingException
     */
    public static byte[] formatObjectToBytes(Object obj) throws JsonProcessingException {
        byte[] result = null;
        logger.debug("【JsonUtil】格式化Object为string字符串操作");
        result = objectMapper.writeValueAsBytes(obj);
        return result;
    }

    /**
     * 转换json字符串为Java对象
     *
     * @param data json字符串
     * @return Object
     * @throws JsonParseException   json转换异常
     * @throws JsonMappingException json映射异常
     * @throws IOException          io异常
     * @throws IOException
     * @throws JsonMappingException
     * @throws JsonParseException
     */
    public static Object formatDataStrToObjectBean(String data, Class<?> c) throws JsonParseException, JsonMappingException, IOException {
        logger.debug("【JsonUtil】格式化string为JAVA BEAN 操作");
        return objectMapper.readValue(data, c);
    }

    /**
     * 格式化参数为map对象的方法
     *
     * @param data
     * @return
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> formatDataToMap(String data) throws JsonParseException, JsonMappingException, IOException {
        logger.debug("【JsonUtil】格式化string为Map操作");
        Map<String, Object> maps = objectMapper.readValue(data, Map.class);
        return maps;
    }


    /**
     * 转换json为List<T> 对象
     *
     * @param data  json数据
     * @param clazz 需要转换的类
     * @return <T> List<Object>
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     */
    public static <T> List<T> formatDataToList(String data, Class<?> clazz) throws JsonParseException, JsonMappingException, IOException {
        logger.debug("【JsonUtil】格式化string为List<Object>操作");
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class, clazz);
        List<T> list = objectMapper.readValue(data, javaType);
        return list;
    }
}
