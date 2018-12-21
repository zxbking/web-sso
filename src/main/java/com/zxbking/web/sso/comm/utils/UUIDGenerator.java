/*
 * 创建日期 2006-3-24
 *
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package com.zxbking.web.sso.comm.utils;


import java.util.Random;
import java.util.UUID;


/**
 * @author 谭畅
 *
 * 
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
public class UUIDGenerator {
	private static int count=0;
	public static String javaId(){
		count++;
		String resultId=""+System.currentTimeMillis()+""+count;
		if(count>=90){
			count=0;
		}
		return resultId;
	}
	
	/**
	 * 生成UUID 32位
	 * @return
	 */
	public static String uuid(){
		Random r = new Random();
   	 	String id = new UUID(r.nextLong(), r.nextLong()).toString();
		id = id.replaceAll("-", "");
		return id;
	}
	public static String uuid20(){
		return uuid().substring(0,20);
	}
	
	public static String uuidLong() {
		
		String[] chars = new String[] {"1","2","3","4","5","6","7","8","9"};
		String uuid[] = new String[32];
		int radix = chars.length;
		for (int i = 0; i < 32; i++) {
			int a = (int) (Math.random()*radix);
			uuid[i] = chars[0 | a];
		}
		
		return StringUtil.join(uuid);
		
	}
}
