package com.jie.verify.spring.aware;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.stereotype.Component;
import org.springframework.util.StringValueResolver;

/**
 * @author: lizhenjie
 * @date:2025/4/10
 */
@Component
@Slf4j
public class VerifyEmbeddedValueResolverAware implements EmbeddedValueResolverAware {
    private StringValueResolver stringValueResolver;
    @Override
    public void setEmbeddedValueResolver(StringValueResolver resolver) {
        this.stringValueResolver = resolver;
        log.info("VerifyEmbeddedValueResolverAware setEmbeddedValueResolver ...");
    }
}
