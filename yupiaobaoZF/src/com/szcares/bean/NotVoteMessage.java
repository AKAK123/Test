package com.szcares.bean;
/**
 * 未出票短信消息封装实体类
 * @author yaoxc
 *
 */
public class NotVoteMessage {
	
	private String orderNo;
	private String paymentAmount;
	private String paymentMethod;
	private String PaymentDateTime;
	private String messageContent;
	private String messageType;
	private String orgId;
	private String userNumber;
	private String scheduleTime;
	private String extendAccessNum;
	private String detectionMethod;
	
	
	/**
	 * 获取订单号
	 * @return
	 */
	public String getOrderNo() {
		return orderNo;
	}
	/**
	 * 设置订单号
	 * @param orderNo 订单号
	 */
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	/**
	 * 获取支付金额
	 * @return
	 */
	public String getPaymentAmount() {
		return paymentAmount;
	}
	/**
	 * 设置支付金额
	 * @param paymentAmount 支付金额
	 */
	public void setPaymentAmount(String paymentAmount) {
		this.paymentAmount = paymentAmount;
	}
	/**
	 * 获取支付方式
	 * @return
	 */
	public String getPaymentMethod() {
		return paymentMethod;
	}
	/**
	 * 设置支付方式
	 * @param paymentMethod 支付方式
	 */
	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	/**
	 * 获取支付日期时间
	 * @return
	 */
	public String getPaymentDateTime() {
		return PaymentDateTime;
	}
	/**
	 * 设置支付日期时间
	 * @param pushDateTime 推送日期时间
	 */
	public void setPaymentDateTime(String PaymentDateTime) {
		this.PaymentDateTime = PaymentDateTime;
	}
	/**
	 * 获取消息内容
	 * @return
	 */
	public String getMessageContent() {
		return messageContent;
	}
	/**
	 * 设置消息内容
	 * @param messageContent 消息内容
	 */
	public void setMessageContent(String messageContent) {
		this.messageContent = messageContent;
	}
	/**
	 * 获取短信消息类型
	 * @return
	 */
	public String getMessageType() {
		return messageType;
	}
	/**
	 * 设置短信消息类型
	 * @param messageType 短信消息类型
	 */
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
	/**
	 * 获取预约发送时间
	 * @return
	 */
	public String getScheduleTime() {
		return scheduleTime;
	}
	/**
	 * 设置预约发送时间
	 * @param scheduleTime 预约发送时间
	 */
	public void setScheduleTime(String scheduleTime) {
		this.scheduleTime = scheduleTime;
	}
	/**
	 * 获取接入号/扩展号
	 * @return
	 */
	public String getExtendAccessNum() {
		return extendAccessNum;
	}
	/**
	 * 设置接入号/扩展号
	 * @param extendAccessNum 接入号/扩展号
	 */
	public void setExtendAccessNum(String extendAccessNum) {
		this.extendAccessNum = extendAccessNum;
	}
	/**
	 * 获取提交时检测方式
	 * @return
	 */
	public String getDetectionMethod() {
		return detectionMethod;
	}
	/**
	 * 设置提交时检测方式
	 * @param detectionMethod 检测方式
	 */
	public void setDetectionMethod(String detectionMethod) {
		this.detectionMethod = detectionMethod;
	}
	/**
	 * 设置商户ID
	 * @param orgId 商户ID
	 */
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	/**
	 * 获取商户ID
	 * @return
	 */
	public String getOrgId() {
		return orgId;
	}
	/**
	 * 设置短信发送的用户手机号
	 * @param userNumber 用户手机号（多个手机号为[,]分割的字符串）
	 */
	public void setUserNumber(String userNumber) {
		this.userNumber = userNumber;
	}
	/**
	 * 获取短信发送的用户手机号
	 * @return
	 */
	public String getUserNumber() {
		return userNumber;
	}
	@Override
	public String toString() {
		return "TimeoutMessage [PaymentDateTime=" + PaymentDateTime
				+ ", detectionMethod=" + detectionMethod + ", extendAccessNum="
				+ extendAccessNum + ", messageContent=" + messageContent
				+ ", messageType=" + messageType + ", orderNo=" + orderNo
				+ ", orgId=" + orgId + ", paymentAmount=" + paymentAmount
				+ ", paymentMethod=" + paymentMethod + ", scheduleTime="
				+ scheduleTime + ", userNumber=" + userNumber + "]";
	}
}
