package com.wfmyzyz.user.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author admin
 */
public class Md5Utils {

    public static String getMd5Str(String str){
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] bytes = str.getBytes();
        md5.reset();
        byte[] digest = md5.digest(bytes);
        StringBuilder hexValue = new StringBuilder();
        for (int i=0;i<digest.length;i++){
            String hexString = Integer.toHexString(digest[i] & 0xff);
            if (hexString.length() == 1){
                hexValue.append("0");
            }
            hexValue.append(hexString);
        }
        return hexValue.toString();
    }

    //String md5Str = getMd5Str(getMd5Str("123456")+"qAa_u.QgOK&3 &;~\\r3TvdXdi_");管理员账号密码
    //MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQChZJ7yzdqv+tkU/2HluALP+mnd6ZA5W8dpFmUKjhh7SCogpITkQezVb8bpVJhyHu78vB2LiU7PPoJ4R9VpymUcpGkEOu+bU4A9YeJelZjrfnyPsQAikWGY/w+AYVoSvBZK60CC/+YepFI2fFW1Nvh3CxlS78R5CJ3znGe2KGf/1wIDAQAB

    public static void main(String[] args) throws Exception {
//        String randomStr = RandomUtils.getRandomStr(25, 30);
//        System.out.println(randomStr);
//        String md5Str = getMd5Str(getMd5Str("123456")+"qAa_u.QgOK&3 &;~\\r3TvdXdi_");
//        System.out.println(getMd5Str("123456"));
        // qy39cxY1Cwa6txAOpXrQPnB8MxRr9/6w+32kR66cakA6Y+12JwjSSX/8EN5IWipVnSmECH/sV9xfH3IwUrEuWwkVck25pU0m7tRsV36eJGGKWoCuzwJx1JCQ+3hChK3+OGVu3DdOpOYm38MipNNar/iMrsr4v0KyaXuiz+cjlrw=
        String str = RsaUtils.encrypt("e10adc3949ba59abbe56e057f20f883e", "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCWounFGRJT4jVo5RJaO8/rj66bP+NVYG1Rb0cMlafdrooxihxtI+Ahiu7xbMDZU7R5+8VTZ8biQ2T5tCmfIHSlt3pTnC0kHR4Jl+KRQjaZvCcSRsrTYKVU8m7px8v2AoLoahAO3VAaYkDdU4i3IpNI8L1GMMeful+O6pvyHCp5wQIDAQAB");
        System.out.println(str);
//        String deStr = RsaUtils.decrypt("BvU/itWmPwGM/jokVdLbM0daSibAwTWwFPteA54CyytrhCmSEQcZXtzkWPU6u125xRiRM+vsPhV0fMc+44mtlIZbsKntUL5+D3dLXWGvGtwVvvvCaq8Jl8s7rdJ0Mr7FsARmbSLdi7+2uHCikCNM4hpohyBAd5Jd2ocoWXRJv48=","MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAKFknvLN2q/62RT/YeW4As/6ad3pkDlbx2kWZQqOGHtIKiCkhORB7NVvxulUmHIe7vy8HYuJTs8+gnhH1WnKZRykaQQ675tTgD1h4l6VmOt+fI+xACKRYZj/D4BhWhK8FkrrQIL/5h6kUjZ8VbU2+HcLGVLvxHkInfOcZ7YoZ//XAgMBAAECgYBIfqgfLHR049qq4eQZicRJo5P2SDmuahlyjMqqKRW6GrpBm2TOGwbFAMZ6fLNVX6u9/snOqciqsstsN+gW8wDjWs1oyi9wYu0eapfEgIN6ZRxXMYIBicFNvuF5cwRTZg+/BI5oLGm4TcEnPvPjz6UDVIwNNQ0V/50NOhEYXdTU8QJBAOVb3KpC3BU/s1N5wFgJwlGD+pcV1bMvfsjWgcP7RwSZUlBDGhC0Vkp6PLgNroHP8RbrlTrVZVYZVuQgZuPzw5UCQQC0I8A9ajuzXCoNYYMxVR09QupO7zsK8HM5lg8UxYwFqE3GF+1WEmrww1JzOPUppWAckcRyCjM5TPdgjE1Gzxq7AkBdNQSq6HrZ48YzcTH6VRTz3YeTqJrss6t5YLrhcxc8RjaOSYapLTA4Gg7c2SqGtIOMRfaiLyB8adXG5WgK0QA1AkAjIMWgNFMoCtT4fweCW0K0a5QdiHFPFMyjcivGtS80+zkPZCMYIcdQ5AX0Citkz6cKazKPuoV6qxOidDmtkT15AkBh+PlTgWYwZMT132hh3XZ9INwfIr9X5o/MJ16C4P22LUcFYfMCBIIf2tWO5DOMeQPmYeULJjm1gkuxJuN6vKMQ");
//        System.out.println(deStr);

    }
}
