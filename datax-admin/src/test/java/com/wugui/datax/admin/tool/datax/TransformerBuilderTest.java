package com.wugui.datax.admin.tool.datax;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class TransformerBuilderTest {
    public static String getMd5(String md5str) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(md5str.getBytes());
            StringBuffer stringBuffer = new StringBuffer();
            for (byte b : bytes) {
                int bt = b & 0xff;
                if (bt < 16) {
                    stringBuffer.append(0);
                }
                stringBuffer.append(Integer.toHexString(bt));
            }
            md5str = stringBuffer.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }


        return md5str;

    }

    public static void main(String[] args) {
        //5d41402abc4b2a76b9719d911017c592
        //5d41402abc4b2a76b9719d911017c592
        System.out.println(getMd5("hello"));
    }
}
