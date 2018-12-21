package com.zxbking.web.sso.redis;

/**
 * Redis事务执行失败
 * @author zhangxibin 2016/2/29
 *
 */
public class TransactionExecFailedException extends Exception {

	public TransactionExecFailedException(){}
	public TransactionExecFailedException(Throwable cause) {
		super(cause);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
