package com.zxbking.web.sso.comm.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import sun.misc.BASE64Encoder;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Random;

public class PasswordUtil {
	private static final Logger logger = LoggerFactory.getLogger(PasswordUtil.class);
	
	private static String GLOBAL_ENCRYPT_DES_KEY = "376B4A409E5789CE";

	public static String genRandomNum(int pwd_len){
		  //35是因为数组是从0开始的，26个字母+10个数字
		  final int  maxNum = 36;
		  int i;  //生成的随机数
		  int count = 0; //生成的密码的长度
		  char[] str = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
		    'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
		    'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
		  
		  StringBuffer pwd = new StringBuffer("");
		  Random r = new Random();
		  while(count < pwd_len){
		   //生成随机数，取绝对值，防止生成负数，
		   
		   i = Math.abs(r.nextInt(maxNum));  //生成的数最大为36-1
		   
		   if (i >= 0 && i < str.length) {
		    pwd.append(str[i]);
		    count ++;
		   }
		  }
		  
		  return pwd.toString();
		 }
	
	/**
	 * 使用spring
	 * @param password
	 * @param salt
	 * @return
	 */
	public static String springSecurityPasswordEncode(String password,Object salt){
		ShaPasswordEncoder encode = new ShaPasswordEncoder(256);
		encode.setEncodeHashAsBase64(true);
		return encode.encodePassword(password, salt);
	}
	
	/**
	 * des 加密
	 * @param password
	 * @return
	 */
	public static String desencode(String password){
		String key = GLOBAL_ENCRYPT_DES_KEY;
		String result = "";
		try {
			result = EncryptDecryptData.encrypt(key,password);
		} catch (InvalidKeyException e) {
			// TODO 自动生成 catch 块
			logger.error("加密异常",e);
		} catch (NoSuchAlgorithmException e) {
			// TODO 自动生成 catch 块
			logger.error("加密异常",e);
		} catch (IllegalBlockSizeException e) {
			// TODO 自动生成 catch 块
			logger.error("加密异常",e);
		} catch (BadPaddingException e) {
			// TODO 自动生成 catch 块
			logger.error("加密异常",e);
		} catch (NoSuchPaddingException e) {
			// TODO 自动生成 catch 块
			logger.error("加密异常",e);
		} catch (InvalidKeySpecException e) {
			// TODO 自动生成 catch 块
			logger.error("加密异常",e);
		}
		return result;
	}
	
	/**
	 * des 解码
	 * @param password
	 * @return
	 */
	public static String desdecode(String password){
		String key = GLOBAL_ENCRYPT_DES_KEY;
		String result="";
		try {
			result = EncryptDecryptData.decrypt(key,password);
		} catch (InvalidKeyException e) {
			// TODO 自动生成 catch 块
			logger.error("解码异常",e);
		} catch (IllegalBlockSizeException e) {
			// TODO 自动生成 catch 块
			logger.error("解码异常",e);
		} catch (BadPaddingException e) {
			// TODO 自动生成 catch 块
			logger.error("解码异常",e);
		} catch (NoSuchAlgorithmException e) {
			// TODO 自动生成 catch 块
			logger.error("解码异常",e);
		} catch (NoSuchPaddingException e) {
			// TODO 自动生成 catch 块
			logger.error("解码异常",e);
		} catch (InvalidKeySpecException e) {
			// TODO 自动生成 catch 块
			logger.error("解码异常",e);
		}
		return result;
		
	}

	/*以下方法是金三MD加密*/
	/**利用MD5进行加密
	 * @param str  待加密的字符串
	 * @return  加密后的字符串
	 * @throws NoSuchAlgorithmException  没有这种产生消息摘要的算法
	 * @throws UnsupportedEncodingException
	 */
	public static String EncoderByMd5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		//确定计算方法
		MessageDigest md5=MessageDigest.getInstance("MD5");
		BASE64Encoder base64en = new BASE64Encoder();
		//加密后的字符串
		String newstr=base64en.encode(md5.digest(str.getBytes("utf-8")));
		return newstr;
	}
	public static String getMD5(String str) {
		try {
			// 生成一个MD5加密计算摘要
			MessageDigest md = MessageDigest.getInstance("MD5");
			// 计算md5函数
			md.update(str.getBytes());
			// digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
			// BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
			String md5=new BigInteger(1, md.digest()).toString(16);
			//BigInteger会把0省略掉，需补全至32位
			return fillMD5(md5);
		} catch (Exception e) {
			throw new RuntimeException("MD5加密错误:"+e.getMessage(),e);
		}
	}

	public static String fillMD5(String md5){
		return md5.length()==32?md5:fillMD5("0"+md5);
	}

	public static boolean vailJSPassword(String p1,String p2){
		try {
			String p = getMD5(p1);
			return p.equalsIgnoreCase(p2);
		} catch (Exception e) {
			logger.error("验证JS密码异常：",e);
			return false;
		}
	}
}
