package com.jie.verify.spring.postprocessor;

import com.jie.verify.spring.registry.Computer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.stereotype.Component;

/**
 *
 * @author: lizhenjie
 * @date:2025/4/10
 */
@Component
@Slf4j
public class VerifyBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {
    /**
     * 一般用于beanDefinition的注册，比如注册bean，注册beanPostProcessor等
     * @param registry the bean definition registry used by the application context
     * @throws BeansException
     */
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        log.info("VerifyBeanDefinitionRegistryPostProcessor postProcessBeanDefinitionRegistry ...");
        RootBeanDefinition rootBeanDefinition = new RootBeanDefinition(Computer.class);
        registry.registerBeanDefinition("computer",rootBeanDefinition);
    }

    /**
     * 一般用于BeanDefinition的修改，比如修改bean的scope等
     * @param beanFactory the bean factory used by the application context
     * @throws BeansException
     */
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        log.info("VerifyBeanDefinitionRegistryPostProcessor postProcessBeanFactory ...");
        String[] beanDefinitionNames = beanFactory.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            if ("computer".equals(beanDefinitionName)) {
                beanFactory.getBeanDefinition(beanDefinitionName).setScope("prototype");
            }
        }
    }
}
