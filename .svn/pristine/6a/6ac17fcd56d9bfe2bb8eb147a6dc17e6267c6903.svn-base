package com.jiahehongye.robred.utils;


import java.io.IOException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import Decoder.BASE64Decoder;
import Decoder.BASE64Encoder;

/**
 * Created by huangjunhui on 2016/12/16.10:01
 */
public class DesUtil {
    private final static String DES = "DES";

    //des加解密key，测试阶段可以更改，生产环境下请勿更改此值--730216cb6cb04fcb96a8a8286ee9adc8
    private final static String ENCRYPT_KEY = "730216cb6cb04fcb96a8a8286ee9adc8";


    /**
     * Description 根据键值进行加密
     * @param data
     * @param key  加密键byte数组
     * @return
     * @throws Exception
     */
    private static byte[] encrypt(byte[] data, byte[] key) throws Exception {

        //生成可信任的随机源
        SecureRandom sr = new SecureRandom();

        //从原始数据创建desckeyspec对象
        DESKeySpec dks = new DESKeySpec(key);

        //创建一个密钥工厂，然后用它把DESKeySpce转化成SecertKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey secretKey = keyFactory.generateSecret(dks);

        //用cipher完成实际加密操作
        Cipher cipher = Cipher.getInstance(DES);

        //用密钥初始化cipher对象
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, sr);

        return cipher.doFinal(data);
    }

    /**
     * Description 根据键值进行解密
     * @param data
     * @param key  加密键byte数组
     * @return
     * @throws Exception
     */
    private static byte[] decrypt(byte[] data, byte[] key) throws Exception {
        // 生成一个可信任的随机数源
        SecureRandom sr = new SecureRandom();

        // 从原始密钥数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);

        // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey securekey = keyFactory.generateSecret(dks);

        // Cipher对象实际完成解密操作
        Cipher cipher = Cipher.getInstance(DES);

        // 用密钥初始化Cipher对象
        cipher.init(Cipher.DECRYPT_MODE, securekey, sr);

        return cipher.doFinal(data);
    }

    /**
     * Description 根据键值进行加密
     * @param data
     * @param key  加密键byte数组
     * @return
     * @throws Exception
     */
    public static String encrypt(String data, String key){
        try {
            byte[] bt = encrypt(data.getBytes(), key.getBytes());
            return new BASE64Encoder().encode(bt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Description 根据键值进行解密
     * @param data
     * @param key  加密键byte数组
     * @return
     * @throws IOException
     * @throws Exception
     */
    public static String decrypt(String data, String key) {
        try {
            if (data == null)
                return null;
            BASE64Decoder decoder = new BASE64Decoder();
            byte[] buf = decoder.decodeBuffer(data);
            byte[] bt = decrypt(buf,key.getBytes());
            return new String(bt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Description 使用默认键值进行加密
     * @param data
     * @return
     * @throws Exception
     */
    public static String encrypt(String data){
        return encrypt(data, ENCRYPT_KEY);
    }

    /**
     * Description 根据默认键值进行解密
     * @param data
     * @return
     * @throws IOException
     * @throws Exception
     */
    public static String decrypt(String data){
        return decrypt(data, ENCRYPT_KEY);
    }

}
