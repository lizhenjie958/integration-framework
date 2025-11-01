package com.jie.verify.mybatis.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author lizhenjie
 * @date 7/15/23 8:07 下午
 * @see EnableSensitiveDataAutoTransfer
 * 首先mapper上标识了EnableSensitiveDataAutoTransfer
 * 当执行sql语句，将会自动将其自动转换
 * 入库时直接改为加密数据
 * 查询时直接将加密数据解析出来
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
public @interface SensitiveField {
}
