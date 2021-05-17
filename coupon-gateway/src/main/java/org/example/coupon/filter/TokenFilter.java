package org.example.coupon.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 校验请求中传递的Token
 */

@Slf4j
@Component
public class TokenFilter extends AbstractPreZuulFilter {

    @Override
    protected Object cRun() {
        HttpServletRequest request = context.getRequest();
        log.info(
                String.format("%s request to %s", request.getMethod(), request.getRequestURL().toString())
        );
        Object token = request.getParameter("token");
        if (null == token) {
            String msg = "error: token is empty";
            log.error(msg);
            return (fail(401, msg));
        }
        return success();
    }

    @Override
    public int filterOrder() {
        return 1;
    }
}
