package com.jie.verify.spring.definition;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author: lizhenjie
 * @date:2025/3/24
 */
public class AnnotationConfigBeanDefinitionConfiguration {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(BeanDefinitionConfiguration.class);
        BeanDefinition person = context.getBeanDefinition("person");
        System.err.println(person);
        System.err.println(person.getClass().getName());
    }
}
