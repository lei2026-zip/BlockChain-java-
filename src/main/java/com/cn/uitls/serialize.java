package com.cn.uitls;

import com.alibaba.fastjson.*;

public class serialize {
    public static String Serialize(Object date){
         return  JSON.toJSONString(date);
    }
    //反序列化 指定对象
    public static <T> T Deserialize(String date, Class<T> type){
        return  JSONObject.parseObject(date,type);
    }
}
