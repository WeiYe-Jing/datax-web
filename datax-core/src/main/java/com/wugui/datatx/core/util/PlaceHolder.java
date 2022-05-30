package com.wugui.datatx.core.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * Author: duhanmin
 * Description: 占位符操作
 * 支持任意时间格式化和加减运算
 * 例如:${yyyyMMdd%-1d}/${yyyy-MM-01%-2M}等等
 * Date: 2022/5/27 11:10
 */
public class PlaceHolder {

    private static final String DOLLAR = "$";
    private static final String PLACEHOLDER_SPLIT = "%";
    private static final String PLACEHOLDER_LEFT = "{";
    private static final String PLACEHOLDER_RIGHT = "}";
    private static final String CYCLE_YEAR = "y";
    private static final String CYCLE_MONTH = "M";
    private static final String CYCLE_DAY = "d";
    private static final String CYCLE_HOUR = "H";
    private static final String CYCLE_MINUTE = "m";
    private static final String CYCLE_SECOND = "s";
    private static final String[] CYCLES  = new String[]{CYCLE_YEAR, CYCLE_MONTH, CYCLE_DAY, CYCLE_HOUR, CYCLE_MINUTE, CYCLE_SECOND};


    /**
     * @return
     */
    public static ZonedDateTime toZonedDateTime() {
        return ZonedDateTime.now(ZoneId.of("Asia/Shanghai")); // 用指定时区获取当前时间
    }

    /**
     * 处理时间 yyyy-MM-dd HH:mm:ss
     * @param text
     * @return
     */
    public static ZonedDateTime toZonedDateTime(CharSequence text) {
        return ZonedDateTime.parse(text,DateTimeFormatter.ISO_ZONED_DATE_TIME);
    }

    /**
     * 处理时间
     * @param text
     * @param pattern
     * @return
     */
    public static ZonedDateTime toZonedDateTime(CharSequence text, String pattern) {
        LocalDateTime time = DateUtil.parseLocalDateTime(text,pattern);
        ZoneId zoneId = ZoneId.of("+08:00");
        return time.atZone(zoneId);
    }

    /**
     * json中时间占位符的替换
     * @param dateTime
     * @param str
     * @param format
     * @return
     */
    public static String replaces(ZonedDateTime dateTime,String str,boolean format){
        if (JSONUtil.isJson(str)){
            try {
                JSONUtil.parse(str);
                Object object = JSONUtil.parse(str,JSONConfig.create().setOrder(true));
                replaceJson(dateTime,object);
                if (format)
                    return JSONUtil.formatJsonStr(object.toString());
                else return object.toString();
            }catch (Exception e){}
        }
        return replace(dateTime,str);
    }

    /**
     * json中时间占位符的替换
     * @param dateTime
     * @param str
     * @return
     */
    public static String replaces(ZonedDateTime dateTime,String str){
        return replaces(dateTime,str,true);
    }


    /**
     * 具体的替换方法
     * @param dateTime
     * @param str
     * @return
     */
    private static String replace(ZonedDateTime dateTime, String str){
        StringBuilder buffer = new StringBuilder(str);
        int startIndex = str.indexOf(PLACEHOLDER_LEFT);

        while (startIndex != -1) {
            int endIndex = buffer.indexOf(PLACEHOLDER_RIGHT, startIndex);
            if (endIndex != -1) {
                String placeHolder = buffer.substring(startIndex, endIndex + 1);
                String content = placeHolder.replace(PLACEHOLDER_LEFT, "").replace(PLACEHOLDER_RIGHT, "").trim();
                String[] parts = content.split(PLACEHOLDER_SPLIT);
                try{
                    ZonedDateTime ndt = dateTime;
                    for(int i = 1;i< parts.length;i++){
                        ndt = changeDateTime(ndt,parts[i]);
                    }

                    String newContent = ndt.format(DateTimeFormatter.ofPattern(parts[0]));
                    if (buffer.substring(startIndex -1 ,endIndex + 1).contains(DOLLAR)){
                        buffer.replace(startIndex - 1, endIndex + 1, newContent);
                    }else {
                        buffer.replace(startIndex, endIndex + 1, newContent);
                    }
                    startIndex = buffer.indexOf(PLACEHOLDER_LEFT, startIndex + newContent.length());
                }catch (IllegalArgumentException e1){
                    startIndex = buffer.indexOf(PLACEHOLDER_LEFT, endIndex);

                } catch (Exception e2){
                    throw new RuntimeException(e2);
                }
            } else{
                startIndex = -1; //leave while
            }
        }
        return buffer.toString();
    }

    /**
     * 替换参数
     * @param dateTime
     * @param str
     * @return
     */
    private static ZonedDateTime changeDateTime(ZonedDateTime dateTime ,String str){
        if(str == null || str.isEmpty()){
            return dateTime;
        }

        for (String cycle : CYCLES) {
            if (str.contains(cycle)) {
                switch (cycle) {
                    case CYCLE_DAY:
                        return dateTime.plusDays(Integer.parseInt(str.replace(CYCLE_DAY, "")));
                    case CYCLE_HOUR:
                        return dateTime.plusHours(Integer.parseInt(str.replace(CYCLE_HOUR, "")));
                    case CYCLE_MINUTE:
                        return dateTime.plusMinutes(Integer.parseInt(str.replace(CYCLE_MINUTE, "")));
                    case CYCLE_MONTH:
                        return dateTime.plusMonths(Integer.parseInt(str.replace(CYCLE_MONTH, "")));
                    case CYCLE_SECOND:
                        return dateTime.plusSeconds(Integer.parseInt(str.replace(CYCLE_SECOND, "")));
                    case CYCLE_YEAR:
                        return dateTime.plusYears(Integer.parseInt(str.replace(CYCLE_YEAR, "")));
                    default:
                        break;
                }
            }
        }

        return dateTime;
    }

    /**
     * 替换参数
     * @param keyValue
     * @param str
     * @return
     */
    private static String replace(Map<String,String> keyValue, String str){
        StringBuilder buffer = new StringBuilder(str);
        int startIndex = str.indexOf(PLACEHOLDER_LEFT);

        while (startIndex != -1) {
            int endIndex = buffer.indexOf(PLACEHOLDER_RIGHT, startIndex);
            if (endIndex != -1) {
                String placeHolder = buffer.substring(startIndex, endIndex + 1);
                String content = placeHolder.replace(PLACEHOLDER_LEFT, "").replace(PLACEHOLDER_RIGHT, "").trim();
                try{
                    String newContent = keyValue.get(content);
                    if(newContent != null){
                        if (buffer.substring(startIndex -1 ,endIndex + 1).contains(DOLLAR)){
                            buffer.replace(startIndex - 1, endIndex + 1, newContent);
                        }else {
                            buffer.replace(startIndex, endIndex + 1, newContent);
                        }
                        startIndex = buffer.indexOf(PLACEHOLDER_LEFT, startIndex + newContent.length());
                    }else{
                        startIndex = buffer.indexOf(PLACEHOLDER_LEFT, endIndex);
                    }
                }catch (Exception e2){
                    throw new RuntimeException(e2);
                }
            } else{
                startIndex = -1; //leave while
            }
        }
        return buffer.toString();
    }

    /**
     * 替换导入导出的json文件中的占位符
     * @param keyValue
     * @param object
     */
    @SuppressWarnings("DuplicatedCode")
    private static void replaceJson(Map<String,String> keyValue, Object object){

        if(object instanceof JSONArray){
            JSONArray array = (JSONArray)object;
            for(int i = 0;i <array.size();i++){
                Object temp = array.get(i);
                if(temp instanceof JSONArray){
                    replaceJson(keyValue,temp);
                }else if(temp instanceof JSONObject){
                    replaceJson(keyValue,temp);
                }else{
                    array.set(i, replace(keyValue,temp.toString()));
                }
            }
        }else if(object instanceof JSONObject) {
            JSONObject jsonObject = (JSONObject) object;
            for(Map.Entry<String,Object> entry : jsonObject.entrySet()){
                Object temp = entry.getValue();
                if(temp instanceof JSONArray){
                    replaceJson(keyValue,temp);
                }else if(temp instanceof JSONObject){
                    replaceJson(keyValue,temp);
                }else{
                    jsonObject.set(entry.getKey(), replace(keyValue, temp.toString()));
                }
            }
        }

    }

    /**
     * 替换
     * @param keyValue
     * @param json
     * @return
     */
    private static String replaceJson(Map<String,String> keyValue,String json){
        Object object = JSONUtil.parse(json);
        replaceJson(keyValue,object);
        return object.toString();
    }

    /**
     * 替换resource目录下的json文件中的占位符
     * @param dateTime
     * @param object
     */
    @SuppressWarnings("DuplicatedCode")
    private static void replaceJson(ZonedDateTime dateTime, Object object){

        if(object instanceof JSONArray){
            JSONArray array = (JSONArray)object;
            for(int i = 0;i <array.size();i++){
                Object temp = array.get(i);
                if(temp instanceof JSONArray){
                    replaceJson(dateTime,temp);
                }else if(temp instanceof JSONObject){
                    replaceJson(dateTime,temp);
                }else{
                    array.set(i, replace(dateTime,temp.toString()));
                }
            }
        }else if(object instanceof JSONObject) {
            JSONObject jsonObject = (JSONObject) object;
            for(Map.Entry<String,Object> entry : jsonObject.entrySet()){
                Object temp = entry.getValue();
                if(temp instanceof JSONArray){
                    replaceJson(dateTime,temp);
                }else if(temp instanceof JSONObject){
                    replaceJson(dateTime,temp);
                }else{
                    jsonObject.set(entry.getKey(), replace(dateTime, temp.toString()));
                }
            }
        }
    }

    /**
     * 使用环境变量替换值
     * @param template
     * @param map
     * @return
     */
    public static String format(CharSequence template, Map<?, ?> map) {
        return PlaceHolder.format(template,map,"${","}",true);
    }

    /**
     * 格式化文本，使用 {varName} 占位<br>
     * map = {a: "aValue", b: "bValue"} format("{a} and {b}", map) ---=》 aValue and bValue
     *
     * @param template   文本模板，被替换的部分用 {key} 表示
     * @param map        参数值对
     * @param leftStr 左边占位符
     * @param rightStr 右边占位符
     * @param ignoreNull 是否忽略 {@code null} 值，忽略则 {@code null} 值对应的变量不被替换，否则替换为""
     * @return
     */
    public static String format(CharSequence template, Map<?, ?> map, CharSequence leftStr, CharSequence rightStr, boolean ignoreNull) {
        if (null == template) {
            return null;
        }
        if (null == map || map.isEmpty()) {
            return template.toString();
        }

        if (StrUtil.isBlank(leftStr)) {
            leftStr = "";
        }

        if (StrUtil.isBlank(rightStr)) {
            rightStr = "";
        }

        String template2 = template.toString();
        String value;
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            value = StrUtil.utf8Str(entry.getValue());
            if (null == value && ignoreNull) {
                continue;
            }
            template2 = StrUtil.replace(template2, leftStr.toString() + entry.getKey() + rightStr, value);
        }
        return template2;
    }
}
