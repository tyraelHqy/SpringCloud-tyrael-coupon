package org.example.coupon.service.impl;

import com.google.common.base.Stopwatch;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.example.coupon.constant.Constant;
import org.example.coupon.dao.CouponTemplateDao;
import org.example.coupon.entity.CouponTemplate;
import org.example.coupon.service.IAsyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 异步服务接口实现
 */
@Slf4j
@Service
public class AsyncServiceImpl implements IAsyncService {

    /** CouponTemplate Dao */
    private final CouponTemplateDao templateDao;

    /** 注入Redis 模板类 */
    private final StringRedisTemplate redisTemplate;

    @Autowired
    public AsyncServiceImpl(CouponTemplateDao templateDao, StringRedisTemplate redisTemplate) {
        this.templateDao = templateDao;
        this.redisTemplate = redisTemplate;
    }

    /**
     * 根据模板异步地创建优惠券码
     *
     * @param template
     */
    @Async("getAsyncExecutor")
    @Override
    public void asyncConstructCouponByTemplate(CouponTemplate template) {
        Stopwatch stopwatch = Stopwatch.createStarted();

        Set<String> couponCodes = buildCouponCode(template);
        String redisKey = String.format("%s%s", Constant.RedisPrefix.COUPON_TEMPLATE,template.getId().toString());
        log.info("Push Coupon Code to Redis: {}",redisTemplate.opsForList().rightPushAll(redisKey,couponCodes));
        template.setAvailable(true);
        templateDao.save(template);

        stopwatch.stop();
        log.info("Construct CouponCode by Template Cost: {}ms ",stopwatch.elapsed(TimeUnit.MILLISECONDS));

        // TODO 发送短信或者邮件通知优惠券模板已经可用
        log.info("CouponTemplate({}) is Available! " ,template.getId());
    }

    /**
     * 构造优惠券码
     * 对应每一张优惠券：18位信息
     * 前4位：产品线+类型
     * 中间6位：日期随机（210101）
     * 后8位：0-9随机数构成
     *
     * @param template
     * @return Set<String> 与Template.count值相同个数的优惠券码
     */
    private Set<String> buildCouponCode(CouponTemplate template) {
        Stopwatch stopwatch = Stopwatch.createStarted();

        Set<String> result = new HashSet<>(template.getCount());

        // 前4位：
        String prefix4 = template.getProductLine().getCode().toString()
                + template.getCategory().getCode();
        String date = new SimpleDateFormat("YYMMdd").format(template.getCreateTime());

        for (int i = 0; i != template.getCount(); i++) {
            result.add(prefix4 + buildCouponCodeSuffix14(date));
        }
        while (result.size() < template.getCount()) {
            result.add(prefix4+buildCouponCodeSuffix14(date));
        }

        assert result.size() == template.getCount();
        stopwatch.stop();
        log.info("Build Coupon Code Cost: {}ms ",stopwatch.elapsed(TimeUnit.MILLISECONDS));
        return result;
    }

    /**
     * 构造优惠券码的后14位
     *
     * @param date 创建优惠券的日期
     * @return 14位优惠券码
     */
    private String buildCouponCodeSuffix14(String date) {
        char[] bases = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9'};

        List<Character> chars = date.chars().mapToObj(e -> (char) e).collect(Collectors.toList());
        Collections.shuffle(chars);
        String mid6 = chars.stream().map(Objects::toString).collect(Collectors.joining());

        // 后八位
        String suffix8 = RandomStringUtils.random(1, bases) + RandomStringUtils.randomNumeric(7);

        return mid6 + suffix8;

    }
}
