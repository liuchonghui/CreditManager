package com.sina.engine.base.request.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Locale;

public class Utils {
	
	/**
	 * 对象的属性名称和属性值转换为map
	 * @param model
	 * @return
	 */
	public static LinkedHashMap<String, Object> getAllRequestMap(Object model){
		LinkedHashMap<String, Object> requestDataMap = new LinkedHashMap<String, Object>();
		if(model == null){
			return requestDataMap;
		}
		Field[] superFields = model.getClass().getSuperclass().getDeclaredFields();
		for(int i=0;i<superFields.length;i++){ 
	    	String name = superFields[i].getName();
	    	Object value = getFieldValueByName(superFields[i].getName(), model);
	    	if(value != null){
	    	   requestDataMap.put(name, value);
	    	}
	    }
		
		Field[] fields = model.getClass().getDeclaredFields(); 
	    for(int i=0;i<fields.length;i++){ 
	    	String name = fields[i].getName();
	    	Object value = getFieldValueByName(fields[i].getName(), model);
	    	if(value != null){
	    	   requestDataMap.put(name, value);
	    	}
	    }
		return requestDataMap;
	}
	
	public static LinkedHashMap<String, Object> getSelfRequestMap(Object model){
		LinkedHashMap<String, Object> requestDataMap = new LinkedHashMap<String, Object>();
		if(model == null){
			return requestDataMap;
		}
		
		Field[] fields = model.getClass().getDeclaredFields(); 
	    for(int i=0;i<fields.length;i++){ 
	    	String name = fields[i].getName();
	    	Object value = getFieldValueByName(fields[i].getName(), model);
	    	if(value != null){
	    	   requestDataMap.put(name, value);
	    	}
	    }
		return requestDataMap;
	}
	
	/**
	 * 创建缓存和任务map中的key
	 * @param model
	 * @return
	 */
	public static String creatMapKey(Object model){
		String mapKey = "";
		if(model == null){
			return mapKey;
		}
		LinkedHashMap<String, Object> requestMap = getAllRequestMap(model);
		for(String key : requestMap.keySet()){	
			mapKey = mapKey+requestMap.get(key);
		}
		return mapKey;
	}
	
	/**
	 * 根据属性名称获取对象属性值
	 * @param fieldName
	 * @param o
	 * @return
	 */
	public static Object getFieldValueByName(String fieldName, Object o) { 
        try {  
            String firstLetter = fieldName.substring(0, 1).toUpperCase(Locale.getDefault());  
            String getter = "get" + firstLetter + fieldName.substring(1);  
            Method method = o.getClass().getMethod(getter, new Class[] {});  
            Object value = method.invoke(o, new Object[] {});  
            return value;  
        } catch (Exception e) {  
            
            return null;  
        }  
    } 
	/**
	 * 克隆对象
	 * @param src
	 * @return
	 * @throws RuntimeException
	 */
	@SuppressWarnings("unchecked")
	public static <T> T cloneTo(T src) throws RuntimeException {
		ByteArrayOutputStream memoryBuffer = new ByteArrayOutputStream();
		ObjectOutputStream out = null;
		ObjectInputStream in = null;
		T dist = null;
		try {
			out = new ObjectOutputStream(memoryBuffer);
			out.writeObject(src);
			out.flush();
			in = new ObjectInputStream(new ByteArrayInputStream(memoryBuffer.toByteArray()));
			dist = (T) in.readObject();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (out != null)
				try {
					out.close();
					out = null;
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			if (in != null)
				try {
					in.close();
					in = null;
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
		}
		return dist;
	}
	
	
//	public static Properties  getConfigProperties(Context con){
//	    Properties properties = new Properties();
//    	try {
//    		InputStream is = con.getAssets().open("config.properties");
//	    	properties.load(is);
//	    	return properties;
//    	} catch (Exception e) {
//    	   e.printStackTrace();
//    	}
//    	return null;
//    }
	
//	public static Properties  getSwitchChannelProperties(Context con){
//	    Properties properties = new Properties();  
//    	try {  
//    		InputStream is = con.getAssets().open("switch_channelid.properties");  
//	    	properties.load(is);  
//	    	return properties;
//    	} catch (Exception e) {  
//    	   e.printStackTrace();  
//    	}  
//    	return null;
//    }
	
}
