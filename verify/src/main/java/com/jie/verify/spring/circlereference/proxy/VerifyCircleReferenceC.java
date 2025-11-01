package com.jie.verify.spring.circlereference.proxy;

import com.jie.verify.spring.aop.VerifyAop;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author: lizhenjie
 * @date:2025/5/6
 */
@Slf4j
@Component
public class VerifyCircleReferenceC {
    @Resource
    private VerifyCircleReferenceD verifyCircleReferenceD;

    @VerifyAop
    public void test() {
        log.info("VerifyCircleReferenceC test");
    }
}
