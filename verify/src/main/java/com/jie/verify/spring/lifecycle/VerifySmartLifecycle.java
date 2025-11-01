package com.jie.verify.spring.lifecycle;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;

/**
 *
 * @author: lizhenjie
 * @date:2025/4/1
 */
@Slf4j
@Component
public class VerifySmartLifecycle implements SmartLifecycle {
    @Override
    public void start() {
        log.info("VerifySmartLifecycle start...");
    }

    @Override
    public void stop() {
        log.info("VerifySmartLifecycle stop...");
    }

    @Override
    public boolean isRunning() {
        log.info("VerifySmartLifecycle isRunning...");
        return true;
    }
}
