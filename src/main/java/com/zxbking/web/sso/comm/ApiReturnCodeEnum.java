package com.zxbking.web.sso.comm;

/**
 * Created by zhangxibin on 2016/8/27.
 * 定义返回值常量（成功或各种异常）
 */

/**
 错误代码分为6位 前2位代表分类 中间两位代表二级分类 最后2为为异常序号
 公共 00
 SSO模块 01
 */
public enum ApiReturnCodeEnum {
    Success("000000","请求成功"),
    IllegalArgument("000001","参数错误"),
    UnknownFail("000002","未知错误"),
    InvokeCall("000003","内部错误"),
    SystemBusy("000004","系统繁忙"),
    NoTicket("000004","系统繁忙"),
    updateFail("000005","数据修改失败或数据不存在！"),
    updateFail001("10006","岗位已存在"),
    deleteFail("000006","数据删除失败或数据不存在！"),
    saveFail("000007","数据保存失败！"),
    queryFail("000008","数据查询失败！"),
    TokenKey("000009","X-API-TOKEN不允许为空！"),

    AccountNotFound("010001","用户不存在"),
    AuthenticationFailed("010002","账号验证失败"),

    Account01("020001","用户信息不允许为空！"),
    Account02("030002","用户名不允许为空！"),
    Account03("030003","账号已存在！"),
    Account04("030004","保存失败，系统异常！"),
    Account05("030005","账号已存在！"),
    Account06("030006","修改密码失败，账号或密码不正确！"),
    Account07("030007","修改密码失败，密码不允许为空！"),
    Account08("030008","修改失败！"),
    Account09("030009","所需参数不允许为空"),
    Account10("030010","修改失败，用户不存在或者系统异常"),
    Account11("030011","相同用户名存在多条用户信息"),
    Account12("030012","用户信息不存在"),
    Account13("030013","账号密码不允许为空"),
    Account14("030014","人员姓名不允许为空"),
    Account15("030015","请选择性别"),
    Account16("030016","请选择有效标志"),
    Account17("030017","账号不允许为空"),
    Account18("030018","账号标识id不允许为空"),
    Account19("030019","人员代码不允许为空"),
    Account20("030020","账号信息不允许为空"),
    Account21("030021","账号密碼不允许为空"),
    Account22("030022","账号失效"),

    Fun01("040001","职能信息不允许为空！"),
    Fun02("040002","保存失败！"),
    Fun03("040003","岗位或职能不允许为空"),
    Fun04("040004","所需处理的记录不允许为空"),
    Fun05("040005","查询执行异常"),
    Fun06("040006","职能类型已存在"),
    Fun07("040007","修改无数据受影响"),
    Fun08("040008","职能类型不允许为空"),
    Fun09("040009","职能类型绑定了机构，不允许删除。"),

    Tax01("050001","机构信息不允许为空"),
    Tax02("050002","机构编码重复"),
    Tax03("050003","机构保存失败"),
    Tax04("050004","被删除机构存在下级机构，不允许删除"),

    Identity01("060001","身份信息不允许为空"),
    Identity02("060002","身份或岗位信息不允许为空"),
    Identity03("060003","所需处理的信息不允许为空"),
    Identity04("060004","身份失效"),
    Identity05("060005","身份标识不允许为空"),

    Feedback01("070001","所需处理的信息不允许为空"),
    Feedback02("070002","反馈信息修改失败或数据不存在！"),
    Feedback03("070003","反馈信息删除失败或数据不存在！"),
    Feedback04("070004","反馈信息保存失败！"),
    Feedback05("070005","功能不被允许删除，必须先解除功能和功能集关系"),

    DirectorySecurity("100001","添加数据字典失败"),
    DirectorySecurity01("100002","数据字段已存在"),
    DirectorySecurity02("100003","修改数据字典失败"),


    Attachment01("080001","附件信息不能为空"),
    Attachment02("080002","上传附件数量超过最大限制"),
    Attachment03("080003","文件类型错误"),
    Attachment04("080004","单个文件大小超过最大限制"),
    Attachment05("080005","文件流输出异常"),
    Attachment06("080006","根据主键未查询到附件信息")
    ;

    private String code;
    private String msg;
    ApiReturnCodeEnum(String code, String message){
        this.code = code;
        this.msg = message;
    }
    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public static ApiReturnCodeEnum getApiReturnCodeEnum(String code) {
        for(ApiReturnCodeEnum e : ApiReturnCodeEnum.values()) {
            if(e.getCode().equals(code)){
                return e;
            }
        }
        return null;
    }
}
