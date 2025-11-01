package com.jie.verify.spring.listener.app;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @author: lizhenjie
 * @date:2025/4/1
 */
@Slf4j
@Component
public class VerifyStartedApplicationListener implements ApplicationListener<ApplicationStartedEvent> {

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        log.info("VerifyStartedApplicationListener onApplicationEvent...");
    }
}
