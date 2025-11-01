package com.jie.verify.spring.enable;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author: lizhenjie
 * @date:2025/3/20
 */
@Slf4j
public class WaiterRegistrar implements ImportBeanDefinitionRegistrar {
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        log.info("WaiterRegistrar registerBeanDefinitions invoke...");
        registry.registerBeanDefinition("waiter",new RootBeanDefinition(Waiter.class));
    }
}
