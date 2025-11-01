package com.jie.verify.mybatis.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author lizhenjie
 * @date 7/16/23 3:36 下午
 * 标识在mapper上，标识该mapper开启敏感数据自动转换功能
 * 映射时将会自动返回已经解密好的数据
 * 注：如果mapper的参数为单个参数，并且不是我们自己构建DO对象，如果你想让这个参数可以自动解密
 * 那么请加入mybaties的@param注解，标识出这个字段的名称
 * 结合SimpleParameterEncrypts注解实现自动解密
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface EnableSensitiveDataAutoTransfer {
}
