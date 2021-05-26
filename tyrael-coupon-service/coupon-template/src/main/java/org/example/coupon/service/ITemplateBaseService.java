package org.example.coupon.service;

import org.example.coupon.entity.CouponTemplate;
import org.example.coupon.exception.CouponException;
import org.example.coupon.vo.CouponTemplateSDK;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 优惠券模板基础（view，delete ... ）服务定义
 */
public interface ITemplateBaseService {

    /**
     * 根据优惠券模板 id 获取优惠券模板信息
     * @param id
     * @return
     * @throws CouponException
     */
    CouponTemplate buildTemplateInfo(Integer id) throws CouponException;

    /**
     * 查找所有可用的优惠券模板
     * @return
     */
    List<CouponTemplateSDK> findAllUsableTemplate();

    /**
     * 获取模板 ids 到 CouponTemplateSDK的映射
     * @param ids
     * @return Map<key: 模板 id,value CouponTemplateSDK>
     */
    Map<Integer,CouponTemplateSDK> findIds2TemplateSDK(Collection<Integer> ids);

}
