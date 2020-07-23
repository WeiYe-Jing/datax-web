package com.wugui.datatx.core.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * json utils
 */
public class JSONUtils {

    private static final Logger logger = LoggerFactory.getLogger(JSONUtils.class);


    /**
     * json representation of object
     * @param object object
     * @return object to json string
     */
    public static String toJson(Object object) {
        try{
            return JSON.toJSONString(object,false);
        } catch (Exception e) {
            logger.error("object to json exception!",e);
        }

        return null;
    }


    /**
     *
     * This method deserializes the specified Json into an object of the specified class. It is not
     * suitable to use if the specified class is a generic type since it will not have the generic
     * type information because of the Type Erasure feature of Java. Therefore, this method should not
     * be used if the desired type is a generic type. Note that this method works fine if the any of
     * the fields of the specified object are generics, just the object itself should not be a
     * generic type.
     *
     * @param json the string from which the object is to be deserialized
     * @param clazz the class of T
     * @param <T> T
     * @return an object of type T from the string
     * classOfT
     */
    public static <T> T parseObject(String json, Class<T> clazz) {
        if (StringUtils.isEmpty(json)) {
            return null;
        }

        try {
            return JSON.parseObject(json, clazz);
        } catch (Exception e) {
            logger.error("parse object exception!",e);
        }
        return null;
    }

    /**
     * json to list
     *
     * @param json json string
     * @param clazz class
     * @param <T> T
     * @return list
     */
    public static <T> List<T> toList(String json, Class<T> clazz) {
        if (StringUtils.isEmpty(json)) {
            return new ArrayList<>();
        }
        try {
            return JSONArray.parseArray(json, clazz);
        } catch (Exception e) {
            logger.error("JSONArray.parseArray exception!",e);
        }

        return new ArrayList<>();
    }

    /**
     * json to map
     *
     * {@link #toMap(String, Class, Class)}
     *
     * @param json json
     * @return json to map
     */
    public static Map<String, String> toMap(String json) {
        if (StringUtils.isEmpty(json)) {
            return null;
        }

        try {
            return JSON.parseObject(json, new TypeReference<HashMap<String, String>>(){});
        } catch (Exception e) {
            logger.error("json to map exception!",e);
        }

        return null;
    }

    /**
     *
     * json to map
     *
     * @param json json
     * @param classK classK
     * @param classV classV
     * @param <K> K
     * @param <V> V
     * @return to map
     */
    public static <K, V> Map<K, V> toMap(String json, Class<K> classK, Class<V> classV) {
        if (StringUtils.isEmpty(json)) {
            return null;
        }

        try {
            return JSON.parseObject(json, new TypeReference<HashMap<K, V>>() {});
        } catch (Exception e) {
            logger.error("json to map exception!",e);
        }

        return null;
    }

    /**
     * object to json string
     * @param object object
     * @return json string
     */
    public static String toJsonString(Object object) {
        try{
            return JSON.toJSONString(object,false);
        } catch (Exception e) {
            throw new RuntimeException("Object json deserialization exception.", e);
        }
    }

    public static JSONObject parseObject(String text) {
        try{
            return JSON.parseObject(text);
        } catch (Exception e) {
            throw new RuntimeException("String json deserialization exception.", e);
        }
    }

    public static JSONArray parseArray(String text) {
        try{
            return JSON.parseArray(text);
        } catch (Exception e) {
            throw new RuntimeException("Json deserialization exception.", e);
        }
    }

}
