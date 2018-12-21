package com.zxbking.web.sso.comm;

import com.zxbking.web.sso.comm.utils.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Tandy on 2016/6/6.
 * 提供统一的REST相应对象封装
 * data 用于提供返回对象的封装
 *
 */
public class RestResponse<T> {

    private static final Logger logger = LoggerFactory.getLogger(RestResponse.class);
    //是否请求成功
    private boolean success;

    //如果success = false ，必须提供错误代号
    private String errorCode;

    //如果success = false,指出错误消息
    private String errorMsg;

    //如果success = true,可提供返回对象，也可以不提供
    private T data;

    public boolean isSuccess() {
        return success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    /**
     * 提供成功的静态方法，方便使用
     * @return
     */
    public static RestResponse success(){
        return success(null);
    }
    /**
     * 提供成功的静态方法，方便使用
     * @param data 希望作为返回值输出的结果对象
     * @return
     *                  返回success为true的相应对象
     */
    public static RestResponse success(Object data){
        RestResponse restResponse = new RestResponse();
        restResponse.setSuccess(true);
        restResponse.setData(data);

        if(logger.isDebugEnabled()){
            logger.debug("输出响应："+restResponse);
        }
        return restResponse;
    }

    /**
     * 提供失败对象的静态方法，方便输出
     * @param errorCode  错误码
     * @param errorMsg   错误消息
     * @return
     */
    public static RestResponse failed(String errorCode,String errorMsg){
        RestResponse restResponse = new RestResponse();
        restResponse.setSuccess(false);
        restResponse.setErrorCode(errorCode);
        restResponse.setErrorMsg(errorMsg);
        return restResponse;
    }

    /**
     * 包装一个指定对象数据类型的响应对象
     * @param <T>
     * @return
     */
    public <T> RestResponse<T> wrapIndicateTypeRestResponse(T data) {
        RestResponse<T> restResponse = new RestResponse<T>();
        restResponse.setSuccess(this.isSuccess());
        restResponse.setErrorMsg(this.getErrorMsg());
        restResponse.setErrorCode(this.getErrorCode());
        restResponse.setData(data);
        return restResponse;
    }

    /**
     * 提供失败对象的静态方法，方便输出
     * @param error  错误码
     * @return
     */
    public static RestResponse failed(ApiReturnCodeEnum error){
        RestResponse restResponse = new RestResponse();
        restResponse.setSuccess(false);
        restResponse.setErrorCode(error.getCode());
        restResponse.setErrorMsg(error.getMsg());
        return restResponse;
    }

    @Override
    public String toString() {
        return JSONUtil.objectToJson(this);
    }
}
