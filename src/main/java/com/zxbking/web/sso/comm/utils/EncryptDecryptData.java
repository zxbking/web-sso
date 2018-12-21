package com.zxbking.web.sso.comm.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

/**
* 通过DES加密解密实现一个String字符串的加密和解密.
* 
* @author sds
* 
*/
public class EncryptDecryptData {
    private static final Logger logger = LoggerFactory.getLogger(EncryptDecryptData.class);
//    public static void main(String[] args) throws NoSuchAlgorithmException,
//            InvalidKeyException, NoSuchPaddingException,
//            InvalidKeySpecException, IllegalBlockSizeException,
//            BadPaddingException {
//
//    	String skey = "376B4A409E5789CE";
//        System.out.println("密匙===>" + skey);
//        String str = "888888"; // 待加密数据
//        System.out.println("加密前："+str);
//        // 2.1 >>> 调用加密方法
//        String encryptedData = encrypt(skey, str);
//        System.out.println("加密后："+encryptedData);
////         3.1 >>> 调用解密方法
//       String result = decrypt(skey, "E953957DC33B4FB7C8FA78E970A65D07");
//        System.out.println("解密后："+result);
//    }
    
	
    public static String generateKey(){
    	// 1.1 >>> 首先要创建一个密匙
        // DES算法要求有一个可信任的随机数源
        SecureRandom sr = new SecureRandom();
        // 为我们选择的DES算法生成一个KeyGenerator对象
        KeyGenerator kg = null;
		try {
			kg = KeyGenerator.getInstance("DES");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
            logger.error("获取加密算法异常",e);
		}
        kg.init(sr);
        // 生成密匙
        SecretKey key = kg.generateKey();
        // 获取密匙数据
        byte rawKeyData[] = key.getEncoded();
        String skey = byteArrayToHexString(rawKeyData);
        return skey;
    }
    
    
    public static byte[] hexToBytes(char[] hex) {
        int length = hex.length / 2;
        byte[] raw = new byte[length];
        for (int i = 0; i < length; i++) {
          int high = Character.digit(hex[i * 2], 16);
          int low = Character.digit(hex[i * 2 + 1], 16);
          int value = (high << 4) | low;
          if (value > 127)
            value -= 256;
          raw[i] = (byte) value;
        }
        return raw;
      }
    
    public static String byteArrayToHexString(byte[] b) {
        StringBuffer sb = new StringBuffer(b.length * 2);
        for (int i = 0; i < b.length; i++) {
          int v = b[i] & 0xff;
          if (v < 16) {
            sb.append('0');
          }
          sb.append(Integer.toHexString(v));
        }
        return sb.toString().toUpperCase();
      }
    /**
    * 加密方法
    * 
    * @param skey
    * @param str
    * @return
    * @throws InvalidKeyException
    * @throws NoSuchAlgorithmException
    * @throws IllegalBlockSizeException
    * @throws BadPaddingException
    * @throws NoSuchPaddingException
    * @throws InvalidKeySpecException
    */
    public static String encrypt(String skey, String str)
            throws InvalidKeyException, NoSuchAlgorithmException,
            IllegalBlockSizeException, BadPaddingException,
            NoSuchPaddingException, InvalidKeySpecException {
    	byte rawKeyData[] = hexToBytes(skey.toCharArray());
        // DES算法要求有一个可信任的随机数源
        SecureRandom sr = new SecureRandom();
        // 从原始密匙数据创建一个DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(rawKeyData);
        // 创建一个密匙工厂，然后用它把DESKeySpec转换成一个SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey key = keyFactory.generateSecret(dks);
        // Cipher对象实际完成加密操作
        Cipher cipher = Cipher.getInstance("DES");
        // 用密匙初始化Cipher对象
        cipher.init(Cipher.ENCRYPT_MODE, key, sr);
        // 现在，获取数据并加密
        byte data[] = str.getBytes();
        // 正式执行加密操作
        byte[] encryptedData = cipher.doFinal(data);
        String result = byteArrayToHexString(encryptedData);
        return result;
    }

    /**
    * 解密方法
    * 
    * @param skey
    * @param encryptedString
    * @throws IllegalBlockSizeException
    * @throws BadPaddingException
    * @throws InvalidKeyException
    * @throws NoSuchAlgorithmException
    * @throws NoSuchPaddingException
    * @throws InvalidKeySpecException
    */
    public static String decrypt(String skey, String encryptedString)
            throws IllegalBlockSizeException, BadPaddingException,
            InvalidKeyException, NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidKeySpecException {
    	byte[] encryptedData =hexToBytes(encryptedString.toCharArray());
    	byte rawKeyData[] = hexToBytes(skey.toCharArray());
        // DES算法要求有一个可信任的随机数源
        SecureRandom sr = new SecureRandom();
        // 从原始密匙数据创建一个DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(rawKeyData);
        // 创建一个密匙工厂，然后用它把DESKeySpec对象转换成一个SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey key = keyFactory.generateSecret(dks);
        // Cipher对象实际完成解密操作
        Cipher cipher = Cipher.getInstance("DES");
        // 用密匙初始化Cipher对象
        cipher.init(Cipher.DECRYPT_MODE, key, sr);
        // 正式执行解密操作
        byte decryptedData[] = cipher.doFinal(encryptedData);
        return new String(decryptedData);
    }

}
