package com.wfmyzyz.user.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author admin
 */
public class DatetimeUtils {

    private static final String chinaDateFormatStr = "yyyy-MM-dd HH:mm:ss";

    /**
     * 获取格式化现在日期时间
     * @return
     */
    public static String getNowDatetimeStr(){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(chinaDateFormatStr);
        return dateTimeFormatter.format(LocalDateTime.now());
    }

    /**
     * 获取现在日期时间
     * @return
     */
    public static LocalDateTime getNowDateTime(){
        return LocalDateTime.now();
    }
}
