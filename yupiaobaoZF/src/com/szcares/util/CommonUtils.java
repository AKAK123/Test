package com.szcares.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.UnknownHostException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.szcares.action.ReRuleXmlInfoAction;

import net.sf.json.JSONObject;

/**
 * 共通方法
 * @author yaoxc
 */
public class CommonUtils {
	
	private static final Logger logger = Logger.getLogger(CommonUtils.class);

	/**
	 * 获取短信内容
	 * 
	 * @param 短信类型
	 *            String
	 * @param 拼接内容
	 *            String
	 * @return 短信内容
	 */
	public static String getMessageContent(String type,  Object array[]) {

		String messageContent = MessageConstants.RETURN_MSG_BLANK;
		// 读写properties文件
		Properties properties = new Properties();
		InputStream inputStream = null;
		try {

			// 加载设有content内容的properties文件
			String webPath = CommonUtils.class.getResource("/").getPath();
			String url = webPath.substring(1, webPath.length())
					+ "SendSms.properties";
			inputStream = new FileInputStream(url);
			properties.load(inputStream);

			// 取.content为key的值
			String msg = properties.getProperty(type + ".content");
			if (msg != null) {
				messageContent = MessageFormat.format(msg, array);
			}
			return messageContent;
		} catch (IOException ex) {
			logger.error("CommonUtils.getMessageContent 发生 IOException 异常!");
		} catch (Exception e) {
			logger.error("CommonUtils.getMessageContent 发生异常!");
		} finally {
			try {
				//modify by chencl 2017-7-12
				if(null!=inputStream){
				inputStream.close();
				}
			} catch (IOException e) {
				logger.error("close inputStream 发生 IOException 异常!");
			}
		}
		return messageContent;
	}
	/**
	 * 获取服务器真实动态处理的请求来源Referer方法
	 * @param reqIp 请求服务器的真实IP地址
	 * @return IP字符串
	 * @throws UnknownHostException
	 */
	public static String getRealDynamicReferer(String reqIp) throws UnknownHostException{
		String url = "http://"+reqIp;
	
		return (Constants.REQUEST_URL_SC1.equals(url) || Constants.REQUEST_URL_SC2.equals(url))? url : Constants.REQUEST_URL;
	}
	
	/**
	 * 对象字符串 的处理
	 * @param str 处理字符串对象
	 * @return "" or 非null本身
	 */
	public static String strProcessing(String str) {
		
		return str == null ? "" : str.trim();
	}
	
	/**
	 * 根据商户号获取不同MD5文件
	 * @param orgId 商户号
	 * @return 不同商户对应的MD5文件名称,如果商户号ID为null，则返回空串。
	 */
	public static String getMD5FileNameById(String orgId) {
		
		String fileName = "";
		if(!"".equals(strProcessing(orgId))){
			if(Constants.ORGID_WY.equals(orgId)){
				fileName = "MD5WY.txt";
			}else if(Constants.ORGID_KY.equals(orgId)){
				fileName = "MD5KY.txt";
			}else{
				fileName = "MD5YX.txt";
			}
		}
		
		return fileName;
	}
	
	/**
	 * 将符合要求格式的字符串封装进map集合
	 * @param reqParameter 字符串参数（参数格式必须为：payamount=0.01&apptype=B2C&bankid=ALIPAY_MOBILE）
	 * @return map集合对象。
	 */
	public static Map<String,String> strToMap(String reqParameter) {
		
		Map<String,String> map = null;
		if(!StringUtils.isBlank(reqParameter)){
			map = new HashMap<String, String>();
			String[] params = reqParameter.split("&");
			String[] keyValues = null;
			for (String param : params) {
				keyValues = param.split("=");
				
				if(keyValues.length > 1)
					map.put(keyValues[0], keyValues[1]);
				else
					map.put(keyValues[0], "");
			}
		}
		return map;
	}

	
	/**
	 * 获取对象中所有的值 放入JSON
	 * @param object
	 * @return
	 */
	public static JSONObject getJSON(Object object){
		Class<? extends Object> className=object.getClass();
		JSONObject json=new JSONObject();
		Field[] declaredFields = className.getDeclaredFields();
		for (Field field : declaredFields) {
			try {
				String val = field.get(object)+"";
				String name = field.getName();
				json.put(name, val);
			} catch (Exception e) {
				logger.error("CommonUtils.getJSON 发生异常");
			} 
			
		}
		return json;
	}
}
