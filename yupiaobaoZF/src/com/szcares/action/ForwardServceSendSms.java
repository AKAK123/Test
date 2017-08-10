package com.szcares.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.szcares.bean.NotVoteMessage;
import com.szcares.bean.PostMessage;
import com.szcares.bean.PushMessage;
import com.szcares.bean.TimeoutMessage;
import com.szcares.bean.VerifyCodeMessage;
import com.szcares.util.CommonUtils;
import com.szcares.util.Constants;
import com.szcares.util.MessageConstants;
import com.szcares.util.SendSmsUitl;


/**
 * 转发短信服务处理类
 * @author yaoxc
 *
 */
public class ForwardServceSendSms extends HttpServlet{
	
	private static final Logger logger = Logger.getLogger(ForwardServceSendSms.class);

	private static final long serialVersionUID = -3071999102283708443L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doPost(req, resp);
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		//String serviceUrl = CommonUtils.strProcessing(req.getParameter("serviceUrl"));
    	String reqParameter = CommonUtils.strProcessing(req.getParameter("reqParameter"));
    	String username = req.getParameter("username");
    	String pwd = req.getParameter("pwd");
    	
    	/*System.out.println("==========> serviceUrl: "+serviceUrl);
    	System.out.println("==========> reqParameter: "+reqParameter);
    	System.out.println("==========> IP: "+InetAddress.getLocalHost().getHostAddress());*/
    	System.out.println("==========> reqParameter: "+reqParameter);
    	boolean result = false;
    	
    	if (username == null || pwd == null) {
    		
    		Object message = null;
    		Map<String,String> map = CommonUtils.strToMap(reqParameter);
    		String smsType = CommonUtils.strProcessing(map.get("smsType"));
    		
    		if(smsType.equals(Constants.MESSAGE_TYPE_4)){//订单推送异常提醒类型
    			//订单未推送给代理！商户{0}，订单{1}，PNR<{2}>，乘机人{3}，支付方式{4}，于{5}支付成功。
    			String[] arry = new String[]{map.get("orgId"),map.get("orderNo"),map.get("pnrNo"),map.get("passenger"),map.get("paymentMehtod"),map.get("payDateTime")};
    			message = packPushMessage(arry);
    			
    		}else if("".equals(smsType)){//业务流程成功处理完成发送短信提醒出票中类型
    			//商户号：{0}，订单号：{1}，已于{2}支付成功，支付方式：{3}，PNR:<{4}>，乘机人：{5}，请出票。
    			message = packPostMessage(reqParameter);
    			
    		}else if(smsType.equals(Constants.MESSAGE_TYPE_5)){//订单已支付，easypay后台确认收款通知出票超时
    			//未收到easypay确认支付！商户{0}，订单{1}，PNR<{2}>，乘机人{3}，支付方式{4}，于{5}完成支付。
    			String[] arry = new String[]{map.get("orgId"),map.get("orderNo"),map.get("pnrNo"),map.get("passenger"),map.get("paymentMehtod"),map.get("payDateTime")};
    			message = packTimeoutMessage(arry);
    			
    		}else if(smsType.equals(Constants.MESSAGE_TYPE_6)){//订单已支付，已确认收款通知/订单推送成功，出票延误
    			//代理出票延误（超时30分钟）！于{0}订单推送成功；商户{1}，订单{2}，PNR<{3}>，乘机人{4}，支付方式{5}。
    			String[] arry = new String[]{map.get("payDateTime"),map.get("orgId"),map.get("orderNo"),map.get("pnrNo"),map.get("passenger"),map.get("paymentMehtod")};
    			message = packNotVoteMessage(arry);
    			
    		}else if(smsType.equals(Constants.MESSAGE_TYPE_1)){//短信验证码
    			String[] arry = new String[]{map.get("verifyCode")};
    			message = packCodeMessage(arry,map);
    			
    		}else if (smsType.equals(Constants.MESSAGE_TYPE_16)) {//出票通知失败
				String[] array = new String[]{map.get("timer"),map.get("merchant"),map.get("orderNo"),map.get("pnrNo"),map.get("passenger"),map.get("paymentMehtod"),map.get("tkno")};
				message = ticketToticeMessage(array);
			}else if(smsType.equals(Constants.MESSAGE_TYPE_20)){//pnr取消异常监控
				message = packageMessage(map);
			}else if(smsType.equals(Constants.MESSAGE_TYPE_21)){//订单过多通知后台负责人
				message = noticeBackendMessage(map);
			}else if(smsType.equals(Constants.MESSAGE_TYPE_17)){//通知用户支付成功订单确认
				String[] array = {map.get("orderNo"),map.get("passenger"),map.get("flightInfo"),map.get("payAmount")};
				message = noticeUserPay(array,map.get("phoneNum"));
			}
    		
			if("0".equals(SendSmsUitl.sms(message))){//发送短信提醒成功
				result = true;
			}else{
				result = "0".equals(SendSmsUitl.sms(message))? true : false;
			}
    	}
    	
    	 try
 	    {
 	      	PrintWriter out;
 	        resp.setCharacterEncoding("UTF-8");
 	        out = resp.getWriter();
 	        out.write(String.valueOf(result));
 	        out.flush();
 	        out.close();
 	    }catch (Exception e) {
 	    	logger.error("ForwardServceSendSms.doPost 发生异常!");
 	    }
	}
	/**
	 * 封装发送短信监控消息内容
	 * @param map 短信消息内容封装Map集合
	 * @return PostMessage（发送短信消息实例对象）
	 */
	private PostMessage packageMessage(Map<String, String> map) {
		
		PostMessage message = new PostMessage();
		//超过半小时，未取消的PNR NO：[{0}]，请及时处理！
		String[] arry = new String[]{map.get("pnrNos")};
		String messageContent = CommonUtils.getMessageContent(Constants.MESSAGE_TYPE_20, arry);
		message.setMessageContent(messageContent);
		message.setUserNumber(map.get("tels"));//发送的号码串，如多个号码则用“,”分割。例：13122232993,15845676335
		message.setScheduleTime(MessageConstants.RETURN_MSG_BLANK);
		message.setExtendAccessNum(Constants.EXTEND_ACCESS_NUM);
		message.setF(Constants.COMMOND_CHECK_TYPE_1);
		
		return message;
	}
	
	/**
	 * 通知后台管理员方法
	 * @param arry
	 * @return
	 */
	public PostMessage noticeBackendMessage(Map<String, String> map) {
		PostMessage message = new PostMessage();
		//用户{0}，于{1}，频繁发送订单
		String[] arry = new String[]{map.get("userId"),map.get("dateTime")};
		String messageContent = CommonUtils.getMessageContent(Constants.MESSAGE_TYPE_21, arry);
		message.setMessageContent(messageContent);
		message.setUserNumber(Constants.ORDER_PUSH_NUMBER);//发送的号码串，如多个号码则用“,”分割。例：13122232993,15845676335
		message.setScheduleTime(MessageConstants.RETURN_MSG_BLANK);
		message.setExtendAccessNum(Constants.EXTEND_ACCESS_NUM);
		message.setF(Constants.COMMOND_CHECK_TYPE_1);
		
		return message;
	}
	
	/**
	 * 通知用户支付成功 订单确认
	 * @param array
	 * @return
	 */
	private PushMessage noticeUserPay(String[] array,String phoneNum){
		System.out.println("----------packPushMessage arry.length= "+array.length);
		PushMessage message = new PushMessage();
		String messageContent = CommonUtils.getMessageContent(Constants.MESSAGE_TYPE_17, array);
		System.out.println("***************短信发送的内容： "+messageContent);
		message.setMessageContent(messageContent);
		message.setUserNumber(phoneNum);
		message.setScheduleTime(MessageConstants.RETURN_MSG_BLANK);
		message.setExtendAccessNum(Constants.EXTEND_ACCESS_NUM);
		message.setDetectionMethod(Constants.COMMOND_CHECK_TYPE_1);
		return message;
	}
	
	private VerifyCodeMessage packCodeMessage(String[] arry, Map<String, String> map) {
		
		VerifyCodeMessage message =  new VerifyCodeMessage();
		String messageContent = CommonUtils.getMessageContent(Constants.MESSAGE_TYPE_1, arry);
		
		message.setMessageContent(messageContent);
		message.setUserNumber(map.get("mobileNo"));
		message.setScheduleTime(MessageConstants.RETURN_MSG_BLANK);
		message.setExtendAccessNum(Constants.EXTEND_ACCESS_NUM);
		message.setDetectionMethod(Constants.COMMOND_CHECK_TYPE_1);
		return message;
	}
	
	/**
	 * 打包订单推送异常短信发送消息
	 * @param arry 推送消息的数组集合
	 * @return 推送消息对象（PushMessage）
	 */
	private PushMessage packPushMessage(String[] arry) {
		PushMessage message = new PushMessage();
		if(arry != null && arry.length >= 5){
			String messageContent = CommonUtils.getMessageContent(Constants.MESSAGE_TYPE_4, arry);
			System.out.println("***************短信发送的内容： "+messageContent);
			/*message.setOrgId(arry[0]);
			message.setOrderNo(arry[1]);
			message.setPaymentMethod(arry[2]);
			message.setPaymentAmount(arry[3]);
			message.setPushDateTime(arry[4]);
			message.setMessageType(Constants.MESSAGE_TYPE_4);*/
			message.setMessageContent(messageContent);
			message.setUserNumber(Constants.ORDER_PUSH_NUMBER_ERROR);
			message.setScheduleTime(MessageConstants.RETURN_MSG_BLANK);
			message.setExtendAccessNum(Constants.EXTEND_ACCESS_NUM);
			message.setDetectionMethod(Constants.COMMOND_CHECK_TYPE_1);
		}
		
		return message;
	}
	/**
	 * 打包催出票短信发送消息
	 * @param messageContent 发送短信的消息内容
	 * @return 发送短信消息对象（PushMessage）
	 */
	private PostMessage packPostMessage(String messageContent) {
		//商户号：{0}，订单号：{1}，已于{2}支付成功，支付方式：{3}，PNR:<{4}>，乘机人：{5}，请出票。
		PostMessage message = new PostMessage();
		//String messageContent = CommonUtils.getMessageContent(Constants.MESSAGE_TYPE_3, arry);
		message.setMessageContent(messageContent);
		//message.setMessageType(Constants.MESSAGE_TYPE_3);
		message.setUserNumber(Constants.ORDER_PUSH_NUMBER);
		message.setScheduleTime(MessageConstants.RETURN_MSG_BLANK);
		message.setExtendAccessNum(Constants.EXTEND_ACCESS_NUM);
		message.setF(Constants.COMMOND_CHECK_TYPE_1);
		
		return message;
	}
	
	/**
	 * 封装支付后确认收款通知超时消息
	 * @param arry 支付后确认收款通知超时消息数组集合
	 * @return 超时消息对象（TimeoutMessage）
	 */
	private TimeoutMessage packTimeoutMessage(String[] arry){
		
		TimeoutMessage message = new TimeoutMessage();
		
		if(arry != null && arry.length >= 5){
			String messageContent = CommonUtils.getMessageContent(Constants.MESSAGE_TYPE_5, arry);
			System.out.println("***************短信发送的内容： "+messageContent);
			/*message.setOrgId(arry[0]);
			message.setOrderNo(arry[1]);
			message.setPaymentMethod(arry[2]);
			message.setPaymentAmount(arry[3]);
			message.setPaymentDateTime(arry[4]);
			message.setMessageType(Constants.MESSAGE_TYPE_5);*/
			message.setMessageContent(messageContent);
			message.setUserNumber(Constants.ORDER_TIEMOUT_NUMBER);
			message.setScheduleTime(MessageConstants.RETURN_MSG_BLANK);
			message.setExtendAccessNum(Constants.EXTEND_ACCESS_NUM);
			message.setDetectionMethod(Constants.COMMOND_CHECK_TYPE_1);
		}

		return message;
	}
	
	/**
	 * 封装出票延误短信通知信息处理
	 * @param arry 已确认收款出票延误消息数组集合
	 * @return 出票延误消息对象（NotVoteMessage）
	 */
	public NotVoteMessage packNotVoteMessage(String[] arry) {
		
		NotVoteMessage message = new NotVoteMessage();
		if(arry != null && arry.length >= 5){
			String messageContent = CommonUtils.getMessageContent(Constants.MESSAGE_TYPE_6, arry);
			System.out.println("***************短信发送的内容： "+messageContent);
			/*message.setOrgId(arry[0]);
			message.setOrderNo(arry[1]);
			message.setPaymentMethod(arry[2]);
			message.setPaymentAmount(arry[3]);
			message.setPaymentDateTime(arry[5]);
			message.setMessageType(Constants.MESSAGE_TYPE_6);*/
			message.setMessageContent(messageContent);
			message.setUserNumber(Constants.ORDER_TIEMOUT_NUMBER);
			message.setScheduleTime(MessageConstants.RETURN_MSG_BLANK);
			message.setExtendAccessNum(Constants.EXTEND_ACCESS_NUM);
			message.setDetectionMethod(Constants.COMMOND_CHECK_TYPE_1);
		}

		return message;
	}
	
	/**
	 * 出票通知失败
	 * @param arry 
	 * @return 
	 */
	public NotVoteMessage ticketToticeMessage(String[] arry) {
		
		NotVoteMessage message = new NotVoteMessage();
		if(arry != null && arry.length >= 5){
			String messageContent = CommonUtils.getMessageContent(Constants.MESSAGE_TYPE_16, arry);
			System.out.println("***************短信发送的内容： "+messageContent);
			/*message.setOrgId(arry[0]);
			message.setOrderNo(arry[1]);
			message.setPaymentMethod(arry[2]);
			message.setPaymentAmount(arry[3]);
			message.setPaymentDateTime(arry[5]);
			message.setMessageType(Constants.MESSAGE_TYPE_6);*/
			message.setMessageContent(messageContent);
			message.setUserNumber(Constants.ORDER_TIEMOUT_NUMBER);
			message.setScheduleTime(MessageConstants.RETURN_MSG_BLANK);
			message.setExtendAccessNum(Constants.EXTEND_ACCESS_NUM);
			message.setDetectionMethod(Constants.COMMOND_CHECK_TYPE_1);
		}
		
		return message;
	}
	


}
