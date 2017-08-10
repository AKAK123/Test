package com.szcares.action;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.szcares.util.CommonUtils;
import com.szcares.util.Constants;
import com.szcares.util.QueryPayResultInfoUtil;
import com.szcares.util.SecurityScanner;
import com.travelsky.ypbao.util.CreateBIllNober;


public class ForwardServce extends HttpServlet{

	private static final long serialVersionUID = -875639780676670625L;
	private static final Logger logger = Logger.getLogger(ForwardServce.class);
	private static final int MAX_STR_LEN = 1024;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doPost(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String serviceUrl = CommonUtils.strProcessing(req.getParameter("serviceUrl"));
    	String reqParameter = CommonUtils.strProcessing(req.getParameter("reqParameter"));
    	String username = req.getParameter("username");
    	String pwd = req.getParameter("pwd");
    	String reqIp = req.getRemoteAddr();
    	
    	String result = "";
    	
    	if (username == null || pwd == null) {
    		//modify by chencl 2017-7-12
        	OutputStreamWriter out = null;
        	BufferedReader reader = null;
        	Reader reader2 = null;
    		//mobilepay.servlet 支付前判断依据 ，easypay/services/DataService 生成billno判断依据 
			//OrderFeedback 订单推送网赢判断依据
    		if(serviceUrl.lastIndexOf(Constants.DATASERVICE) != -1){//生成billNo
    			
    			Map<String,String> map = CommonUtils.strToMap(reqParameter);
    			map.put(Constants.SERVICE_URL_KEY, serviceUrl);
    			map.put(Constants.MD5_FILE_KEY, CommonUtils.getMD5FileNameById(map.get("orgId")));
    			
    			if(map.get("serviceType").equals("CREATEBILLNO")){//生成billno
    				result = new CreateBIllNober().createNewBillNo(map);
    			}else if(map.get("serviceType").equals("QUERYPAYINFO")){//查询订单明细
					//boolean bl = QueryPayResultInfo.queryPayInfo(map.get("billno"), map.get("orderNo"), map.get("bankID"));
					//resp.getWriter().print(bl);
    				result=QueryPayResultInfoUtil.queryPayInfo(map.get("billno"), map.get("orderNo"), map.get("bankID"));
    				//resp.getWriter().print(payinfo);
					//return;
    			}
    			
    			
	    		
    		}else{//非生成billNo处理
    			URL url = null;
    			OutputStream outputStream = null;
    			InputStream inputStream = null;
	    		try {
	    			
	    			url = new URL(serviceUrl);
	    			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
	    			connection.setDoOutput(true);
	    			connection.setRequestMethod("POST");
	    			
	    			if(serviceUrl.lastIndexOf(Constants.MOBILEPAY) != -1){//支付前的serviceUrl
	    				String referer = CommonUtils.getRealDynamicReferer(reqIp);
	    				
	    				logger.info(String.format("动态获取Referer结果=============> %s", referer));//【runqian 2017-07-15】sonar 日志 bug 修复
	    			}
	
	    			outputStream = connection.getOutputStream();
	    			out = new OutputStreamWriter(outputStream, "UTF-8");
	    			out.write(reqParameter);
	    			out.flush();
	    			
	    			inputStream = connection.getInputStream();
	    			reader2 = new InputStreamReader(inputStream,"GB2312");
	    			reader = new BufferedReader(reader2);
	    			
	    			/**modify by chencl 2017-7-17**/
	    			StringBuffer buf = new StringBuffer();
	    			int intc;
	    		    while ((intc = reader.read()) != -1) {
	    				char c = (char)intc;
	    				if(c=='\n'){
	    					break;
	    				}
	    				if(buf.length()>=MAX_STR_LEN){
	    					try {
	    						throw new Exception("input too length");
	    					} catch (Exception e) {
	    						logger.error("throw input too length Exception");;
	    					}
	    				}
	    				buf.append(c);
	    		    }
	    			result = buf.toString();
	    		}catch (IOException e) {
	    			logger.error("ForwardServce.doPost 发生 IOException 异常");
	    		}finally{
	    			if(null!=inputStream){//【runqian 2017-07-19】sonar bug 资源未关闭修复
	    				try {
	    					inputStream.close();
						} catch (Exception e2) {
							logger.info("inputStream close exception!");
						}
	    			}
	    			if(null!=outputStream){//【runqian 2017-07-19】sonar bug 资源未关闭修复
	    				try {
	    					outputStream.close();
						} catch (Exception e2) {
							logger.info("outputStream close exception!");
						}
	    			}
	    			if(null!=reader2){//【runqian 2017-07-19】sonar bug 资源未关闭修复
	    				try {
	    					reader2.close();
						} catch (Exception e2) {
							logger.info("reader2 close exception!");
						}
	    			}
	    			if(null!=reader){//【runqian 2017-07-15】sonar bug 资源未关闭修复
	    				try {
	    					reader.close();
						} catch (Exception e2) {
							logger.info("reader close exception!");
						}
	    			}
	    			if(null!=out){
	    				try {
	    					out.close();
						} catch (Exception e2) {
							logger.info("out close exception!");
						}
	    			}
	    		}
    		}
    	}//end of username == null || pwd == null
    	//System.out.println("-------------------------支付前信息传递EasyPay同步返回数据： "+result);
    	 try
    	    {
    	      	PrintWriter out;
    	        resp.setCharacterEncoding("UTF-8");
    	        out = resp.getWriter();
    	        String dealResultString = SecurityScanner.htmlEncode(result);
    	        out.write(CommonUtils.strProcessing(dealResultString));
    	        out.flush();
    	        out.close();
    	    }catch (Exception e) {
    	      logger.error("ForwardServce.doPost 发生异常");
    	    }
	}
	
	
	
}


