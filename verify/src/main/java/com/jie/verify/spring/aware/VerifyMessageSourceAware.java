package com.jie.verify.spring.aware;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.stereotype.Component;

/**
 * @author: lizhenjie
 * @date:2025/4/10
 */
@Component
@Slf4j
public class VerifyMessageSourceAware implements MessageSourceAware {
    private MessageSource messageSource;
    @Override
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
        log.info("VerifyMessageSourceAware setMessageSource ...");
    }
}
