package com.szcares.action;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public class DoServerToForward extends HttpServlet{
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(DoServerToForward.class);
	
	private static final long serialVersionUID = 1L;

	private static final int MAX_STR_LEN = 1024;

	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}
	
	
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		logger.info("DoServerToForward::::::::begin");
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("text/json");
		PrintWriter out = resp.getWriter();
		String url = req.getParameter("url");
		String param = req.getParameter("param");
		String strByParam = sendGet(url, param);
		logger.info(String.format("resp::::::::::: %s", strByParam));//【runqian 2017-07-15】sonar 日志 bug 修复
		out.print(strByParam);
		logger.info("DoServerToForward::::::::end");
	}
	
	/**
     * 向指定URL发送GET方法的请求
     * 
     * @param url
     *            发送请求的URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        Reader reader = null;
        InputStream inputStream = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
//            // 获取所有响应头字段
//            Map<String, List<String>> map = connection.getHeaderFields();
//            // 遍历所有的响应头字段
//            for (String key : map.keySet()) {
//                System.out.println(key + "------->>>>>>" + map.get(key));
//            }
            // 定义 BufferedReader输入流来读取URL的响应
            inputStream = connection.getInputStream();
            reader = new InputStreamReader(inputStream,"UTF-8");//【runqian 2017-07-15】sonar bug 资源未关闭修复
            in = new BufferedReader(reader);
            /**modify by chencl 2017-7-17**/
            StringBuffer buf = new StringBuffer();
            int intc;
		    while ((intc = reader.read()) != -1) {
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
            System.out.println("发送GET请求出现异常！");
        }
        // 使用finally块来关闭输入流
        finally {
        	if(inputStream != null) {
        		try {
        			inputStream.close();
				} catch (Exception e) {
					logger.info("close inputStream exception!");
				}
        	}
        	if(reader != null) {
        		try {
					reader.close();
				} catch (Exception e) {
					logger.info("sendGet function close reader exception!");
				}
        	}
        	if(in != null) {
        		try {
        			in.close();
				} catch (Exception e) {
					logger.info("sendGet function close in exception!");
				}
        	}
        }
        return result;
    }

}
