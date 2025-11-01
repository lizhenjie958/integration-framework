package com.jie.verify.spring.ioc;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.ClassPathResource;

import java.util.Arrays;

/**
 * @author: lizhenjie
 * @date:2025/3/21
 */
public class ListableBeanFactoryApplication {
    public static void main(String[] args) {
        ClassPathResource classPathResource = new ClassPathResource("bean.xml");
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
        reader.loadBeanDefinitions(classPathResource);
        System.out.println("加载XML文件后的容器中的bean如下：");
        Arrays.stream(beanFactory.getBeanDefinitionNames()).forEach(System.out::println);

        beanFactory.registerSingleton("dog", new Dog());
        System.out.println("手动注册单实例Bean后容器中所有的bean如下：");
        // ListableBeanFactory不会把手动注册的bean列举出来
        Arrays.stream(beanFactory.getBeanDefinitionNames()).forEach(System.out::println);
    }
}
