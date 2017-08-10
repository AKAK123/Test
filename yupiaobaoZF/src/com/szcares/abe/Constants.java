package com.szcares.abe;

public abstract class Constants {

	/**  保单交易类型:保单生成    **/
	public static final String INSURANCE_ORDER = "insurance_order";
	/**  保单交易类型:保单取消    **/
	public static final String INSURANCE_ORDER_CANCEL = "insurance_cancel";
	/**  保单交易类型:保单查询    **/
	public static final String INSURANCE_ORDER_QUERY = "insurance_query";
	
	/** RMI Service name **/
	public static final String INSURANCE_SERVICE_NODE = "InsuranceManager";
	
	/** ABE用户名 **/
	public static final String USERNAME = "SZX348ABE";
	/** ABE密码 **/
	public static final String PASSWORD = "SZX348ABE123";
	/** 代理人office号 **/
	public static final String OFFICECODE = "szx348c";
	
	/** 代理人office name **/
	public static final String OFFICENAME = "深圳市达志成贸易有限公司";
	
	/**支付平台代码**/
	public static final int PAYCODE=1;
	
	/**操作人代号**/
	public static final String OPERATOR="szky";
	
	/**航协号**/
	public static final String IATANO="08017074";
	
	/** ABE 货币类型   **/
	public static final String CURRENCYTYPE="CNY";
	/** ABE 返回 结果 **/
	public static final String ABE_RESULT_SUCCESS = "success";
	public static final String ABE_RESULT_FAIL = "fail";
	
	/** ABE 可购买保单的电子客票状态 **/
	public static final String ABE_TICKET_FLIGHTSTATUS="open|suspend|note|check in";
	/** ABE 可取消保单的电子客票状态 **/
	public static final String ABE_TICKET_FLIGHTSTATUS_CANCE1="lift";
	public static final String ABE_TICKET_FLIGHTSTATUS_CANCE2="used";
	/** ABE 可购买保单的旅客证件类型 **/
	public static final String ABE_TICKET_PASSENGERIDTYPE="NI,PP,CC";
	
	/** ABE 保险公司代码   **/
	public static final String COMPANYCODE="YPRH";
	/** ABE 保险公司名称   **/
	public static final String COMPANYNAME="招商局仁和人寿保险股份有限公司";
	/** ABE 保险产品ID **/
	public static final String INSURANCEPRODUCTID="2c9184e31be3f624011be3f854500002";
	/** ABE 保险公司保险产品名称   **/
	public static final String INSURANCEPRODUCTNAME="航空意外险";
	/** ABE 保险产品代码   **/
	public static final String PRODUCTCODE="1000000";
	/** ABE 保险公司产品协议号  **/
	public static final String PROTOCOLID="12115000044";
	/** ABE 保险公司协议产品名称  **/
	public static final String PROTOCOLPRODUCTNAME="余票宝B2C航意险（仁和20元）";
	/** ABE 保险公司结算方式 **/
	public static final String CHECKTO="A";
	/** ABE 保费 20元 **/
	public static final String PREMIUM="20";
	/** ABE 保额60万 **/
	public static final String REPAY="600000";
	
	
	
	
}
