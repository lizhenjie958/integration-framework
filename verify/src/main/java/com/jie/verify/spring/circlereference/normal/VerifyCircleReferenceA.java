package com.jie.verify.spring.circlereference.normal;

import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author: lizhenjie
 * @date:2025/4/29
 */
@Component
public class VerifyCircleReferenceA {
    @Resource
    private VerifyCircleReferenceB verifyCircleReferenceB;
}
