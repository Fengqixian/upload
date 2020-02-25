package com.clinbrain.bd.mdm.strategy.util;

/**
 * @Auther: 梁乐乐
 * @Date: 2019/10/13 11:12
 * @Description:
 */
public class EncryUtil {
    /**
     * 使用默认密钥进行DES加密
     */
    public static String encrypt(String plainText) {
        try {
            return new DESUtil().encrypt(plainText);
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * 使用指定密钥进行DES解密
     */
    public static String encrypt(String plainText, String key) {
        try {

            return new DESUtil(key).encrypt(plainText);
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * 使用默认密钥进行DES解密
     */
    public static String decrypt(String plainText) {
        try {
            return new DESUtil().decrypt(plainText);
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * 使用指定密钥进行DES解密
     */
    public static String decrypt(String plainText, String key) {
        try {
            return new DESUtil(key).decrypt(plainText);
        } catch (Exception e) {
            return null;
        }
    }

    public static void main(String[] args) {
        String str = "01234ABCDabcd!@#$";
        String t = "";
        System.out.println("加密后：" + (t = EncryUtil.encrypt(str, "ASDASDF")));
        System.out.println("解密后：" + EncryUtil.decrypt(t,"ASDASDF"));
    }
}
