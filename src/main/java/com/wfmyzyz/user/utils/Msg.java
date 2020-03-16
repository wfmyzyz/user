package com.wfmyzyz.user.utils;

import com.alibaba.fastjson.JSONObject;
import com.wfmyzyz.user.config.ProjectConfig;
import com.wfmyzyz.user.enums.ResponseEnum;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Msg {
    private int code;
    private String msg;
    private Map<String,Object> map = new HashMap<>();

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Map<String, Object> getMap() {
        return map;
    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }

    public static Msg success(){
        return success(ResponseEnum.SUCCESS.getMsg());
    }

    public static Msg success(String message){
        Msg msg = new Msg();
        msg.setCode(ResponseEnum.SUCCESS.getCode());
        msg.setMsg(message);
        return msg;
    }

    public static Msg error(){
        return error(ResponseEnum.FAIL.getMsg());
    }

    public static Msg error(String message){
        Msg msg = new Msg();
        msg.setCode(ResponseEnum.FAIL.getCode());
        msg.setMsg(message);
        return msg;
    }

    public Msg add(String key,Object value){
        map.put(key,value);
        return this;
    }

    public static Msg needLogin(){
        Msg msg = new Msg();
        msg.setCode(ResponseEnum.LOGIN.getCode());
        msg.setMsg(ResponseEnum.LOGIN.getMsg());
        return msg;
    }

    public static Msg noPower(){
        Msg msg = new Msg();
        msg.setCode(ResponseEnum.POWER.getCode());
        msg.setMsg(ResponseEnum.POWER.getMsg());
        return msg;
    }

    /**
     * 响应需要登录
     * @param response
     */
    public static void needLogin(HttpServletResponse response){
        String resultText = JSONObject.toJSONString(needLogin());
        try {
            response.getWriter().println(resultText);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 过滤器告知没有权限
     */
    public static void noPower(HttpServletResponse response){
        String resultText = JSONObject.toJSONString(noPower());
        try {
            response.getWriter().println(resultText);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Msg resultError(BindingResult result){
        Msg msg = Msg.error();
        for (FieldError error:result.getFieldErrors()){
        msg.add(error.getField(),error.getDefaultMessage());
        }
        return msg;
    }

    @Override
    public String toString() {
        return "Msg{" +
        "code=" + code +
        ", msg='" + msg + '\'' +
        ", map=" + map +
        '}';
    }

}
