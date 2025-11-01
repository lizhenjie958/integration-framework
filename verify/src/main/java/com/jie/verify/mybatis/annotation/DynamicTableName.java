package com.jie.verify.mybatis.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author lizhenjie
 * @date 7/7/23 2:49 下午
 * 动态表名
 * 如果你的是自己实现的分表，可以使用它使表名变成动态的
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface DynamicTableName {
    /**
     * 原始表名
     *
     * @return
     */
    String originName();
}
