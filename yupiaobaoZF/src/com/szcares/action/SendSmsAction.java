package com.szcares.action;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;

import com.szcares.bean.NotVoteMessage;
import com.szcares.bean.PostMessage;
import com.szcares.bean.PushMessage;
import com.szcares.bean.TimeoutMessage;
import com.szcares.util.Constants;
import com.szcares.util.MessageConstants;

public class SendSmsAction extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6595388409163105230L;
	private Logger logger = Logger.getLogger(SendSmsAction.class);
	private static Properties properties = null;
	
	public static String getProperty(String key) {
		return properties.getProperty(key);
	}

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doPost(req, resp);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setCharacterEncoding(MessageConstants.CHARACTER_ENCODING);
		resp.setContentType(MessageConstants.CONTENT_TYPE);
		req.setCharacterEncoding(MessageConstants.CHARACTER_ENCODING);
		String msgType = req.getParameter("msgType");// 短信发送类型 sendMsg
														// sendErrorMsg
														// getMsgCount
		String msgMap = req.getParameter("msgMap");// 短信内容 json格式字符串
		String sendMsgType = req.getParameter("sendMsgType");// 短信内容 json格式字符串
		JSONObject resultJson = new JSONObject();// 响应结果JSON
		PrintWriter out = resp.getWriter();
		Map<String, Object[]> msgMapSend = new HashMap<String, Object[]>();
		try {
			JSONObject map = JSONObject.fromObject(msgMap);// 短信内容转换JSON
			// modify by chencl 2017-7-12
			Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, String> entry = it.next();
				msgMapSend.put(entry.getKey(), map.getJSONArray(entry.getKey()).toArray());
			}
			if ("".equals(msgType) || null == msgType) {
				resultJson.put("returnCode", MessageConstants.RETURN_MSG_ID_800001);
				resultJson.put("returnMsg", "短信发送类型为空");
				out.print(resultJson.toString());
				return;
			}
			if ("".equals(msgMap) || null == msgMap) {
				if (!msgType.equals("getMsgCount")) {// 如果不是查询短信条数 短信发送内容不能为空
					resultJson.put("returnCode", MessageConstants.RETURN_MSG_ID_800001);
					resultJson.put("returnMsg", "短信发送内容为空");
					out.print(resultJson.toString());
					return;
				}
			}
			if (msgType.equals("sendMsg")) {
				String result = this.sendMsg(msgMapSend);
				if (sendMsgType != null) {
					this.sendMsg(msgMapSend, sendMsgType);
				}
				if ("0".equals(result)) {
					resultJson.put("returnCode", MessageConstants.RETURN_MSG_ID_999999);
					resultJson.put("returnMsg", MessageConstants.RETURN_MSG_STR_999999);
				} else {
					resultJson.put("returnCode", MessageConstants.RETURN_MSG_ID_800001);
					resultJson.put("returnMsg", MessageConstants.RETURN_MSG_STR_800001);
				}
			}
			if (msgType.equals("sendErrorMsg")) {
				String result = this.sendErrorMsg(msgMapSend);
				if ("0".equals(result)) {
					resultJson.put("returnCode", MessageConstants.RETURN_MSG_ID_999999);
					resultJson.put("returnMsg", MessageConstants.RETURN_MSG_STR_999999);
				} else {
					resultJson.put("returnCode", MessageConstants.RETURN_MSG_ID_800001);
					resultJson.put("returnMsg", MessageConstants.RETURN_MSG_STR_800001);
				}
			}
			if (msgType.equals("getMsgCount")) {
				String msgCount = this.getMsgCount();
				resultJson.put("returnCode", MessageConstants.RETURN_MSG_ID_999999);
				resultJson.put("returnMsg", msgCount);
			}
			if (msgType.equals("sendCodeMsg")) {
				String result = this.sendCodeMsg(msgMapSend);
				if ("0".equals(result)) {
					resultJson.put("returnCode", MessageConstants.RETURN_MSG_ID_999999);
					resultJson.put("returnMsg", MessageConstants.RETURN_MSG_STR_999999);
				} else {
					resultJson.put("returnCode", MessageConstants.RETURN_MSG_ID_800001);
					resultJson.put("returnMsg", MessageConstants.RETURN_MSG_STR_800001);
				}
			}
			if (msgType.equals("sendGardTicketMsg")) {
				String result = this.sendGardTicketMsg(msgMapSend);
				if ("0".equals(result)) {
					resultJson.put("returnCode", MessageConstants.RETURN_MSG_ID_999999);
					resultJson.put("returnMsg", MessageConstants.RETURN_MSG_STR_999999);
				} else {
					resultJson.put("returnCode", MessageConstants.RETURN_MSG_ID_800001);
					resultJson.put("returnMsg", MessageConstants.RETURN_MSG_STR_800001);
				}
			}
			if (msgType.equals("sendCountTicketMsg")) {
				String result = this.sendCountTicketMsg(msgMapSend);
				if ("0".equals(result)) {
					resultJson.put("returnCode", MessageConstants.RETURN_MSG_ID_999999);
					resultJson.put("returnMsg", MessageConstants.RETURN_MSG_STR_999999);
				} else {
					resultJson.put("returnCode", MessageConstants.RETURN_MSG_ID_800001);
					resultJson.put("returnMsg", MessageConstants.RETURN_MSG_STR_800001);
				}
			}

		} catch (Exception e) {
			logger.info("doPost function exception occurred!");
			resultJson.put("returnCode", MessageConstants.RETURN_MSG_ID_800009);
			resultJson.put("returnMsg", MessageConstants.RETURN_MSG_STR_800009);
		}

		out.print(resultJson.toString());
		out.flush();
		out.close();

	}

	private String sendCountTicketMsg(Map<String, Object[]> msgMap) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyymmddhhmmss");
		PostMessage postMessage = new PostMessage();
		postMessage.setMessageType(Constants.MESSAGE_TYPE_15);// 短信类型
		String sms = MessageConstants.RETURN_MSG_BLANK;
		/**modify by chencl 2017-7-17**/
		Iterator<Entry<String, Object[]>> it = msgMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, Object[]> entry = it.next();
			String messageContent = getMessageContent(Constants.MESSAGE_TYPE_15, msgMap.get(entry.getKey()));
			/* modify by chencl 2017-7-12 **/
			// messageContent.replaceAll(" ", "'");
			String newSerialNumber = sdf.format(new Date());
			postMessage.setMessageContent(messageContent);// 短信内容
			postMessage.setUserNumber(entry.getKey());// 手机号
			postMessage.setSerialNumber(newSerialNumber);// 流水号
			postMessage.setScheduleTime(MessageConstants.RETURN_MSG_BLANK);// 计划发送时间
			postMessage.setExtendAccessNum(Constants.EXTEND_ACCESS_NUM);// 接入号扩展号
			postMessage.setF(Constants.COMMOND_CHECK_TYPE_1);// 提交时检测方式
			sms = this.sms(postMessage);// 发送短信接口
			logger.info("-------------抢票超过次数过多通知发送成功------------------");
		}
		return sms;

	}

	/**
	 * 发送短信接口
	 * 
	 * @param msgMap
	 *            KEY:手机号码 Value:短信内容
	 * 
	 */
	public String sendCodeMsg(Map<String, Object[]> msgMap) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyymmddhhmmss");
		PostMessage postMessage = new PostMessage();
		postMessage.setMessageType(Constants.MESSAGE_TYPE_1);// 短信类型
		// modify by chencl 2017-7-12
		String result = "";
		Iterator<Map.Entry<String, Object[]>> it = msgMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, Object[]> entry = it.next();
			String messageContent = getMessageContent(Constants.MESSAGE_TYPE_1, msgMap.get(entry.getKey()));
			String newSerialNumber = sdf.format(new Date());
			postMessage.setMessageContent(messageContent);// 短信内容
			postMessage.setUserNumber(entry.getKey());// 手机号
			postMessage.setSerialNumber(newSerialNumber);// 流水号
			postMessage.setScheduleTime(MessageConstants.RETURN_MSG_BLANK);// 计划发送时间
			postMessage.setExtendAccessNum(Constants.EXTEND_ACCESS_NUM);// 接入号扩展号
			postMessage.setF(Constants.COMMOND_CHECK_TYPE_1);// 提交时检测方式
			result = this.sms(postMessage);// 发送短信接口
			logger.info(String.format("-------------短信发送成功------------------ %s", result));//【runqian 2017-07-15】sonar 日志 bug 修复
		}
		return result;
	}

	private String sendMsg(Map<String, Object[]> msgMap, String sendMsgType) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyymmddhhmmss");
		PostMessage postMessage = new PostMessage();
		if (sendMsgType.equals("13")) {
			postMessage.setMessageType(Constants.MESSAGE_TYPE_13);// 短信类型
		} else {
			postMessage.setMessageType(Constants.MESSAGE_TYPE_12);// 短信类型
		}
		// modify by chencl 2017-7-12
		String result = "";
		Iterator<Map.Entry<String, Object[]>> it = msgMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, Object[]> entry = it.next();
			String messageContent = "";
			if (sendMsgType.equals("13")) {
				messageContent = getMessageContent(Constants.MESSAGE_TYPE_13, msgMap.get(entry.getKey()));
			} else {
				messageContent = getMessageContent(Constants.MESSAGE_TYPE_12, msgMap.get(entry.getKey()));
			}
			String newSerialNumber = sdf.format(new Date());
			postMessage.setMessageContent(messageContent);// 短信内容
			postMessage.setUserNumber(entry.getKey());// 手机号
			postMessage.setSerialNumber(newSerialNumber);// 流水号
			postMessage.setScheduleTime(MessageConstants.RETURN_MSG_BLANK);// 计划发送时间
			postMessage.setExtendAccessNum(Constants.EXTEND_ACCESS_NUM);// 接入号扩展号
			postMessage.setF(Constants.COMMOND_CHECK_TYPE_1);// 提交时检测方式
			result = sms(postMessage);// 发送短信接口
			logger.info(String.format("-------------短信发送成功------------------ %s", result));//【runqian 2017-07-15】sonar 日志 bug 修复
		}
		return result;
	}

	/**
	 * 发送短信接口
	 * 
	 * @param msgMap
	 *            KEY:手机号码 Value:短信内容 String[] 乘机人姓名 出发日期 航空公司 航班号 出发时间 出发机场
	 *            到达时间 到达机场 票号
	 */
	public String sendMsg(Map<String, Object[]> msgMap) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyymmddhhmmss");
		PostMessage postMessage = new PostMessage();
		postMessage.setMessageType(Constants.MESSAGE_TYPE_12);// 短信类型
		String result = "";
		Iterator<Map.Entry<String, Object[]>> it = msgMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, Object[]> entry = it.next();
			String messageContent = getMessageContent(Constants.MESSAGE_TYPE_12, msgMap.get(entry.getKey()));
			String newSerialNumber = sdf.format(new Date());
			postMessage.setMessageContent(messageContent);// 短信内容
			postMessage.setUserNumber(entry.getKey());// 手机号
			postMessage.setSerialNumber(newSerialNumber);// 流水号
			postMessage.setScheduleTime(MessageConstants.RETURN_MSG_BLANK);// 计划发送时间
			postMessage.setExtendAccessNum(Constants.EXTEND_ACCESS_NUM);// 接入号扩展号
			postMessage.setF(Constants.COMMOND_CHECK_TYPE_1);// 提交时检测方式
			result = this.sms(postMessage);// 发送短信接口
			logger.info(String.format("-------------短信发送成功------------------ %s", result));//【runqian 2017-07-15】sonar 日志 bug 修复
		}
		return result;
	}

	/**
	 * 异常监控短信发送
	 * 
	 * @param msgMap
	 * @return
	 */
	public String sendErrorMsg(Map<String, Object[]> msgMap) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyymmddhhmmss");
		PostMessage postMessage = new PostMessage();
		postMessage.setMessageType(Constants.MESSAGE_TYPE_7);// 短信类型
		String sms = MessageConstants.RETURN_MSG_BLANK;
		// modify by chencl 2017-7-12
		Iterator<Map.Entry<String, Object[]>> it = msgMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, Object[]> entry = it.next();
			String messageContent = getMessageContent(Constants.MESSAGE_TYPE_7, msgMap.get(entry.getKey()));
			// messageContent.replaceAll(" ", "'");
			String newSerialNumber = sdf.format(new Date());
			postMessage.setMessageContent(messageContent);// 短信内容
			postMessage.setUserNumber(entry.getKey());// 手机号
			postMessage.setSerialNumber(newSerialNumber);// 流水号
			postMessage.setScheduleTime(MessageConstants.RETURN_MSG_BLANK);// 计划发送时间
			postMessage.setExtendAccessNum(Constants.EXTEND_ACCESS_NUM);// 接入号扩展号
			postMessage.setF(Constants.COMMOND_CHECK_TYPE_1);// 提交时检测方式
			sms = this.sms(postMessage);// 发送短信接口
			logger.info("-------------系统异常短信通知发送成功------------------");
		}
		return sms;
	}

	/**
	 * 抢票信息发送
	 * 
	 * @param msgMap
	 * @return
	 */
	public String sendGardTicketMsg(Map<String, Object[]> msgMap) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyymmddhhmmss");
		PostMessage postMessage = new PostMessage();
		postMessage.setMessageType(Constants.MESSAGE_TYPE_8);// 短信类型
		String sms = MessageConstants.RETURN_MSG_BLANK;
		// modify by chencl 2017-7-12
		Iterator<Map.Entry<String, Object[]>> it = msgMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, Object[]> entry = it.next();
			String messageContent = getMessageContent(Constants.MESSAGE_TYPE_8, msgMap.get(entry.getKey()));
			// messageContent.replaceAll(" ", "'");
			String newSerialNumber = sdf.format(new Date());
			postMessage.setMessageContent(messageContent);// 短信内容
			postMessage.setUserNumber(entry.getKey());// 手机号
			postMessage.setSerialNumber(newSerialNumber);// 流水号
			postMessage.setScheduleTime(MessageConstants.RETURN_MSG_BLANK);// 计划发送时间
			postMessage.setExtendAccessNum(Constants.EXTEND_ACCESS_NUM);// 接入号扩展号
			postMessage.setF(Constants.COMMOND_CHECK_TYPE_1);// 提交时检测方式
			sms = this.sms(postMessage);// 发送短信接口
			logger.info("-------------抢票通知发送成功------------------");
		}
		return sms;
	}

	/**
	 * 获取剩余短信条数
	 * 
	 * @throws IOException
	 * @throws HttpException
	 */
	public String getMsgCount() {
		String count = MessageConstants.RETURN_MSG_BLANK;
		try {
			HttpClient httpclient = new HttpClient();
			PostMethod post = new PostMethod("http://sms.api.ums86.com:8899/sms/Api/SearchNumber.do");// 接口地址
			post.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "gbk");
			post.addParameter("SpCode", Constants.SP_CODE);// 企业编号
			post.addParameter("LoginName", Constants.LOGIN_NAME);// 用户名称
			post.addParameter("Password", getProperty(Constants.LOGIN_PASSWORD));// 用户密码
			int executeMethod = httpclient.executeMethod(post);
			if (executeMethod == 200) {
				String responseBodyAsString = post.getResponseBodyAsString();
				String[] results = responseBodyAsString.split("&");
				if (results.length == 3) {
					count = results[2];
				}
			}
		} catch (Exception e) {
			logger.error("getMsgCount function exception occurred!");
		}
		return count;
	}

	/**
	 * 发送短信
	 * 
	 * @param entity
	 *            发送短信的消息实体对象
	 * @return 发送成功与否标识（0：成功，否则发送失败）
	 */
	public String sms(Object entity) {

		String result = "";
		try {
			HttpClient httpclient = new HttpClient();
			PostMethod post = new PostMethod(Constants.POST_URL);// 接口地址
			post.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "gbk");
			post.addParameter("SpCode", Constants.SP_CODE);// 企业编号
			post.addParameter("LoginName", Constants.LOGIN_NAME);// 用户名称
			post.addParameter("Password", getProperty(Constants.LOGIN_PASSWORD));// 用户密码

			if (entity instanceof PostMessage) {// 出票短信提醒

				PostMessage postMessage = (PostMessage) entity;
				post.addParameter("MessageContent", postMessage.getMessageContent());// 短信内容
				post.addParameter("UserNumber", postMessage.getUserNumber());// 手机号码
				post.addParameter("SerialNumber", postMessage.getSerialNumber());// 流水号
				post.addParameter("ScheduleTime", postMessage.getScheduleTime());// 预约发送时间
				post.addParameter("ExtendAccessNum", postMessage.getExtendAccessNum());// 接入号扩展号
				post.addParameter("f", postMessage.getF());// 提交时检测方式
				// logger.info("----------------PostMessage------->
				// "+postMessage.toString());

			} else if (entity instanceof PushMessage) {// 订单推送短信提醒

				PushMessage msg = (PushMessage) entity;
				post.addParameter("OrderNo", msg.getOrderNo());
				post.addParameter("PaymentAmount", msg.getPaymentAmount());
				post.addParameter("PaymentMethod", msg.getPaymentMethod());
				post.addParameter("PushDateTime", msg.getPushDateTime());
				post.addParameter("MessageContent", msg.getMessageContent());
				post.addParameter("MessageType", msg.getMessageType());
				post.addParameter("OrgId", msg.getOrgId());
				post.addParameter("UserNumber", msg.getUserNumber());
				post.addParameter("ScheduleTime", msg.getScheduleTime());
				post.addParameter("ExtendAccessNum", msg.getExtendAccessNum());
				post.addParameter("DetectionMethod", msg.getDetectionMethod());
				// logger.info("-----------------PushMessage------>
				// "+msg.toString());

			} else if (entity instanceof TimeoutMessage) {// 订单已支付，确认通知出票超时的短信提醒

				TimeoutMessage msg = (TimeoutMessage) entity;
				post.addParameter("OrderNo", msg.getOrderNo());
				post.addParameter("PaymentAmount", msg.getPaymentAmount());
				post.addParameter("PaymentMethod", msg.getPaymentMethod());
				post.addParameter("PaymentDateTime", msg.getPaymentDateTime());
				post.addParameter("MessageContent", msg.getMessageContent());
				post.addParameter("MessageType", msg.getMessageType());
				post.addParameter("OrgId", msg.getOrgId());
				post.addParameter("UserNumber", msg.getUserNumber());
				post.addParameter("ScheduleTime", msg.getScheduleTime());
				post.addParameter("ExtendAccessNum", msg.getExtendAccessNum());
				post.addParameter("DetectionMethod", msg.getDetectionMethod());
				// logger.info("-----------------TimeoutMessage------>
				// "+msg.toString());
			} else if (entity instanceof NotVoteMessage) {

				NotVoteMessage msg = (NotVoteMessage) entity;
				post.addParameter("OrderNo", msg.getOrderNo());
				post.addParameter("PaymentAmount", msg.getPaymentAmount());
				post.addParameter("PaymentMethod", msg.getPaymentMethod());
				post.addParameter("PaymentDateTime", msg.getPaymentDateTime());
				post.addParameter("MessageContent", msg.getMessageContent());
				post.addParameter("MessageType", msg.getMessageType());
				post.addParameter("OrgId", msg.getOrgId());
				post.addParameter("UserNumber", msg.getUserNumber());
				post.addParameter("ScheduleTime", msg.getScheduleTime());
				post.addParameter("ExtendAccessNum", msg.getExtendAccessNum());
				post.addParameter("DetectionMethod", msg.getDetectionMethod());
				// logger.info("-----------------NotVoteMessage------>
				// "+msg.toString());
			}

			httpclient.executeMethod(post);

			String info = new String(post.getResponseBody(), "gbk");

			String[] array0 = info.split("&")[0].split("=");
			result = array0[1];

		} catch (Exception e) {
			logger.info("sms function exception occurred!");
		}
		return result;
	}

	/**
	 * 获取短信内容
	 * 
	 * @param 短信类型
	 *            String
	 * @param 拼接内容
	 *            String
	 * @return 短信内容
	 */
	public String getMessageContent(String type, Object array[]) {

		String messageContent = "";
		// 读写properties文件
		Properties properties = new Properties();
		InputStream inputStream = null;
		try {

			// 加载设有content内容的properties文件
			String webPath = SendSmsAction.class.getResource("/").getPath();
			String url = webPath.substring(1, webPath.length()) + "SendSms.properties";
			inputStream = new FileInputStream(url);
			properties.load(inputStream);

			// 取.content为key的值
			String msg = properties.getProperty(type + ".content");

			if (msg != null) {

				messageContent = MessageFormat.format(msg, array);
			}
			return messageContent;
		} catch (IOException ex) {
			logger.error("SendSmsAction.getMessageContent 发生 IOException 异常");
		} catch (Exception e) {
			logger.error("SendSmsAction.getMessageContent 发生异常");
		} finally {
			try {
				if (null != inputStream) {
					inputStream.close();
				}
			} catch (IOException e) {
				logger.error("inputStream close 异常");
			}
		}
		return messageContent;
	}

}
