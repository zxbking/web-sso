package com.zxbking.web.sso.dbmodel;

import java.io.Serializable;

public class User implements Serializable {
    /**
     * 
     * 表 : userinfo
     * 对应字段 : id
     */
    private String id;

    /**
     * 
     * 表 : userinfo
     * 对应字段 : username
     */
    private String username;

    /**
     * 
     * 表 : userinfo
     * 对应字段 : password
     */
    private String password;

    private static final long serialVersionUID = 1L;

    /**
     * get method 
     *
     * @return userinfo.id：
     */
    public String getId() {
        return id;
    }

    /**
     * set method 
     *
     * @param id  
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    /**
     * get method 
     *
     * @return userinfo.username：
     */
    public String getUsername() {
        return username;
    }

    /**
     * set method 
     *
     * @param username  
     */
    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    /**
     * get method 
     *
     * @return userinfo.password：
     */
    public String getPassword() {
        return password;
    }

    /**
     * set method 
     *
     * @param password  
     */
    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }
}