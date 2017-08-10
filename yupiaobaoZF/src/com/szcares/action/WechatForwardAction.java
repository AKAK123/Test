package com.szcares.action;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.szcares.util.CommonUtils;

/**
 * 微信转发处理控制类
 * @author yaoxc
 *
 */
public class WechatForwardAction extends HttpServlet {

	private static final long serialVersionUID = 201150033645621356L;
	private static final Logger logger = Logger.getLogger(WechatForwardAction.class);
	private static final int MAX_STR_LEN = 1024;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doPost(req, resp);
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		try {//【runqian 2017-07-17】sonar Incomplete Servlet Error Handling bug 修复
			 
			String serviceUrl = CommonUtils.strProcessing(req.getParameter("serviceUrl"));
	    	String reqParameter = CommonUtils.strProcessing(req.getParameter("reqParameter"));
	    	System.out.println("-------------reqParameter="+reqParameter);
			URL url = null;
			BufferedReader reader = null;
			Reader reader2 = null;
			HttpURLConnection connection = null;
			OutputStreamWriter out = null;
			OutputStream outputStream = null;
			InputStream inputStream = null;
			String requestMethod = "1".equals(reqParameter)? "GET" : "POST";
			String result = null;
			try {
				url = new URL(serviceUrl);
				connection = (HttpURLConnection)url.openConnection();
				connection.setDoOutput(true);
				connection.setDoInput(true);
				connection.setRequestMethod(requestMethod);
				
				if (!"1".equals(reqParameter)) {
					outputStream = connection.getOutputStream();
					out = new OutputStreamWriter(outputStream, "UTF-8");
					out.write(reqParameter);
					out.flush();
				}
				
				inputStream = connection.getInputStream();
				reader2 = new InputStreamReader(inputStream,"UTF-8");
				reader = new BufferedReader(reader2);
				
				StringBuffer buffer = new StringBuffer();
				int intc;
				while ((intc = reader.read()) != -1) {
					char c = (char)intc;
					if(c=='\n'){
						break;
					}
					if(buffer.length()>=MAX_STR_LEN){
						try {
							throw new Exception("input too length");
						} catch (Exception e) {
							logger.error("throw input too length Exception");;
						}
					}
					buffer.append(c);
	            }
				result = buffer.toString();
				
			} catch (IOException e) {
				logger.error("WechatForwardAction.doPost 发生异常");
			} finally {
				if(inputStream != null) {//【runqian 2017-07-15】sonar bug 资源未关闭修复
					try {
						inputStream.close();
					} catch (Exception e2) {
						logger.error("inputStream close exception!");
					}
				}
				if(outputStream != null) {//【runqian 2017-07-15】sonar bug 资源未关闭修复
					try {
						outputStream.close();
					} catch (Exception e2) {
						logger.error("outputStream close exception!");
					}
				}
				if(out != null) {//【runqian 2017-07-15】sonar bug 资源未关闭修复
					try {
						out.close();
					} catch (Exception e2) {
						logger.error("out close exception!");
					}
				}
				if(connection != null) {
					try {
						connection.disconnect();
					} catch (Exception e2) {
						logger.error("connection disconnect exception!");
					}
				}
				if(reader2 != null) {
					try {
						reader2.close();
					} catch (Exception e2) {
						logger.error("reader2 close exception!");
					}
				}
				if(reader != null) {
					try {
						reader.close();
					} catch (Exception e2) {
						logger.error("reader close exception!");
					}
				}
			}
			//logger.info("返回结果为： "+  result);
			resp.setCharacterEncoding("UTF-8");
			PrintWriter printWriter = null;//【runqian 2017-07-15】sonar bug 资源未关闭修复
			try {
				printWriter = resp.getWriter();
				printWriter.print(CommonUtils.strProcessing(result));
				printWriter.flush();
			} catch (Exception e) {
				logger.error("printWriter exception occurred!");
			} finally {
				if(printWriter != null) {
					try {
						printWriter.close();
					} catch (Exception e2) {
						logger.error("printWriter close exception!");
					}
				}
			}
			
		} catch (Exception e) {
			logger.error("WechatForwardAction.doPost 异常");
		}
	}
}
