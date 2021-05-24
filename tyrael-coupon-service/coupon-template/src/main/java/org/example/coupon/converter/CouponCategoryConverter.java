package org.example.coupon.converter;

import org.example.coupon.constant.CouponCategory;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * X：实体属性的类型
 * Y：是数据库字段的类型
 */

@Converter
public class CouponCategoryConverter implements AttributeConverter<CouponCategory,String> {

    /**
     * 将实体属性X转换为Y，存储在数据库中，插入和更新时执行的动作
     * @param couponCategory
     * @return
     */
    @Override
    public String convertToDatabaseColumn(CouponCategory couponCategory) {
        return couponCategory.getCode();
    }

    /**
     * 将数据库中的列值反序列化为实体的属性,查询操作时执行的操作
     * @param code
     * @return
     */
    @Override
    public CouponCategory convertToEntityAttribute(String code) {
        return CouponCategory.of(code);
    }
}
