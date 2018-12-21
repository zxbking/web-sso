package com.zxbking.web.sso.comm.utils;


import com.zxbking.web.sso.comm.ApiReturnCodeEnum;

/**
 * Created by zhangxibin  on 2016/8/26.
 */
public abstract class HyhtApiException extends Exception{

    private String context;

    public HyhtApiException(Throwable t) {
        super(t);
    }

    public HyhtApiException() {
        super();
    }

    public HyhtApiException(String context){
        super();
        this.context = context;
    }

    public HyhtApiException(ExceptionContext context){
        super();
        if(context!=null){
            this.context = context.toString();
        }
    }

    public abstract ApiReturnCodeEnum getApiExceptionEnum();

    public final String getCode(){
        return getApiExceptionEnum().getCode();
    }

    public final String getSimpleMessage(){
        return getApiExceptionEnum().getMsg();
    }

    public final String getMessage(){
        return "[code="+getCode()+"]"+"[message="+getSimpleMessage()+"]"+"[context="+this.context+"]";
    }

}
