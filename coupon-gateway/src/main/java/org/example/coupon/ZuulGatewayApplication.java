package org.example.coupon;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * 网关应用启动入口
 * 1. @EnableZuulProxy 标识当前的应用时 Zuul Server
 * 2. @SpringCloudApplication 组合了当前的 SpringBoot 应用 + 服务发现 + 熔断
 */

@EnableZuulProxy
@SpringCloudApplication
public class ZuulGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(ZuulGatewayApplication.class,args);
    }
}
