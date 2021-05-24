package org.example.coupon.converter;

import com.alibaba.fastjson.JSON;
import org.example.coupon.vo.TemplateRule;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class TemplateRuleConverter implements AttributeConverter<TemplateRule, String> {
    @Override
    public String convertToDatabaseColumn(TemplateRule templateRule) {
        return JSON.toJSONString(templateRule);
    }

    @Override
    public TemplateRule convertToEntityAttribute(String templateRule) {
        return JSON.parseObject(templateRule,TemplateRule.class);
    }
}
