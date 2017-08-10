package com.szcares.action;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.szcares.abe.Constants;
import com.szcares.abe.InsuranceUser;
import com.szcares.abe.InsuranceUtil;
import com.travelsky.tdp.insure.eterm.service.client.model.InsuranceItem;
import com.travelsky.tdp.insure.eterm.service.client.model.PolicyFlight;
import com.travelsky.tdp.insure.eterm.service.client.model.PolicySchedule;
import com.travelsky.tdp.insure.eterm.service.client.model.ScheduleRQ;
import com.travelsky.tdp.insure.eterm.service.client.model.ScheduleRS;
import com.travelsky.tdp.insure.eterm.service.client.servicedelegate.InsureServiceDelegate;

/**
 * 保险交易接口
 * @author AKin
 *
 */
public class YPBInsuranceAction extends HttpServlet {
	
	private static final long serialVersionUID = 1461265794933122396L;
	
	private static final Logger logger = Logger.getLogger(YPBInsuranceAction.class);
	
	private InsureServiceDelegate insureServiceDelegateTarget;

	@Override
	public void init() throws ServletException {
		ApplicationContext ctx = new ClassPathXmlApplicationContext(new String[] { "InsureClientContext.xml" });
		insureServiceDelegateTarget = (InsureServiceDelegate) ctx.getBean("insureServiceDelegateTarget");
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		 doPost(req, resp);
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html;charset=UTF-8");
		String jsonString=req.getParameter("key");	
		logger.info("前端请求参数：jsonString="+jsonString);
		InsuranceUser iUser=null;
		JSONObject json_rs = new JSONObject();
		try
		{
			iUser= InsuranceUtil.getInsuranceUserFormJsonStr(jsonString);
		} catch (JSONException e)
		{
			json_rs.put("resultCode", "-1");
			json_rs.put("message", "传入的参数数据格式有误！");
			logger.info("resp jsonString="+JSONObject.toJSONString(json_rs,SerializerFeature.WriteMapNullValue));
			resp.getWriter().print(JSONObject.toJSONString(json_rs,SerializerFeature.WriteMapNullValue));
			return;
		}
			String eTicketNo=iUser.getEticketNo();
			String passengerNo=iUser.getPassengerNo();
			String transType=iUser.getTransType();		//交易类型
			if(transType!=null&&!transType.trim().equals("")){
				if (transType.equals(Constants.INSURANCE_ORDER))
				{
					if (eTicketNo == null || eTicketNo.equals("") || eTicketNo.equals("null")) {
						json_rs.put("resultCode", "-1");
						json_rs.put("message", "保单生成:电子客票信息查询:请求传入参数乘客身份证号码为空");
					} else if (passengerNo == null || passengerNo.equals("") || passengerNo.equals("null")) {
						json_rs.put("resultCode", "-1");
						json_rs.put("message", "保单生成:电子客票信息查询:请求传入参数电子客票号为空");
					} else{
						ScheduleRS rs=doPreparePolicySchedule(eTicketNo,passengerNo);		//电子客票信息查询
						if (rs!=null)
						{
							if (rs.getResult().equals(Constants.ABE_RESULT_SUCCESS))
							{
								json_rs=planPolicySchedule(iUser,rs);			//保单生成
							} else
							{
								logger.error("保单生成:ABE电子客票查询接口返回失败!"+rs.getMessage());
								json_rs.put("resultCode", "-1");
								json_rs.put("message", "保单生成:ABE电子客票查询接口返回失败!"+rs.getMessage());
							}
						} else{
							logger.error("保单生成:ABE电子客票查询接口返回为空");
							json_rs.put("resultCode", "-1");
							json_rs.put("message", "保单生成:ABE电子客票查询接口返回为空");
						}
					}
				}else if (transType.equals(Constants.INSURANCE_ORDER_CANCEL)){
					json_rs=cancelOrderForEticketNo(eTicketNo);	//全部保单取消
				}else {
					logger.info("传入交易类型参数错误!");
					json_rs.put("resultCode", "-1");
					json_rs.put("message", "传入交易类型参数错误!");
				}
			}else {
				logger.info("传入参数类型为空");
				json_rs.put("resultCode", "-1");
				json_rs.put("message", "传入参数类型为空");
			}
		logger.info("resp jsonString="+JSONObject.toJSONString(json_rs,SerializerFeature.WriteMapNullValue));
		resp.getWriter().print(JSONObject.toJSONString(json_rs,SerializerFeature.WriteMapNullValue));
	}
	
	/**
	 * 电子客票信息查询
	 * @param ticketNo
	 * @throws ServletException
	 * @throws IOException
	 */
	@SuppressWarnings("unused")
	private ScheduleRS doPreparePolicySchedule(final String ticketNo,final String passengerNo)
	{
		JSONObject json_rs = new JSONObject();
		ScheduleRQ rq = new ScheduleRQ();
		ScheduleRS rs=null;
		//获取请求参数
		logger.info("电子客票信息查询:前端传入参数:ticketNo="+ticketNo+",passengerNo="+passengerNo);
		if (ticketNo == null || ticketNo.equals("") || ticketNo.equals(null)) {
			json_rs.put("resultCode", "-1");
			json_rs.put("message", "电子客票信息查询:请求传入参数乘客身份证号码为空");
			return null;
		} else if (passengerNo == null || passengerNo.equals("") || passengerNo.equals(null)) {
			json_rs.put("resultCode", "-1");
			json_rs.put("message", "电子客票信息查询:请求传入参数电子客票号为空");
			return null;
		} else{
			rq.setETicketNo(ticketNo); // 电子客票号
			rq.setUserName(Constants.USERNAME);
			rq.setOfficeCode(Constants.OFFICECODE);
			rq.setPassword(Constants.PASSWORD);
			rq.setPassengerNo(passengerNo);
			try
			{
				logger.info("ABE电子客票号查询,传入对象参数="+JSONObject.toJSONString(rq));
				rs = insureServiceDelegateTarget.preparePolicySchedule(rq);
				logger.info("ABE电子客票号查询,返回对象参数="+JSONObject.toJSONString(rs));
			} catch (Exception e)
			{
				logger.error("ABE电子客票号查询异常！");
			}
		}
		return rs;
	}
	
	/**
	 * 保单生成
	 * @param ticketNo
	 * @param jsonString
	 * @return
	 * @throws Exception
	 */
	private JSONObject planPolicySchedule(InsuranceUser iUser,ScheduleRS rs)
	{
		JSONObject json_rs = new JSONObject();
		if (iUser == null) {
			json_rs.put("result", "fail");
			json_rs.put("message", "保单生成:请求传入参数为空");
			return json_rs;
		}else if(rs==null){
			json_rs.put("result", "fail");
			json_rs.put("message", "保单生成:请求传入参数为空");
			return json_rs;
		}else{
			//获取请求参数
			String ticketNo=iUser.getEticketNo();
			String telNoString=iUser.getTelNo();
			PolicySchedule  policySchedule_rs=rs.getSchedule();
			PolicySchedule policySchedule_rq=new PolicySchedule();
			policySchedule_rq.setTicketNo(ticketNo);
			policySchedule_rq.setTicketFare(policySchedule_rs.getTicketFare());			//票价
			policySchedule_rq.setCurrencyType(policySchedule_rs.getCurrencyType());		//货币类型
			policySchedule_rq.setPassengerName(policySchedule_rs.getPassengerName());	//旅客姓名
			policySchedule_rq.setPassengerIDtype(policySchedule_rs.getPassengerIDtype());//旅客证件类型
			policySchedule_rq.setPassengerID(policySchedule_rs.getPassengerID());
			policySchedule_rq.setInsuranceCount(1);												//总保险份数
			policySchedule_rq.setTotalPremium(Float.valueOf(Constants.PREMIUM));				//总保费：多条保险需要累加
			policySchedule_rq.setTotalRepay(Float.valueOf(Constants.REPAY));					//总保额：多条保险需要累加
			policySchedule_rq.setOperator(Constants.OPERATOR);							//操作人代号
			policySchedule_rq.setIataNo(policySchedule_rs.getIataNo());					//航协号
			policySchedule_rq.setAirlineOrderNo(policySchedule_rs.getAirlineOrderNo());	//航空公司订单号
			policySchedule_rq.setCheckTo(Constants.CHECKTO);							//结算方式
			policySchedule_rq.setTicketType(policySchedule_rs.getTicketType());			//票的类型
			policySchedule_rq.setApplicantName(policySchedule_rs.getPassengerName());				
			policySchedule_rq.setApplicantIDtype(policySchedule_rs.getPassengerIDtype());
			policySchedule_rq.setApplicantID(policySchedule_rs.getPassengerID());
			policySchedule_rq.setApplicantMobile(telNoString);
			policySchedule_rq.setApplicantEmail(iUser.getEmail());
			policySchedule_rq.setInsuredName(policySchedule_rs.getPassengerName());
			policySchedule_rq.setInsuredIDtype(policySchedule_rs.getPassengerIDtype());
			policySchedule_rq.setInsuredID(policySchedule_rs.getPassengerID());
			policySchedule_rq.setInsuredMobile(telNoString);
			policySchedule_rq.setInsuredEmail(iUser.getEmail());
			ScheduleRQ scheduleRQ= new ScheduleRQ();
			scheduleRQ.setETicketNo(ticketNo);
			scheduleRQ.setUserName(Constants.USERNAME);
			scheduleRQ.setPassword(Constants.PASSWORD);
			scheduleRQ.setOfficeCode(Constants.OFFICECODE);
			scheduleRQ.setReissueFlag("");												//分账标识
			// 获取电子客票号的航班列表
			int itemOrder=0;
			Set policyFlight_rs_set = policySchedule_rs.getPolicyFlights();
			Set<InsuranceItem> insuranceItem_rq_Set = new HashSet<InsuranceItem>();
			for (Iterator iterator = policyFlight_rs_set.iterator(); iterator.hasNext();)
			{
				 PolicyFlight policyFlight_rs= (PolicyFlight) iterator.next();
				 int flightOrder=Integer.valueOf(policyFlight_rs.getFlightOrder());
					//添加PolicyFlight列表请求数据
					PolicyFlight policyFlight_rq=new PolicyFlight();
					policyFlight_rq.setPolicyFlightID(String.valueOf(flightOrder++));	//航班ID
					policyFlight_rq.setFlightOrder(String.valueOf(flightOrder));		//航班序号
					policyFlight_rq.setCarrier(policyFlight_rs.getCarrier());			//承运人
					policyFlight_rq.setFlightNumber(policyFlight_rs.getFlightNumber()); //航班号
					policyFlight_rq.setOriginAirport(policyFlight_rs.getOriginAirport());//起始机场				
					policyFlight_rq.setDestAirport(policyFlight_rs.getDestAirport());	//目的机场
					policyFlight_rq.setFlightDate(policyFlight_rs.getFlightDate());		//航班日期
					policyFlight_rq.setFlightTime(policyFlight_rs.getFlightTime());		//航班时间
					policyFlight_rq.setFlightStatus(policyFlight_rs.getFlightStatus());	//航班状态
					
					//添加InsuranceItems列表请求数据
					InsuranceItem insuranceItem_rq=new InsuranceItem();
					insuranceItem_rq.setItemOrder(String.valueOf(itemOrder));
					insuranceItem_rq.setCompanyCode(Constants.COMPANYCODE);
					insuranceItem_rq.setCompanyName(Constants.COMPANYNAME);
					insuranceItem_rq.setPremium(Float.valueOf(Constants.PREMIUM));
					insuranceItem_rq.setRepay(Float.valueOf(Constants.REPAY));
					insuranceItem_rq.setInsuranceCount(1);										//固定值
					insuranceItem_rq.setBenefitorType("4");										//固定值
					insuranceItem_rq.setProductCode(Constants.PRODUCTCODE);						//保险产品代码-固定值
					insuranceItem_rq.setInsuranceProductName(Constants.INSURANCEPRODUCTNAME);	//保险产品名称-固定值
					insuranceItem_rq.setInsuranceProductId(Constants.INSURANCEPRODUCTID);		//保险产品ID-固定值
					insuranceItem_rq.setProtocolId(Constants.PROTOCOLID);						//产品协议号-固定值
					insuranceItem_rq.setProtocolProductName(Constants.PROTOCOLPRODUCTNAME);		//协议产品名称-固定值
					
					insuranceItem_rq.setRemark1(iUser.getInsuranceId());			//保险订单id
					insuranceItem_rq.setRemark2(String.valueOf(Constants.PAYCODE));	//支付平台代码
					insuranceItem_rq.setRemark3("");								//保单唯一序列码
					insuranceItem_rq.setRemark4(iUser.getPaySerialNumber());		//支付平台返回的流水号
					insuranceItem_rq_Set.add(insuranceItem_rq);
					policyFlight_rs.setInsuranceItems(insuranceItem_rq_Set);	//添加保险列表到航班列表
					itemOrder++;
					break;
			}
			policySchedule_rq.setPolicyFlights(policyFlight_rs_set);			//将航班列表添加到客票信息
			scheduleRQ.setPolicySchedule(policySchedule_rq);					//将客票信息添加到请求对象
			ScheduleRS scheduleRS=null;
			try
			{
				logger.info("ABE保单生成接口,传入对象参数="+JSONObject.toJSONString(scheduleRQ));
				scheduleRS = insureServiceDelegateTarget.planPolicySchedule(scheduleRQ);
				logger.info("ABE保单生成接口,返回对象="+JSONObject.toJSONString(scheduleRS));
			} catch (Exception e)
			{
				logger.error("调用ABE保单生成交易异常！");
				json_rs.put("resultCode", "-1");
				json_rs.put("message", "ABE保单生成:调用ABE保单生成交易异常！");
				return json_rs;
			}
			if (scheduleRS!=null)
			{
				String result=scheduleRS.getResult();
				if (result.equals(Constants.ABE_RESULT_SUCCESS)) {
					json_rs.put("resultCode", "0");
					json_rs.put("message", "成功");
					PolicySchedule psSchedule=scheduleRS.getSchedule();
					PolicyFlight policyFlight_rs=(PolicyFlight)rs.getSchedule().getPolicyFlights().iterator().next();
					InsuranceItem insuranceItem_rs=(InsuranceItem)policyFlight_rs.getInsuranceItems().iterator().next();
					//返回生成成功参数
					json_rs.put("passengerNo", InsuranceUtil.getValNotNull(psSchedule.getPassengerID()));			//	乘机人身份证号码
					json_rs.put("eTicketNo", InsuranceUtil.getValNotNull(psSchedule.getTicketNo()));				//	电子客票号
					json_rs.put("insuranceInsureNo", "");															//	保险公司保单号
					json_rs.put("companyCode", InsuranceUtil.getValNotNull(insuranceItem_rs.getCompanyCode()));		//	保险公司代码
					json_rs.put("companyName", InsuranceUtil.getValNotNull(insuranceItem_rs.getCompanyName()));		//	保险公司名称
					json_rs.put("premium", InsuranceUtil.getValNotNull(""+insuranceItem_rs.getPremium()));				//	保费
					json_rs.put("repay", InsuranceUtil.getValNotNull(""+insuranceItem_rs.getRepay()));					//	保额
					json_rs.put("insuranceCount", InsuranceUtil.getValNotNull(""+insuranceItem_rs.getInsuranceCount()));//保险条数
					json_rs.put("ItemOrder", InsuranceUtil.getValNotNull(insuranceItem_rs.getItemOrder()));			//保险条目序列号
					json_rs.put("productCode", InsuranceUtil.getValNotNull(insuranceItem_rs.getProductCode()));		//保险产品代码
					json_rs.put("insuranceProductName", InsuranceUtil.getValNotNull(insuranceItem_rs.getInsuranceProductName()));		//保险产品名称		
					json_rs.put("protocolId", InsuranceUtil.getValNotNull(insuranceItem_rs.getProtocolId()));		//产品协议号
					json_rs.put("protocolProductName", InsuranceUtil.getValNotNull(insuranceItem_rs.getProtocolProductName()));		//协议产品名称
					json_rs.put("validdate", InsuranceUtil.getValNotNull(insuranceItem_rs.getValidDate()));			//保单有效期开始时间
					json_rs.put("invaliddate", InsuranceUtil.getValNotNull(insuranceItem_rs.getInvalidDate()));		//保单有效期结束时间
					json_rs.put("benefitorType", InsuranceUtil.getValNotNull(insuranceItem_rs.getBenefitorType()));	//受益人类型
					json_rs.put("formerOrderId", InsuranceUtil.getValNotNull(insuranceItem_rs.getFormerOrderId()));	//旧订单号
					json_rs.put("reissueFlag", InsuranceUtil.getValNotNull(insuranceItem_rs.getReissueFlag()));		//分账标识
					json_rs.put("insureNo", InsuranceUtil.getValNotNull(insuranceItem_rs.getInsureNo()));		//保单号
				} else if (result.equals(Constants.ABE_RESULT_FAIL)) {
					logger.error("ABE保单生成接口返回失败!"+scheduleRS.getMessage());
					json_rs.put("resultCode", "-1");
					json_rs.put("message", "ABE保单生成:返回结果为失败");
				} else {
					json_rs.put("resultCode", "-1");
					json_rs.put("message", "ABE保单生成:返回结果类型有误！结果为"+result);
				}
			}else {
				json_rs.put("resultCode", "-1");
				json_rs.put("message", "ABE保单生成:返回为空！");
			}
		}
		return json_rs;
	}
	
	/**
	 * 保单取消:全部
	 * @param jsonString
	 * @return
	 * @throws Exception
	 */
	private JSONObject cancelOrderForEticketNo(final String eTicketNo)
	{
		JSONObject json_rs = new JSONObject();
		if (eTicketNo == null || eTicketNo.equals("") || eTicketNo.equals(null)) {
			json_rs.put("resultCode", "-1");
			json_rs.put("message", "保单取消:请求传入参数电子票号为空");
		}else{
			ScheduleRQ scheduleRQ = new ScheduleRQ();
			scheduleRQ.setETicketNo(eTicketNo);
			scheduleRQ.setUserName(Constants.USERNAME);
			scheduleRQ.setOfficeCode(Constants.OFFICECODE);
			scheduleRQ.setPassword(Constants.PASSWORD);
			ScheduleRS scheduleRS=null;
			try
			{
				logger.info("ABE保单取消口,传入对象参数="+JSONObject.toJSONString(scheduleRQ));
				scheduleRS = insureServiceDelegateTarget.cancelOrder(scheduleRQ);
				logger.info("ABE保单取消,返回对象="+JSONObject.toJSONString(scheduleRS));
			} catch (Exception e)
			{
				logger.error("调用保单取消交易异常!"+eTicketNo);
			}
			if (scheduleRS!=null)
			{
				String result=scheduleRS.getResult();
				if (result.equals(Constants.ABE_RESULT_SUCCESS)) {
					json_rs.put("resultCode", "0");
					json_rs.put("message", "成功");
				} else if (result.equals(Constants.ABE_RESULT_FAIL)) {
					logger.info("ABE保单取消接口返回失败！提示："+scheduleRS.getMessage());
					json_rs.put("resultCode", "-1");
					json_rs.put("message", scheduleRS.getMessage());
				} else {
					logger.info("ABE保单取消:返回结果类型有误！结果为!"+result);
					json_rs.put("resultCode", "-1");
					json_rs.put("message", "ABE保单取消:返回结果类型有误！结果为"+result);
				}
			}else {
				json_rs.put("resultCode", "-1");
				json_rs.put("message", "ABE保单取消:返回为空！");
			}
		}
		return json_rs;
	}
}
