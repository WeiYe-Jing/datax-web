package com.wugui.datax.admin.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @explain JAVA反射工具类
 * @author Song
 * @date 2019/12/17
 */
public class ReflectionUtil {

    /**
     * 获取私有成员变量的值
     * @param instance 要获取的对象
     * @param filedName 获取的变量名称
     * @return 返回获取变量的信息（需要强转）
     */
    public static Object getPrivateField(Object instance, String filedName) throws NoSuchFieldException, IllegalAccessException {
        Field field = instance.getClass().getDeclaredField(filedName);
        field.setAccessible(true);
        return field.get(instance);
    }

    /**
     * 设置私有成员的值
     * @param instance 要获取的对象
     * @param fieldName 要获取的变量名
     * @param value 设置的值
     */
    public static void setPrivateField(Object instance, String fieldName, Object value) throws NoSuchFieldException, IllegalAccessException {
        Field field = instance.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(instance, value);
    }

    /**
     * 访问私有方法
     * @param instance 要获取的对象
     * @param methodName 私有方法的名称
     * @param classes  CLASS的返回信息
     * @param objects 参数信息
     * @return
     */
    public static Object invokePrivateMethod(Object instance, String methodName, Class[] classes, String objects) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Method method = instance.getClass().getDeclaredMethod(methodName, classes);
        method.setAccessible(true);
        return method.invoke(instance, objects);
    }

}
