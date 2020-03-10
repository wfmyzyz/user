package com.wfmyzyz.user.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.wfmyzyz.user.config.ProjectConfig;
import com.wfmyzyz.user.user.domain.Action;
import com.wfmyzyz.user.user.domain.User;
import com.wfmyzyz.user.user.service.IActionService;
import com.wfmyzyz.user.user.service.IUserService;
import com.wfmyzyz.user.utils.RequestUtils;
import com.wfmyzyz.user.utils.TokenUtils;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @author admin
 */
@Component
@Aspect
@Slf4j
public class ControllerActionAopInterceptor {


    @Autowired
    private TokenUtils tokenUtils;
    @Autowired
    private IUserService userService;
    @Autowired
    private IActionService actionService;

    /**
     * 切入点表达式
     */
    @Pointcut("execution(public * com.wfmyzyz.user.*.controller.*.*.*(..))")
    public void privilege(){
    }

    @Around("privilege()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        log.info("动作日志开始---");
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        ApiOperation annotation = method.getAnnotation(ApiOperation.class);
        String value = annotation.value();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader(ProjectConfig.TOKEN_KEY);
        Integer userIdByToken = tokenUtils.getUserIdByToken(token);
        Action action = new Action();
        action.setTitle(value);
        action.setToken(token);
        action.setIp(RequestUtils.getIpAddr(request));
        if (userIdByToken != null){
            User user = userService.getById(userIdByToken);
            action.setUsername(user.getUsername());
            action.setUserId(user.getUserId());
        }
        Object[] args = pjp.getArgs();
        String[] parameterNameArgs = ((MethodSignature) pjp.getSignature()).getParameterNames();
        StringBuffer paramsBuf = new StringBuffer();
        for (int i=0;i < args.length; i++){
            if (paramsBuf.length() > 0){
                paramsBuf.append("|");
            }
            paramsBuf.append(parameterNameArgs[i]).append(" = ").append(args[i]);
        }
        action.setParams(paramsBuf.toString());
        Object result = pjp.proceed();
        action.setResult(JSONObject.toJSONString(result));
        actionService.save(action);
        log.info("动作日志结束---");
        return result;
    }
}
