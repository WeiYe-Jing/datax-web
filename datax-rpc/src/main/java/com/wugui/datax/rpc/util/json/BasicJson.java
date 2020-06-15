package com.wugui.datax.rpc.util.json;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xuxueli 2018-11-30
 */
public class BasicJson {


    private static final BasicJsonReader basicJsonReader = new BasicJsonReader();
    private static final BasicJsonwriter basicJsonwriter = new BasicJsonwriter();


    /**
     * object to json
     *
     * @param object
     * @return
     */
    public static String toJson(Object object) {
        return basicJsonwriter.toJson(object);
    }

    /**
     * parse json to map
     *
     * @param json
     * @return only for filed type "null、ArrayList、LinkedHashMap、String、Long、Double、..."
     */
    public static Map<String, Object> parseMap(String json) {
        return basicJsonReader.parseMap(json);
    }

    /**
     * json to List
     *
     * @param json
     * @return
     */
    public static List<Object> parseList(String json) {
        return basicJsonReader.parseList(json);
    }


    public static void main(String[] args) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("msg", "success");
        result.put("arr", Arrays.asList("111", "222"));
        result.put("float", 1.11f);
        result.put("temp", null);

        String json = toJson(result);
        System.out.println(json);

        Map<String, Object> mapObj = parseMap(json);
        System.out.println(mapObj);

        List<Object> listInt = parseList("[111,222,33]");
        System.out.println(listInt);

    }



    /*// parse biz-object from map-object
    private static <T> T parseBizObjectFromMapObject(final Map<String, Object> mapObject, Class<T> businessClass){
        // parse class (only first level)
        try {
            Object newItem = businessClass.newInstance();
            Field[] fieldList = basicJsonwriter.getDeclaredFields(businessClass);
            for (Field field: fieldList) {

                // valid val
                Object fieldValue = mapObject.get(field.getName());
                if (fieldValue == null) {
                    continue;
                }

                // valid type
                if (field.getType() != fieldValue.getClass()) {

                    if (fieldValue instanceof LinkedHashMap) {

                        // Map-Value >> only support "class | map"
                        if (field.getType() != Map.class) {
                            fieldValue = parseBizObjectFromMapObject((LinkedHashMap)fieldValue, field.getType());
                        }
                    } else if (fieldValue instanceof ArrayList) {

                        // List-Value >> only support "List<Base> | List<Class>"
                        List<Object> fieldValueList = (ArrayList)fieldValue;
                        if (fieldValueList.size() > 0) {

                            Class list_field_RealType = (Class<?>)((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
                            if (FieldReflectionUtil.validBaseType(list_field_RealType)) {
                                // List<Base
                                if (list_field_RealType != fieldValueList.get(0).getClass()) {

                                    List<Object> list_newItemList = new ArrayList<>();
                                    for (Object list_oldItem: fieldValueList) {

                                        Object list_newItem = FieldReflectionUtil.parseValue(list_field_RealType, String.valueOf(list_oldItem));
                                        list_newItemList.add(list_newItem);
                                    }

                                }
                            } else {
                                // List<Class>
                                fieldValue = parseBizObjectListFromMapList((ArrayList)fieldValue, list_field_RealType);
                            }
                        }

                    } else {

                        // Base-Value >> support base
                        fieldValue = FieldReflectionUtil.parseValue(field.getType(), String.valueOf(fieldValue) );
                    }
                }

                // field set
                field.setAccessible(true);
                field.set(newItem, fieldValue);
            }

            return (T) newItem;
        } catch (Exception e) {
            throw new IllegalArgumentException("Cannot parse JSON", e);
        }
    }

    // parse biz-object-list from map-list
    public static <T> List<T> parseBizObjectListFromMapList(List<Object> listObject, Class<T> businessClass) {
        // valid
        if (listObject.size() == 0) {
            return new ArrayList<>();
        }
        if (listObject.get(0).getClass() != LinkedHashMap.class) {
            throw new IllegalArgumentException("Cannot parse JSON, custom class must match LinkedHashMap");
        }
        // parse business class
        try {
            List<Object> newItemList = new ArrayList<>();
            for (Object oldItem: listObject) {

                Map<String, Object> oldItemMap = (Map<String, Object>) oldItem;
                Object newItem = parseBizObjectFromMapObject(oldItemMap, businessClass);

                newItemList.add(newItem);
            }
            return (List<T>) newItemList;

        } catch (Exception e) {
            throw new IllegalArgumentException("Cannot parse JSON", e);
        }
    }


    *//**
     * parse json to <T>
     *
     * @param json
     * @param businessClass     null for base-class "Integer、Long、Map ... " , other for business-class
     * @param <T>
     * @return
     *//*
    public static <T> T parseObject(String json, Class<T> businessClass) {

        // map object
        Map<String, Object> mapObject = basicJsonReader.parseMap(json);

        if (businessClass == null || mapObject.size()==0) {
            // parse map class, default
            return (T) mapObject;
        } else {
            // parse business class
            return parseBizObjectFromMapObject(mapObject, businessClass);
        }
    }

    *//**
     * json to List<T>
     *
     * @param json
     * @param businessClass     null for base-class "Integer、Long、Map ... " , other for business-class
     * @param <T>
     * @return
     *//*
    public static <T> List<T> parseList(String json, Class<T> businessClass) {

        // list object
        List<Object> listObject = basicJsonReader.parseList(json);

        if (businessClass==null || listObject.size()==0) {
            // parse map class, default
            return (List<T>) listObject;
        } else {
            return parseBizObjectListFromMapList(listObject, businessClass);
        }

    }*/


}
