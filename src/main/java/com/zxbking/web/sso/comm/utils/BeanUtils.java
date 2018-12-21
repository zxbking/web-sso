package com.zxbking.web.sso.comm.utils;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaProperty;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 扩展Apache Commons BeanUtils, 提供一些反射方面缺失功能的封装.
 */
@SuppressWarnings({"unchecked","rawtypes"})
public class BeanUtils extends org.apache.commons.beanutils.BeanUtils {

	protected static final Log logger = LogFactory.getLog(BeanUtils.class);

	private BeanUtils() {
	}

	/**
	 * 循环向上转型,获取对象的DeclaredField.
	 *
	 * @throws NoSuchFieldException 如果没有该Field时抛出.
	 */
	public static Field getDeclaredField(Object object, String propertyName) throws NoSuchFieldException {
		return getDeclaredField(object.getClass(), propertyName);
	}

	/**
	 * 循环向上转型,获取对象的DeclaredField.
	 *
	 * @throws NoSuchFieldException 如果没有该Field时抛出.
	 */
	public static Field getDeclaredField( Class clazz, String propertyName) throws NoSuchFieldException {
		for (Class superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {
			try {
				return superClass.getDeclaredField(propertyName);
			} catch (NoSuchFieldException e) {
				// Field不在当前类定义,继续向上转型
			}
		}
		throw new NoSuchFieldException("No such field: " + clazz.getName() + '.' + propertyName);
	}

	/**
	 * 暴力获取对象变量值,忽略private,protected修饰符的限制.
	 *
	 * @throws NoSuchFieldException 如果没有该Field时抛出.
	 */
	public static Object forceGetProperty(Object object, String propertyName) throws NoSuchFieldException {

		Field field = getDeclaredField(object, propertyName);

		boolean accessible = field.isAccessible();
		field.setAccessible(true);

		Object result = null;
		try {
			result = field.get(object);
		} catch (IllegalAccessException e) {
			logger.info("error wont' happen");
		}
		field.setAccessible(accessible);
		return result;
	}

	/**
	 * 暴力设置对象变量值,忽略private,protected修饰符的限制.
	 *
	 * @throws NoSuchFieldException 如果没有该Field时抛出.
	 */
	public static void forceSetProperty(Object object, String propertyName, Object newValue)
			throws NoSuchFieldException {

		Field field = getDeclaredField(object, propertyName);
		boolean accessible = field.isAccessible();
		field.setAccessible(true);
		try {
			field.set(object, newValue);
		} catch (IllegalAccessException e) {
			logger.info("Error won't happen");
		}
		field.setAccessible(accessible);
	}

	/**
	 * 暴力调用对象函数,忽略private,protected修饰符的限制.
	 *
	 * @throws NoSuchMethodException 如果没有该Method时抛出.
	 */
	public static Object invokePrivateMethod(Object object, String methodName, Object... params)
			throws NoSuchMethodException {
		Class[] types = new Class[params.length];
		for (int i = 0; i < params.length; i++) {
			types[i] = params[i].getClass();
		}

		Class clazz = object.getClass();
		Method method = null;
		for (Class superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {
			try {
				method = superClass.getDeclaredMethod(methodName, types);
				break;
			} catch (NoSuchMethodException e) {
				// 方法不在当前类定义,继续向上转型
			}
		}

		if (method == null)
			throw new NoSuchMethodException("No Such Method:" + clazz.getSimpleName() + methodName);

		boolean accessible = method.isAccessible();
		method.setAccessible(true);
		Object result = null;
		try {
			result = method.invoke(object, params);
		} catch (Exception e) {
//			ReflectionUtils.handleReflectionException(e);
		}
		method.setAccessible(accessible);
		return result;
	}

	/**
	 * 按Filed的类型取得Field列表.
	 */
	public static List<Field> getFieldsByType(Object object, Class type) {
		List<Field> list = new ArrayList<Field>();
		Field[] fields = object.getClass().getDeclaredFields();
		for (Field field : fields) {
			if (field.getType().isAssignableFrom(type)) {
				list.add(field);
			}
		}
		return list;
	}

	/**
	 * 按FiledName获得Field的类型.
	 */
	public static Class getPropertyType(Class type, String name) throws NoSuchFieldException {
		return getDeclaredField(type, name).getType();
	}

	/**
	 * 获得field的getter函数名称.
	 */
	public static String getGetterName(Class type, String fieldName) {
		if (type.getName().equals("boolean")) {
			return "is" + StringUtils.capitalize(fieldName);
		} else {
			return "get" + StringUtils.capitalize(fieldName);
		}
	}

	/**
	 * 获得field的getter函数,如果找不到该方法,返回null.
	 */
	public static Method getGetterMethod(Class type, String fieldName) {
		try {
			return type.getMethod(getGetterName(type, fieldName));
		} catch (NoSuchMethodException e) {
//			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	public static Object getProperty2(Object bean,String propertyName){
		
		try {
//			String preMethod="";
//			
//			Field field=getDeclaredField(bean,propertyName);
//			
//			if(field.getType().getName().equals(boolean.class.getName())){
//				preMethod="is";
//			}else{
//				preMethod="get";
//			}
//			String getMethodName=preMethod+StringUtil.firstToUpper(propertyName);
			Method m=getGetterMethod(bean.getClass(), propertyName);
			Object result=m.invoke(bean);
			return result;
		} catch (Exception e) {
			System.out.println("取得对象【"+bean+"】属性【"+propertyName+"】时异常");
		}
		return null;
	}
	/**
	 * 迭代取得深层次的属性对象
	 * @param bean
	 * @param propertyName
	 * @return
	 */
	public static Object getPropertyCascade(Object bean,String propertyName){
		int idx=propertyName.indexOf(".");
		Object result=null;
		try{
		if(idx<=0){
			String firstPropertyName=propertyName.substring(0);
			result= getProperty2(bean, firstPropertyName);
		}else{
			String firstPropertyName=propertyName.substring(0,idx);
			String leftPropertyName=propertyName.substring(idx+1);
			Object tmpResult=getProperty2(bean, firstPropertyName);
			result= getPropertyCascade(tmpResult, leftPropertyName);
		}}catch(Exception ex){
			System.out.println("取得对象【"+bean+"】属性【"+propertyName+"】时异常");
		}
		return result;
	}
	
	/**
	 * 清除指定对象中所有属性值为空字符串的属性值为null
	 * @param bean
	 */
	public static void clearNonCharToNull(Object bean){
		List<Field> fields = getFieldsByType(bean, String.class);
		for (Field field : fields) {
			String propertyname = field.getName();
			Object value = getProperty2(bean, propertyname);
			if(value != null){
				if(((String)value).equals("")){
					try {
						setProperty(bean, propertyname, null);
					} catch (IllegalAccessException e) {
						logger.error("访问属性异常",e);
					} catch (InvocationTargetException e) {
						logger.error("反射调用异常",e);
					}
				}
			}
		}
	}
	
	/**
	 * 复制javabean属性,可指定是否将null值的属性进行复制
	 * @param dest
	 * @param orig
	 * @param copyNullValueProperty
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static void  copyProperties2(Object dest, Object orig,
			boolean copyNullValueProperty) throws IllegalAccessException,
			InvocationTargetException {

		PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean();

		// Validate existence of the specified beans
		if (dest == null) {
			throw new IllegalArgumentException("No destination bean specified");
		}
		if (orig == null) {
			throw new IllegalArgumentException("No origin bean specified");
		}
		// if (log.isDebugEnabled()) {
		// log.debug("BeanUtils.copyProperties(" + dest + ", " +
		// orig + ")");
		// }

		// Copy the properties, converting as necessary
		if (orig instanceof DynaBean) {
			DynaProperty[] origDescriptors = ((DynaBean) orig).getDynaClass()
					.getDynaProperties();
			for (int i = 0; i < origDescriptors.length; i++) {
				String name = origDescriptors[i].getName();
				// Need to check isReadable() for WrapDynaBean
				// (see Jira issue# BEANUTILS-61)
				if (propertyUtilsBean.isReadable(orig, name)
						&& propertyUtilsBean.isWriteable(dest, name)) {
					Object value = ((DynaBean) orig).get(name);
					copyProperty(dest, name, value);
				}
			}
		} else if (orig instanceof Map) {
			Iterator entries = ((Map) orig).entrySet().iterator();
			while (entries.hasNext()) {
				Map.Entry entry = (Map.Entry) entries.next();
				String name = (String) entry.getKey();
				if (propertyUtilsBean.isWriteable(dest, name)) {
					copyProperty(dest, name, entry.getValue());
				}
			}
		} else /* if (orig is a standard JavaBean) */{
			PropertyDescriptor[] origDescriptors = propertyUtilsBean
					.getPropertyDescriptors(orig);
			for (int i = 0; i < origDescriptors.length; i++) {
				
				String name = origDescriptors[i].getName();
				if ("class".equals(name)) {
					continue; // No point in trying to set an object's class
				}
				if (propertyUtilsBean.isReadable(orig, name)
						&& propertyUtilsBean.isWriteable(dest, name)) {
					try {
						Object value = propertyUtilsBean.getSimpleProperty(
								orig, name);
//						System.out.println("------------name:"+name+" \t  ;value:"+propertyUtilsBean.getSimpleProperty(
//								orig, name));
						if (!copyNullValueProperty && value == null) {
							
							continue;
						}
						copyProperty(dest, name, value);
					} catch (NoSuchMethodException e) {
						// Should not happen
					}
				}
			}
		}

	}

	/**
	 * 使用map中的属性设置bean属性值
	 * @param bean
	 * @param propMap
	 */
	public static void setPropertys(Object bean, Map<String,Object> propMap) {
		for (String propName : propMap.keySet()) {
			Field field = null;
			try {
				field = BeanUtils.getDeclaredField(bean.getClass(), propName);
			} catch (NoSuchFieldException e) {
			}
			if(field != null){
				Object value = propMap.get(propName);
				try {
					BeanUtils.setProperty(bean, propName, value);
				} catch (IllegalAccessException e) {
					logger.error("访问属性异常",e);
				} catch (InvocationTargetException e) {
					logger.error("反射调用异常",e);
				}
			}
		}
	}
}
