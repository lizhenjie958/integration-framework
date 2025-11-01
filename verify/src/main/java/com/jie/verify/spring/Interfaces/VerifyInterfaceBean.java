package com.jie.verify.spring.Interfaces;

import org.springframework.context.annotation.Bean;

/**
 * @author: lizhenjie
 * @date:2025/4/17
 */
public interface VerifyInterfaceBean {
    @Bean
    default InterfaceBean interfaceBean(){
        return new InterfaceBean();
    };
}
