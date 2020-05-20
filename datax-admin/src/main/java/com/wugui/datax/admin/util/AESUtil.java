package com.wugui.datax.admin.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;

@Slf4j
public class AESUtil {
    // 密钥
    public static String key = "AD42F6697B035B7580E4FEF93BE20BAD";
    private static String charset = "utf-8";
    // 偏移量
    private static int offset = 16;
    private static String transformation = "AES/CBC/PKCS5Padding";
    private static String algorithm = "AES";

    /**
     * 加密
     *
     * @param content
     * @return
     */
    public static String encrypt(String content) {
        return encrypt(content, key);
    }

    /**
     * 解密
     *
     * @param content
     * @return
     */
    public static String decrypt(String content) {
        return decrypt(content, key);
    }

    /**
     * 加密
     *
     * @param content 需要加密的内容
     * @param key     加密密码
     * @return
     */
    public static String encrypt(String content, String key) {
        try {
            if(StringUtils.isNotBlank(content)) {
                SecretKeySpec skey = new SecretKeySpec(key.getBytes(), algorithm);
                IvParameterSpec iv = new IvParameterSpec(key.getBytes(), 0, offset);
                Cipher cipher = Cipher.getInstance(transformation);
                byte[] byteContent = content.getBytes(charset);
                cipher.init(Cipher.ENCRYPT_MODE, skey, iv);// 初始化
                byte[] result = cipher.doFinal(byteContent);
                return new Base64().encodeToString(result); // 加密
            }
        } catch (Exception e) {
            log.warn("content encrypt error {}",e.getMessage());
        }
        return null;
    }

    /**
     * AES（256）解密
     *
     * @param content 待解密内容
     * @param key     解密密钥
     * @return 解密之后
     * @throws Exception
     */
    public static String decrypt(String content, String key) {
        try {
            if(StringUtils.isNotBlank(content)){
                SecretKeySpec skey = new SecretKeySpec(key.getBytes(), algorithm);
                IvParameterSpec iv = new IvParameterSpec(key.getBytes(), 0, offset);
                Cipher cipher = Cipher.getInstance(transformation);
                cipher.init(Cipher.DECRYPT_MODE, skey, iv);// 初始化
                byte[] result = cipher.doFinal(new Base64().decode(content));
                return new String(result); // 解密
            }
        } catch (Exception e) {
            log.warn("content decrypt error {}",e.getMessage());
        }
        return content;
    }

    public static void main(String[] args) throws Exception {
        String s = "root";
        String encryptResultStr = encrypt(s);
        System.out.println(s + " 加密后 ：" + encryptResultStr);
        System.out.println("mysql" + " 加密后 ：" + encrypt("mysql"));
        System.out.println("解密后：" + decrypt(encryptResultStr));
    }

    /**
     * 将16进制转换为二进制
     *
     * @param hexStr
     * @return
     */
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1)
            return null;
        byte[] result = new byte[hexStr.length() / 2];
        try {
            for (int i = 0; i < hexStr.length() / 2; i++) {
                int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
                int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
                result[i] = (byte) (high * 16 + low);
            }
            return result;
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将二进制转换成16进制
     *
     * @param buf
     * @return
     */
    public static String parseByte2HexStr(byte[] buf) {
        StringBuilder sb = new StringBuilder();
        for (byte b : buf) {
            String hex = Integer.toHexString(b & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }
}