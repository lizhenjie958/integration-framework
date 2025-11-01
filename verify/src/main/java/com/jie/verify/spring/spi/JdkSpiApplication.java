package com.jie.verify.spring.spi;

import java.util.ServiceLoader;

/**
 * @author: lizhenjie
 * @date:2025/3/20
 */
public class JdkSpiApplication {
    public static void main(String[] args) {
        ServiceLoader.load(DemoDao.class).forEach(System.out::println);
    }
}
