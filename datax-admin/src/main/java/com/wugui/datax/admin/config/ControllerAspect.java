package com.wugui.datax.admin.config;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by water on 2020.03.13.
 */
@Slf4j
@Aspect
@Component
public class ControllerAspect {

    /**
     * ~ 第一个 * 代表任意修饰符及任意返回值. ~ 第二个 * 定义在web包或者子包 ~ 第三个 * 任意方法 ~ .. 匹配任意数量的参数.
     */
    @Pointcut("execution(* com.wugui.datax.admin.controller..*.*(..))")
    public void logPointcut() {
    }

    @Around("logPointcut()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String method = signature.getDeclaringTypeName() + "." + signature.getName();
        long start = System.currentTimeMillis();
        String input = Arrays.toString(joinPoint.getArgs());
        log.info("---controller:{},input:{}", method, input);
        try {
            Object resObj = joinPoint.proceed();
            // log.info("---controller:{}, resObj:{},input:{}", method, resObj, input);
            long cost = System.currentTimeMillis() - start;
            log.info("---controller: {} costTime is {}ms", method, cost);
            return resObj;
        } catch (Throwable e) {
            long excepCost = System.currentTimeMillis() - start;
            log.error(
                    "controller request failed {} costTime is {}ms with exception {}",
                    method, excepCost, e.getMessage());
            throw e;
        }
    }

//    private Object changeRestReturn(Object obj) {
////反射获取object的data属性值(subEvent中的eventId)
//        String eventId = null;
//        Class jsonClass = obj.getClass();//通过entitySave的Object对象，获取运行时类的对象
//        Field dataField = null;
//        try {
//            //获取object中的data属性
//            dataField = jsonClass.getDeclaredField("data");
//            dataField.setAccessible(true);//设置data属性为可访问的
//            SubEvent tempSubEvent = new SubEvent();
//            try {
//                //通过Field.get(Object)获取object的data(SubEvent)中的eventId属性
//                tempSubEvent = (SubEvent) dataField.get(entitySave);
//                eventId = tempSubEvent.getEventId();
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            }
//        } catch (NoSuchFieldException e) {
//            e.printStackTrace();
//        }
//        return obj;
//    }


    /**
     * 根据属性名获取属性值
     */
    private String getFieldValueByName(String fieldName, Object o) {
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = o.getClass().getMethod(getter);
            Object value = method.invoke(o);
            return ((value != null) ? value.toString() : null);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * 获取属性类型(type)，属性名(name)，属性值(value)的map组成的list
     */
    private Map<String, String> getFileds(Object o) {
        Map<String, String> map = new HashMap<>();
        Field[] fields = o.getClass().getDeclaredFields();
        for (Field field : fields) {
            map.put(field.getName(), getFieldValueByName(field.getName(), o));
        }
        return map;
    }

}
