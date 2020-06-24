package com.wugui.datax.admin.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * DataX JSON 用户名密码解密
 *
 * @author zhouhongfa@gz-yibo.com
 * @ClassName JSONUtils
 * @Version 1.0
 * @since 2019/7/31 14:54
 */
public class JSONUtils {

    /**
     * 返回格式化的json
     *
     * @param object
     * @return
     */
    public static String formatJson(Object object) {
        return JSON.toJSONString(object, true);
    }

    public static String decryptJson(String jsonStr) {
        JSONObject json = JSONObject.parseObject(jsonStr);
        JSONObject job = json.getJSONObject("job");
        JSONArray content = job.getJSONArray("content");
        for (int i = 0; i < content.size(); i++) {
            ((JSONObject) content.get(i)).put("reader", decrypt(content.getString(i), "reader"));
            ((JSONObject) content.get(i)).put("writer", decrypt(content.getString(i), "writer"));
        }
        job.put("content", content);
        json.put("job", job);
        return json.toJSONString();
    }

    public static JSONObject decrypt(String content, String key) {
        JSONObject writer = JSONObject.parseObject(JSONObject.parseObject(content).getString(key));
        JSONObject writerParams = JSONObject.parseObject(writer.getString("parameter"));

        String dUsername = AESUtil.decrypt(writerParams.getString("username"));
        String username = dUsername == null ? writerParams.getString("username") : dUsername;
        writerParams.put("username", username);

        String dPassword = AESUtil.decrypt(writerParams.getString("password"));
        String password = dPassword == null ? writerParams.getString("password") : dPassword;
        writerParams.put("password", password);

        writer.put("parameter", writerParams);
        return writer;
    }


    public static void main(String[] args) {
        String s = "{\n" +
                "  \"job\": {\n" +
                "    \"setting\": {\n" +
                "      \"speed\": {\n" +
                "        \"channel\": 3\n" +
                "      },\n" +
                "      \"errorLimit\": {\n" +
                "        \"record\": 0,\n" +
                "        \"percentage\": 0.02\n" +
                "      }\n" +
                "    },\n" +
                "    \"content\": [\n" +
                "      {\n" +
                "        \"reader\": {\n" +
                "          \"name\": \"mysqlreader\",\n" +
                "          \"parameter\": {\n" +
                "            \"username\": \"8ivlZcKGclwi5NwpuzkXzg==\",\n" +
                "            \"password\": \"fZfGM5387/mzCDxqjb2thQ==\",\n" +
                "            \"column\": [\n" +
                "              \"`id`\"\n" +
                "            ],\n" +
                "            \"splitPk\": \"\",\n" +
                "            \"connection\": [\n" +
                "              {\n" +
                "                \"table\": [\n" +
                "                  \"datax_plugin\"\n" +
                "                ],\n" +
                "                \"jdbcUrl\": [\n" +
                "                  \"jdbc:mysql://localhost:3306/datax_web\"\n" +
                "                ]\n" +
                "              }\n" +
                "            ]\n" +
                "          }\n" +
                "        },\n" +
                "        \"writer\": {\n" +
                "          \"name\": \"mysqlwriter\",\n" +
                "          \"parameter\": {\n" +
                "            \"username\": \"8ivlZcKGclwi5NwpuzkXzg==\",\n" +
                "            \"password\": \"fZfGM5387/mzCDxqjb2thQ==\",\n" +
                "            \"column\": [\n" +
                "              \"`id`\"\n" +
                "            ],\n" +
                "            \"preSql\": [\n" +
                "              \"\"\n" +
                "            ],\n" +
                "            \"connection\": [\n" +
                "              {\n" +
                "                \"table\": [\n" +
                "                  \"job_log\"\n" +
                "                ],\n" +
                "                \"jdbcUrl\": \"jdbc:mysql://localhost:3306/datax_web\"\n" +
                "              }\n" +
                "            ]\n" +
                "          }\n" +
                "        }\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}";
        System.out.println(decryptJson(s));
    }
}
