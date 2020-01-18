package com.wfmyzyz.user.config;

/**
* @author auto
*/
public class AutoConfig {
    /**
    * 数据库配置
    */
    public static final String DATA_URL = "jdbc:mysql://localhost:3306/user?useUnicode=true&characterEncoding=utf-8&useSSL=true&serverTimezone=UTC&allowMultiQueries=true&zeroDateTimeBehavior=convertToNull";
    public static final String DATA_DRIVER = "com.mysql.cj.jdbc.Driver";
    public static final String DATA_USERNAME = "root";
    public static final String DATA_PASSWORD = "Mysql!@#456";

    /**
    * 项目名
    */
    public static final String PROJECT_NAME = "user";
    public static final String PACK_NAME = "com.wfmyzyz.user";
}
