package com.wfmyzyz.user.utils;

/**
* @author admin
*/
public class StringUtils {

    private static final char UNDERLINE='_';
    /**
    * 下划线 转 驼峰
    * @param param
    * @return
    */
    public static String underlineToCamel(String param){
        if (param==null||"".equals(param.trim())){
            return "";
        }
        int len=param.length();
        StringBuilder sb=new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = Character.toLowerCase(param.charAt(i));
            if (c == UNDERLINE){
                if (++i<len){
                    sb.append(Character.toUpperCase(param.charAt(i)));
                }
            }else{
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
    * 首字母转大写
    * @param str
    * @return
    */
    public static String indexCodeChangeBig(String str){
        return str.substring(0,1).toUpperCase()+str.substring(1);
    }

    /**
    * 根据目标替换字符
    * @param str
    * @param target
    * @param replacement
    * @return
    */
    public static String replaceStr(String str,String target,String replacement){
        return str.replace(target,replacement);
    }

}
