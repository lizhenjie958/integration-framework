package com.jie.verify.spring.enable;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: lizhenjie
 * @date:2025/3/20
 */
@Configuration
public class BarConfiguration {

    @Bean
    public Bar bar(){
        return new Bar();
    }
}
