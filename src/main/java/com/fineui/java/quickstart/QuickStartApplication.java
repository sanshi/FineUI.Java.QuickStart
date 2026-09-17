package com.fineui.java.quickstart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * FineUI.Java 快速入门（电影管理 CRUD）启动类。
 *
 * <p>只引入 fineui-java 一个库，方言 / 回发端点 / 页面路由扫描 / 内嵌 F.js 运行时全部经自动配置装配，
 * 业务侧零 {@code @Configuration}。{@code @FineUIPage} 页面类在本包下，由 starter 扫描器登记路由；
 * 数据访问用 Spring Data JPA + H2。
 */
@SpringBootApplication
public class QuickStartApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuickStartApplication.class, args);
    }
}
