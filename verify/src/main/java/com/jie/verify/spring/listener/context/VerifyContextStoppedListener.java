package com.jie.verify.spring.listener.context;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.stereotype.Component;

/**
 * @author: lizhenjie
 * @date:2025/4/13
 */
@Component
@Slf4j
public class VerifyContextStoppedListener implements ApplicationListener<ContextStoppedEvent> {
    @Override
    public void onApplicationEvent(ContextStoppedEvent event) {
        log.info("VerifyContextStoppedListener onApplicationEvent ...");
    }
}
