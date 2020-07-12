package com.wugui.datax.rpc.test;

import com.wugui.datax.rpc.serialize.Serializer;
import com.wugui.datax.rpc.serialize.impl.HessianSerializer;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xuxueli 2015-10-30 21:02:55
 */
public class SerializerTest {

    public static void main(String[] args) throws IllegalAccessException, InstantiationException {
        Serializer serializer = HessianSerializer.class.newInstance();
        System.out.println(serializer);
        try {
            Map<String, String> map = new HashMap<String, String>();
            map.put("aaa", "111");
            map.put("bbb", "222");
            System.out.println(serializer.deserialize(serializer.serialize("ddddddd"), String.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
