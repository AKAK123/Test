package com.szcares.bean;
/**
 * 存储用户的相关信息
 * @author Administrator
 *
 */
/**
 * @author Administrator
 *
 */
/**
 * @author Administrator
 *
 */
public class User
{
  private String office;
  private String username;
  private String password;

  public String getOffice()
  {
    return this.office;
  }
  public void setOffice(String office) {
    this.office = office;
  }
  public String getUsername() {
    return this.username;
  }
  public void setUsername(String username) {
    this.username = username;
  }
  public String getPassword() {
    return this.password;
  }
  public void setPassword(String password) {
    this.password = password;
  }
	
	/** 
	 * 用户相关信息的字符串，用于流量统计
	 * @see java.lang.Object#toString()
	 */
  public String toString()
  {
    String tempOffice = this.office == null ? "" : this.office;
    String tempUsername = this.username == null ? "" : this.username;
    return "user[office:" + tempOffice + ", username:" + tempUsername + "]";
  }
}