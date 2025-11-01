package com.jie.verify.spring.listener.app;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartingEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @author: lizhenjie
 * @date:2025/4/1
 */
@Slf4j
@Component
public class VerifyStartingApplicationListener implements ApplicationListener<ApplicationStartingEvent> {
    @Override
    public void onApplicationEvent(ApplicationStartingEvent event) {
        // bean加载完毕后，才会有此监听，so，这个日志不会打印
        log.info("VerifyStartApplicationListener onApplicationEvent...");
    }
}
