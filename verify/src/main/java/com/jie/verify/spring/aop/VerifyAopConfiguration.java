package com.jie.verify.spring.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @author: lizhenjie
 * @date:2025/4/28
 */
@Aspect
@Component
public class VerifyAopConfiguration {
    @Pointcut("@annotation(com.jie.verify.spring.aop.VerifyAop)")
    public void verifyAop() {
    }

    @Around("verifyAop()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("before verifyAop");
        Object proceed = joinPoint.proceed();
        System.out.println("after verifyAop");
        return proceed;
    }
}
