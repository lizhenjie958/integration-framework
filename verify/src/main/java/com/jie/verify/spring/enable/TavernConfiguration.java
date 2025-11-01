package com.jie.verify.spring.enable;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;

/**
 * @author: lizhenjie
 * @date:2025/3/20
 */
@Configuration
@EnableTavern
public class TavernConfiguration {

    public static void main(String[] args) {
        // AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(TavernConfiguration.class);
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.getEnvironment().setActiveProfiles("city");
        context.register(TavernConfiguration.class);
        context.refresh();


        for (String beanDefinitionName : context.getBeanDefinitionNames()) {
            System.err.println(beanDefinitionName);
        }
        System.err.println("---------------------");
        context.getBeansOfType(Bartender.class).forEach((k,v)-> System.err.println(k + "\t" + v));
    }
}
