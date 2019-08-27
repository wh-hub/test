package com.kgc.springboot_1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @SpringBootApplication是一个复合注解
 *
 * @SpringBootConfiguration:  springboot文件配置信息类
 *    @Configuration:配置文件注解,表示修饰的类 是一个IoC中的配置文件类
 * @EnableAutoConfiguration:借助@Import的帮助，将所有符合自动配置条件的bean定义加载到IoC容器
 * @ComponentScan:功能其实就是自动扫描并加载符合条件的组件,自动扫描同级或者子目录中的注解信息
 */
@SpringBootApplication
public class Springboot1Application {

    public static void main(String[] args) {
        SpringApplication.run(Springboot1Application.class, args);
    }

}
