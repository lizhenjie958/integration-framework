package com.jie.verify.spring.postprocessor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * @author: lizhenjie
 * @date:2025/4/10
 */
@Component
@Slf4j
public class VerifyBeanPostProcessor implements BeanPostProcessor {
    /**
     * bean 初始化前置处理
     * @param bean the new bean instance
     * @param beanName the name of the bean
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        log.info("postProcessBeforeInitialization begin bean:{}", beanName);
        return bean;
    }

    /**
     * bean初始化后置处理
     * @param bean the new bean instance
     * @param beanName the name of the bean
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        log.info("postProcessAfterInitialization begin bean:{}", beanName);
        return bean;
    }
}
