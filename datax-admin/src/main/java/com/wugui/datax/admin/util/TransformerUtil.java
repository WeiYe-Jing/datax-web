package com.wugui.datax.admin.util;

public class TransformerUtil {

    public static String getTransformerName(String name) {
        if ("Hiding".equals(name)) {
            return "dx_hiding";
        } else if ("Floor".equals(name)) {
            return "dx_floor";
        } else if ("Enumerate".equals(name)) {
            return "dx_enum";
        } else if ("Prefix Preserve".equals(name)) {
            return "dx_prefix_preserve";
        } else if ("MD5".equals(name)) {
            return "dx_md5";
        }
        return name;
    }
}
