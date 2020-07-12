package com.wugui.datax.rpc.serialize;

/**
 * serializer
 * <p>
 * Tips：模板方法模式：定义一个操作中算法的骨架（或称为顶级逻辑），将一些步骤（或称为基本方法）的执行延迟到其子类中；
 * Tips：基本方法：抽象方法 + 具体方法final + 钩子方法；
 * Tips：Enum 时最好的单例方案；枚举单例会初始化全部实现，此处改为托管Class，避免无效的实例化；
 *
 * @author xuxueli 2015-10-30 21:02:55
 */
public abstract class AbstractSerializer {


    /**
     * 序列化
     *
     * @param obj
     * @param <T>
     * @return
     */
    public abstract <T> byte[] serialize(T obj);

    /**
     * 反序列化
     *
     * @param bytes
     * @param clazz
     * @param <T>
     * @return
     */
    public abstract <T> Object deserialize(byte[] bytes, Class<T> clazz);


}
