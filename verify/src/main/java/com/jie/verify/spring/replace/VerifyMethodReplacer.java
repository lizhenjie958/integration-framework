package com.jie.verify.spring.replace;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.MethodReplacer;

import java.lang.reflect.Method;

/**
 * @author: lizhenjie
 * @date:2025/4/28
 */
@Slf4j
public class VerifyMethodReplacer implements MethodReplacer {
    @Override
    public Object reimplement(Object obj, Method method, Object[] args) throws Throwable {
        log.info("VerifyMethodReplacer reimplement...");
        return new VerifyMethodReplacer();
    }
}
