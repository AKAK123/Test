package com.szcares.action;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.szcares.util.CommonUtils;
import com.szcares.util.MyX509TrustManager;
/**
 * 转发处理微信公众号服务类
 * @author yaoxc
 *
 */
public class ServiceWechatAction extends HttpServlet{

	private static final long serialVersionUID = 5747835098444309390L;
	private static final Logger logger = Logger.getLogger(ServiceWechatAction.class);
	private static final int MAX_STR_LEN = 1024;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		try {//【runqian 2017-07-17】sonar Incomplete Servlet Error Handling bug 修复
			
		String serviceUrl = CommonUtils.strProcessing(req.getParameter("serviceUrl"));
    	String reqParameter = CommonUtils.strProcessing(req.getParameter("reqParameter"));
    	String username = req.getParameter("username");
    	String pwd = req.getParameter("pwd");
    	/*String reqIp = req.getRemoteAddr();
    	System.out.println("==========> serviceUrl: "+serviceUrl);
    	System.out.println("==========> reqParameter: "+reqParameter);
    	System.out.println("==========> IP: "+InetAddress.getLocalHost().getHostAddress());
    	System.out.println("==========> reqIp: "+reqIp);*/
    	
    	String result = "";
    	
    	if (username == null || pwd == null) {//以下处理微信对接
    		
    		String requestMethod = "GET";
    		if(serviceUrl.lastIndexOf("create") != -1){//创建菜单请求方式
    			requestMethod = "POST";
    		}
    		
    		OutputStream outputStream = null;//【runqian 2017-07-15】sonar bug 资源未关闭修复
    		InputStream inputStream = null;
    		InputStreamReader inputStreamReader = null;
    		BufferedReader bufferedReader = null;
    		
    		try {
    			//创建SSLContext对象，并使用我们指定的信任管理器初始化
    			TrustManager[] tm = {new MyX509TrustManager()};
    			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
    			sslContext.init(null, tm, new java.security.SecureRandom());
    			//从上述SSLContext对象中得到SSLSocketFactory对象
    			SSLSocketFactory ssf = sslContext.getSocketFactory();

    			URL url = new URL(serviceUrl);
    			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
    			conn.setSSLSocketFactory(ssf);
    			conn.setDoOutput(true);
    			conn.setDoInput(true);
    			conn.setUseCaches(false);
    			conn.setRequestMethod(requestMethod);
    			//当reqParameter不为null时向输出流写数据
    			if (!"".equals(reqParameter)) {
    				outputStream = conn.getOutputStream();
    				outputStream.write(reqParameter.getBytes("UTF-8"));
    			}
    			inputStream = conn.getInputStream();
    			inputStreamReader = new InputStreamReader(inputStream, "utf-8");
    			bufferedReader = new BufferedReader(inputStreamReader);
    			StringBuffer buffer = new StringBuffer();
    			int intc;
				while ((intc = bufferedReader.read()) != -1) {
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
    			//释放资源
    			bufferedReader.close();
    			inputStreamReader.close();
    			inputStream.close();
    			inputStream = null;
    			conn.disconnect();
    			result = buffer.toString();
    			//System.out.println("微信对接: "+result);
    		} catch (ConnectException ce) {
    			logger.error("连接超时异常！");
    		} catch (Exception e) {
    			logger.error("https请求异常！");
    		} finally {
        		if(bufferedReader != null){
    				try {
    					bufferedReader.close();
    				} catch (IOException e) {
    					logger.error("close bufferedReader exception");
    				}
    			}
    			if(inputStreamReader != null){
    				try {
    					inputStreamReader.close();
    				} catch (IOException e) {
    					logger.error("close inputStreamReader exception");
    				}
    			}
    			if(inputStream != null){
    				try {
    					inputStream.close();
    				} catch (IOException e) {
    					logger.error("close inputStream exception");
    				}
    			}
    			if(outputStream != null){
    				try {
    					outputStream.close();
    				} catch (IOException e) {
    					logger.error("close outputStream exception");
    				}
    			}
    		}
    	}//end of username == null || pwd == null
    	
	    	 try {
	    	      	PrintWriter out;
	    	        //resp.setHeader("Content-Encoding", "gzip");
	    	        resp.setCharacterEncoding("UTF-8");
	    	        out = resp.getWriter();
	    	        out.write(CommonUtils.strProcessing(result));
	    	        out.flush();
	    	        out.close();
		    } catch (Exception e) {
		      logger.error("ServiceWechatAction.doPost 异常");
		    }
		} catch (Exception e) {
			logger.error("ServiceWechatAction.doPost 异常");
		}
	}
}
