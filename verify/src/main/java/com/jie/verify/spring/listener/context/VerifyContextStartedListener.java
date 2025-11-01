package com.jie.verify.spring.listener.context;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.stereotype.Component;

/**
 * @author: lizhenjie
 * @date:2025/4/13
 */
@Component
@Slf4j
public class VerifyContextStartedListener implements ApplicationListener<ContextStartedEvent> {
    @Override
    public void onApplicationEvent(ContextStartedEvent event) {
        log.info("VerifyContextStartedListener onApplicationEvent...");
    }
}
