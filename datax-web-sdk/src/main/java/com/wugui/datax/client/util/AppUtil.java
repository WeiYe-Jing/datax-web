package com.wugui.datax.client.util;

import cn.hutool.crypto.digest.DigestUtil;

import java.util.*;

/**
 * 应用接入AccessKey/SecretKey工具类
 *
 * @author Locki
 * @date 2020/10/13
 */
public class AppUtil {
    //生成 SecretKey 密钥
    private final static String SERVER_NAME = "datax_web_api_1234567809";
    private final static String[] chars = new String[]{"a", "b", "c", "d", "e", "f",
            "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
            "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z"};

    /**
     * @Description: <p>
     * 短8位UUID思想其实借鉴微博短域名的生成方式，但是其重复概率过高，而且每次生成4个，需要随即选取一个。
     * 本算法利用62个可打印字符，通过随机生成32位UUID，由于UUID都为十六进制，所以将UUID分成8组，每4个为一组，然后通过模62操作，结果作为索引取出字符，
     * 这样重复率大大降低。
     * 经测试，在生成一千万个数据也没有出现重复，完全满足大部分需求。
     * </p>
     * @author mazhq
     * @date 2019/8/27 16:16
     */
    public static String getAccessKey() {
        StringBuffer shortBuffer = new StringBuffer();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        for (int i = 0; i < 8; i++) {
            String str = uuid.substring(i * 4, i * 4 + 4);
            int x = Integer.parseInt(str, 16);
            shortBuffer.append(chars[x % 0x3E]);
        }
        return shortBuffer.toString();
    }

    /**
     * <p>
     * 通过AccessKey、用户名和内置关键词生成SecretKey
     * </P>
     *
     * @author mazhq
     * @date 2019/8/27 16:32
     */
    public static String getSecretKey(String userName, String accessKey) {
        String[] array = new String[]{userName, accessKey, SERVER_NAME};
        StringBuffer sb = new StringBuffer();
        // 字符串排序
        Arrays.sort(array);
        for (int i = 0; i < array.length; i++) {
            sb.append(array[i]);
        }
        String str = sb.toString();
        return DigestUtil.sha1Hex(str);
    }

    /**
     * 应用接入签名
     *
     * @param params
     * @param secretKey
     * @return {@link String}
     * @author Locki
     * @date 2020/10/13
     */
    public static String sign(Map<String, String> params, String secretKey) {
        String sign = "";
        if (params == null || params.size() < 1) {
            return sign;
        }
        Map<String, String> sorted = sortMap(params);
        StringBuffer sb = new StringBuffer();
        sorted.forEach((k, v) -> {
            if (!"sign".equals(k)) {
                sb.append(k).append("=").append(v == null ? "" : v).append("&");
            }
        });
        sb.append("secretKey").append("=").append(secretKey);
        sign = DigestUtil.md5Hex(sb.toString()).toUpperCase();
        return sign;

    }

    /**
     * Map按照key排序
     *
     * @param params
     * @return {@link Map< String, String>}
     * @author jiangyang
     * @date 2020/10/13
     */
    public static Map<String, String> sortMap(Map<String, String> params) {
        Map<String, String> sorted = new LinkedHashMap<>();
        if (params == null || params.size() < 1) {
            return sorted;
        }
        params.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEachOrdered(x -> sorted.put(x.getKey(), x.getValue()));
        return sorted;
    }

    public static void main(String[] args) {
        String accessKey = getAccessKey();
        String secretKey = getSecretKey("admin", accessKey);
        System.out.println("accessKey: " + accessKey);
        System.out.println("secretKey: " + secretKey);
        System.out.println(System.currentTimeMillis());

        Map<String, String> map = new HashMap<>();
        map.put("timestamp", "1602727212137");
        map.put("pageNo", "1");
        map.put("accessKey", "wj24UV2A");
        map.put("pageSize", "10");
        map.put("searchVal", "");
        System.out.println(sign(map, "bfd81d8725f3a38af0643ea5f910d7c3c7bc6f74"));
    }
}
