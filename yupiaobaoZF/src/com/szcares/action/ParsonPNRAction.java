package com.szcares.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.travelsky.internalshop.util.QueryPayResultInfo;
import com.travelsky.parserintershop.service.ParserPNRService;
import com.travelsky.parserintershop.util.HttpInvokeException;
import com.yeexing.webservice.business.impl.YeexingServiceImpl;

import net.sf.json.JSONObject;

public class ParsonPNRAction extends HttpServlet{
	/**
	 * 操作PNR部分
	 */
	private static final long serialVersionUID = -2281348547603176L;
	private ParserPNRService serve=new ParserPNRService();//生产环境
	private Logger logger=Logger.getLogger(ParsonPNRAction.class);
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {//【runqian 2017-07-17】sonar Incomplete Servlet Error Handling bug 修复
			invokeBusinessLogic(req, resp);
		/**modify by chencl 2017-7-17**/
		} catch (Exception e) {
			logger.error("ParsonPNRAction.doPost 异常");
		}
	}
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		invokeBusinessLogic(req, resp);
	}
	
	@SuppressWarnings("static-access")
	private void invokeBusinessLogic(HttpServletRequest req,
			HttpServletResponse resp) throws UnsupportedEncodingException {
		req.setCharacterEncoding("utf-8");
		String key=req.getParameter("key");
		if (!key.isEmpty()) {
			JSONObject json=new JSONObject().fromObject(key);
			String type="";
			if (json.containsKey("type")) {
				type=json.getString("type");
			}
			if (!type.isEmpty()) {
				PrintWriter out=null;
				String xml="";
				if (type.equals("getPnrStr")) {//获取pnr信息
					try {
						String urlReset=json.getString("urlReset");
						String officeId=json.getString("officeId");
						String pnrNo=json.getString("pnrNo");
						xml=serve.getPNRString(urlReset,officeId,pnrNo);
					} catch (HttpInvokeException e) {
						logger.error("获取pnr信息发生 HttpInvokeException 异常");
					} catch (Exception e) {
						logger.error("获取pnr信息发生异常");
					}
				}else if (type.equals("cancePnr")) {//取消PNR
					String cancel_url=json.getString("cancel_url");
					String offiid=json.getString("offiid");
					String pnrNo=json.getString("pnrNo");
					xml=serve.cancePnr(cancel_url, offiid,pnrNo);//生产
				}else if (type.equals("isPayed")) {//检查是否已经支付
					String DsDh=json.getString("DsDh");
					String Ddbh=json.getString("Ddbh");
					String Hzwz=json.getString("Hzwz");
					Boolean flage=QueryPayResultInfo.queryPayInfo(DsDh,Ddbh,Hzwz);
					xml=flage.toString();
				}else if (type.equals("creatPnr")) {
					String officeId=json.getString("officeId");
					String pnrRequest=json.getString("pnrRequest");
					String pnrUrl=json.getString("pnrUrl");
					System.out.println(pnrRequest);
					xml=serve.createPNR(pnrUrl,officeId,null,pnrRequest);
				}else if(type.equals("pnrBook")){
					try {
//						IBEServicePortType port = new IBEService().getIBEServiceHttpPort();
//						String userName=json.getString("userName");
						String pnr=json.getString("pnr");
						String plcid=json.getString("plcid");
						String ibePrice=json.getString("ibePrice");
						String out_orderid=json.getString("out_orderid");
						String disc=json.getString("disc");
						String extReward=json.getString("extReward");
						YeexingServiceImpl yeexing=new YeexingServiceImpl();
						xml=yeexing.pnrBook_xml(pnr,plcid,ibePrice,out_orderid,disc,extReward+"");
//						Map<String, String> map=new HashMap<String, String>();
//						map.put("userName", userName);
//						map.put("pnr", pnr);
//						map.put("plcid", plcid);
//						map.put("ibePrice", ibePrice);
//						map.put("out_orderid", out_orderid);
//						map.put("disc", disc);
//						map.put("extReward", extReward);
//						String sign=SignUtil.getSign(map, key);
//						System.out.println(sign);
//						xml=port.pnrBook(userName, pnr, plcid, ibePrice, out_orderid, disc, extReward, sign);
					} catch (Exception e) {
						for (StackTraceElement s:e.getStackTrace()) {
							logger.error("ParsonPNRAction.invokeBusinessLogic 发生异常");
						}
					}
				}else if(type.equals("pnrMatch")){
//					IBEServicePortType port = new IBEService().getIBEServiceHttpPort();
//					String userName=json.getString("userName");
					String pnr=json.getString("pnr");
//					Map<String, String> map=new HashMap<String, String>();
//					map.put("userName", userName);
//					map.put("pnr", pnr);
//					String sign=SignUtil.getSign(map, key);
//					xml=port.pnrMatchAirp(userName, pnr, sign);
					YeexingServiceImpl yeexing=new YeexingServiceImpl();
					xml=yeexing.pnrMatchAirp_xml(pnr);
					
				}
		    	 try
		    	 {
		    		 resp.setHeader("Content-Encoding", "gzip");
		    		 resp.setCharacterEncoding("UTF-8");
		    		 out = resp.getWriter();
		    		 out.write(xml);
		    		 out.flush();
		    		 out.close();
		    	 }catch (Exception e) {
		    		 logger.error("获取pnr信息resp异常！");
		    	 }finally{
		    		 if (out!=null) {
						out.close();
					}
		    	 }
			}
		}
	}
}
