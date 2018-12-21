package com.isuwang.web.sso.test;/**
 * Created by Ya on 2018/12/19.
 */

import com.zxbking.web.sso.comm.utils.PasswordUtil;

/**
 * @author: zhangxibin
 * @date: 2018/12/19
 * @description:
 */

public class PasswordUtilsTest {
    public static void main(String[] args) {
        String password = "123456";
        String username = "zhangxb";
        String pm = PasswordUtil.springSecurityPasswordEncode(password, username);
        System.out.println(pm);
    }
}
