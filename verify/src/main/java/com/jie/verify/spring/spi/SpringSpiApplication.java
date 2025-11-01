package com.jie.verify.spring.spi;

import org.springframework.core.io.support.SpringFactoriesLoader;

import java.util.List;

/**
 * @author: lizhenjie
 * @date:2025/3/20
 */
public class SpringSpiApplication {
    public static void main(String[] args) {
        List<DemoDao> demoDaos = SpringFactoriesLoader.loadFactories(DemoDao.class, SpringSpiApplication.class.getClassLoader());
        System.err.println(demoDaos);
    }
}
