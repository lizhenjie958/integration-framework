package com.jie.verify;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan(basePackages = "com.jie")
@EnableAspectJAutoProxy
public class VerifyApplication {

    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext context = startMethodOne(args);
    }

    private static ConfigurableApplicationContext startMethodOne(String[] args){
        ConfigurableApplicationContext run = SpringApplication.run(VerifyApplication.class, "--spring.main.banner-mode=off");
        return run;
    }

    private static ConfigurableApplicationContext startMethodTwo(String[] args){
        SpringApplication springApplication = new SpringApplication(VerifyApplication.class);
        springApplication.setBannerMode(Banner.Mode.OFF);
        ConfigurableApplicationContext run = springApplication.run(args);
        return run;
    }

    private static ConfigurableApplicationContext startMethodThree(String[] args){
        ConfigurableApplicationContext run = new SpringApplicationBuilder(VerifyApplication.class)
                .bannerMode(Banner.Mode.OFF)
                .listeners()
                .run(args);
        return run;
    }
}
