package com.wfmyzyz.user.interceptor;

import com.wfmyzyz.user.utils.RequestUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MemberSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;


/**
 * @author admin
 */
@Component
@Aspect
public class ControllerAopInterceptor {

    public static final Logger logger = LoggerFactory.getLogger("ControllerAopInterceptor");

    /**
     * 切入点表达式
     */
    @Pointcut("execution(public * com.wfmyzyz.user.*.controller.*.*.*(..))")
    public void privilege(){
    }

    @Around("privilege()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        //获取类名
        String className = pjp.getTarget().getClass().getName();
        //获取方法名
        String methodName = pjp.getSignature().getName();
        //获取方法参数名称
        String[] parameterNameArgs = ((MethodSignature) pjp.getSignature()).getParameterNames();
        //获取方法参数
        Object[] args = pjp.getArgs();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String requestURL = request.getRequestURI();
        String ip = RequestUtils.getIpAddr(request);
        //打印拼接请求参数集合
        StringBuffer paramsBuf = new StringBuffer();
        for (int i=0;i < args.length; i++){
            if (paramsBuf.length() > 0){
                paramsBuf.append("|");
            }
            paramsBuf.append(parameterNameArgs[i]).append(" = ").append(args[i]);
        }
        StringBuffer headerBuf = new StringBuffer();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()){
            String key  = headerNames.nextElement();
            String value = request.getHeader(key);
            if (headerBuf.length() > 0){
                headerBuf.append(" | ");
            }
            headerBuf.append(key).append(" = ").append(value);
        }
        long start = System.currentTimeMillis();
        logger.info("请求| ip:{} | 请求接口:{} | 请求类:{} | 方法 :{} | 参数:{} | 请求header:{}|请求时间 :{}", ip, requestURL, className, methodName, paramsBuf.toString(),
                headerBuf.toString(), start);
        // 执行目标方法
        Object result = pjp.proceed();
        logger.info("返回| 请求接口:{}| 方法 :{} | 请求时间:{} | 处理时间:{} 毫秒 | 返回结果 :{}", requestURL, methodName, start, (System.currentTimeMillis() - start), result);
        return result;
    }
}






