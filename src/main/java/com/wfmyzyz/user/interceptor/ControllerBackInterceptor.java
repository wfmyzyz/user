package com.wfmyzyz.user.interceptor;

import com.wfmyzyz.user.user.domain.Authority;
import com.wfmyzyz.user.user.service.IAuthorityService;
import com.wfmyzyz.user.user.service.IUserRoleService;
import com.wfmyzyz.user.utils.RequestUtils;
import com.wfmyzyz.user.utils.TokenUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author admin
 */
@Component
public class ControllerBackInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(ControllerBackInterceptor.class);
    @Autowired
    private TokenUtils tokenUtils;
    @Autowired
    private IAuthorityService authorityService;
    @Autowired
    private IUserRoleService userRoleService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        response.setContentType("text/html;charset=UTF-8");
        String requestURI = request.getRequestURI();
        logger.info("Back访问:IP地址["+ RequestUtils.getIpAddr(request)+"],访问路径["+request.getRequestURL()+"],匹配的路径["+requestURI+"]");
        if (tokenUtils.getUserIdByToken(request) == null){
            tokenUtils.needLogin(response);
            return false;
        }
        if (userRoleService.checkUserIsSuperAdmin(request)){
            return true;
        }
        List<Authority> authorityList = authorityService.getAuthorityList(request);
        if (authorityList == null || authorityList.size() == 0){
            tokenUtils.noAuthorityNeedLogin(response);
            return false;
        }
        List<String> urlList = authorityList.stream().map(Authority::getUrl).distinct().collect(Collectors.toList());
        if (!urlList.contains(requestURI)){
            tokenUtils.noAuthorityNeedLogin(response);
            return false;
        }
        return true;
    }

}
