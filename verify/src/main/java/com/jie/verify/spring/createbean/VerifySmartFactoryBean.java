package com.jie.verify.spring.createbean;

import org.springframework.beans.factory.SmartFactoryBean;
import org.springframework.stereotype.Component;

/**
 * @author: lizhenjie
 * @date:2025/4/27
 */
@Component
public class VerifySmartFactoryBean implements SmartFactoryBean {
    @Override
    public Object getObject() throws Exception {
        return null;
    }

    @Override
    public Class<?> getObjectType() {
        return null;
    }

    @Override
    public boolean isEagerInit() {
        // 标识非懒加载
        return true;
    }
}
