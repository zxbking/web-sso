package com.zxbking.web.sso.comm.utils;



import java.io.Serializable;


public class SecurityUser implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 自定义Authentication对象，使得Subject除了携带用户的登录名外还可以携带更多信息.
	 */
	private String id;
	private String userName;


	public SecurityUser() {
	}

	public SecurityUser(String id, String userName) {
		this.id = id;
		this.userName = userName;
	}
	public String getId() {
		return id;
	}

	public String getUserName() {
		return userName;
	}



	/**
	 */
	@Override
	public String toString() {
		return JSONUtil2.objectToJson(this);
	}


}
