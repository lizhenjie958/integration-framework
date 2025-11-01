package com.jie.verify.spring.listener.apprun;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * 这是springboot的listener能力
 * @author: lizhenjie
 * @date:2025/4/1
 */
@Slf4j
public class VerifySpringApplicationRunListener implements SpringApplicationRunListener {
    private static long startTime;

    // 必须的构造函数（Spring 反射创建实例）
    public VerifySpringApplicationRunListener(SpringApplication application, String[] args) {
    }

    @Override
    public void starting() {
        startTime = System.currentTimeMillis();
        // 应用启动最早阶段，此时日志系统尚未初始化。
        log.info("VerifySpringApplicationRunListener starting...");
        System.err.println("VerifySpringApplicationRunListener starting...");
    }

    @Override
    public void environmentPrepared(ConfigurableEnvironment environment) {
        log.info("VerifySpringApplicationRunListener environmentPrepared...");
    }

    @Override
    public void contextPrepared(ConfigurableApplicationContext context) {
        log.info("VerifySpringApplicationRunListener contextPrepared...");
    }

    @Override
    public void contextLoaded(ConfigurableApplicationContext context) {
        log.info("VerifySpringApplicationRunListener contextLoaded...");
    }

    @Override
    public void started(ConfigurableApplicationContext context) {
        log.info("VerifySpringApplicationRunListener started...");
    }

    @Override
    public void running(ConfigurableApplicationContext context) {
        long l = System.currentTimeMillis() - startTime;
        log.info("VerifySpringApplicationRunListener running...");
        log.info("SpringApplication，启动耗时：" + l + "ms");
    }

    @Override
    public void failed(ConfigurableApplicationContext context, Throwable exception) {
        log.info("VerifySpringApplicationRunListener failed...");
    }
}
