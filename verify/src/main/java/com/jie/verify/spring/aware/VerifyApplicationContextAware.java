package com.jie.verify.spring.aware;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author: lizhenjie
 * @date:2025/4/10
 */
@Component
@Slf4j
public class VerifyApplicationContextAware implements ApplicationContextAware {
    private ApplicationContext applicationContext;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        log.info("VerifyApplicationContextAware setApplicationContext ...");
    }
}
