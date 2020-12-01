package com.wugui.datax.admin.util;

import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *字符串处理工具类
 */
public class StringUtil {

    private static final Logger logger = LoggerFactory.getLogger(StringUtil.class);

    /**
     * 查询字符串ddl中 以tag开始第一个'与第二个'之间的内容
     * @param ddl
     * @param tag
     * @return
     */
    public static String find(String ddl,String tag){
        String result = "";
        String str = ddl.substring(ddl.indexOf(tag) + tag.length()).trim();
        result = str.substring(getCharacterPosition(str,"'",1) + 1,getCharacterPosition(str,"'",2));
        return result;
    }

    /**
     * 查询字符串在字符串中第N次出现的位置
     * @param string
     * @param tag
     * @param time
     * @return
     */
    public static int getCharacterPosition(String string, String tag, int time){
        Matcher slashMatcher = Pattern.compile(tag).matcher(string);
        int mIdx = 0;
        while(slashMatcher.find()) {
            mIdx++;
            if(mIdx == time){
                break;
            }
        }
        return slashMatcher.start();
    }

    /**
     * 字符串转换unicode
     * @param string
     * @return
     */
    public static String string2Unicode(String string) {
        StringBuffer unicode = new StringBuffer();
        for (int i = 0; i < string.length(); i++) {
            // 取出每一个字符
            char c = string.charAt(i);
            // 转换为unicode
            unicode.append("\\u" + Integer.toHexString(c));
        }
        return unicode.toString();
    }

    /**
     * unicode 转字符串
     * @param unicode 全为 Unicode 的字符串
     * @return
     */
    public static String unicode2String(String unicode) {
        try{
            unicode = (unicode == null ? "" : unicode);
            if (!unicode.contains("\\"))//如果不是unicode码则原样返回
                return unicode;
            // 该步骤可以处理掉\\t \\n \\u0001等类似字符串
            String resulr = StringEscapeUtils.unescapeJava(unicode);
            if(resulr.length() == 1){
                return resulr;
            }else{
                // 该处处理的是 \\001  等类似字符串
                StringBuffer string = new StringBuffer();
                String[] hex = unicode.split("\\\\");
                for (int i = 1; i < hex.length; i++) {
                    // 转换出每一个代码点
                    int data = Integer.parseInt(hex[i], 16);
                    // 追加成string
                    string.append((char) data);
                }
                return string.toString();
            }
        }catch (Exception e){
            logger.error(e.getMessage());
            return unicode;
        }
    }
}
