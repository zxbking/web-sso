package com.zxbking.web.sso.comm.utils;

import net.sf.json.util.PropertyFilter;

/**
 * JSON化过滤器实现
 * @author tantyou
 */
public class JSONPropertiesFilter implements PropertyFilter {
	public JSONPropertiesFilter(String filters[]){
		this.filters=filters;
	}
	private String[] filters=null;

	public boolean apply(Object source, String name, Object value) {
		for (String filter : filters) {
			if(name.equals(filter)){
				return false;
			}
		}
		return true;
	}
}