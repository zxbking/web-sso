package com.zxbking.web.sso.comm.utils;



import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangxibin on 2017/2/28.
 */
public class ExceptionContext implements Serializable{

    private final static String EMPTY_STR = "";

    private Map<String,Serializable> context = null;

    public ExceptionContext put(String key, Serializable desc){
        if(key!=null){
            if(context == null){
                context = new HashMap<>();
            }
            context.put(key,desc);
        }
        return this;
    }

    public String toString(){
        if(context == null){
            return EMPTY_STR;
        }
        return JSONUtil2.objectToJson(this.context);
    }
}
