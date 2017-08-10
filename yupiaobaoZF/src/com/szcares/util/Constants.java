package com.szcares.util;

/**
 * 系统中的常量定义
 * 
 * @author Administrator
 * 
 */
public class Constants {
	//全局IP地址变量
		//机场IP配置
	public static final String IPconfig_nei="10.128.150.122";
	public static final String IPconfig_wai="218.17.237.47";
		//机房IP配置
//	public static final String IPconfig_nei="10.128.64.214";
//	public static final String IPconfig_wai="113.108.96.3";
	
	//easyPay获取支付明细  183.134.5.17:9280(测试)  202.106.139.5旧
	//http://183.134.5.17:9280/easypay/services/DataService
    //http://easypay.travelsky.com/easypay/services/DataService
	public static final String EASYPAY_PAYINFO_URL="http://easypay.travelsky.com/easypay/services/DataService";
	
	// 系统常量
	public static final String PROJECT_HOME = "project.home";
	public static final String SYSTEM_LOG_APPENDER_NAME = "TRANSLOG";
	// 系统配置
	public static final String ESPEED_INTERNAL_SHOPPING_URL = "espeed.internal.shopping.url";
	public static final String DAILY_FLOW_FILE_DIRECTORY = "daily.flow.file.directory";
	public static final String EXCUTE_CALCULATING_FLOW_TIME = "excute.calculating.flow.time";
	// 用户配置
	public static final String USER_PLACEHOLDER = "0";
	public static final String USER_IP = "user0.ip";
	public static final String USER_OFFICE = "user0.office";
	public static final String USER_USERNAME = "SECURITY_user0.username";
	public static final String USER_PASSWORD = "SECURITY_user0.password";
	// 计算流量
	public static final String HTTP_INVOKE_SUCCESS = "http invoke success: ";
	public static final String HTTP_INVOKE_FAILED = "http invoke failed: ";
	public static final String USER_VALIDATION_SUCCESS = "user validation success: ";
	public static final String USER_VALIDATION_FAILED = "user validation failed: ";
	// 记录流量统计结果
	public static final String TOTAL_VISIT_TIMES = " - total visit times: ";
	public static final String ESPEED_HTTP_INVOKE_SUCCESS = ",  eSPEED http invoke success(times): ";
	public static final String ESPEED_HTTP_INVOKE_FAILED = ",  eSPEED http invoke failed(times): ";
	public static final String DAILY_FLOW_FILE_PREFIX = "daily_flow_";
	public static final String DAILY_FLOW_FILE_SUFFIX = ".txt";
	public static final String ESPEED_AIR_RESRET_COMPLETE_URL = "espeed.internal.AirResRetComplete.url";
	public static final String ESPEED_AIR_BOOK_URL = "espeed.internal.AirBook.url";
	public static final String ESPEED_AIR_BOOK_MODIFY_URL = "espeed.internal.AirBookModify.url";
	public static final String ESPEED_AIR_BRIEF_URL = "espeed.internal.AirBrief.url";
	public static final String ESPEED_AIRDEMANDTICKET_URL = "espeed.internal.AirDemandTicket.url";
	public static final String ESPEED_AIR_DETAILS_URL = "espeed.internal.AirDetails.url";
	public static final String ESPEED_AIR_ITINERARY_RET_URL = "espeed.internal.AirItineraryRet.url";
	public static final String ESPEED_AIR_LOW_FARE_SEARCH_URL = "espeed.internal.AirLowFareSearch.url";
	public static final String ESPEED_AIR_SCHEDULE_URL = "espeed.internal.AirSchedule.url";
	public static final String ESPEED_AIR_TICKET_RESUME_URL = "espeed.internal.AirTicketResume.url";
	public static final String ESPEED_AIR_TICKET_RET_URL = "espeed.internal.AirTicketRet.url";
	public static final String ESPEED_AIR_TICKET_RETRY__URL = "espeed.internal.AirTicketRetry.url";
	public static final String ESPEED_AIR_TICKET_SUSPEND_URL = "espeed.internal.AirTicketSuspend.url";
	public static final String ESPEED_AIR_TICKET_VOID_URL = "espeed.internal.AirTicketVoid.url";
	public static final String ESPEED_CANCEL_PNR_URL = "espeed.internal.CancelPnr.url";
	public static final String ESPEED_DELETE_ITEM_URL = "espeed.internal.DeleteItem.url";
	public static final String ESPEED_MODIFY_PASSENGER_FOID_URL = "espeed.internal.ModifyPassengerFoid.url";
	public static final String ESPEED_MODIFY_PASSENGER_INFO_URL = "espeed.internal.ModifyPassengerinfo.url";
	public static final String ESPEED_AIR_PRICE_URL = "espeed.internal.AirPrice.url";
	public static final String ESPEED_AIR_RESRET_URL = "espeed.internal.AirResRet.url";
	public static final String ESPEED_AIR_AVAIL_URL = "espeed.internal.AirAvail.url";
	public static final String ESPEED_AIR_SEAT_MAP_URL = "espeed.internal.AirSeatMap.url";
	public static final String ESPEED_AIR_CALENDAR_SHOP_URL = "espeed.internal.AirCalendarShop.url";
	public static final String ESPEED_AIR_AirBook_URL = "http://" + IPconfig_wai + ":8888/DomShopping/xml/AirBook";// 订购机票
//	public static final String ESPEED_AIR_AirBook_URL = "http://" + IPconfig_wai + ":8081/DomShopping/xml/AirBook";// 订购机票公司配置
//	public static final String ESPEED_OTA_AirResRetRQ_URL = "http://" + IPconfig_wai + ":8081/DomShopping/xml/AirResRet";// 机票查询公司配置
	public static final String ESPEED_OTA_AirResRetRQ_URL = "http://" + IPconfig_wai + ":8888/DomShopping/xml/AirResRet";// 机票查询达志成配置
	public static final String ESPEED_AirBookModify_URL = "http://" + IPconfig_wai + ":8888/DomShopping/xml/AirBookModify";// 修改机票
//	public static final String ESPEED_AirBookModify_URL = "http://" + IPconfig_wai + ":8081/DomShopping/xml/AirBookModify";// 修改机票
	public static final String ESPEED_AirScheduleRQ_URL = "http://" + IPconfig_wai + ":8888/DomShopping/xml/AirSchedule";// 航班查询
	public static final String ESPEED_AirResRetComplete_URL="http://" + IPconfig_wai + ":8888/DomShopping/xml/AirResRetComplete";
	public static final String ESPEED_AirPriceBySeg_URL="http://" + IPconfig_wai + ":8888/DomShopping/xml/AirPriceBySeg";//舱位运价查询
	public static final String ESPEED_AirRuleDisplay_URL="http://" + IPconfig_wai + ":8888/DomShopping/xml/AirRuleDisplayD";//航班退改签规则查询
	public static final String ESPEED_GetPnrPrice_URL="http://" + IPconfig_wai + ":7080/YPBShopping/xml/GetPriceShopping";
	public static final String ESPEED_REFUNDTICKET_BACK_URL="http://" + IPconfig_wai + ":7080/ycbback/ordercancel/refundTicketback.do";//退票回调地址
	public static final String ESPEED_CHANGE_BACK_URL="http://" + IPconfig_wai + ":7080/ycbback/ordercancel/changeback.do";//改期或改证件回调地址
	public static final String ESPEED_GetTuigaiqian_URL="http://" + IPconfig_wai + ":7080/YPBShopping/xml/ReRuleXmlInfo";
	public static final String ESPEED_StartQuartzAction_URL="http://" + IPconfig_wai + ":7088/YPBShopping/xml/startQuartzAction";
//	public static final String ESPEED_StartQuartzAction_URL="192.168.1.96:8080/YPBShopping/xml/startQuartzAction";
//	public static final String ESPEED_GetTuigaiqian_URL="http://192.168.1.96:8080/YPBShopping/xml/ReRuleXmlInfo";
//	public static final String ESPEED_GetPnrPrice_URL="http://localhost:8080/YPBShopping/xml/GetPriceShopping";
	public static final String ESPEED_AirResRet_URL="http://" + IPconfig_wai + ":8888/DomShopping/xml/AirResRet";//验证订单退改签URL
//	public static final String ESPEED_AirResRet_URL="http://" + IPconfig_wai + ":8081/DomShopping/xml/AirResRet";//验证订单退改签URL
//	public static final String PNR_CHECK_URL="http://" + IPconfig_wai + ":8081/DomShopping/xml/DNResRet";//PNR验证URL
	public static final String PNR_CHECK_URL="http://" + IPconfig_wai + ":8888/DomShopping/xml/DNResRet";//PNR验证URL
	public static final String pnr_ticketTimeLimit = "1";// pnr
	/**
	 *  网赢商户号（YUPBAO_WY）
	 * @return
	 */
	public static final String ORGID_WY = "YUPBAO_WY";
	
	/**
	 *  凯亚商户号（YUPBAO_KY）
	 * @return
	 */
	public static final String ORGID_KY = "YUPBAO_KY";
	
	/**
	 *  易行商户号（YUPBAO_YX）
	 * @return
	 */
	public static final String ORGID_YX = "YUPBAO_YX";
	/**
	 * 版本号
	 * @return
	 */
	public static final String VERSION = "1.0";
	/**
	 * APP类型
	 * @return
	 */
	public static final String APP_TYPE = "B2C";
	/**
	 * 支付货币类型
	 * @return
	 */
	public static final String CURTYPE = "CNY";
	/**
	 * 订单类型
	 * @return
	 */
	public static final String ORDER_TYPE = "0";
	/**
	 * 语言版本
	 * @return
	 */
	public static final String LAN = "CN";
	/**
	 * 网赢支付返回路径标识
	 * @return
	 */
//	public static final String RETURN_ID_WY = "id_pay_410";//测试：id_pay_410      生产:id_ypb_wy
	public static final String RETURN_ID_WY = "id_ypb_wy";
	/**
	 * 易行支付返回路径标识
	 * @return
	 */
//	public static final String RETURN_ID_YX = "id_pay_411";//测试：id_pay_411      生产:id_ypb_yx
	public static final String RETURN_ID_YX = "id_ypb_yx";
	/**
	 * 订单名称
	 * @return
	 */
	public static final String ORDER_NAME = "YUPIAOBAO";
	/**
	 * 支付方式类别
	 * @return
	 */
	public static final String PAY_TYPE = "10";
	/**
	 * 订单推送完成催出票短信通知的号码
	 * @return
	 */
	//public static final String ORDER_PUSH_NUMBER = "13122232991";
	public static final String ORDER_PUSH_NUMBER = "18670020376,13691627050,18521303581,18681484876,18666669852";
	/**
	 * 订单推送异常短信提醒号码 ,13691627050,15507597295
	 * @return
	 */
	public static final String ORDER_PUSH_NUMBER_ERROR = "18670020376,13691627050,18521303581,18681484876,18666669852";
	/**
	 * 支付后确认收款到账和出票延误提醒号码 ,13691627050,15507597295
	 * @return
	 */
	public static final String ORDER_TIEMOUT_NUMBER = "18670020376,13691627050,18521303581,18681484876,18666669852";
	/**
	 * 确认收款超时最大时限（单位毫秒）
	 * @return
	 */
	public static final long TIMEOUT_MAX = 60000;
	/**
	 * 出票超时最大时限（单位毫秒）
	 * @return
	 */
	public static final long PRINT_TICKET_TIMEOUT_MAX = 1500000;//25分钟时限

	/**
	 *  取消期限
	 * @return
	 */
//	public static final String pnr_offficeId = "SZX543";// OFFICE号
	public static final String pnr_offficeId = "SZX348";// OFFICE号
//	public static final String pnr_offficeId = "SZX530";// OFFICE号
	/** 短信接口地址:"http://gd.ums86.com:8899/sms/Api/Send.do" **/
	/**
	 * 短信接口地址
	 * @return
	 */
	public static final String POST_URL = "http://gd.ums86.com:8899/sms/Api/Send.do";
	/**
	 *  企业编号
	 * @return
	 */
	public static final String SP_CODE = "216696";
	/**
	 *  用户名称
	 * @return
	 */
	public static final String LOGIN_NAME = "admin3";
	/**
	 *  用户密码
	 * @return
	 */
	public static final String LOGIN_PASSWORD = "SECURITY_LOGIN_PASSWORD";
	/**
	 *  提交时检测方式
	 * @return
	 */
	public static final String COMMOND_CHECK_TYPE_1 = "1";
	/**
	 * 接入号扩展号
	 * @return
	 */
	public static final String EXTEND_ACCESS_NUM = "";
	/**
	 * 短信类型(验证码)
	 * @return
	 */
	public static final String MESSAGE_TYPE_1 = "1";
	/**
	 * 短信类型(修改手机号)
	 * @return
	 */
	public static final String MESSAGE_TYPE_2 = "2";
	/**
	 * 短信类型(出票提醒)
	 * @return
	 */
	public static final String MESSAGE_TYPE_12 = "12";
	public static final String MESSAGE_TYPE_13 = "13";
	/**
	 * 短信类型(出票提醒类型)
	 * @return
	 */
	public static final String MESSAGE_TYPE_3 = "3";
	/**
	 * 订单推送异常提醒类型
	 * @return
	 */
	public static final String MESSAGE_TYPE_4 = "4";
	/**
	 * 已支付，未返回确认收款通知(确认收款到账提醒类型)
	 * @return
	 */
	public static final String MESSAGE_TYPE_5 = "5";
	/**
	 * @return 支付宝支付方式
	 */
	/**
	 * 已支付，已确认收款通知出票延误短信模板类型(出票延误提醒类型)
	 * @return
	 */
	public static final String MESSAGE_TYPE_6 = "6";
	/**
	 * 系统异常短信通知模板类型(系统异常提醒)
	 * @return
	 */
	public static final String MESSAGE_TYPE_7 = "7";
	/**
	 * 抢票计划模板
	 * @return
	 */
	public static final String MESSAGE_TYPE_8 = "8";
	public static final String MESSAGE_TYPE_15 = "15";
	/**
	 * 短信未发送异常监控
	 */
	public static final String MESSAGE_TYPE_16 = "16";
	/**
	 * 用户支付完成短信发送
	 */
	public static final String MESSAGE_TYPE_17 = "17";
	/**
	 * pnr取消异常监控模板类型
	 */
	public static final String MESSAGE_TYPE_20 = "20";
	/**
	 * 用户发送订单过多提醒后台负责人短信模板
	 */
	public static final String MESSAGE_TYPE_21 = "21";
	/**
	 * @return 支付宝支付方式
	 */
	public static final String PAYMENT_METHOD_AL = "ALIPAY_MOBILE";
	/**
	 * @return 便捷支付方式
	 */
	public static final String PAYMENT_METHOD_BJ = "ALIPAY_WAP";
	/**
	 * @return 微信支付方式
	 */
	public static final String PAYMENT_METHOD_WX = "TENPAYGW_MOBILE";
	/**
	 * 消息推送url
	 * @return
	 */
	public static final String SMS_PUSH_URL = "http://122.119.120.35:8180/umeweb/webservice/SCCastEvent?";
	/**
	 * @return 支付成功通知地址
	 */
	public static final String PAY_NOTIFY_URL = "http://" + IPconfig_wai + ":7080/ycbback/payInform";//生产
//	public static final String PAY_NOTIFY_URL = "http://myone-yi.iask.in/ycbback/payInform";//测试
	/**
	 * 出票成功通知地址
	 * @return
	 */
	public static final String OUT_NOTIFY_URL = "http://" + IPconfig_wai + ":7080/ycbback/notify/outNotify";//生产
//	public static final String OUT_NOTIFY_URL = "http://myone-yi.iask.in/ycbback/notify/outNotify";//测试
	
	/**
	 * 支付前信息请求Easypay接口的url
	 * @return
	 */
	//public static final String EASYPAY_URL="http://202.106.139.5/easypay/mobilepay.servlet";//测试
	public static final String EASYPAY_URL = "https://easypay.travelsky.com/easypay/mobilepay.servlet";//生产
	/**
	 * 转发服务器处理地址
	 * @return
	 */
	public static final String FORWARDSERVICE_URL ="http://" + IPconfig_wai + ":7080/yupiaobaoZF/forwardServce";
	/**
	 * 转发服务器专业处理短信提醒业务地址
	 * @return
	 */
	public static final String SENDSMS_URL ="http://" + IPconfig_wai + ":7080/yupiaobaoZF/forwardServce/sendSms";
	/**
	 * 生成billNO的url
	 * @return
	 */
//	public static final String CREATE_BILLNO_URL="http://202.106.139.5/easypay/services/DataService";//测试
	public static final String CREATE_BILLNO_URL="https://easypay.travelsky.com/easypay/services/DataService";//生产
	/**
	 * 推送网赢url
	 * @return
	 */
	public static final String WANG_YING_URL="http://116.31.82.110:8085/OrderFeedback.ashx";//生产
//	public static final String WANG_YING_URL="http://116.31.82.110:8082/OrderFeedback.ashx";//生产
	//public static final String WANG_YING_URL="http://116.31.82.110:8082/OrderFeedback_test.ashx";//测试
	/**
	 * 外网服务的url(测试和生产都需打开，程序已经做了智能判断)
	 * @return
	 */
	public static final String REQUEST_URL="http://61.144.230.200";//测试
	public static final String REQUEST_URL_SC1="http://113.108.96.3";//生产1
	public static final String REQUEST_URL_SC2="http://" + IPconfig_wai + "";//生产2
	/**
	 * 获取access_token url
	 * @return
	 */
	public static final String ACCESS_TOKEN_URL="https://api.weixin.qq.com/cgi-bin/token";
	/**
	 * 获取access_token的参数
	 * @return
	 */
	public static final String GRANT_TYPE ="client_credential";
	/**
	 * 获取access_token的参数,第三方用户唯一凭证密钥
	 * @return
	 */
	public static final String APPSECRET="07ffaa355523c9d2d4ab205878629b11";
	/**
	 * 生成预支付订单url
	 * @return
	 */
	public static final String CREATE_GENPREPAY_URL="https://api.weixin.qq.com/pay/genprepay";
	
	/**
	 * serviceUrl是否为支付前判断依据
	 * @return
	 */
	public static final String MOBILEPAY="mobilepay.servlet";
	/**
	 * serviceUrl是否为订单推送网赢判断依据
	 * @return
	 */
	public static final String ORDERFEEDBACK="OrderFeedback";
	/**
	 * serviceUrl是否为生成billNo判断依据
	 * @return
	 */
	public static final String DATASERVICE="easypay/services/DataService";
	/**
	 * 实际请求服务器的URL代表的Map数据结构键key（key值不可变更）
	 * @return
	 */
	public static final String SERVICE_URL_KEY="create_billno_url";
	
	/**
	 * MD5文件的名做为Map数据结构的key（key值不可变更）
	 * @return
	 */
	public static final String MD5_FILE_KEY="md5_file_name";
	
	public static String USER_PATH = "/config/user.properties";
	
	public static String SERVER_PATH = "/config/server.properties";
}
