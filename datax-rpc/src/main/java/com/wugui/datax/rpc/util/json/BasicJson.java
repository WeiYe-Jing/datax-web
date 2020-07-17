package com.wugui.datax.rpc.util.json;

import java.util.List;
import java.util.Map;

/**
 * @author xuxueli 2018-11-30
 */
public class BasicJson {


    private static final BasicJsonReader BASIC_JSON_READER = new BasicJsonReader();
    private static final BasicJsonwriter BASIC_JSON_WRITER = new BasicJsonwriter();


    /**
     * object to json
     *
     * @param object
     * @return
     */
    public static String toJson(Object object) {
        return BASIC_JSON_WRITER.toJson(object);
    }

    /**
     * parse json to map
     *
     * @param json
     * @return only for filed type "null、ArrayList、LinkedHashMap、String、Long、Double、..."
     */
    public static Map<String, Object> parseMap(String json) {
        return BASIC_JSON_READER.parseMap(json);
    }

    /**
     * json to list
     *
     * @param json
     * @return
     */
    public static List<Object> parseList(String json) {
        return BASIC_JSON_READER.parseList(json);
    }


}
