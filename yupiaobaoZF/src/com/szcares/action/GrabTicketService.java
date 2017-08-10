package com.szcares.action;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.szcares.bean.IGrabTicket;

import net.sf.json.JSONObject;


public class GrabTicketService extends HttpServlet{

	private static final long serialVersionUID = -875639780676670625L;
	private static final Logger logger = Logger.getLogger(GrabTicketService.class);
	private IGrabTicket grabTicket;
	
	@Override
	public void init() throws ServletException {
		ApplicationContext ctx = new ClassPathXmlApplicationContext(
				new String[] { "applicationContext.xml" });
		grabTicket = (IGrabTicket) ctx.getBean("grabTicketInvoker");
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doPost(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {//【runqian 2017-07-17】sonar Incomplete Servlet Error Handling bug 修复
		logger.info("+++++++++++++++抢票监控转发开始+++++++++++++++");
		PrintWriter out = resp.getWriter();
		String tType = req.getParameter("tType");
		String returnCode="";
		if (tType.equals("grabTicketNotice")) {
			returnCode=grabTicketNotice(req,resp);
		}else if (tType.equals("addGrabTicketPlan")) {
			returnCode=addGrabTicketPlan(req,resp);
		}
		out.write(returnCode);
		logger.info("+++++++++++++++抢票监控转发结束+++++++++++++++");
		/**modify by chencl 2017-7-17**/
		} catch (Exception e) {
			logger.error("GrabTicketService.doPost 异常");
		}
	}
	
	private String grabTicketNotice(HttpServletRequest req, HttpServletResponse resp) {
		String map = req.getParameter("hbMap");
		HashMap<String, Object> hbMap=new HashMap<String, Object>();
		JSONObject json = JSONObject.fromObject(map);
		hbMap.put("flightNo", json.getString("flightNo"));
		hbMap.put("clazz", json.getString("clazz"));
		hbMap.put("flightDate", json.getString("flightDate"));
		hbMap.put("departureAirport",json.getString("departureAirport"));
		hbMap.put("arrivalAirport", json.getString("arrivalAirport"));
		hbMap.put("airlineCode", json.getString("airlineCode"));
		hbMap.put("ticketCount", json.getString("ticketCount"));
		hbMap.put("userId", json.getString("userId"));
		hbMap.put("cuuid", json.getString("cuuid"));
		hbMap.put("mobile", json.getString("mobile"));
		hbMap.put("appId",json.getString("appId"));
		hbMap.put("isuse", json.getString("isuse"));
		HashMap<String, Object> ticketNotice = grabTicket.grabTicketNotice(hbMap);
		logger.info(String.format("grabTicketNotice+++++++++++++++request:%s, response:%s +++++++++++++++", hbMap.toString(), ticketNotice));//【runqian 2017-07-15】sonar 日志 bug 修复
		String resultCode = ticketNotice.get("resultCode") + "";
		try {
			if (resultCode.equals("1")) {
				logger.info("grabTicketNotice+++++++++++++++已成功发送通知+++++++++++++++");
			}
		} catch (Exception e) {
			logger.info("grabTicketNotice function exception occurred!");
		}
		return resultCode;

	}
	
	private String addGrabTicketPlan(HttpServletRequest req, HttpServletResponse resp) {
		String map = req.getParameter("hbMap");
		HashMap<String, Object> hbMap=new HashMap<String, Object>();
		JSONObject json = JSONObject.fromObject(map);
		hbMap.put("flightNo", json.getString("flightNo"));
		hbMap.put("clazz", json.getString("clazz"));
		hbMap.put("flightDate", json.getString("flightDate"));
		hbMap.put("departureAirport",json.getString("departureAirport"));
		hbMap.put("arrivalAirport", json.getString("arrivalAirport"));
		hbMap.put("isuse", json.getString("isuse"));
		HashMap<String, Object> ticketNotice = grabTicket.addGrabTicketPlan(hbMap);
		String resultCode = ticketNotice.get("resultCode") + "";
		logger.info(String.format("addGrabTicketPlan+++++++++++++++request:%s, response:%s +++++++++++++++", hbMap.toString(), ticketNotice));//【runqian 2017-07-15】sonar 日志 bug 修复
		try {
			if (resultCode.equals("1")) {
				if (json.getString("isuse").equals("1")) {
					logger.info("addGrabTicketPlan+++++++++++++++已生成抢票，监控开启+++++++++++++++");
				}else{
					logger.info("addGrabTicketPlan+++++++++++++++已生成抢票，监控关闭+++++++++++++++");
				}
			}
		} catch (Exception e) {
			logger.info("addGrabTicketPlan function exception occurred!");
		}
		return resultCode;
	}
	
}


