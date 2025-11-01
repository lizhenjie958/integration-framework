package com.jie.verify.spring.initializer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author: lizhenjie
 * @date:2025/4/8
 */
@Slf4j
public class FirstVerifyContextInitializer implements ApplicationContextInitializer {
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        log.info("FirstVerifyContextInitializer initialize .....");
    }
}
