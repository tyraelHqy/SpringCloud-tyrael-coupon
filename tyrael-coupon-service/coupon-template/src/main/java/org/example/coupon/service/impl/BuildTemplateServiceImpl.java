package org.example.coupon.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.coupon.dao.CouponTemplateDao;
import org.example.coupon.entity.CouponTemplate;
import org.example.coupon.exception.CouponException;
import org.example.coupon.service.IAsyncService;
import org.example.coupon.service.IBuildTemplateService;
import org.example.coupon.vo.TemplateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 构建优惠券模板接口实现
 */
@Slf4j
@Service
public class BuildTemplateServiceImpl implements IBuildTemplateService {

    /**
     * 异步服务
     */
    private final IAsyncService asyncService;

    /**
     * CouponTemplate Dao
     */
    private final CouponTemplateDao templateDao;

    @Autowired
    public BuildTemplateServiceImpl(IAsyncService asyncService, CouponTemplateDao templateDao) {
        this.asyncService = asyncService;
        this.templateDao = templateDao;
    }

    @Override
    public CouponTemplate buildTemplate(TemplateRequest request) throws CouponException {

        // 参数合法性校验
        if(!request.validate()){
            throw new CouponException("BuildTemplate Param is Not Valid");
        }

        // 判断同名的数据库是否存在
        if(null != templateDao.findByName(request.getName())){
            throw new CouponException("Exist Same Name Template");
        }

        // 构造CouponTemplate 并且保存到数据库中
        CouponTemplate template = requestToTemplate(request);
        template = templateDao.save(template);

        // 根据优惠券模板异步生成优惠券码
        asyncService.asyncConstructCouponByTemplate(template);
        return template;
    }

    /**
     * 将TemplateRequest 转换为 CouponTemplate
     * @return
     */
    private CouponTemplate requestToTemplate(TemplateRequest request){
        return new CouponTemplate(
                request.getName(),
                request.getLogo(),
                request.getDesc(),
                request.getCategory(),
                request.getProductLine(),
                request.getCount(),
                request.getUserId(),
                request.getTarget(),
                request.getRule()
        );
    }
}
