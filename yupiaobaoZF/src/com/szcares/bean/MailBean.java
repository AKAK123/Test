package com.szcares.bean;


/**
 * Created by huc on 2017/2/10.
 */
public class MailBean {
	
	/**
	 * 邮件服务器地址
	 */
	private String host;
	/**
	 * 发件人登陆名称
	 */
	private String userName;
	/**
	 * 发件人登陆密码
	 */
	private String passWord;
	/**
	 * 授权
	 */
	private String smtpAuth;
	/**
	 * 超时时间
	 */
	private String smtpTimeOut;
	/**
	 * 来自
	 */
	private String from;
	/**
	 * 来自用户
	 */
    private String fromName;
    /**
     * 接收邮件用户
     */
    private String[] toEmails;
    /**
     * 邮件主题 
     */
    private String subject;
    /**
     * 邮件内容
     */
    private String context;
    /**
     * 发送用户
     */
    private String user;
    /**
     * 测试服务器地址（测试）
     */
    private String serverHost;
    
    public String getServerHost() {
		return serverHost;
	}

	public void setServerHost(String serverHost) {
		this.serverHost = serverHost;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	public String getSmtpAuth() {
		return smtpAuth;
	}

	public void setSmtpAuth(String smtpAuth) {
		this.smtpAuth = smtpAuth;
	}

	public String getSmtpTimeOut() {
		return smtpTimeOut;
	}

	public void setSmtpTimeOut(String smtpTimeOut) {
		this.smtpTimeOut = smtpTimeOut;
	}

    public String getFrom() {
        return from;
    }

    public String getFromName() {
        return fromName;
    }

    public String[] getToEmails() {
        return toEmails;
    }

    public String getSubject() {
        return subject;
    }

    public String getContext() {
        return context;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public void setToEmails(String[] toEmails) {
        this.toEmails = toEmails;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setContext(String context) {
        this.context = context;
    }
    
    

}
