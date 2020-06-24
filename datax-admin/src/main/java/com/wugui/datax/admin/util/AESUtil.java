package com.wugui.datax.admin.util;

import com.wugui.datax.admin.core.conf.JobAdminConfig;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

@Slf4j
public class AESUtil {

    private static String DEFAULT_CIPHER_ALGORITHM = "SHA1PRNG";
    private static String KEY_ALGORITHM = "AES";

    /**
     * 加密
     *
     * @param key
     * @param messBytes
     * @return
     */
    private static byte[] encrypt(Key key, byte[] messBytes) throws Exception {
        if (key != null) {

            Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return cipher.doFinal(messBytes);
        }
        return null;
    }

    /**
     * AES（256）解密
     *
     * @param key
     * @param cipherBytes
     * @return
     */
    private static byte[] decrypt(Key key, byte[] cipherBytes) throws Exception {
        if (key != null) {

            Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);
            return cipher.doFinal(cipherBytes);
        }
        return null;
    }


    /**
     * 生成加密秘钥
     *
     * @return
     * @throws NoSuchAlgorithmException
     */
    private static KeyGenerator getKeyGenerator() {

        String key = JobAdminConfig.getAdminConfig().getDataSourceAESKey();

        KeyGenerator keygen = null;
        try {
            keygen = KeyGenerator.getInstance(KEY_ALGORITHM);
            SecureRandom secureRandom = SecureRandom.getInstance(DEFAULT_CIPHER_ALGORITHM);
            secureRandom.setSeed(key.getBytes());
            keygen.init(128, secureRandom);
        } catch (NoSuchAlgorithmException e) {
            log.warn("Get key generator error {}", e.getMessage());
        }

        return keygen;
    }

    public static String encrypt(String message) {
        try {
            KeyGenerator keygen = getKeyGenerator();
            SecretKey secretKey = new SecretKeySpec(keygen.generateKey().getEncoded(), KEY_ALGORITHM);
            return Base64.getEncoder().encodeToString(encrypt(secretKey, message.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            log.warn("content encrypt error {}", e.getMessage());
        }
        return null;
    }

    public static String decrypt(String ciphertext) {
        try {
            KeyGenerator keygen = getKeyGenerator();
            SecretKey secretKey = new SecretKeySpec(keygen.generateKey().getEncoded(), KEY_ALGORITHM);
            return new String(decrypt(secretKey, Base64.getDecoder().decode(ciphertext)), StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.warn("content decrypt error {}", e.getMessage());
        }
        return null;
    }

    public static void main(String[] args) {
        String message = "root";
        String ciphertext = encrypt(message);

        System.out.println("加密后密文为: " + ciphertext);
        System.out.println("解密后明文为:" + decrypt(ciphertext));
    }

}