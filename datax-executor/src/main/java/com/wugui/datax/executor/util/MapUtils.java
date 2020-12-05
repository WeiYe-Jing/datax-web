package com.wugui.datax.executor.util;

import java.util.Map;

/**
 * map集合工具类
 * @author WangAoQi
 */
public class MapUtils {
    /**
     * 获取map中第一个数据值
     *
     * @param map 数据源
     * @return
     */
    public static String getFirstOrNull(Map<Boolean, String> map) {
        String obj = null;
        for (Map.Entry<Boolean, String> entry : map.entrySet()) {
            obj = entry.getValue();
            if (obj != null) {
                break;
            }
        }
        return  obj;
    }
}
