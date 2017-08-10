package com.szcares.bean;

/**
 * @author jihb
 * 
 */
public class PostMessage implements java.io.Serializable {

	private static final long serialVersionUID = -7324160837770865500L;
	/** 接口地址 */
	private String url;
	/** 企业编号 */
	private String spCode;
	/** 用户名称 */
	private String loginName;
	/** 用户密码 */
	private String password;
	/** 短信内容 */
	private String messageContent;
	/** 手机号码 */
	private String userNumber;
	/** 流水�?*/
	private String serialNumber;
	/** 预约发�?时间 */
	private String scheduleTime;
	/** 接入号扩展号 */
	private String extendAccessNum;
	/** 提交时检测方�?*/
	private String f;
	/** 短信类型 */
	private String messageType;
	/** 商户号 */
	private String orgId;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getSpCode() {
		return spCode;
	}

	public void setSpCode(String spCode) {
		this.spCode = spCode;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

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

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
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

	public String getF() {
		return f;
	}

	public void setF(String f) {
		this.f = f;
	}

	public String getMessageType() {
		return messageType;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	@Override
	public String toString() {
		return "PostMessage [extendAccessNum=" + extendAccessNum + ", f=" + f
				+ ", loginName=" + loginName + ", messageContent="
				+ messageContent + ", messageType=" + messageType
				+ ", password=" + password + ", scheduleTime=" + scheduleTime
				+ ", serialNumber=" + serialNumber + ", spCode=" + spCode
				+ ", url=" + url + ", userNumber=" + userNumber + "]";
	}

	

}
