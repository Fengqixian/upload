package com.clinbrain.bd.mdm.strategy.util;

import cn.hutool.core.util.StrUtil;
import com.clinbrain.bd.mdm.strategy.dto.RegDto;

import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * @Auther: 梁乐乐
 * @Date: 2019/9/18 09:30
 * @Description: base64加解密规则
 */
public class Base64Util {

    /**
     * 利用base64的加密方法加密
     *
     * @param value 加密值
     * @return 加密后的值
     */
    public static String DataToBase64Enc(String value) {
        if (StrUtil.isNotEmpty(value)) {
            return Base64.getEncoder().encodeToString(value.getBytes());
        } else {
            return "";
        }
    }

    /**
     * 基于base64解密值
     *
     * @param value 加密值
     * @return 解密后的值
     */
    public static String Base64ToDataDec(String value) {
        if (StrUtil.isNotEmpty(value)) {
            try {
                return new String(Base64.getDecoder().decode(value), "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return value;
            }
        } else {
            return "";
        }
    }
}
