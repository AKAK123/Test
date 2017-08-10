package com.szcares.action;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.szcares.bean.MailBean;
import com.szcares.util.SendMailUtil;

/**
 * IBR+ 推送Action
 * @author huc
 *
 */
public class FlightIBEPushAction extends HttpServlet{
	
	private static final long serialVersionUID = 4128181190512321180L;
	private static final String IBE_PUSH_URL = "http://116.31.82.110:8085/PolicySPFeedback.ashx";
	
	private static final Logger logger = Logger.getLogger(FlightIBEPushAction.class); 
	
	private static final int MAX_STR_LEN = 1024;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}
	
	
	/**
	 * IBE+ 推送转发
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {//【runqian 2017-07-17】sonar Incomplete Servlet Error Handling bug 修复
		req.setCharacterEncoding("utf-8");
		resp.setContentType("text/html;charset=utf-8");
		PrintWriter writer = null;
		InputStream inputStream = null;
		try {
			writer = resp.getWriter();
			inputStream = req.getInputStream();
			String result = "";
			if (inputStream.read() != -1) {
				result += inputStream.read();
			}
			log(result);
			int size = inputStream.available();
			
			if (!(size <= 0)) {
				String post = post(inputStream);
				writer.write(post);
				writer.write("推送成功");
				logger.info(String.format("推送结果: %s", post));//【runqian 2017-07-15】sonar 日志 bug 修复
			}else {
				writer.write("文件流为空！");
				logger.info("文件流为空！");
			}
			
		} catch (Exception e) {
			logger.error("FlightIBEPushAction.doPost 出现异常");
		}finally{
			if(null!=writer){
			writer.close();
			}
			if(null!=inputStream){
			inputStream.close();
			}
		}
		
		} catch (Exception e) {
			logger.error("FlightIBEPushAction.doPost 异常");
		}
		
	}
	
	
	
	/**
	 * IBE+ 推送网络请求
	 * @param in
	 * @return
	 */
	public String post(InputStream in){
		 String result = "";
		 BufferedReader ins = null;
		 OutputStream dos = null;
		 OutputStream outputStream = null;
		try {
			URL url = new URL(IBE_PUSH_URL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestMethod("POST");
			conn.setUseCaches(false);
			conn.setInstanceFollowRedirects(true);
			conn.setRequestProperty("Content-Type", "text/html");
			conn.connect();
			outputStream = conn.getOutputStream();
			dos = new DataOutputStream(outputStream);
			byte[] xmlBytes = new byte[in.available()];
			in.read(xmlBytes);
			dos.write(xmlBytes);
			
			// 定义 BufferedReader输入流来读取URL的响应
			ins = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			/**modify by chencl 2017-7-17**/
			StringBuffer buf = new StringBuffer();
			int intc;
			while ((intc = ins.read()) != -1) {
				char c = (char)intc;
				if(c=='\n'){
					break;
				}
				if(buf.length()>=MAX_STR_LEN){
					try {
						throw new Exception("input too length");
					} catch (Exception e) {
						logger.error("throw input too length Exception");;
					}
				}
            	buf.append(c);
            }
			result = buf.toString();
			
			} catch (Exception e) {
				MailBean mailBean = new MailBean();
				mailBean.setSubject("系统异常："+e.getMessage());
				mailBean.setToEmails(new String[] { "admin@geeknets.com" });
				mailBean.setContext(e.toString());
				try {
					SendMailUtil.sendMail(mailBean);
				} catch (UnsupportedEncodingException e1) {
					logger.error("FlightIBEPushAction.post 发生 UnsupportedEncodingException 异常");
				} catch (MessagingException e1) {
					logger.error("CalculateFlow.post 发生 MessagingException 异常");
				}
			} finally {
				if(ins != null) {//【runqian 2017-07-15】sonar bug 资源未关闭修复
					try {
						ins.close();
					} catch (Exception e2) {
						logger.error("ins close exception!");
					}
				}
				if(outputStream != null) {//【runqian 2017-07-15】sonar bug 资源未关闭修复
					try {
						outputStream.close();
					} catch (Exception e2) {
						logger.error("outputStream close exception!");
					}
				}
				if(dos != null) {
					try {
						dos.flush();
						dos.close();
					} catch (Exception e2) {
						logger.error("dos close exception!");
					}
				}
				if(in != null) {
					try {
						in.close();
					} catch (Exception e2) {
						logger.error("in close exception!");
					}
				}
				
			}
		return result; 
	}
	
	

	
	

}
