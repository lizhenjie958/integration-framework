package com.jie.verify.spring.aware;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * @author: lizhenjie
 * @date:2025/4/10
 */
@Slf4j
@Component
public class VerifyEnvironmentAware implements EnvironmentAware {
    private Environment environment;
    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
        log.info("VerifyEnvironmentAware setEnvironment ...");
    }
}
