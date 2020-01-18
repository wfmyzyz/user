package com.wfmyzyz.user.interceptor;

import com.wfmyzyz.user.utils.Msg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

/**
 * @author admin
 * 需将参数封装为json
 */
@ControllerAdvice
public class ControllerExceptionHandler {

    public static final Logger logger = LoggerFactory.getLogger("ControllerExceptionHandler");

    /**
     * 配置参数验证异常
     * @param request
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public Msg handleMethodArgumentNotValidException(HttpServletRequest request, MethodArgumentNotValidException e) {
        StringBuilder sb = new StringBuilder();
        String msg = null;
        for (ObjectError objectError:e.getBindingResult().getAllErrors()){
            FieldError fieldError = (FieldError) objectError;
            sb.append(fieldError.getDefaultMessage()).append(": ");
        }
        if (sb.length() > 0){
            msg = sb.substring(0,sb.length() - 2);
        }
        logger.error("请求地址: {}，入参校验错误:{}", request.getRequestURI(), msg);
        return Msg.error().add("error",msg);
    }

    /**
     * 入参请求参数校验
     * @param request
     * @param e
     * @return
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public Msg handleConstraintViolationException(HttpServletRequest request, ConstraintViolationException e){
        StringBuilder sb = new StringBuilder();
        String msg = null;
        for (ConstraintViolation<?> violation:e.getConstraintViolations()){
            sb.append(violation.getMessage()).append(": ");
        }
        if (sb.length() > 0){
            msg = sb.substring(0,sb.length() - 2);
        }
        logger.error("请求地址: {}，入参校验错误:{}", request.getRequestURI(), msg);
        return Msg.error().add("error",msg);
    }
}
