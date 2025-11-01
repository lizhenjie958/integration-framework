package com.jie.verify.spring.definition;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: lizhenjie
 * @date:2025/3/24
 */
@Configuration
public class BeanDefinitionConfiguration {

    @Bean
    public Person person() {
        return new Person();
    }
}
