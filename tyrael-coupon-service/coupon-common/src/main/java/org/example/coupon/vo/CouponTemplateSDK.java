package org.example.coupon.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 微服务之间用的优惠券模板信息定义
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CouponTemplateSDK {

    /**
     * 优惠券模板主键
     */
    private Integer id;

    /**
     * 优惠券模板名称
     */
    private String name;

    /**
     * 优惠券模板logo
     */
    private String logo;

    /**
     * 优惠券模板描述
     */
    private String desc;

    /**
     * 优惠券模板分类
     */
    private String category;

    /**
     * 优惠券模板产品线
     */
    private Integer productLine;

    /**
     * 优惠券模板的编码
     */
    private String key;

    /**
     * 目标用户
     */
    private Integer target;

    /**
     * 优惠券规则
     */
    private TemplateRule rule;
}
