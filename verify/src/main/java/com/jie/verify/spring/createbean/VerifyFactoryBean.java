package com.jie.verify.spring.createbean;

import org.springframework.beans.factory.SmartFactoryBean;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 *  configurableApplicationContext.getBean("verifyFactoryBean")  获取Bean
 *  configurableApplicationContext.getBean("&verifyFactoryBean"); 获取Bean的工厂Bean
 * @author: lizhenjie
 * @date:2025/4/25
 */
@Component
public class VerifyFactoryBean implements SmartFactoryBean<VerifyFactoryBeanCreate> {
    @Override
    public VerifyFactoryBeanCreate getObject() throws Exception {
        return new VerifyFactoryBeanCreate(LocalDateTime.now());
    }

    @Override
    public Class<?> getObjectType() {
        return VerifyFactoryBeanCreate.class;
    }

//    @Override
//    public boolean isSingleton() {
//        return FactoryBean.super.isSingleton();
//    }


    @Override
    public boolean isEagerInit() {
        return true;
    }
}
