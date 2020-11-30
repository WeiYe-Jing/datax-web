package com.wugui.datax.admin.util;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MatcherUtils {

    public static List<String> PatternMatcher(String constr){
        List<String> list = new ArrayList<>();
        Pattern p = Pattern.compile("\\$\\{(.*?)}");
        // 匹配】
        Matcher matcher = p.matcher(constr);
        while (matcher.find()) {
            System.out.println("woo: " + matcher.group());
            if(!list.contains(matcher.group())){
                list.add(matcher.group());
            }
        }
        return  list;
    }



    public static void main(String[] args) {
        List<String> strings = MatcherUtils.PatternMatcher("${12}12");
        System.out.println(StringUtils.join(strings.toArray(), ",") );
        System.out.println(StringUtils.join(strings.toArray(), ","));
    }
}
