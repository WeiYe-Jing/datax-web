package com.wugui.datax.rpc.util;

import java.util.HashMap;

/**
 * @author xuxueli 2019-02-19
 */
public class ClassUtil {

    private static final HashMap<String, Class<?>> PRIM_CLASSES = new HashMap<>();

    static {
        PRIM_CLASSES.put("boolean", boolean.class);
        PRIM_CLASSES.put("byte", byte.class);
        PRIM_CLASSES.put("char", char.class);
        PRIM_CLASSES.put("short", short.class);
        PRIM_CLASSES.put("int", int.class);
        PRIM_CLASSES.put("long", long.class);
        PRIM_CLASSES.put("float", float.class);
        PRIM_CLASSES.put("double", double.class);
        PRIM_CLASSES.put("void", void.class);
    }

    public static Class<?> resolveClass(String className) throws ClassNotFoundException {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException ex) {
            Class<?> cl = PRIM_CLASSES.get(className);
            if (cl != null) {
                return cl;
            } else {
                throw ex;
            }
        }
    }

}
