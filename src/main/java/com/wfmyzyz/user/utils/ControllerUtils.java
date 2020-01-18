package com.wfmyzyz.user.utils;

import com.alibaba.fastjson.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * @author admin
 */
public class ControllerUtils {

    /**
     * 转jsonArray为IntegerList
     * @param jsonArray
     * @return
     */
    public static List<Integer> jsonArrayChangeIntegerList(JSONArray jsonArray){
        if (jsonArray == null){
            return null;
        }
        List<Integer> integerList = new ArrayList<>();
        jsonArray.forEach(e->integerList.add((Integer) e));
        return integerList;
    }


}
