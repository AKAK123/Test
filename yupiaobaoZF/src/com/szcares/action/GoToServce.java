package com.szcares.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.szcares.exception.HttpInvokeException;
import com.szcares.util.HttpClientDemo;


public class GoToServce extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6948882581318020213L;
	private static final Logger logger = Logger.getLogger(GoToServce.class);

	  public void init() throws ServletException
	  {
	    super.init();
	    logger.info("yupiaobao GoToServce ");
	  }

	  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
	    throws ServletException, IOException
	  {
		  try {//【runqian 2017-07-17】sonar Incomplete Servlet Error Handling bug 修复
			  logger.info("do get method begin");
			  invokeBusinessLogic(req, resp);
			  logger.info("do get method end");
		  } catch (Exception e) {
			  logger.error("GoToServce.doGet 异常");
		  }
	  }

	  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
	    throws ServletException, IOException
	  {
	    logger.info("do post method begin");
	    invokeBusinessLogic(req, resp);
	    logger.info("do post method end");
	  }

	  private void invokeBusinessLogic(HttpServletRequest req, HttpServletResponse resp)
	    throws ServletException, IOException
	  {
//		String ip = req.getRemoteAddr();
//		System.out.println(ip+"----------------");
//		String userIndex = UserContainer.getUserIndexByIP(ip);
//		System.out.println(userIndex+"----------------userIndex");
//	    if (StringUtils.isEmpty(userIndex))
//	    {
//	    	resp.sendError(403, "");
//	    	logger.info("user validation failed: the ip [" + ip + "] is not valid");
//	    	return;
//	    }
			
//		}
		 	String serviceUrl=req.getParameter("serviceUrl");
	    	String reqStr=req.getParameter("reqStr");
	    	String username=req.getParameter("username");
	    	String pwd=req.getParameter("pwd");
	    	String xml="";
	    	if (username==null||pwd==null) {
	    		try {
					xml=HttpClientDemo.httpPost(serviceUrl, reqStr);
				} catch (HttpInvokeException e) {
					logger.error("GoToServce.invokeBusinessLogic 发生 HttpInvokeException 异常");
				}
			}else {
				xml=HttpClientDemo.Post(username, pwd, serviceUrl, reqStr);	
			}
	    	PrintWriter out=null;
	    	 try
	    	    {
	    	        resp.setHeader("Content-Encoding", "gzip");
	    	        resp.setCharacterEncoding("UTF-8");
	    	        out = resp.getWriter();
	    	        out.write(xml);
	    	        out.flush();
	    	        out.close();
	    	    }catch (Exception e) {
	    	      logger.error("GoToServce.invokeBusinessLogic 发生异常");
	    	    }finally{
	    	    	if (out!=null) {
						out.close();
					}
	    	    }
	  }
	}
