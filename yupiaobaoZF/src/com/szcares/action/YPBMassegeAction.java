package com.szcares.action;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;

import com.szcares.util.Constants;
import com.szcares.util.JsonUtil;
import com.szcares.util.MessageConstants;

/**
 * 短信发送接口
 * @author yjian
 *
 */
public class YPBMassegeAction extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1461265794933122396L;
	
	private static final Logger logger = Logger.getLogger(YPBMassegeAction.class);
	
	private static Properties properties = null;
	
	public static String getProperty(String key) {
		return properties.getProperty(key);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		//req.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html;charset=UTF-8");
		
		String old = req.getParameter("message");
		String message = old == null ? null : new String(old.getBytes("ISO8859-1"),"UTF-8");		
		String phone = req.getParameter("phone");
		
		String result = smsSend(phone,message);

		resp.getWriter().print(result);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {//【runqian 2017-07-17】sonar Incomplete Servlet Error Handling bug 修复
			req.setCharacterEncoding("UTF-8");
			resp.setContentType("text/html;charset=UTF-8");
			
			String message = req.getParameter("message");		
			String phone = req.getParameter("phone");
			
			String result = smsSend(phone,message);

			resp.getWriter().print(result);
		} catch (Exception e) {
			logger.error("YPBMassegeAction.doPost 异常");
		}
	}
	
	public String smsSend(String phone,String message) throws UnsupportedEncodingException, IOException{
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		if(message == null || phone == null){
			result.put("code", "-1");
			result.put("msg", "请提交短信发送参数");
		}else{
			HttpClient httpclient = new HttpClient();
			PostMethod post = new PostMethod(Constants.POST_URL);//接口地址
			post.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,MessageConstants.CONTENT_CHARSET);
			post.addParameter("SpCode", Constants.SP_CODE);//企业编号
			post.addParameter("LoginName", Constants.LOGIN_NAME);//用户名称
			post.addParameter("Password", getProperty(Constants.LOGIN_PASSWORD));//用户密码
			post.addParameter("MessageContent", message);//短信内容
			post.addParameter("UserNumber", phone);//手机号码
			
			httpclient.executeMethod(post);

			String info = new String(post.getResponseBody(),
					MessageConstants.CONTENT_CHARSET);
			logger.info(String.format("短信发送执行结果: %s", info));//【runqian 2017-07-15】sonar 日志 bug 修复
			result = smsResultAnalyze(info);
		}
		return JsonUtil.map2json(result);
	}
	
	/**
	 * 解析短信发送执行结果
	 * @param str
	 * @return
	 */
	public Map<String, Object> smsResultAnalyze(String str) {
		Map<String, Object> result = new HashMap<String, Object>();
		String code = str.split("&")[0].split("=")[1];
		String msg = str.split("&")[1].split("=")[1];
		result.put("code", code);
		result.put("msg", msg);
		return result;
	}

}
