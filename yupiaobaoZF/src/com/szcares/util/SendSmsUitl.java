package com.szcares.util;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;

import com.szcares.bean.NotVoteMessage;
import com.szcares.bean.PostMessage;
import com.szcares.bean.PushMessage;
import com.szcares.bean.TimeoutMessage;
import com.szcares.bean.VerifyCodeMessage;

/**
 * 发送短信消息的工具类
 * @author yaoxc
 *
 */
public class SendSmsUitl {
	
	private static final Logger logger = Logger.getLogger(SendSmsUitl.class);
	
	private static Properties properties = null;
	
	public static String getProperty(String key) {
		return properties.getProperty(key);
	}

	/**
	 * 发送短信
	 * @param entity 发送短信的消息实体对象
	 * @return 发送成功与否标识（0：成功，否则发送失败）
	 */
	public static String sms(Object entity) {
		
		String result = MessageConstants.RETURN_MSG_BLANK;
		try {
			HttpClient httpclient = new HttpClient();
			PostMethod post = new PostMethod(Constants.POST_URL);//接口地址
			post.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,MessageConstants.CONTENT_CHARSET);
			post.addParameter("SpCode", Constants.SP_CODE);//企业编号
			post.addParameter("LoginName", Constants.LOGIN_NAME);//用户名称
			post.addParameter("Password", getProperty(Constants.LOGIN_PASSWORD));//用户密码
			
			if(entity instanceof PostMessage){//出票短信提醒
				
				PostMessage postMessage = (PostMessage)entity;
				//post.addParameter("OrgId", postMessage.getOrgId());
				post.addParameter("MessageContent", postMessage.getMessageContent());//短信内容
				post.addParameter("UserNumber", postMessage.getUserNumber());//手机号码
				//post.addParameter("SerialNumber", postMessage.getSerialNumber());//流水号
				post.addParameter("ScheduleTime", postMessage.getScheduleTime());//预约发送时间
				post.addParameter("ExtendAccessNum", postMessage.getExtendAccessNum());//接入号扩展号
				post.addParameter("f", postMessage.getF());//提交时检测方式
				logger.info("----------------PostMessage------->");//【runqian 2017-07-15】sonar 日志 bug 修复
				
			}else if(entity instanceof PushMessage){//订单推送短信提醒
				
				PushMessage msg = (PushMessage)entity;
				/*post.addParameter("OrderNo", msg.getOrderNo());
				post.addParameter("PaymentAmount", msg.getPaymentAmount());
				post.addParameter("PaymentMethod", msg.getPaymentMethod());
				post.addParameter("PushDateTime", msg.getPushDateTime());
				post.addParameter("MessageType", msg.getMessageType());
				post.addParameter("OrgId", msg.getOrgId());*/
		
				post.addParameter("MessageContent", msg.getMessageContent());
				post.addParameter("UserNumber", msg.getUserNumber());
				post.addParameter("ScheduleTime", msg.getScheduleTime());
				post.addParameter("ExtendAccessNum", msg.getExtendAccessNum());
				post.addParameter("f", msg.getDetectionMethod());
				//post.addParameter("SerialNumber", "20151029102629000001");//流水号
				//logger.info("-----------------PushMessage------> "+msg.toString());
				System.out.println("-----------------PushMessage------> "+msg.toString());
				
			}else if(entity instanceof TimeoutMessage){//订单已支付，确认通知出票超时的短信提醒
				
				TimeoutMessage msg = (TimeoutMessage)entity;
			/*	post.addParameter("OrderNo", msg.getOrderNo());
				post.addParameter("PaymentAmount", msg.getPaymentAmount());
				post.addParameter("PaymentMethod", msg.getPaymentMethod());
				post.addParameter("PaymentDateTime", msg.getPaymentDateTime());
				post.addParameter("MessageType", msg.getMessageType());
				post.addParameter("OrgId", msg.getOrgId());*/
				post.addParameter("MessageContent", msg.getMessageContent());
				post.addParameter("UserNumber", msg.getUserNumber());
				post.addParameter("ScheduleTime", msg.getScheduleTime());
				post.addParameter("ExtendAccessNum", msg.getExtendAccessNum());
				post.addParameter("f", msg.getDetectionMethod());
				logger.info("-----------------TimeoutMessage------>");//【runqian 2017-07-15】sonar 日志 bug 修复
			}else if(entity instanceof NotVoteMessage){
				
				NotVoteMessage msg = (NotVoteMessage)entity;
				/*post.addParameter("OrderNo", msg.getOrderNo());
				post.addParameter("PaymentAmount", msg.getPaymentAmount());
				post.addParameter("PaymentMethod", msg.getPaymentMethod());
				post.addParameter("PaymentDateTime", msg.getPaymentDateTime());
				post.addParameter("MessageType", msg.getMessageType());
				post.addParameter("OrgId", msg.getOrgId());*/
				post.addParameter("MessageContent", msg.getMessageContent());
				post.addParameter("UserNumber", msg.getUserNumber());
				post.addParameter("ScheduleTime", msg.getScheduleTime());
				post.addParameter("ExtendAccessNum", msg.getExtendAccessNum());
				post.addParameter("f", msg.getDetectionMethod());
				logger.info("-----------------NotVoteMessage-------");//【runqian 2017-07-15】sonar 日志 bug 修复
			}else if(entity instanceof VerifyCodeMessage){
				
				VerifyCodeMessage msg = (VerifyCodeMessage)entity;
				/*post.addParameter("OrderNo", msg.getOrderNo());
				post.addParameter("PaymentAmount", msg.getPaymentAmount());
				post.addParameter("PaymentMethod", msg.getPaymentMethod());
				post.addParameter("PaymentDateTime", msg.getPaymentDateTime());
				post.addParameter("MessageType", msg.getMessageType());
				post.addParameter("OrgId", msg.getOrgId());*/
				post.addParameter("MessageContent", msg.getMessageContent());
				post.addParameter("UserNumber", msg.getUserNumber());
				post.addParameter("ScheduleTime", msg.getScheduleTime());
				post.addParameter("ExtendAccessNum", msg.getExtendAccessNum());
				post.addParameter("f", msg.getDetectionMethod());
				//logger.info("-----------------VerifyCodeMessage------> "+msg.toString());
			}


			httpclient.executeMethod(post);

			String info = new String(post.getResponseBody(),
					MessageConstants.CONTENT_CHARSET);

			String[] array0 = info.split("&")[0].split("=");
			result = array0[1];
			logger.info(String.format("短信发送结果result= %s ******%s", result, info));//【runqian 2017-07-15】sonar 日志 bug 修复
		} catch (Exception e) {
			logger.error("SendSmsUitl.sms 发生异常");
		}
		return result;
	}
	
	/**
	 * 发送短信接口
	 * @param msgMap  KEY:手机号码 Value:短信内容
	 * String[] 乘机人姓名   出发日期  航空公司  航班号   出发时间  出发机场  到达时间  到达机场  票号
	 */
	public static void sendMsg(Map<String, String[]> msgMap){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyymmddhhmmss");
		PostMessage postMessage = new PostMessage();
		postMessage.setMessageType(Constants.MESSAGE_TYPE_12);// 短信类型
		//modify by chencl,don't use keyset 2017-7-12
		Iterator<Map.Entry<String, String[]>> it = msgMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, String[]> entry = it.next();
			String messageContent=CommonUtils.getMessageContent(Constants.MESSAGE_TYPE_12, msgMap.get(entry.getKey()));
			String newSerialNumber=sdf.format(new Date());
			postMessage.setMessageContent(messageContent);// 短信内容
			postMessage.setUserNumber(entry.getKey());// 手机号
			postMessage.setSerialNumber(newSerialNumber);// 流水号
			postMessage.setScheduleTime(MessageConstants.RETURN_MSG_BLANK);// 计划发送时间
			postMessage.setExtendAccessNum(Constants.EXTEND_ACCESS_NUM);// 接入号扩展号
			postMessage.setF(Constants.COMMOND_CHECK_TYPE_1);// 提交时检测方式
			SendSmsUitl.sms(postMessage);// 发送短信接口
			logger.info("-------------pnr修改发送短信接口成功------------------");
		}
		
	}
	
	
	public static String sendErrorMsg(Map<String, String[]> msgMap){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyymmddhhmmss");
		PostMessage postMessage = new PostMessage();
		postMessage.setMessageType(Constants.MESSAGE_TYPE_7);// 短信类型
		//modify by chencl,don't use keyset 2017-7-12
		String sms="";
		Iterator<Map.Entry<String, String[]>> it = msgMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, String[]> entry = it.next();
			String messageContent=CommonUtils.getMessageContent(Constants.MESSAGE_TYPE_7, msgMap.get(entry.getKey()));
			/*modify by chencl 2017-7-12*/
			//messageContent.replaceAll(" ", "'");
			String newSerialNumber=sdf.format(new Date());
			postMessage.setMessageContent(messageContent);// 短信内容
			postMessage.setUserNumber(entry.getKey());// 手机号
			postMessage.setSerialNumber(newSerialNumber);// 流水号
			postMessage.setScheduleTime(MessageConstants.RETURN_MSG_BLANK);// 计划发送时间
			postMessage.setExtendAccessNum(Constants.EXTEND_ACCESS_NUM);// 接入号扩展号
			postMessage.setF(Constants.COMMOND_CHECK_TYPE_1);// 提交时检测方式
			sms = SendSmsUitl.sms(postMessage);// 发送短信接口
			System.out.println("短信发送状态::::::::::"+sms);
			logger.info("-------------系统异常短信通知发送成功------------------");
		}
		return sms;
	}
	
	/**
	 * 获取剩余短信条数
	 * @throws IOException 
	 * @throws HttpException 
	 */
	public static String getMsgCount(){
		String count="";
		try {
			HttpClient httpclient = new HttpClient();
			PostMethod post = new PostMethod("http://sms.api.ums86.com:8899/sms/Api/SearchNumber.do");//接口地址
			post.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,MessageConstants.CONTENT_CHARSET);
			post.addParameter("SpCode", Constants.SP_CODE);//企业编号
			post.addParameter("LoginName", Constants.LOGIN_NAME);//用户名称
			post.addParameter("Password", getProperty(Constants.LOGIN_PASSWORD));//用户密码
			int executeMethod = httpclient.executeMethod(post);
			if (executeMethod==200) {
				String responseBodyAsString = post.getResponseBodyAsString();
				String[] results = responseBodyAsString.split("&");
				if (results.length==3) {
					count=results[2];
				}
				System.out.println(responseBodyAsString);
			}
		} catch (Exception e) {
			logger.info("getMsgCount function exception occurred!");
		}
		return count;
	}
}
