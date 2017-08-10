package com.szcares.abe;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sun.istack.internal.FinalArrayList;
import com.travelsky.tdp.insure.eterm.service.client.model.InsuranceItem;
import com.travelsky.tdp.insure.eterm.service.client.model.PolicyFlight;
import com.travelsky.tdp.insure.eterm.service.client.model.ScheduleRS;
import com.szcares.abe.Constants;
import com.szcares.abe.InsuranceUser;
import com.szcares.action.YPBInsuranceAction;

public class InsuranceUtil {
	
	private static final Logger logger = Logger.getLogger(InsuranceUtil.class);
	
	public static String getValNotNull(String val)
	{
		if(val==null||val.trim().equals("")){
			return "";
		}
		return val;
	}
	public static void main(String[] args)
	{
		String val=null;
		System.out.println("-"+InsuranceUtil.getValNotNull(val)+"-");
	}
	
	/**
	 * 生成单个InsuranceItem返回对象
	 * @return
	 */
	public static InsuranceItem getSingleInsuranceItem()
	{
		InsuranceItem insuranceItem=new InsuranceItem();
		insuranceItem.setItemOrder("0");
		insuranceItem.setCompanyCode(Constants.COMPANYCODE);
		insuranceItem.setCompanyName(Constants.COMPANYNAME);
		insuranceItem.setPremium(new Float(Constants.PREMIUM));
		insuranceItem.setRepay(new Float(Constants.REPAY));
		insuranceItem.setInsuranceCount(1);
		insuranceItem.setBenefitorType("4");
		insuranceItem.setBenefitorName("");
		insuranceItem.setBenefitorID("");
		insuranceItem.setProductCode(Constants.PRODUCTCODE);
		insuranceItem.setInsuranceProductName(Constants.INSURANCEPRODUCTNAME);
		insuranceItem.setInsuranceProductId(Constants.INSURANCEPRODUCTID);
		insuranceItem.setProtocolId(Constants.PROTOCOLID);
		insuranceItem.setProtocolProductName(Constants.PROTOCOLPRODUCTNAME);
		insuranceItem.setInsureNo("");
		insuranceItem.setValidDate("");
		insuranceItem.setInvalidDate("");
		insuranceItem.setRemark("");
		insuranceItem.setFormerOrderId("");
		insuranceItem.setReissueFlag("");
		insuranceItem.setRemark1("");
		insuranceItem.setRemark2("");
		insuranceItem.setRemark3("");
		insuranceItem.setRemark4("");
		insuranceItem.setRemark5("");
		insuranceItem.setInsuranceInsureNo("");
		return insuranceItem;
	}
	
	/**
	 * 生成单个PolicyFlight返回对象
	 * @return
	 */
	public static PolicyFlight getSinglePolicyFlight(ScheduleRS scheduleRS)
	{
		PolicyFlight policyFlightRS=new PolicyFlight();
		policyFlightRS=(PolicyFlight)scheduleRS.getSchedule().getPolicyFlights().iterator().next();
		PolicyFlight policyFlight=new PolicyFlight();
		policyFlight.setPolicyFlightID("1");
		policyFlight.setFlightOrder("1");
		policyFlight.setCarrier(policyFlightRS.getCarrier());
		policyFlight.setFlightNumber(policyFlightRS.getFlightNumber());
		policyFlight.setOriginAirport(policyFlightRS.getOriginAirport());
		policyFlight.setDestAirport(policyFlightRS.getDestAirport());
		policyFlight.setFlightDate(policyFlightRS.getFlightDate());
		policyFlight.setFlightTime(policyFlightRS.getFlightTime());
		policyFlight.setFlightStatus(policyFlightRS.getFlightStatus());
		return policyFlight;
	}
	
	/**
	 * 解析json数据,返回List投保人信息
	 * @param jsonString
	 * @return
	 */
	public static List<InsuranceUser> getListInsuranceUserFormJsonStr(String jsonString)
	{
		List<InsuranceUser> list=new ArrayList<InsuranceUser>();
		JSONArray json = (JSONArray) JSONArray.parse(jsonString);
		// 参数接收
		for (Object jsObj:json)
		{
			JSONObject jsonObject=(JSONObject)jsObj;
			InsuranceUser iUser=new InsuranceUser();
			iUser.setUserName(jsonObject.getString("userName"));
			iUser.setUserName(jsonObject.getString("idType"));
			iUser.setUserName(jsonObject.getString("idNo"));
			iUser.setUserName(jsonObject.getString("telNo"));
			iUser.setUserName(jsonObject.getString("email"));
			iUser.setUserName(jsonObject.getString("benefType"));
			iUser.setUserName(jsonObject.getString("benefName"));
			iUser.setUserName(jsonObject.getString("bennefIDNo"));
			list.add(iUser);
		}
		return list;
	}
	
	/**
	 * 解析json数据,返回单个购买保险人信息
	 * @param jsonString
	 * @return
	 */
	public static InsuranceUser getInsuranceUserFormJsonStr(String jsonString)
	{
		InsuranceUser iUser=new InsuranceUser();
		JSONObject jsonObject=  JSONObject.parseObject(jsonString);
		iUser.setTransType(jsonObject.getString("transType"));
		iUser.setPassengerNo(jsonObject.getString("passengerNo"));
		iUser.setInsuranceId(jsonObject.getString("insuranceId"));
		iUser.setPaySerialNumber(jsonObject.getString("paySerialNumber"));
		iUser.setEticketNo(jsonObject.getString("eTicketNo"));
		iUser.setUserName(jsonObject.getString("userName"));
		iUser.setIdType(jsonObject.getString("idType"));
		iUser.setIdNo(jsonObject.getString("idNo"));
		iUser.setTelNo(jsonObject.getString("telNo"));
		iUser.setEmail(jsonObject.getString("email"));
		iUser.setBenefType(jsonObject.getString("benefType"));
		iUser.setBenefName(jsonObject.getString("benefName"));
		iUser.setBennefIDNo(jsonObject.getString("bennefIDNo"));
		return iUser;
	}
}	
