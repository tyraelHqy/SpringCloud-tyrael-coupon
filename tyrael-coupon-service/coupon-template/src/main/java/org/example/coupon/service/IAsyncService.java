package org.example.coupon.service;

import org.example.coupon.entity.CouponTemplate;

/**
 * 异步服务接口定义
 */
public interface IAsyncService {

    /**
     * 根据模板异步地创建优惠券码
     * @param template
     */
    void asyncConstructCouponByTemplate(CouponTemplate template);

}
