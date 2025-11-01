package com.jie.verify.spring.enable;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * @author: lizhenjie
 * @date:2025/3/20
 */
@Configuration
@Profile("city")
public class BartenderConfiguration {

    @Bean
    public Bartender smallBartender() {
        Bartender bartender = new Bartender("张小三");
        return bartender;
    }

    @Bean
    public Bartender bigBartender() {
        Bartender bartender = new Bartender("张大三");
        return bartender;
    }
}
