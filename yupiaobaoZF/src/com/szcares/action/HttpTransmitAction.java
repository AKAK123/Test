package com.szcares.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import net.sf.json.JSONObject;

import com.szcares.util.HttpClient;
import com.szcares.util.SecurityScanner;

/**
 * http请求转发器(使用地方：航班订阅)
 * @author Yjian
 * @time 2017-2-22
 */
public class HttpTransmitAction extends HttpServlet {
	private static final long serialVersionUID = 7818864534540332029L;
	
	private static final Logger logger = Logger.getLogger(YPBMassegeAction.class);

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		logger.info("http请求转发----------------- start --------------------------");
		//响应结果类型("XML"、"JSON")
		String resultType = request.getParameter("resultType");
		if("xml".equals(resultType)){
			response.setContentType("text/xml;charset=UTF-8");
		}else{
			response.setContentType("text/html;charset=UTF-8");
		}
		//http请求类型("post"、"get")
		String httpType = request.getParameter("httpType");
		//http请求链接
		String url = request.getParameter("httpUrl");
		
		String result = "";
		try {
			if("post".equals(httpType)){
				//post请求参数整理
				String param = request.getParameter("postParam");
				JSONObject json = JSONObject.fromObject(param);
				/*Map<String, String> paramMap = new HashMap<String, String>();
				for(Object key : json.keySet()){
					paramMap.put(key.toString(), json.get(key).toString());
				}
				result = HttpClient.post(url, paramMap);*/
				result = HttpClient.post(url, json.toString());
			}else{
				result = HttpClient.get(url);
			}
			logger.info("http请求转发成功 类型");//【runqian 2017-07-15】sonar 日志 bug 修复
		} catch (Exception e) {
			logger.info("http请求转发失败，发生异常！");//【runqian 2017-07-15】sonar 日志 bug 修复
		}
		
		logger.info("http请求转发----------------- end --------------------------");
		
		String dealResultString = SecurityScanner.htmlEncode(result);
		response.getWriter().print(dealResultString);
		
	}

}
