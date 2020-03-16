package com.wfmyzyz.user.interceptor;

import com.wfmyzyz.user.utils.Msg;
import com.wfmyzyz.user.utils.RequestUtils;
import com.wfmyzyz.user.utils.TokenUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author admin
 */
@Component
public class ControllerBaseInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(ControllerBaseInterceptor.class);
    @Autowired
    private TokenUtils tokenUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.info("Base访问:IP地址["+RequestUtils.getIpAddr(request)+"],访问路径["+request.getRequestURL()+"]");
        Msg.noPower(response);
        return false;
    }
}
