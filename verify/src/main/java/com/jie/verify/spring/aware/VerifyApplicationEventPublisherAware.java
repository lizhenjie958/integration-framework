package com.jie.verify.spring.aware;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;

/**
 * @author: lizhenjie
 * @date:2025/4/10
 */
@Component
@Slf4j
public class VerifyApplicationEventPublisherAware implements ApplicationEventPublisherAware {
    private ApplicationEventPublisher applicationEventPublisher;
    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
        log.info("VerifyApplicationEventPublisherAware setApplicationEventPublisher ...");
    }
}
