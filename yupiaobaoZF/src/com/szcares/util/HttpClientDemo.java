package com.szcares.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;

import com.szcares.exception.HttpInvokeException;
import com.szcares.exception.ServiceException;


/**
 * http 客户端调用示例(数据压缩版)
 */
public class HttpClientDemo {
	
	private static Logger log = Logger.getLogger(HttpClientDemo.class);
	
	private static final int MAX_STR_LEN = 1024;
	
	public static String Post(String username,String pwd,String serviceUrl,String reqStr){
		// 构造HttpClient 的实例
		StringBuffer respStr = new StringBuffer();
		HttpClient httpClient = new HttpClient();
		// 调用验证信息
		HttpState state = new HttpState();
		Credentials credentials = new UsernamePasswordCredentials(username, pwd);
		state.setCredentials(AuthScope.ANY, credentials);
		httpClient.setState(state);
		//modify by chencl 2017-7-12
		InputStream is = null;
		BufferedReader br = null;
		// 创建POST 方法的实例
		PostMethod postMethod = new PostMethod(serviceUrl);
		// 使用系统提供的默认的恢复策略
		postMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler());
		ByteArrayOutputStream out = null;
		GZIPOutputStream gzip = null;
		try {
			// 请求参数的数据压缩
			out = new ByteArrayOutputStream();
			gzip = new GZIPOutputStream(out);
			if (reqStr != null && !"".equals(reqStr)) {
				gzip.write(reqStr.getBytes());
			}
			RequestEntity requestEntity = new ByteArrayRequestEntity(out
					.toByteArray());
			postMethod.setRequestEntity(requestEntity);
			postMethod.addRequestHeader("Content-Type",
					"text/xml;charset=utf-8");
			postMethod.addRequestHeader("accept-encoding", "gzip");
			postMethod.addRequestHeader("content-encoding", "gzip");
			// 执行getMethod
			int statusCode = httpClient.executeMethod(postMethod);
			if (statusCode != HttpStatus.SC_OK) {
				throw new Exception("Invoke Get Method Failed, HttpStatus = "
						+ statusCode);
			}
			// 返回结果的数据解压
			is = postMethod.getResponseBodyAsStream();
			br = new BufferedReader(new InputStreamReader(
					new GZIPInputStream(is)));
			int intc;
			while ((intc = br.read()) != -1) {
				char c = (char)intc;
				if(c=='\n'){
					break;
				}
				if(respStr.length()>=MAX_STR_LEN){
					try {
						throw new Exception("input too length");
					} catch (Exception e) {
						log.error("throw input too length Exception");;
					}
				}
				respStr.append(c);
            }
			out.flush();
		} catch (Exception e) {
			// 发生致命的异常，可能是协议不对或者返回的内容有问题
			respStr.append("kong");
		} finally {
			if(gzip != null){//【runqian 2017-07-15】sonar bug 资源未关闭修复
				try {
					gzip.close();
				} catch (IOException e) {
					log.error("close gzip 时发生 IOException 异常!");
				}
			}
			if(out != null){
				try {
					out.close();
				} catch (IOException e) {
					log.error("close out 时发生 IOException 异常!");
				}
			}
			if(postMethod != null){//【runqian 2017-07-15】sonar bug 资源未关闭修复
				postMethod.releaseConnection();
			}
			if(br != null){
				try {
					br.close();
				} catch (IOException e) {
					log.error("close br 时发生 IOException 异常!");
				}
			}
			if(is != null){
				try {
					is.close();
				} catch (IOException e) {
					log.error("close is 时发生 IOException 异常!");
				}
			}
		}
		return respStr.toString();
	}
public static String httpPost(String serviceUrl, String reqStr)throws HttpInvokeException, ServiceException {
	// 构造HttpClient的实例
	HttpClient httpClient = new HttpClient();
	// 创建POST方法的实例
	PostMethod postMethod = new PostMethod(serviceUrl);
	RequestEntity entity = null;
	try {
		entity = new StringRequestEntity(reqStr, "text/xml", "UTF-8");
	} catch (UnsupportedEncodingException e1) {
		throw new HttpInvokeException(e1);
	}
	
	
	postMethod.setRequestEntity(entity);
	// 使用系统提供的默认的恢复策略
	postMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
			new DefaultHttpMethodRetryHandler());
	
	String respStr = "";
	InputStream inputStream = null;
	try {
		// 执行postMethod
		int statusCode = httpClient.executeMethod(postMethod);
		if (statusCode != HttpStatus.SC_OK) {
			throw new ServiceException(
					"Invoke Post Method Failed, HttpStatus = " + statusCode);
		}
		// 读取内容
		inputStream = postMethod.getResponseBodyAsStream();
		respStr = LogReader.readInputToString(inputStream);
	} catch (HttpException e) {
		// 发生致命的异常，可能是协议不对或者返回的内容有问题
		throw new HttpInvokeException(e);
	} catch (IOException e) {
		// 发生网络异常
		throw new HttpInvokeException(e);
	} finally {
		if(inputStream != null){
			try {
				inputStream.close();
			} catch (Exception e2) {
				log.error("close inputStream exception!");
			}
		}
		// 释放连接
		if(postMethod != null){//【runqian 2017-07-15】sonar bug 资源未关闭修复
			try {
				postMethod.releaseConnection();
			} catch (Exception e2) {
				log.error("postMethod releaseConnection exception!");
			}
		}
	}
	return respStr;
	}
	
	/*public static void main(String[] args) throws ServiceException, HttpInvokeException {
		System.out.println(httpPost("http://localhost:8089/ycbback/orderPull","<welcome-file-list><welcome-file>index.jsp</welcome-file></welcome-file-list>"));
	}*/
}
