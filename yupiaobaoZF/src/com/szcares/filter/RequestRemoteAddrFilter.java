package com.szcares.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

/**
 * 对所有的请求来源，进行过滤
 * 
 * @author htzeng
 * 
 */
public class RequestRemoteAddrFilter implements Filter {
	private Logger logger = Logger.getLogger(RequestRemoteAddrFilter.class);
	private final String[] NULL_STRING_ARRAY = new String[0];
	private final String URL_SPLIT_PATTERN = ",";// 逗号进行分割
	/**
	 * 黑名单
	 */
	private String[] blackList = null;
	private String blackResponseReturnMsg = "";

	public void destroy() {
		logger.info("黑名单IP过滤结束");
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		logger.info("黑名单IP过滤开始...");
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		httpRequest.setCharacterEncoding("utf-8");

		// 获取来源IP
		String clientIp = getIpAddr(httpRequest);

		// 过滤
		for (String blackIp : blackList) {
			if (blackIp != "" && clientIp != null && blackIp.equals(clientIp)) {
				logger.info(String.format("当前来源IP地址在黑名单内，将退出:[%s]", blackIp));//【runqian 2017-07-15】sonar 日志 bug 修复
				PrintWriter out = null;
				String responseMsg = blackResponseReturnMsg;
				try {
					httpResponse.setHeader("Content-Encoding", "gzip");
					httpResponse.setCharacterEncoding("UTF-8");
					out = httpResponse.getWriter();
					out.write(responseMsg);
					out.flush();
					out.close();
				} catch (Exception e) {
					logger.error("RequestRemoteAddrFilter.doFilter 发生异常");
				} finally {
					if (out != null) {
						out.close();
					}
					logger.info("黑名单过滤结束");
				}
				return;
			}
		}

		// 没在黑名单中，继续下一步操作
		chain.doFilter(request, response);
	}

	public void init(FilterConfig config) throws ServletException {
		String blackMobileListStr = config.getInitParameter("blackList");
		blackList = strToArray(blackMobileListStr);
		blackResponseReturnMsg = config.getInitParameter("blackResponseReturnMsg");
	}

	/**
	 * 多个IP进行分割
	 */
	private String[] strToArray(String urlStr) {
		if (urlStr == null) {
			return NULL_STRING_ARRAY;
		}
		String[] urlArray = urlStr.split(URL_SPLIT_PATTERN);

		List<String> urlList = new ArrayList<String>();

		for (String url : urlArray) {
			url = url.trim();
			if (url.length() == 0) {
				continue;
			}

			urlList.add(url);
		}
		return urlList.toArray(NULL_STRING_ARRAY);
	}

	/**
	 * 获得当前请求来源的IP地址
	 * 
	 * @param request
	 * @return
	 */
	private String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
}
