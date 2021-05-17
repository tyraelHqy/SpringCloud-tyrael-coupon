package org.example.coupon.advice;

import org.example.coupon.exception.CouponException;
import org.example.coupon.vo.CommonResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常处理
 */
@RestControllerAdvice
public class GlobalExceptionAdvice {

    /**
     * 对CouponException 进行统一处理
     */
    @ExceptionHandler(value = CouponException.class)
    public CommonResponse<String> handlerCouponException(HttpServletRequest request, CouponException exception){
        CommonResponse<String> response = new CommonResponse<>(
                -1,"bussiness error"
        );
        response.setData(exception.getMessage());
        return response;
    }
}
