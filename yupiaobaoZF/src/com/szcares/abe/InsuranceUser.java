package com.szcares.abe;

/**
 * 投保人信息类
 * @author AKin
 *
 */
public class InsuranceUser {
	//基本信息
	private String transType;
	private String passengerNo;
	private String userName;
	private String idType;
	private String idNo;
	private String telNo;
	private String email;
	//我方系统保险订单id
	private  String insuranceId;
	private  String eticketNo;
	//支付平台返回的流水号
	private  String paySerialNumber;
	//受益信息
	private String benefType;
	private String benefName;
	private String bennefIDNo;
	
	//set、get
	
	public String getUserName()
	{
		return userName;
	}
	public String getTransType()
	{
		return transType;
	}
	public void setTransType(String transType)
	{
		this.transType = transType;
	}
	public String getPassengerNo()
	{
		return passengerNo;
	}
	public void setPassengerNo(String passengerNo)
	{
		this.passengerNo = passengerNo;
	}
	public String getEticketNo()
	{
		return eticketNo;
	}
	public void setEticketNo(String eticketNo)
	{
		this.eticketNo = eticketNo;
	}
	public String getPaySerialNumber()
	{
		return paySerialNumber;
	}
	public void setPaySerialNumber(String paySerialNumber)
	{
		this.paySerialNumber = paySerialNumber;
	}
	public String getInsuranceId()
	{
		return insuranceId;
	}
	public void setInsuranceId(String insuranceId)
	{
		this.insuranceId = insuranceId;
	}
	public void setUserName(String userName)
	{
		this.userName = userName;
	}
	public String getIdType()
	{
		return idType;
	}
	public void setIdType(String idType)
	{
		this.idType = idType;
	}
	public String getIdNo()
	{
		return idNo;
	}
	public void setIdNo(String idNo)
	{
		this.idNo = idNo;
	}
	public String getTelNo()
	{
		return telNo;
	}
	public void setTelNo(String telNo)
	{
		this.telNo = telNo;
	}
	public String getEmail()
	{
		return email;
	}
	public void setEmail(String email)
	{
		this.email = email;
	}
	public String getBenefType()
	{
		return benefType;
	}
	public void setBenefType(String benefType)
	{
		this.benefType = benefType;
	}
	public String getBenefName()
	{
		return benefName;
	}
	public void setBenefName(String benefName)
	{
		this.benefName = benefName;
	}
	public String getBennefIDNo()
	{
		return bennefIDNo;
	}
	public void setBennefIDNo(String bennefIDNo)
	{
		this.bennefIDNo = bennefIDNo;
	}
}
