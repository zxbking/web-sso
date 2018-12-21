package com.zxbking.web.sso.comm;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.zxbking.web.sso.comm.utils.HyhtApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;


/**
 * Created by zhangxibin on 2016/6/14.
 * 抽象控制器类，提供获取当前登录用户的方法
 */
public abstract class AbstractRestController {
    public Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 对Controller层统一的异常处理
     * @param request
     * @param ex
     * @return
     */
    @ExceptionHandler(Exception.class)
    public RestResponse exp(HttpServletRequest request, Exception ex) {
        RestResponse failed ;
        Throwable resultEx;
        Throwable cause = ex.getCause();
        if(cause != null){
            resultEx = cause;
        }else{
            resultEx = ex;
        }
        if(resultEx instanceof HyhtApiException){
            failed = RestResponse.failed(((HyhtApiException) resultEx).getCode(),((HyhtApiException) resultEx).getSimpleMessage());
        }else if(resultEx instanceof MethodArgumentNotValidException || resultEx instanceof JsonMappingException){
            failed = RestResponse.failed(ApiReturnCodeEnum.IllegalArgument.getCode(), ApiReturnCodeEnum.IllegalArgument.getMsg());
        }else{
            failed = RestResponse.failed(ApiReturnCodeEnum.UnknownFail.getCode(), ApiReturnCodeEnum.UnknownFail.getMsg());
        }
        logger.error("调用接口出现异常：",resultEx);
        return failed;
    }


    /**
     * 获取当前用户账号的用户名
     * @return 当前用户账号的用户名
     */
    protected String getCurrentAccountUserName(){
        //从登录通过的凭证中取出用户
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user.getUsername();
    }

}
