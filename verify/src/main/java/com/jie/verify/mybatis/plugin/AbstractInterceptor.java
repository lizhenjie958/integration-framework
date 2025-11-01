package com.jie.verify.mybatis.plugin;

import com.jie.verify.mybatis.annotation.SensitiveField;
import io.micrometer.core.instrument.util.StringUtils;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.Security;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lizhenjie
 * @date 7/15/23 3:38 下午
 */
public abstract class AbstractInterceptor implements Interceptor {
    private static final String SM4_KEY = "b71485970c901f7d";
    public static final String SM4_ALGORITHM = "SM4";
    public static final String SM4_ALGORITHM_NAME = "SM4/ECB/PKCS7Padding";
    public static final String BC = "BC";
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static final String BASE_PACKAGE = "com.jie.verify.mybatis.model";

    /**
     * 获取mapper的类对象
     *
     * @param mappedStatement
     * @return mapperClass
     * @throws ClassNotFoundException
     */
    protected Class<?> getMapperClass(MappedStatement mappedStatement) throws ClassNotFoundException {
        String className = mappedStatement.getId();
        className = className.substring(0, className.lastIndexOf("."));
        Class<?> clazz = Class.forName(className);
        return clazz;
    }

    /**
     * 查找mapper的方法
     *
     * @param mappedStatement
     * @return mapperMetchod
     * @throws ClassNotFoundException
     */
    protected Method getMapperMethod(MappedStatement mappedStatement) throws ClassNotFoundException {
        String className = mappedStatement.getId();
        Class<?> clazz = getMapperClass(mappedStatement);
        String methodName = className.substring(className.lastIndexOf(".") + 1);
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.getName().equalsIgnoreCase(methodName)) {
                return method;
            }
        }
        throw new RuntimeException("mapper方法处理异常");
    }

    /**
     * 获取属性上所有敏感字段
     *
     * @param clazzName
     * @return 敏感属性名和描述
     */
    protected Map<String, Field> getSensitiveFiledMap(Class<?> clazzName) {
        Map<String, Field> result = new HashMap<>();
        fillSensitiveFiledMap(result, clazzName);
        Class<?> superclass = clazzName.getSuperclass();
        while (superclass != Object.class && superclass != null) {
            fillSensitiveFiledMap(result, superclass);
            superclass = superclass.getSuperclass();
        }
        return result;
    }

    /**
     * 填充敏感数据的字段名和描述
     *
     * @param result 敏感属性名和描述map
     * @param clazz 数据的实体类
     */
    private void fillSensitiveFiledMap(Map<String, Field> result, Class<?> clazz) {
        for (Field declaredField : clazz.getDeclaredFields()) {
            if (declaredField.isAnnotationPresent(SensitiveField.class)) {
                result.put(declaredField.getName(), declaredField);
            }
        }
    }

    /**
     * 加解密数据转换，仅仅支持本包下的
     *
     * @param clazz 数据实体类所在的包
     * @return 数据实体类所在的包是否在dao包下
     */
    protected boolean isDaoPackage(Class<?> clazz) {
        return clazz.getPackage().getName().startsWith(BASE_PACKAGE);
    }

    /**
     * 加密
     *
     * @param value
     * @return
     */
    protected String doEncrypt(String value) {
        if (StringUtils.isBlank(value)) {
            return value;
        }
        return encrypt(value);
    }

    /**
     * 解密
     *
     * @param value
     * @return
     */
    protected String doDecrypt(String value) {
        if (StringUtils.isBlank(value)) {
            return value;
        }
        return decrypt(value);
    }

    public static String decrypt(String cipherText) {
        try {
            Key key = getKey(SM4_KEY);
            Cipher cipher = Cipher.getInstance(SM4_ALGORITHM_NAME, BC);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] plainText = cipher.doFinal(Base64.getDecoder().decode(cipherText));
            return new String(plainText, DEFAULT_CHARSET);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String encrypt(String plainText) {
        try {
            Key key = getKey(SM4_KEY);
            Cipher cipher = Cipher.getInstance(SM4_ALGORITHM_NAME, BC);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] cipherText = cipher.doFinal(plainText.getBytes(DEFAULT_CHARSET));
            return Base64.getEncoder().encodeToString(cipherText);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Key getKey(String key) throws Exception {
        byte[] keyBytes = key.getBytes(DEFAULT_CHARSET);
        Security.addProvider(new BouncyCastleProvider());
        KeyGenerator keyGenerator = KeyGenerator.getInstance(SM4_ALGORITHM);
        keyGenerator.init(128);
        return new SecretKeySpec(keyBytes, SM4_ALGORITHM);
    }

}
