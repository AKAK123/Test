package com.szcares.util;

import java.net.URL;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.travelsky.easypay.dataservice.client.sign.EasyPaySignHelper;
import com.travelsky.easypay.dataservice.client.sign.SignInfo;
import com.travelsky.easypay.dataservice.client_axis.querypayinfo.QueryPayInfo;
import com.travelsky.easypay.dataservice.client_axis.querypayinfo.QueryPayInfoRequest;
import com.travelsky.easypay.dataservice.xml.PayInfoType;
import com.travelsky.easypay.dataservice.xml.QueryPayInfoResponseDocument;
import com.travelsky.easypay.tool.ApplicationConstants;
import com.travelsky.internalshop.util.CreateBIllNober;
import com.travelsky.szcares.web.dataservice.DataService;
import com.travelsky.szcares.web.dataservice.DataServiceRequest;
import com.travelsky.szcares.web.dataservice.DataServiceResponse;
import com.travelsky.szcares.web.dataservice.DataServiceServiceLocator;

/**
 * 查询支付明细
 * @author tanhai
 *
 */
public class QueryPayResultInfoUtil {
	private static final Logger logger = Logger
			.getLogger(CreateBIllNober.class);

	/**
	 * 查询支付明细
	 */
	public static String queryPayInfo(String billno, String orderNo,
			String bankID) {
		DataServiceRequest request = new DataServiceRequest();
		QueryPayInfo querypayinfo = new QueryPayInfo();
		QueryPayInfoRequest queryPayInfoRequest = new QueryPayInfoRequest();
		queryPayInfoRequest.setAppname("YPB");
		queryPayInfoRequest.setAppType("B2C");
		queryPayInfoRequest.setOrgid("YUPBAO");
		queryPayInfoRequest.setServiceType("QUERYPAYINFO");
		queryPayInfoRequest.setVersion("1.0");
		queryPayInfoRequest.setBillno(billno);
		queryPayInfoRequest.setOrderno(orderNo);

		queryPayInfoRequest.setBankid(bankID);
		queryPayInfoRequest
				.setServerurl(Constants.EASYPAY_PAYINFO_URL);
		String xml = "";
		String result="";
		try {
			xml = querypayinfo.transReq2XML(queryPayInfoRequest);
			logger.info(String.format("生成的XML： %s", xml));//【runqian 2017-07-15】sonar 日志 bug 修复
		} catch (Exception e1) {
			logger.error("QueryPayResultInfoUtil.queryPayInfo 发生异常");
		}
		request.setAppType("B2C");
		request.setOrgid("YUPBAO_WY");
		request.setRequestXML(xml);
		request.setServiceType("QUERYPAYINFO");
		request.setVersion("1.0");
		String content = request.getOrgid() + "&" + request.getAppType() + "&"
				+ request.getServiceType() + "&" + request.getVersion() + "&"
				+ xml + "&";
		logger.info(String.format("待验签： %s", content));//【runqian 2017-07-15】sonar 日志 bug 修复
		try {
			EasyPaySignHelper md5 = new EasyPaySignHelper();
			SignInfo signinfo = new SignInfo();
			String md5path = (Thread.currentThread().getContextClassLoader()
					.getResource("") + "MD5.txt").trim();
			signinfo.setPrikey_path(md5path.substring(6, md5path.length()));
			signinfo.setOrgcontent(content);
			
			request.setSignature(md5.doSign(signinfo).getSigcontent());
			DataServiceServiceLocator locator = new DataServiceServiceLocator();

			URL url = new URL(Constants.EASYPAY_PAYINFO_URL);
			DataService proxy = locator.getDataService(url);
			DataServiceResponse res = proxy.handle(request);

			if (res.getResultCode() != ApplicationConstants._RESULT_CODE_SUCCESS) {
				throw new Exception(res.getErrorDesc());
			}
			String content_resp = res.getServiceType() + "&" + res.getVersion()
					+ "&" + res.getResponseXML() + "&" + res.getResultCode()
					+ "&" + res.getErrorDesc() + "&";
			signinfo.setOrgcontent(content_resp);
			signinfo.setSigcontent(res.getSignature());
			signinfo.setPubkey_path(md5path.substring(6, md5path.length()));
			boolean flag = md5.doVerify(signinfo);
			if (!flag)
				throw new Exception("返回验证错误！");

			logger.info(String.format("queryPayInfo返回结果: %s", res.getResponseXML()));//【runqian 2017-07-15】sonar 日志 bug 修复
			QueryPayInfoResponseDocument respdoc = QueryPayInfoResponseDocument.Factory
					.parse(res.getResponseXML());
			QueryPayInfoResponseDocument.QueryPayInfoResponse resp = respdoc
					.getQueryPayInfoResponse();
			QueryPayInfoResponseDocument.QueryPayInfoResponse.Payinfos payinfos = resp
					.getPayinfos();
			//StringBuffer outStr = new StringBuffer();
			for (int i = 0; i < payinfos.getPayInfoArray().length; i++) {
				PayInfoType payinfo = payinfos.getPayInfoArray(i);
				JSONObject json=new JSONObject();
				json.put("sysbillno", payinfo.getSysbillno());//EasyPay流水号
				json.put("appname", payinfo.getAppname());//应用名
				json.put("apptype", payinfo.getApptype());
				json.put("orgid", payinfo.getOrgid());
				
				json.put("orderno", payinfo.getOrderno());
				json.put("orderdate", payinfo.getOrderdate());
				json.put("ordertime", payinfo.getOrdertime());
				json.put("orderamount", payinfo.getOrderamount());
				json.put("ordercurtype", payinfo.getOrdercurtype());
				json.put("ordertype", payinfo.getOrdertype());
				
				json.put("billno", payinfo.getBillno());
				json.put("bankid", payinfo.getBankid());
				json.put("paystatus", payinfo.getPaystatus());
				json.put("paydate", payinfo.getPaydate());
				json.put("lastpaydate", payinfo.getLastpaydate());
				json.put("returndate", payinfo.getReturndate());
				json.put("isrefund", payinfo.getIsrefund());
				json.put("lan", payinfo.getLanguage());
				json.put("msg", payinfo.getMessage());				
				
				json.put("ext1", payinfo.getExt1());
				json.put("ext2", payinfo.getExt2());
				
				json.put("returntype", "server");
				json.put("returntransdate", "");
				json.put("signature", "");
				json.put("execType", "YPB_REQ");
				
				
				logger.info(String.format("支付结果返回 %s", json.toString()));//【runqian 2017-07-15】sonar 日志 bug 修复
				
				result=json.toString();
				
			}
		} catch (Exception e) {
			logger.error("QueryPayResultInfoUtil.queryPayInfo 发生异常");
			result=e.getMessage();
		}

		return result;
	}
}
