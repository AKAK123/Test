package com.szcares.bean;
/**
 * 短信消息封装类
 * @author yaoxc
 *
 */
public class VerifyCodeMessage {
	
	private String messageContent;
	private String userNumber;
	private String scheduleTime;
	public String getMessageContent() {
		return messageContent;
	}
	public void setMessageContent(String messageContent) {
		this.messageContent = messageContent;
	}
	public String getUserNumber() {
		return userNumber;
	}
	public void setUserNumber(String userNumber) {
		this.userNumber = userNumber;
	}
	public String getScheduleTime() {
		return scheduleTime;
	}
	public void setScheduleTime(String scheduleTime) {
		this.scheduleTime = scheduleTime;
	}
	public String getExtendAccessNum() {
		return extendAccessNum;
	}
	public void setExtendAccessNum(String extendAccessNum) {
		this.extendAccessNum = extendAccessNum;
	}
	public String getDetectionMethod() {
		return detectionMethod;
	}
	public void setDetectionMethod(String detectionMethod) {
		this.detectionMethod = detectionMethod;
	}
	private String extendAccessNum;
	private String detectionMethod;
}
