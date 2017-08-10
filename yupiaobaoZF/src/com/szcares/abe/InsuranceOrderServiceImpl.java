package com.szcares.abe;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.sun.istack.internal.FinalArrayList;
import com.sun.org.apache.regexp.internal.recompile;
import com.travelsky.tdp.insure.eterm.service.client.model.InsuranceItem;
import com.travelsky.tdp.insure.eterm.service.client.model.PolicyFlight;
import com.travelsky.tdp.insure.eterm.service.client.model.PolicySchedule;
import com.travelsky.tdp.insure.eterm.service.client.model.ScheduleRQ;
import com.travelsky.tdp.insure.eterm.service.client.model.ScheduleRS;
import com.travelsky.tdp.insure.eterm.service.client.servicedelegate.InsureServiceDelegate;

/**
 * 航意险接口服务类
 * 
 * @author AKin
 *
 */
public class InsuranceOrderServiceImpl implements InsuranceOrderService {
	private Logger log = Logger.getLogger(InsuranceOrderServiceImpl.class);
	private InsuranceABEService insuranceABEService;

	public ScheduleRS preparePolicySchedule(final String ticketNo) throws Exception
	{
		JSONObject json_rs = new JSONObject();
		ScheduleRQ rq = new ScheduleRQ();
		ScheduleRS rs=null ;
		if (ticketNo == null || ticketNo.equals("") || ticketNo.equals(null)) {
			json_rs.put("result", "fail");
			json_rs.put("message", "电子客票信息查询:请求传入参数电子客票号为空");
		} else {
			rq.setETicketNo(ticketNo); // 电子客票号
			rq.setUserName(Constants.USERNAME);
			rq.setOfficeCode(Constants.OFFICECODE);
			rq.setPassword(Constants.PASSWORD);
			insuranceABEService=new InsuranceABEServiceImpl();
			rs = insuranceABEService.preparePolicySchedule(rq);
//			if (rs != null) {
//				if (rs.getResult().equals(Constants.ABE_RESULT_SUCCESS)) {
//					json_rs.put("result", "success");
//					json_rs.put("message", "success");
//					Iterator<PolicyFlight> it = rs.getSchedule().getPolicyFlights().iterator();
//					while (it.hasNext()) {
//						json_rs.put("ticketNoStatus",it.next().getFlightStatus());
//					}
//				} else if (rs.getResult().equals(Constants.ABE_RESULT_FAIL)) {
//					json_rs.put("result", "fail");
//					json_rs.put("message", rs.getMessage());
//				} else {
//					json_rs.put("result", "fail");
//					json_rs.put("message", "电子客票信息查询:返回失败！返回消息："+rs.getMessage());
//				}
//			} else {
//				json_rs.put("result", "fail");
//				json_rs.put("message", "ABE电子客票信息查询:返回为空！");
//			}
		}
		//json不过滤空值：JSONObject.toJSONString(rs,SerializerFeature.WriteMapNullValue)) 
		//json过滤空值 ： JSONObject.toJSONString(rs)
		//log.debug("**************** preparePolicySchedule ****************: return ScheduleRS  json="+ JSONObject.toJSONString(json_rs,SerializerFeature.WriteMapNullValue));
		//log.debug("**************** preparePolicySchedule ****************: return json="+ json_rs.toJSONString());
		//return json_rs;
		return rs;
	}
	
	public JSONObject planPolicySchedule(final String ticketNo, String jsonString) throws Exception
	{
		JSONObject json_rs = new JSONObject();
		if (jsonString == null || jsonString.equals("") || jsonString.equals(null)) {
			json_rs.put("result", "fail");
			json_rs.put("message", "保单生成:请求传入参数为空");
		}else{
			//获取请求参数
			InsuranceUser iUser=InsuranceUtil.getInsuranceUserFormJsonStr(jsonString);
			// 查询电子客票号信息
			ScheduleRQ rq = new ScheduleRQ();
			rq.setETicketNo(ticketNo);
			ScheduleRS rs = insuranceABEService.preparePolicySchedule(rq);
			PolicySchedule  policySchedule_rs=rs.getSchedule();
			
			PolicySchedule policySchedule_rq=new PolicySchedule();
			policySchedule_rq.setTicketNo(ticketNo);
			policySchedule_rq.setTicketFare(policySchedule_rs.getTicketFare());			//票价
			policySchedule_rq.setCurrencyType(policySchedule_rs.getCurrencyType());		//货币类型
			policySchedule_rq.setPassengerName(policySchedule_rs.getPassengerName());	//旅客姓名
			policySchedule_rq.setPassengerIDtype(policySchedule_rs.getPassengerIDtype());//旅客证件类型
			//计算总保险金额
			BigDecimal bigDecimal_count=new BigDecimal(policySchedule_rs.getPolicyFlights().size());		//需购买数量
			BigDecimal bigDecimal_premium=new BigDecimal(Constants.PREMIUM);						 	//保险单价
			BigDecimal bigDecimal_repay=new BigDecimal(Constants.REPAY);						 		//保额单价
			BigDecimal bigTotal_premium=bigDecimal_count.multiply(bigDecimal_premium);					//总保险金额
			BigDecimal bigTotal_repay=bigDecimal_repay.multiply(bigDecimal_repay);				 		//总保额金额
			policySchedule_rq.setTotalPremium(Float.valueOf(bigTotal_premium.toString()));				//总保费
			policySchedule_rq.setTotalRepay(Float.valueOf(bigTotal_repay.toString()));					//总保额

			policySchedule_rq.setIataNo(policySchedule_rs.getIataNo());					//航协号
			policySchedule_rq.setOperator(Constants.OPERATOR);							//操作人代号
			policySchedule_rq.setAirlineOrderNo(policySchedule_rs.getAirlineOrderNo());	//航空公司订单号
			policySchedule_rq.setCheckTo(Constants.CHECKTO);							//结算方式
			policySchedule_rq.setTicketType(policySchedule_rs.getTicketType());			//票的类型
			// 获取电子客票号的航班列表
			int itemOrder=0;
			Set policyFlight_rs_set = policySchedule_rs.getPolicyFlights();
			Set<InsuranceItem> insuranceItem_rq_Set = new HashSet<InsuranceItem>();
			for (Iterator iterator = policyFlight_rs_set.iterator(); iterator.hasNext();)
			{
				 PolicyFlight policyFlight_rs= (PolicyFlight) iterator.next();
				 int flightOrder=Integer.valueOf(policyFlight_rs.getFlightOrder());
				// 判断是否为可购买状态
				if (Constants.ABE_TICKET_FLIGHTSTATUS.indexOf(policyFlight_rs.getFlightStatus().toLowerCase()) >= 0) {
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
					itemOrder++;
					//添加InsuranceItems列表请求数据
					InsuranceItem insuranceItem_rq=new InsuranceItem();
					insuranceItem_rq.setItemOrder(String.valueOf(itemOrder));
					insuranceItem_rq.setCompanyCode(Constants.COMPANYCODE);
					insuranceItem_rq.setCompanyName(Constants.COMPANYNAME);
					insuranceItem_rq.setPremium(Float.valueOf(Constants.PREMIUM));
					insuranceItem_rq.setRepay(Float.valueOf(Constants.REPAY));
					insuranceItem_rq.setInsuranceCount(1);
					insuranceItem_rq.setBenefitorType("4");
					insuranceItem_rq.setBenefitorID("");
					insuranceItem_rq.setBenefitorName("");
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
					break;
				} else {
					json_rs.put("result", "fail");
					json_rs.put("message", "保单生成:该电子票号状态不可购买保单！");
				}
				policyFlight_rs.setInsuranceItems(insuranceItem_rq_Set);	//添加保险列表到航班列表
			}
			policySchedule_rq.setApplicantName(policySchedule_rs.getPassengerName());				
			policySchedule_rq.setApplicantIDtype(policySchedule_rs.getPassengerIDtype());
			policySchedule_rq.setApplicantID(policySchedule_rs.getPassengerID());
			policySchedule_rq.setApplicantMobile(iUser.getTelNo());
			policySchedule_rq.setApplicantEmail(iUser.getEmail());
			policySchedule_rq.setInsuredName(policySchedule_rs.getPassengerName());
			policySchedule_rq.setInsuredIDtype(policySchedule_rs.getPassengerIDtype());
			policySchedule_rq.setInsuredID(policySchedule_rs.getPassengerID());
			policySchedule_rq.setInsuredMobile(iUser.getTelNo());
			policySchedule_rq.setInsuredEmail(iUser.getEmail());
			policySchedule_rq.setPolicyFlights(policyFlight_rs_set);		//将航班列表添加到客票信息
			
			ScheduleRQ scheduleRQ= new ScheduleRQ();
			scheduleRQ.setETicketNo(ticketNo);
			scheduleRQ.setUserName(Constants.USERNAME);
			scheduleRQ.setPassword(Constants.PASSWORD);
			scheduleRQ.setOfficeCode(Constants.OFFICECODE);
			scheduleRQ.setReissueFlag("");
			scheduleRQ.setPolicySchedule(policySchedule_rq);					//将客票信息添加到请求对象
			ScheduleRS scheduleRS = insuranceABEService.planPolicySchedule(scheduleRQ);
			if (scheduleRS!=null)
			{
				if (scheduleRS.getResult().equals(Constants.ABE_RESULT_SUCCESS)) {
					json_rs.put("result", "success");
					json_rs.put("message", "success");
				} else if (scheduleRS.getResult().equals(Constants.ABE_RESULT_FAIL)) {
					json_rs.put("result", "fail");
					json_rs.put("message", scheduleRS.getMessage());
				} else {
					json_rs.put("result", "fail");
					json_rs.put("message", "ABE保单生成:返回失败！");
				}
			}else {
				json_rs.put("result", "fail");
				json_rs.put("message", "ABE保单生成:返回为空！");
			}
		}
		return json_rs;
	}

	public JSONObject cancelOrderForEticketNo(final String jsonString) throws Exception
	{
		JSONObject json_rs = new JSONObject();
		if (jsonString == null || jsonString.equals("") || jsonString.equals(null)) {
			json_rs.put("result", "fail");
			json_rs.put("message", "保单取消:请求传入参数为空");
		}else{
			InsuranceUser iUser=InsuranceUtil.getInsuranceUserFormJsonStr(jsonString);
			if (iUser!=null&&iUser.getEticketNo()!=null)
			{
				String eticketNoStatus="";
				if(eticketNoStatus.equals(Constants.ABE_TICKET_FLIGHTSTATUS_CANCE1)||eticketNoStatus.equals(Constants.ABE_TICKET_FLIGHTSTATUS_CANCE2)){
					json_rs.put("result", "fail");
					json_rs.put("message", "保单取消:票号状态无法办理");
				}else {
					ScheduleRQ scheduleRQ = new ScheduleRQ();
					scheduleRQ.setETicketNo(iUser.getEticketNo());
					scheduleRQ.setUserName(Constants.USERNAME);
					scheduleRQ.setOfficeCode(Constants.OFFICECODE);
					scheduleRQ.setPassword(Constants.PASSWORD);
					scheduleRQ.setReissueFlag("");
					ScheduleRS scheduleRS = insuranceABEService.cancelOrder(scheduleRQ);
					if (scheduleRS!=null)
					{
						if (scheduleRS.getResult().equals(Constants.ABE_RESULT_SUCCESS)) {
							json_rs.put("result", "success");
							json_rs.put("message", "success");
						} else if (scheduleRS.getResult().equals(Constants.ABE_RESULT_FAIL)) {
							json_rs.put("result", "fail");
							json_rs.put("message", scheduleRS.getMessage());
						} else {
							json_rs.put("result", "fail");
							json_rs.put("message", "ABE保单生成:返回失败！");
						}
					}else {
						json_rs.put("result", "fail");
						json_rs.put("message", "ABE保单取消:返回为空！");
					}
				}
			}else {
				json_rs.put("result", "fail");
				json_rs.put("message", "保单取消:请求传入参数电子票号为空");
			}
		}
		return json_rs;
	}

	public ScheduleRS orderMaintainInit(ScheduleRQ scheduleRQ) throws Exception
	{
		return insuranceABEService.orderMaintainInit(scheduleRQ);
	}

	public ScheduleRS orderMaintainMergeInit(ScheduleRQ scheduleRQ)
			throws Exception
	{
		return insuranceABEService.orderMaintainMergeInit(scheduleRQ);
	}

	public ScheduleRS orderMaintain(ScheduleRQ scheduleRQ) throws Exception
	{
		return insuranceABEService.orderMaintain(scheduleRQ);
	}

	public ScheduleRS searchOrders(ScheduleRQ scheduleRQ) throws Exception
	{
		return insuranceABEService.searchOrders(scheduleRQ);
	}

}
