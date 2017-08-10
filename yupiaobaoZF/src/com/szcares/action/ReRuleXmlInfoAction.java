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

public class ReRuleXmlInfoAction extends HttpServlet {
	private static final long serialVersionUID = -6948882581318020213L;
	private static final Logger logger = Logger
			.getLogger(ReRuleXmlInfoAction.class);

	public void init() throws ServletException {
		super.init();
		logger.info("yupiaobao ReRuleXmlInfoAction ");
	}

	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {//【runqian 2017-07-17】sonar Incomplete Servlet Error Handling bug 修复
			logger.info("do get method begin");
			invokeBusinessLogic(req, resp);
			logger.info("do get method end");
		} catch (Exception e) {
			logger.error("ReRuleXmlInfoAction.doGet 异常");
		}
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {//【runqian 2017-07-17】sonar Incomplete Servlet Error Handling bug 修复
			logger.info("do post method begin");
			invokeBusinessLogic(req, resp);
			logger.info("do post method end");
		} catch (Exception e) {
			logger.error("ReRuleXmlInfoAction.doPost 异常");
		}
	}

	private void invokeBusinessLogic(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		String urlString = "http://10.128.150.122:8888/DomShopping/xml/AirRuleDisplayD";

		String serviceUrl = urlString;
		String reqStr = req.getParameter("reqStr");
		String username = req.getParameter("username");
		String pwd = req.getParameter("pwd");
		String xml = "";
		if ((username == null) || (pwd == null)){
			try {
				xml = HttpClientDemo.httpPost(serviceUrl, reqStr);
			} catch (HttpInvokeException e) {
				logger.error("ReRuleXmlInfoAction.invokeBusinessLogic 发生 HttpInvokeException 异常");
			}
		} else {
			xml = HttpClientDemo.Post(username, pwd, serviceUrl, reqStr);
		}
		logger.info(String.format("AirRuleDisplayD-result::::::::::::::::: %s", xml));//【runqian 2017-07-15】sonar 日志 bug 修复
		try {
			resp.setHeader("Content-Encoding", "xml");
			resp.setCharacterEncoding("UTF-8");
			resp.setContentType("text/xml");
			PrintWriter out = resp.getWriter();
			out.write(xml);
			out.flush();
			out.close();
		} catch (Exception e) {
			logger.error("ReRuleXmlInfoAction.invokeBusinessLogic 发生异常");
		}
	}
}