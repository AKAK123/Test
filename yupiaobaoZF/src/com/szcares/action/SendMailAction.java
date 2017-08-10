package com.szcares.action;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.szcares.bean.MailBean;
import com.szcares.util.IOUtils;
import com.szcares.util.SendMailUtil;

/**
 * 邮件发送Action
 * @author huc
 *
 */
public class SendMailAction extends HttpServlet{
	
	private static final long serialVersionUID = 993729955668825446L;
	
	private static final Logger LOGGER = Logger.getLogger(SendMailUtil.class);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}
	
	/***
	 * 发送邮件转发
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		resp.setContentType("text/html;charset=utf-8");
		LOGGER.info("send mail starting...");
		ServletInputStream inputStream = req.getInputStream();
		Writer writer = resp.getWriter();
		try {
			String mail = IOUtils.toString(inputStream);
			JSONObject parseObject = JSONObject.parseObject(mail);
			MailBean mailBean = (MailBean) JSON.toJavaObject(parseObject,MailBean.class);
			SendMailUtil.sendMail(mailBean);
			
		} catch (Exception e) {
			writer.write("fail");
			LOGGER.error("SendMailAction.doPost 发生异常");
		} finally {
			inputStream.close();
			writer.flush();
			LOGGER.info("mail send finish....");
		}
		
		
		
	}
	
	
	
	

}
