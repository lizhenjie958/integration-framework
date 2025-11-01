package com.jie.verify.spring.createbean;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.stereotype.Component;

/**
 * 初始化完成后回调
 * @author: lizhenjie
 * @date:2025/4/25
 */
@Component
@Slf4j
public class VerifySmartInitializingSingleton implements SmartInitializingSingleton {
    @Override
    public void afterSingletonsInstantiated() {
        log.info("VerifySmartInitializingSingleton afterSingletonsInstantiated...");
    }
}
