package org.example.coupon.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.coupon.exception.CouponException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 健康检查接口
 */

@Slf4j
@RestController
public class HealthCheck {

    /**
     * 服务发现客户端
     */
    @Autowired
    private DiscoveryClient client;

    /**
     * 服务注册接口，提供了获取服务id的方法
     */
    @Qualifier("eurekaRegistration")
    @Autowired
    private Registration registration;

    /**
     * 健康检查接口
     * 127.0.0.1:7001/coupon-template/health
     */
    @GetMapping("/health")
    public String health() {
        log.debug("view health api");
        return "CouponTemplate Is OK!";
    }

    /**
     * 异常检查接口
     * 127.0.0.1:7001/coupon-template/exception
     *
     * @return
     * @throws CouponException
     */
    @GetMapping("/exception")
    public String exception() throws CouponException {
        log.debug("view exception api");
        throw new CouponException("CouponTemplate Has Some Problem");
    }

    /**
     * 获取Eureka Server上的微服务元信息
     * 127.0.0.1:7001/coupon-template/info
     * @return
     */
    @GetMapping("/info")
    public List<Map<String, Object>> info() {
        // 大约等待2分钟时间才能够获取到客户的信息
        List<ServiceInstance> instances = client.getInstances(registration.getServiceId());
        List<Map<String, Object>> result = new ArrayList<>(instances.size());

        instances.forEach(i -> {
            Map<String,Object> info = new HashMap<>();
            info.put("serviceId",i.getServiceId());
            info.put("instanceId",i.getInstanceId());
            info.put("port",i.getPort());
            result.add(info);
        });
        return result;
    }
}
