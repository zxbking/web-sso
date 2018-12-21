package com.zxbking.web.sso.comm.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.sf.ezmorph.object.DateMorpher;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;
import net.sf.json.util.JSONUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

/**
 * 解析json字符串
 * 该解析类不建议使用，请使用JSONUtil2类
 * @author tantyou
 * @date 2008-2-10
 */
@SuppressWarnings({"unchecked","rawtypes"})
public class JSONUtil {
	private static final Logger logger = LoggerFactory.getLogger(JSONUtil.class);
	/**
	 * 将简单的json字符串解析到map中，并以集合的形式返回
	 * 
	 * @param json
	 * @return
	 */
	
	public static List<Map<String, Object>> parseList(String json) {
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		if(StringUtils.isEmpty(json)){
			return list;
		}
		JSONArray jsonArray = JSONArray.fromObject(json);
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObj = jsonArray.getJSONObject(i);
			Map map = new HashMap();
			
			for (Object key : jsonObj.keySet()) {
				String sKey = (String) key;
				Object value = jsonObj.get(sKey);
				if(value instanceof JSONObject){
					JSONObject jsonValue = (JSONObject)value;
					if(jsonValue.isNullObject()){
						value = null;
					}
				}
				
				map.put(sKey,value);
			}
			list.add(map);
		}
		return list;
		
	}
	

	/**
	 * 解析单个对象
	 * 
	 * @param json
	 * @return
	 */
	public static Map parseObject(String json) {
		Map resultMap = new HashMap();
		JSONObject jsonObject = JSONObject.fromObject(json);
		for (Object key : jsonObject.keySet()) {
			Object obj= jsonObject.get(key);
			if(obj!=null && obj instanceof JSONObject){
				if(((JSONObject)obj).isNullObject()){
					obj = null;
				}
			}
			resultMap.put(key,obj);
		}
		return resultMap;
	}
	
	/**
	 * 解析单个对象
	 * 拥有日期型字段并且日期型字段以{time:2333322341}形式存在的时候，将该元素解析成Date对象
	 * @param json
	 * @return
	 */
	public static Map parseObjectHasDate(String json) {
		Map resultMap = new HashMap();
		JSONObject jsonObject = JSONObject.fromObject(json);
		for (Object key : jsonObject.keySet()) {
			Object obj= jsonObject.get(key);
			if(obj!=null && obj instanceof JSONObject){
				if(((JSONObject)obj).isNullObject()){
					obj = null;
				}else{
					//如果是日期型
					Object objX =((JSONObject)obj).get("time");
					if(objX!=null){
						obj = new Date((Long)objX);
					}
				}
			}
			resultMap.put(key,obj);
		}
		return resultMap;
	}
	
	/**
	 * 将Map中的对象序列化成JSON字符串
	 * @param map
	 * @return
	 */
	public static String mapToJson(Map map){
//		JsonConfig config = new JsonConfig();
//		config.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
//		config.setJsonPropertyFilter(new JSONPropertiesFilter(new String[]{}));
		JSONObject obj= JSONObject.fromObject(map);
		return obj.toString();
	}

	/**
	 * 将集合中的对象JSON化
	 * 
	 * @param obj
	 * @param fields
	 * @return
	 */

	public static String listToJson(Collection obj, String[] fields) {
		JSONPropertiesFilter filter = new JSONPropertiesFilter(fields);
		JsonConfig jsconfig = new JsonConfig();
		jsconfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
		jsconfig.setJsonPropertyFilter(filter);
		JSONArray json = JSONArray.fromObject(obj, jsconfig);
		return json.toString();
	}

	/**
	 * 将一个对象json化
	 * 
	 * @param obj
	 * @param fields
	 * @return
	 */
	public static String objectToJson(Object obj, String[] fields) {
		JSONPropertiesFilter filter = new JSONPropertiesFilter(fields);
		JsonConfig jsconfig = new JsonConfig();
		jsconfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
		jsconfig.setJsonPropertyFilter(filter);
		return JSONObject.fromObject(obj, jsconfig).toString();
	}
	
	/**
	 * 将对象转换为map
	 * @param obj
	 * @param fields
	 * @return
	 */
	public static Map objectToMap(Object obj,String[] fields){
		String json = objectToJson(obj, fields);
		return parseObject(json);
	}

	/**
	 * 集合对象JSON化,该方法主要用于使用jdbc查询出的列表集合
	 * @param obj
	 * @param fields
	 * @return
	 */
	public static String listToJson(List<Map> obj,String[] fields) {
		JSONUtils.getMorpherRegistry().registerMorpher(
				new DateMorpher(new String[] { "yyyy-MM-dd" }));
		JsonConfig jsconfig = new JsonConfig();
		if(fields!=null && fields.length>0){
			JSONPropertiesFilter filter = new JSONPropertiesFilter(fields);
			jsconfig.setJsonPropertyFilter(filter);
		}
		jsconfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
		JSONArray json = JSONArray.fromObject(obj, jsconfig);
		String jsonStr = json.toString();
		return jsonStr;
	}
	
	public static String listToJson(List<Map> obj) {
		return listToJson(obj,null);
	}
	
	public static String getPropertyValueByJsonObject(String strJson,String propertyName){
		
		JSONObject jsonObject = JSONObject.fromObject(strJson);

		return jsonObject.getString(propertyName);
	}
	
	public static String getPropertyValuesByJsonArray(String strJson,String propertyName){
		
		StringBuffer sb = new StringBuffer();
		JSONArray jsonArray =  JSONArray.fromObject(strJson);
		for (Iterator iterator = jsonArray.iterator(); iterator.hasNext();) {
			Object object = (Object) iterator.next();
			sb.append("'" +getPropertyValueByJsonObject(object.toString(),propertyName) + "',");
		}
		
		return StringUtil.delEndString(sb.toString(), ",");
	}

	public static String getPropertyValues2ByJsonArray(String strJson,String propertyName){
		
		StringBuffer sb = new StringBuffer();
		JSONArray jsonArray =  JSONArray.fromObject(strJson);
		for (Iterator iterator = jsonArray.iterator(); iterator.hasNext();) {
			Object object = (Object) iterator.next();
			sb.append(getPropertyValueByJsonObject(object.toString(),propertyName) + ",");
		}
		
		return StringUtil.delEndString(sb.toString(), ",");
	}

	
//	public static void main(String[] args) {
//		String json = "[{\"ID\": \"40288aa02910ed8501291106718d0012\", \"FIELD1\": \"总是拿\", \"FIELD2\": \"总是3\", \"FIELD3\": null}]";
//		List<Map<String,Object>> xx = parseList(json);
//		for (Map<String, Object> map : xx) {
//			for (String key : map.keySet()) {
//				String value = (String)map.get(key);
//				if(value == null){
//					System.out.println("空");
//				}
//				System.out.println(key + ":" + value);
//			}
//		}
//	}
	
	
	public static void setCheckedByTreeIds(JSONArray jsonArr, List<String> treeIds, String idIdentify, String childIndetify) {
		for (Iterator iterator = jsonArr.iterator(); iterator.hasNext();) {
			JSONObject obj = (JSONObject) iterator.next();
			String id = (String) obj.get(idIdentify);

			if (treeIds.contains(id)) {
				obj.put("checked", "true");
			}
			Object childObj = obj.get(childIndetify);
			if (childObj != null && childObj instanceof JSONArray) {
				setCheckedByTreeIds((JSONArray) childObj, treeIds, idIdentify, childIndetify);
			} else {
				continue;
			}
		}
	}


    /**
     * 使用jackson转换对象到json串
     * @param obj
     * @return
     */
    public static String objectToJson(Object obj) {
        ObjectMapper mapper = new ObjectMapper();
        // Convert object to JSON string
        String jsonStr = "";
        try {
            jsonStr =  mapper.writeValueAsString(obj);
        } catch (IOException e) {
			logger.error("IO异常",e);
        }
        return jsonStr;
    }
}
