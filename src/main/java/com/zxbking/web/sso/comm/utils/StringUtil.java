package com.zxbking.web.sso.comm.utils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("rawtypes")
public class StringUtil extends StringUtils {
	private static final Logger logger = LoggerFactory.getLogger(StringUtil.class);

	/**
	 * 从InputStream中获取字符串对象
	 * @param is
	 * @param charset
	 * @return
	 */
	public static String valueOf(InputStream is,String charset){
		String result = null;
		if(is != null){
			try{
				byte[] buffer = new byte[1024];
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				int l = is.read(buffer);
				while(l!=-1){
					baos.write(buffer, 0, l);
					l = is.read(buffer);
				}
				is.close();
				result = new String(baos.toByteArray(),charset);
			}catch(Exception ex){
				logger.error("异常",ex);
			}
		}
		return result;
	}
	
	/**
	 * 过滤查询条件中的非法字符
	 * @param str
	 * @return
	 */
	public static String getFilterString(String str) {
		str = str.replace('<', '_');
		str = str.replace('>', '_');
		str = str.replace('"', '_');
		str = str.replace('\'', '_');
		str = str.replace('%', '_');
		str = str.replace(';', '_');
		str = str.replace('(', '_');
		str = str.replace(')', '_');
		str = str.replace('&', '_');
		str = str.replace('+', '_');
		return str;
	}
	
	/**
	 * 首字母改为大字
	 * @param str
	 * @return
	 */
	public static String firstToUpper(String str){
		str = str.trim();
		String ret = "";
		if(str.length()>=1){
			ret = str.substring(0,1).toUpperCase() + str.substring(1,str.length()); 
		}
		return ret;
	}
	
	/**
	 * 首字母改为小字
	 * @param str
	 * @return
	 */
	public static String firstToLower(String str){
		str = str.trim();
		String ret = "";
		if(str.length()>=1){
			ret = str.substring(0,1).toLowerCase() + str.substring(1,str.length()); 
		}
		return ret;
	}





	/**
	 * 将时间转换为parrten2格式
	 * 
	 * @param t1
	 * @param parrten
	 * @param parrten2
	 * @return
	 */
	public static String getTime(String t1, String parrten, String parrten2) {
		return DateUtils.getTime(t1, parrten, parrten2);
	}

	/**
	 * 检测字符串s是否是a-z的字母组成
	 * 
	 * @param s
	 * @return boolean
	 */
	public static boolean checkString(String s) {
		boolean tag = false;
		String regex = "\\p{Lower}+?";
		if (s != null && !"".equals(s)) {
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(s);
			tag = m.matches();
		}
		return tag;
	}

	/**
	 * 根据模式regex，对字符串s，进行匹配
	 * 
	 * @param s
	 *            字符串
	 * @param regex
	 *            模式
	 * @return boolean true=符合，false=不符合
	 */
	public static boolean checkString(String s, String regex) {
		boolean tag = false;
		if (s != null && !"".equals(s)) {
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(s);
			tag = m.matches();
		}
		return tag;
	}

	/**
	 * 将字符串转换成数字
	 * 
	 * @param s
	 *            需转换的字符串
	 * @return int，若该字符串不是数字，则返回－1
	 */
	public static int strToInt(String s) {
		int temp;
		if (StringUtil.isNumeric(s.trim())) {
			temp = Integer.parseInt(s);
		} else {
			temp = 0;
		}
		return temp;
	}

	/**
	 * 将指定字符串重复count次并返回
	 * @param s
	 * @param count
	 * @return
	 */
	public static String repeat(String s,int count){
		StringBuffer result=new StringBuffer();
		for (int i = 0; i < count; i++) {
			result.append(s);
		}
		return result.toString();
	}
	/**
	 * 将字符串转换成小写
	 * 
	 * @param s
	 * @return
	 */
	public static String toLowercase(String s) {
		String tag = null;
		if (s != null && !s.equals("")) {
			tag = s.toLowerCase();
		}
		return tag;
	}

	/**
	 * 生成SQL语句
	 * 
	 * @param tablename
	 *            表名
	 * @param sqlprotasis
	 *            条件
	 * @param columnname
	 *            字段名
	 * @return 生成的SQL语句
	 */
	public static String generationSql(String tablename, String sqlprotasis,
			String[] columnname) {
		StringBuffer bufsql = new StringBuffer("select ");
		for (int i = 0; i < columnname.length; i++) {
			if (i == (columnname.length - 1)) {
				bufsql.append(columnname[i]);
			} else {
				bufsql.append(columnname[i] + ",");
			}
		}
		bufsql.append(" from " + tablename + sqlprotasis);
		return bufsql.toString();
	}

	/**
	 * 生成SQL语句
	 * 
	 * @param tablename
	 *            表名
	 * @param sqlprotasis
	 *            条件
	 * @param columnname
	 *            字段名
	 * @return 生成的SQL语句
	 */
	public static String generationSql(int number, String tablename,
			String sqlprotasis, String[] columnname) {
		StringBuffer bufsql = new StringBuffer("select top " + number + " ");
		for (int i = 0; i < columnname.length; i++) {
			if (i == (columnname.length - 1)) {
				bufsql.append(columnname[i]);
			} else {
				bufsql.append(columnname[i] + ",");
			}
		}
		bufsql.append(" from " + tablename + sqlprotasis);
		return bufsql.toString();
	}

	/**
	 * 截字符串
	 * 
	 * @param s
	 *            字符串
	 * @param n
	 *            截的位数
	 * @return
	 */
	public static String getNumString(String s, int n) {
		String tag;
		tag = s;
		if (s != null && !s.equals("")) {
			tag = tag.substring(0, n);
		} else {
			tag = "";
		}
		return tag;
	}

	/**
	 * 生成SQL语句
	 * 
	 * @param tablename
	 *            表名
	 * @param sqlprotasis
	 *            条件
	 * @return SQL语句
	 */
	public static String generationSql(String tablename, String sqlprotasis) {
		StringBuffer bufsql = new StringBuffer("select ");
		bufsql.append("* from " + tablename + sqlprotasis);
		return bufsql.toString();
	}


	/**
	 * 判断字符串是否是数字
	 * 
	 * @param s
	 * @return 是数字＝true;不是数字＝false
	 */
	public static boolean isNumeric(String s) {
		boolean flag = true;
		if (s != null && !s.equals("")) {
			char[] numbers = s.toCharArray();
			for (int i = 0; i < numbers.length; i++) {
				if (!Character.isDigit(numbers[i])) {
					flag = false;
					break;
				}
			}
		} else {
			flag = false;
		}
		return flag;
	}

	/**
	 * 字符串转化为长整行数字
	 * 
	 * @param s
	 * @return
	 */
	public static long strToLong(String s) {
		long temp = 0;
		if (isNumeric(s)) {
			temp = Long.parseLong(s);
		}
		return temp;
	}
	
	

	/**
	 * 格式化输入文本，使输入和输出表现一样
	 * 
	 * @param input
	 *            输入
	 * @return buf
	 */
	public static String formatHTML(String input) {
		if (input == null || input.length() == 0) {
			return input;
		}
		// 建立一个Stringbuffer 来处理输入的数据
		StringBuffer buf = new StringBuffer(input.length() + 6);
		char ch = ' ';
		// 处理非法字符串
		for (int i = 0; i < input.length(); i++) {
			ch = input.charAt(i);
			if (ch == '<'){
				buf.append("&lt;");
				continue;
			}
			if (ch == '>'){
				buf.append("&gt;");
				continue;
			}
			
			if (ch == '\n'){
				buf.append("<br>");
				continue;
			}
			if (ch == '\''){
				buf.append("&acute;");
				continue;
			}
			if (ch == '"'){
				buf.append("&quot;");
				continue;
			}
			
			if (ch == ' '){
				buf.append("&nbsp;");
				continue;
			}
			buf.append(ch);
		}
		return buf.toString();
	}

	/**
	 * 分割字符串
	 * 
	 * @param input
	 *            要分割的字符串
	 * @return
	 */
	public static String formatBr(String input) {
		if (input == null || input.length() == 0) {
			return input;
		}
		// 建立一个Stringbuffer 来处理输入的数据
		StringBuffer bf = new StringBuffer("");
		String from = "<>";
		StringTokenizer st = new StringTokenizer(input, from, true);
		while (st.hasMoreTokens()) {
			String tmp = st.nextToken();
			if (tmp != null && tmp.equals("<")) {
				String tmp2 = st.nextToken().toLowerCase();
				if (tmp2.equals("br")) {
					st.nextToken();
					bf = bf.append("");
				}
			} else {
				bf.append(tmp);
			}
		}
		return bf.toString();
	}

	/**
	 * encode转码
	 * 
	 * @param str
	 * @return 非空返回str，为空返回""
	 */
	public static String encode(String str) {
		try {
			if (str != null) {
				return (new String(str.getBytes("iso-8859-1"), "gb2312"));
			} else
				return "";
		} catch (Exception e) {
			return e.toString();
		}
	}

	/**
	 * replace字符串替换 (例子：String a = replace("aaabbbccc","bbb","222"); ) 字符串
	 * (返回值：a="aaa222ccc";)
	 * 
	 * @param str
	 *            字符串
	 * @param substr
	 *            被替换的字符串
	 * @param restr
	 *            要替换的字符串
	 * @return
	 */
	public static String replace(String str, String substr, String restr) {
		String[] tmp = split(str, substr);
		String returnstr = null;
		if (tmp.length != 0) {
			returnstr = tmp[0];
			for (int i = 0; i < tmp.length - 1; i++)
				returnstr = dealNull(returnstr) + restr + tmp[i + 1];
		}
		return dealNull(returnstr);
	}

	/**
	 * @param source
	 *            需要替换的StringBuffer
	 * @param strS
	 *            被替换的字符串
	 * @param strD
	 *            用于替换的字符串
	 * @return 用strD字符串替换source里面strS字符串，例： replace(new
	 *         String("kkkk"),"k","e")，返回"eeee"
	 */
	public static StringBuffer replaceAll(StringBuffer source, String strS,
			String strD) {
		if (strS == null || strD == null || strS.length() == 0
				|| strS.equals(strD))
			return source;
		int start = 0, offset = 0, tag = 0;
		while (source.indexOf(strS, tag) != -1) {
			start = source.indexOf(strS, tag);
			offset = start + strS.length();
			tag = start + strD.length();
			source.replace(start, offset, strD);
		}
		return source;
	}

	/**
	 * 根据开始strStart和strEnd提取包含在source中的这段字符串
	 * 
	 * @param source
	 *            源StringBuffer
	 * @param strStart
	 *            开始标记
	 * @param strEnd
	 *            结束标记
	 * @return 返回提取后的StringBuffer
	 */

	public static StringBuffer takeString(StringBuffer source, String strStart,
			String strEnd) {
		int start = source.indexOf(strStart) + strStart.length();
		int end = source.indexOf(strEnd);
		if ((end > start) && end != -1 && start != -1) {
			source = new StringBuffer(source.substring(start, end));
			return source;
		}
		return new StringBuffer();
	}

	/*
	 * Function name: dealNull Description: \u8655理空字符串 Input: String str
	 * Output: 不等於null的String
	 */
	public static String dealNull(Object str) {
		String returnstr = null;
		if (str == null) {
			returnstr = "";
		} else {
			returnstr = str.toString();
		}
		return returnstr;
	}
	
	/**
	 * Function name: split Description: \u5C07字符串劈\u958B成\u6578\u7D44 Input:
	 * \u5C07字符串source劈\u958B成\u6578\u7D44div(例子：String TTT[] =
	 * my_class.split("aaa:bbb:ccc:ddd",":") ; ) Output: \u6578\u7D44
	 * (返回值：TTT[0]="aaa"; TTT[1]="bbb"; TTT[2]="ccc"; TTT[3]="ddd"; )
	 */
	public static String[] split(String source, String div) {
		int arynum = 0, intIdx = 0, intIdex = 0, div_length = div.length();
		if (source.compareTo("") != 0) {
			if (source.indexOf(div) != -1) {
				intIdx = source.indexOf(div);
				for (int intCount = 1;; intCount++) {
					if (source.indexOf(div, intIdx + div_length) != -1) {
						intIdx = source.indexOf(div, intIdx + div_length);
						arynum = intCount;
					} else {
						arynum += 2;
						break;
					}
				}
			} else
				arynum = 1;
		} else
			arynum = 0;

		intIdx = 0;
		intIdex = 0;
		String[] returnStr = new String[arynum];

		if (source.compareTo("") != 0) {

			if (source.indexOf(div) != -1) {

				intIdx = (int) source.indexOf(div);
				returnStr[0] = (String) source.substring(0, intIdx);

				for (int intCount = 1;; intCount++) {
					if (source.indexOf(div, intIdx + div_length) != -1) {
						intIdex = (int) source
								.indexOf(div, intIdx + div_length);

						returnStr[intCount] = (String) source.substring(intIdx
								+ div_length, intIdex);

						intIdx = (int) source.indexOf(div, intIdx + div_length);
					} else {
						returnStr[intCount] = (String) source.substring(intIdx
								+ div_length, source.length());
						break;
					}
				}
			} else {
				returnStr[0] = (String) source.substring(0, source.length());
				return returnStr;
			}
		} else {
			return returnStr;
		}
		return returnStr;
	}

	/**
	 * @param name
	 *            待切割的字符串
	 * @param c
	 *            切割字符串的字符
	 * @return Strin[]
	 */
	public static String[] split(String name, char c) {
		// Figure out the number of parts of the name (this becomes the size
		// of the resulting array).
		if (name == null)
			return null;
		int size = 1;
		for (int i = 0; i < name.length(); i++) {
			if (name.charAt(i) == c) {
				size++;
			}
		}
		String[] propName = new String[size];
		// Use a StringTokenizer to tokenize the property name.
		StringTokenizer tokenizer = new StringTokenizer(name, String.valueOf(c));
		int i = 0;
		while (tokenizer.hasMoreTokens()) {
			propName[i] = tokenizer.nextToken();
			i++;
		}
		return propName;
	}

	/**
	 * 根据文件名获得文件的扩展名
	 * 
	 * @param filename
	 * @return
	 */
	public static String getExt(String filename) {
		if (filename.indexOf(".") < 0) {
			return filename;
		}
		String[] strs = split(filename, '.');
		return strs[strs.length - 1];
	}

	/**
	 * 判断字符串是否包含该字符，如果有好写法，建议替换
	 * 
	 * @param strs
	 *            String[]
	 * @param str
	 *            String
	 * @return boolean
	 */
	public static boolean isInclude(String[] strs, String str) {
		if (strs.length == 0 || str == null || str.length() == 0) {
			return false;
		}
		str = str.toLowerCase();
		for (int i = 0; i < strs.length; i++) {
			if (str.equals(strs[i]))
				return true;
		}
		return false;
	}

//	public static String htmlToText(String inputHtml) {
//		String cleanText = "";
//		Parser parser = Parser.createParser(gbToIso(inputHtml), "GBK");
//		try {
//			TextExtractingVisitor visitor = new TextExtractingVisitor();
//			parser.visitAllNodesWith(visitor);
//			cleanText = ParserUtils.removeEscapeCharacters(visitor
//					.getExtractedText());
//		} catch (ParserException e) {
//			log.error(e.toString());
//		}
//		return isoToGb(cleanText);
//	}

	public static String gbToIso(String str) {
		try {
			if (str != null && !str.equals("")) {
				byte[] byteStr = str.getBytes("gb2312");
				return new String(byteStr, "ISO-8859-1");
			} else
				return "";
		} catch (Exception e) {
			return str;
		}
	}

	public static String isoToGb(String str) {
		try {
			if (str != null && !str.equals("")) {
				byte[] byteStr = str.getBytes("ISO-8859-1");
				return new String(byteStr, "gb2312");
			} else
				return "";
		} catch (Exception e) {
			return str;
		}
	}

	public static String isoToUTF8(String str) {
		try {
			if (str != null && !str.equals("")) {
				byte[] byteStr = str.getBytes("ISO-8859-1");
				return new String(byteStr, "UTF-8");
			} else
				return "";
		} catch (Exception e) {
			return str;
		}
	}
	// 不做任何处理，为了替换招聘的isoToGb
	public static String retString(String str) {
		return str;
	}

	public static String singleParameter(String name, String value) {
		if (name != null && !"".equals(name)) {
			return "&" + name + "=" + value;
		}
		return "";
	}

	// 生成单个字段条件
	public static String single1SearchContent(String searchname,
			String searchtype, String column) {
		if (searchname != null && !"".equals(searchname)) {
			if ("1".equals(searchtype)) {
				return " and " + column + " = '" + searchname + "'";
			} else {
				return " and " + column + " like '%" + searchname + "%'";
			}
		}
		return "";
	}

	// 生成单个字段条件
	public static String single2SearchContent(String searchname,
			String searchtype, String column) {
		if (searchname != null && !"".equals(searchname)) {
			if ("1".equals(searchtype)) {
				return " and " + column + " = '" + searchname + "'";
			} else if ("2".equals(searchtype)) {
				return " and " + column + " < '" + searchname + "'";
			} else {
				return " and " + column + " > '" + searchname + "'";
			}
		}
		return "";
	}

	public static String GBTounicode(String s) {
		StringBuffer ss = new StringBuffer();
		for (int i = 0; i < s.length(); ++i) {
			ss.append("\\u" + Integer.toHexString(s.charAt(i)));
		}
		String temp = new String(ss);
		return temp;

	}

	public static String unicodeToGB(String s) {
		StringBuffer sb = new StringBuffer();
		StringTokenizer st = new StringTokenizer(s, "\\u");
		while (st.hasMoreTokens()) {
			sb.append(st.nextToken());
		}
		return sb.toString();
	}
	



	/**
	 * 将指定字符串中的指定字符替换成同其对应的Ascii码
	 * eg. '#'->'$35$'	其中35为#的Ascii码
	 * @param sString	源字符串
	 * @param sChar	特殊字符
	 * @return
	 */
	public static String encodeSpecialChar(String sString,char sChar){
		String sResult=sString;
		int iCode=sChar;
		sResult= StringUtils.replace(sResult,""+sChar,"$"+iCode+"$");
		return sResult;
	}
	public static String encodeSpecialChar(String sString,String sChar){
		return encodeSpecialChar(sString,sChar.charAt(0));
	}
	public static String decodeSpecialChar(String sString,String sChar){
		return decodeSpecialChar(sString,sChar.charAt(0));
	}
	/**
	 * 解析通过encodeSpecialChar方法编码过的特殊字符
	 * @param sString	源字符串
	 * @param sChar	特殊字符
	 * @return
	 */
	public static String decodeSpecialChar(String sString,char sChar){
		int iCode=sChar;
		String sResult=sString;
		sResult= StringUtils.replace(sResult,"$"+iCode+"$",""+sChar);
		return sResult;
	}
	/**
	//替代非法XML字符	
	**/
	public static String encodeXml(String sXml) {
		sXml=encodeSpecialChar(sXml,'&');
		sXml=encodeSpecialChar(sXml,'>');
		sXml=encodeSpecialChar(sXml,'<');
		return sXml ;
	}
	/**
	//转回原串	
	**/
	public static String decodeXml(String sXml) {
		sXml = decodeSpecialChar(sXml,'<') ;
		sXml = decodeSpecialChar(sXml,'>');
		sXml = decodeSpecialChar(sXml,'&');
		return sXml ;
	}

	/**
	 * 将制定的特殊符号编码
	 * @str 需要编码的字符串
	 * @splitStr 逗号分隔特殊字符   "',%,&,*,/,\"
	 */
	public static String encodeChars(String str,String splitStr){
		String[] splitArray = splitStr.split(",");
		for(int i=0;i<splitArray.length;i++){
			String specialChar = splitArray[i];
			str = encodeSpecialChar(str,specialChar);
		}
		return str;
	}
	/**
	 * 将制定的特殊符号编码
	 * @str 逗号分隔特殊字符   "',%,&,*,/,\"
	 */
	public static String  decodeChars(String str,String splitStr){
		String[] splitArray = splitStr.split(",");
		for(int i=0;i<splitArray.length;i++){
			String specialChar = splitArray[i];
			str = decodeSpecialChar(str,specialChar);
		}
		return str;
	}
	
	
	public static  String encodeChar(String str){
		str = encodeSpecialChar(str,'\'');
		str = encodeSpecialChar(str,';');
		str = encodeSpecialChar(str,'&');
		str = encodeSpecialChar(str,'"');
		return str;
	}

	public static  String  decodeChar(String str){
		str = decodeSpecialChar(str,'\'');
		str = decodeSpecialChar(str,';');
		str = decodeSpecialChar(str,'&');
		str = decodeSpecialChar(str,'"');
		return str;
	}
	/**
	 * 将Map中的值连成字符串
	 * @param map
	 * @param separator
	 * @return
	 */
	public static String serializeMapValue(Map map,String separator){
		String sResult="";
		int i=0;
		for (Object o : map.values()) {
			String sTmp = o.toString();
			sResult += ((i == 0 ? "" : separator) + sTmp);
			i++;
		}
		return sResult;		
	}

	public static String stringTojeson(List<String> strList){
		 String x="";
		 int i=0;
		 for (String path : strList) {
			x+=(i++>0?",":"")+"'"+path+"'";
		}
		return x;
	}
	
	
	/**
	 * 不为空
	 *@author  MrBao 
	 *@date 	  2009-5-4
	 *@param str
	 *@return
	 *@return boolean
	 *@remark
	 */
	public static boolean isNotEmpty(String str){
		return str!=null && str.length()>0;
	}
	/**
	 * 为空
	 *@author  MrBao 
	 *@date 	  2009-5-4
	 *@param str
	 *@return
	 *@return boolean
	 *@remark
	 */
	public static boolean isEmpty(String str){
		return str==null ||  str.length()<=0 ;
	}
	
	/**
	 * 去掉字符串最后面字符
	 *@author  MrBao 
	 *@date 	  2009-7-14
	 *@param sourceString
	 *@param cutString
	 *@return
	 *@return String
	 *@remark
	 */
	public static String delEndString(String sourceString,String cutString){
		String resultStr = null;
		if(isNotEmpty(sourceString) && isNotEmpty(cutString)){
			if(sourceString.endsWith(cutString)){
				resultStr = sourceString.substring(0,sourceString.length()- cutString.length());
			}else{
				resultStr = sourceString;
			}
		}
		return resultStr;
	}
	
	public static String delStartString(String sourceString,String cutString){
		String resultStr = null;
		if(isNotEmpty(sourceString) && isNotEmpty(cutString)){
			if(sourceString.startsWith(cutString)){
				resultStr = sourceString.substring(cutString.length(),sourceString.length());
			}else{
				resultStr = sourceString;
			}
		}
		return resultStr;
	}
	
	/**
	 * 将用逗号分隔的ids  1,2  转换成in括号内用的格式  '1','2'
	 *@author  MrBao 
	 *@date 	  2009-7-29
	 *@param ids
	 *@return
	 *@return String
	 *@remark
	 */
	public static String idsToWhereIn(String ids){
		
		StringBuffer sb = new StringBuffer();
		if(StringUtil.isNotEmpty(ids)){
			String [] arrId = ids.split(",");
			for(String strId : arrId){
				sb.append("'" + strId + "',");
			}
		}
		if(sb.toString().length() >0 && sb.toString().endsWith(",")){
			ids = StringUtil.delEndString(sb.toString(), ",");
		}
		return ids;
	}
	

	/**
	 * 比较两个IP地址
	 * @param ip1
	 * @param ip2
	 * @return  ip1>ip2   1
	 *          ip1<ip2   -1
	 *          ip1=ip2   0
	 */
	public static int compareIp(String ip1,String ip2){
		String tip1[] = StringUtil.split(ip1, ".");
		String tip2[] =  StringUtil.split(ip2, ".");
		int result = 0;
		for(int i=0;i<tip1.length;i++){
			int n1 = Integer.parseInt(tip1[i]);
			int n2 = Integer.parseInt(tip2[i]);
			if(n1<n2){
				result = -1;
				break;
			}
			if(n1>n2){
				result = 1;
				break;
			}
			if(n1==n2){
				continue;
			}
		}
		return result;
	}
	
	/**
	 * 判断IP地址ipX是否在ip1 和ip2的范围之内
	 * @param ip1
	 * @param ip2
	 * @param ipX
	 * @return
	 */
	public static boolean isIpBetween(String ip1,String ip2,String ipX){
		int r1 = compareIp(ipX,ip1);
		if(r1 == 0)
			return true;
		
		int r2 = compareIp(ipX,ip2);
		if(r2 == 0)
			return true;
		
		if(r1 == 1 && r2 == -1)
			return true;
		
		if(r1 == -1 && r2 == 1)
			return true;
		
		return false;
		
	}
	
	
	

	/**
	 * 计算规则字符串的值
	 * 使用eviMap中存在的占位符的值替换指定字符串表达式中的占位符并返回替换后的字符串
	 * 
	 *  String exp = "/userfiles/image/${date(yyyyMM)}/${date(dd)}/${filename}";
		Map ev = new HashMap();
		ev.put("date", new Date());
		ev.put("filename", 1234);
		String ret = StringUtil.evalExp(exp,ev);
		System.out.println(ret);
		输出结果：/userfiles/image/201003/18/1234
	 * @param exp
	 * @param eviMap
	 * @return
	 */
	
	public static String evalExp(String exp,Map eviMap){
		// 匹配所有的dv规则的正则表达式
		String patt = "\\$\\{[^}]+\\}";
		Pattern p3 = Pattern.compile(patt);
		Matcher m3 = p3.matcher(exp);
		while (m3.find()) {
			String rule = m3.group();
			Pattern pattern = Pattern.compile("[\\{ \\( \\) \\}]+");
			String[] strs = pattern.split(rule);
			String var = strs[1];//变量名称
			Object val = eviMap.get(var);
			String value = "";
			if(val!=null){
				if(val instanceof Date){
					String fm = (strs.length>=3?strs[2]:"yyyyMMdd");
					value = DateUtils.formatDate((Date) val, fm);
				}else{
					value = val.toString();
				} 
			}
			exp = StringUtil.replace(exp, rule, value);
		}
		return exp;
	}
	
	/**
	 * md5 for ldap
	 * @param password
	 * @return
	 */
	public static String md5_ldap(String password) {
		byte[] unencodedPassword = password.getBytes();
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			logger.error("异常",e);
			return password;
		}
		md.reset();
		md.update(unencodedPassword);
		byte[] encodedPassword = md.digest();
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < encodedPassword.length; i++) {
			if ((encodedPassword[i] & 0xff) < 0x10) {
				buf.append("0");
			}
			buf.append(Long.toString(encodedPassword[i] & 0xff, 16));
		}

		String standardMd5 = buf.toString();
		byte[] ba = new byte[standardMd5.length() / 2];
		for (int i = 0; i < standardMd5.length(); i = i + 2) {
			ba[i == 0 ? 0 : i / 2] = (byte) (0xff & Integer.parseInt(
					standardMd5.substring(i, i + 2), 16));
		}
		
		return Base64.encodeBase64String(ba);
	}

	/**
	 * 标准MD5加密 32位
	 * @author T61P
	 * @param str
	 * @return
	 */
	public static String md5_32(String str){  
	        MessageDigest messageDigest = null;  
	        try {  
	            messageDigest = MessageDigest.getInstance("MD5");  
	            messageDigest.reset();  
	            messageDigest.update(str.getBytes("UTF-8"));  
	        } catch (NoSuchAlgorithmException e) {  
	            System.out.println("NoSuchAlgorithmException caught!");  
	            System.exit(-1);  
	        } catch (UnsupportedEncodingException e) {
				logger.error("转码异常",e);
	        }
	        byte[] byteArray = messageDigest.digest();  	  
	        StringBuffer md5StrBuff = new StringBuffer();  	  
	        for (int i = 0; i < byteArray.length; i++)  {  
	            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)  
	                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));  
	            else  
	                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));  
	        }  
	        return md5StrBuff.toString();  
	}

	public static String valueOf(Object object) {
		if(object == null)
			return "";
		if(object instanceof String)
			return (String) object;
		else{
			return object.toString();
		}
	} 
	
	/**
	 * 生成随机数字
	 * @param length	随机数字的长度
	 */
	public static String randomNumber(int length) {
		String[] digits = {"1","2","3","4","5","6","7","8","9","0"}; 
		Random rnum = new Random(new Date().getTime()); 

		for(int i = 0; i < digits.length; i++) 
		{ 
		int index = Math.abs(rnum.nextInt()) % 10; 
		String tmpDigit = digits[index]; 
		digits[index] = digits[i]; 
		digits[i] = tmpDigit; 
		} 

		String returnStr = digits[0]; 
		for (int i = 1; i < length; i++) 
		{ 
		returnStr = digits[i] + returnStr; 
		} 
		return returnStr; 
	}
	

	/**
	 * 中文Unicode码转换
	 * @param str
	 * @return
	 */
	public static String unicodeToString(String str) {
		 
	    Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");    
	    Matcher matcher = pattern.matcher(str);
	    char ch;
	    while (matcher.find()) {
	        ch = (char) Integer.parseInt(matcher.group(2), 16);
	        str = str.replace(matcher.group(1), ch + "");    
	    }
	    return str;
	}
	/**
	 * 根据正则表达式中匹配到符合规则的字符串
	 * @param regex
	 * @param text
	 * @return
	 */
	public static String match(String regex, String text) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(text);
        if (m.find()) {
            return m.group(1);
        } else {
            return null;
        }
    }

	/**
	 * 将对象的属性名及父类的属性名，转换成sql查询的条件
	 * @param clazz
	 * @return
     */
	public static String sqlName(Class clazz){

		String result = "";
		//父类参数
		Field[] fs = clazz.getSuperclass().getDeclaredFields();
		for(int i=0;i<fs.length;i++){
			if(!"serialVersionUID".equals(fs[i].getName())&&!"sortNo".equals(fs[i].getName())&&!(fs[i].getName().charAt(0)>='A'&&fs[i].getName().charAt(0)<='Z')){
				result += " " + toSqlName(fs[i].getName()) + " as " + fs[i].getName() + " ,";
			}

		}
		//本身参数
		Field[] fs1 = clazz.getDeclaredFields();
		for(int i=0;i<fs1.length;i++){
			if(!"serialVersionUID".equals(fs1[i].getName())&&!"sortNo".equals(fs1[i].getName())&&!(fs1[i].getName().charAt(0)>='A'&&fs1[i].getName().charAt(0)<='Z')){
				result += " " + toSqlName(fs1[i].getName()) + " as " + fs1[i].getName() + " ,";
			}
		}
		return  result.substring(0,result.length()-1);
	}

	/**
	 * 将驼峰命名规则的name转换成数据库的name
	 * @param objName
	 * @return
     */
	public static String toSqlName(String objName){
		String result = "";
		for(int j=0;j<objName.length();j++){
			char c = objName.charAt(j);
			if(c>='A'&&c<='Z'){
				c+=32;
				result +="_"+c;
			}else{
				result +=c;
			}
		}
		return result;
	}
	/**
	 * 获取主机名称
	 * @return
     */
	public static String getHostName(){
		try {
			String host = InetAddress.getLocalHost().getHostName();
			return host;
		} catch (UnknownHostException e) {
			logger.error("",e);
		}
		return "";
	}
	public static String getHostIp(){
		try {
			String host = InetAddress.getLocalHost().getHostAddress();
			return host;
		} catch (UnknownHostException e) {
			logger.error("",e);
		}
		return "";
	}
	public static String sqlIn(List<String> list){
		String result = "";
		for (int i = 0; i < list.size(); i++) {
			result += " '"+list.get(i)+"' ";
			if(i != list.size()-1){
				result += " , ";
			}
		}
		return result;
	}
	public static String sqlLike(String str){
		return " '%"+str+"%' ";
	}

	public static String strDef = "0123456789abcdefghijklmnopqrstuvwsyzABCDEFGHIJKLMNOPQRSTUVWSYZ";

	/**
	 * 进制转换成62进制，数字和字母大小写
	 * @param num
	 * @return
     */
	public static String radixConvert(long num){
		return radixConvert(num,62,strDef);
	}

	/**
	 * 进制转换
	 * @param num 传入的数字
	 * @param radix 要转换的进制
	 * @return
	 */
	public static String radixConvert(long num, int radix,String strDef) {
		long temp = num / radix;
		String c = strDef.charAt((int) (num % radix)) + "";
		if(temp == 0){
			return c;
		}
		return radixConvert(temp,radix,strDef) + c;
	}


}
