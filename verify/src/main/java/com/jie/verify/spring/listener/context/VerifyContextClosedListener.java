package com.jie.verify.spring.listener.context;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

/**
 * 监听容器关闭
 * @author: lizhenjie
 * @date:2025/4/1
 */
@Slf4j
@Component
public class VerifyContextClosedListener implements ApplicationListener<ContextClosedEvent> {
    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        log.info("VerifyContextClosedListener onApplicationEvent...");
    }
}
