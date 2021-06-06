package org.example.coupon.schedule;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.example.coupon.dao.CouponTemplateDao;
import org.example.coupon.entity.CouponTemplate;
import org.example.coupon.vo.TemplateRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 定时清理已过期的优惠券模板
 */
@Slf4j
@Component
public class ScheduleTask {

    private final CouponTemplateDao templateDao;

    @Autowired
    public ScheduleTask(CouponTemplateDao templateDao) {
        this.templateDao = templateDao;
    }

    /**
     * 下线已过期的优惠券模板
     */
    @Scheduled(fixedRate = 60*60*1000)
    public void offlineCouponTemplate(){
        log.info("Start to Expire CouponTemplate");
        List<CouponTemplate> templates = templateDao.findAllByExpired(false);
        if (CollectionUtils.isEmpty(templates)){
            log.info("Done to Expire CouponTemplate");
            return;
        }

        Date cur = new Date();
        List<CouponTemplate> expireTemplates = new ArrayList<>(templates.size());
        templates.forEach(t ->{
            // 根据规则中的优惠券模板规则中的"过期规则" 校验模板是否过期
            TemplateRule rule = t.getRule();
            if (rule.getExpiratioin().getDeadline() < cur.getTime()){
                t.setExpired(true);
                expireTemplates.add(t);
            }
        });

        if(CollectionUtils.isNotEmpty(expireTemplates)){
            log.info("Expired CouponTemplate Num:{}",templateDao.saveAll(expireTemplates));
        }
        log.info("Done to Expire CouponTemplate");
    }
}
