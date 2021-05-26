package org.example.coupon.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.coupon.dao.CouponTemplateDao;
import org.example.coupon.entity.CouponTemplate;
import org.example.coupon.exception.CouponException;
import org.example.coupon.service.ITemplateBaseService;
import org.example.coupon.vo.CouponTemplateSDK;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 优惠券模板基础服务接口实现
 */

@Service
@Slf4j
public class TemplateBaseServiceImpl implements ITemplateBaseService {

    private final CouponTemplateDao couponTemplateDao;

    @Autowired
    public TemplateBaseServiceImpl(CouponTemplateDao couponTemplateDao) {
        this.couponTemplateDao = couponTemplateDao;
    }

    /**
     * 根据优惠券的模板ID获取优惠券模板信息
     *
     * @param id
     * @return
     * @throws CouponException
     */
    @Override
    public CouponTemplate buildTemplateInfo(Integer id) throws CouponException {
        Optional<CouponTemplate> template = couponTemplateDao.findById(id);
        if (!template.isPresent()) {
            throw new CouponException("Template is Not Exist: " + id);
        }
        return template.get();
    }

    @Override
    public List<CouponTemplateSDK> findAllUsableTemplate() {
        List<CouponTemplate> templates = couponTemplateDao.findAllByAvailableAndExpired(true, false);

        return templates.stream().map(this::template2TemplateSDK).collect(Collectors.toList());
    }

    @Override
    public Map<Integer, CouponTemplateSDK> findIds2TemplateSDK(Collection<Integer> ids) {
        List<CouponTemplate> templates = couponTemplateDao.findAllById(ids);

        return templates.stream().map(this::template2TemplateSDK).collect(Collectors.toMap(
                CouponTemplateSDK::getId, Function.identity()
        ));
    }

    /**
     * 将CouponTemplate转换为CouponTemplateSDK
     *
     * @param template
     * @return
     */
    private CouponTemplateSDK template2TemplateSDK(CouponTemplate template) {
        return new CouponTemplateSDK(
                template.getId(),
                template.getName(),
                template.getLogo(),
                template.getDesc(),
                template.getCategory().getCode(),
                template.getProductLine().getCode(),
                template.getKey(),  // 并不是拼装好的 Template Key
                template.getTarget().getCode(),
                template.getRule()
        );
    }
}
