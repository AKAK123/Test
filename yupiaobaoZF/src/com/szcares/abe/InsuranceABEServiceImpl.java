package com.szcares.abe;

import java.util.HashSet;
import java.util.Set;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.travelsky.tdp.insure.eterm.service.client.model.InsuranceItem;
import com.travelsky.tdp.insure.eterm.service.client.model.PolicyFlight;
import com.travelsky.tdp.insure.eterm.service.client.model.PolicySchedule;
import com.travelsky.tdp.insure.eterm.service.client.model.ScheduleRQ;
import com.travelsky.tdp.insure.eterm.service.client.model.ScheduleRS;
import com.travelsky.tdp.insure.eterm.service.client.servicedelegate.InsureServiceDelegate;
import com.travelsky.tdp.insure.eterm.service.client.servicedelegate.InsureServiceDelegateImpl;
/**
 * 航意险ABE交易对接服务类
 * 
 * @author AKin
 *
 */
public class InsuranceABEServiceImpl implements InsuranceABEService {
	private Logger log=Logger.getLogger(InsuranceABEServiceImpl.class);
	//private static ApplicationContext context = new ClassPathXmlApplicationContext("InsureClientContext.xml");
	//private InsureServiceDelegate delegate=(InsureServiceDelegate) context.getBean("insureServiceDelegateTarget");
	private InsureServiceDelegate delegate;
	public ScheduleRS preparePolicySchedule(ScheduleRQ scheduleRQ) throws Exception{
		ScheduleRQ rq=new ScheduleRQ();
		rq.setETicketNo(scheduleRQ.getETicketNo());	//电子客票号
		//ScheduleRS rs=delegate.preparePolicySchedule(scheduleRQ);
		
		/***************  测试   start ************/
		ScheduleRS rs=new ScheduleRS();
		rs.setResult("success");
		rs.setMessage("success");
		
		Set ic_Set=new HashSet();	//保险条目列表
		for(int i=0;i<5;i++){
			InsuranceItem ic=new InsuranceItem();
			ic_Set.add(ic);
		}
	
		Set pf_Set=new HashSet();	//航班列表
		PolicyFlight pf=new PolicyFlight();
		pf.setFlightStatus("OPEN");
		pf_Set.add(pf);
		pf.setInsuranceItems(ic_Set);
		
		PolicySchedule ps=new PolicySchedule();
		ps.setPolicyFlights(pf_Set);
		rs.setSchedule(ps);
		/***************  测试   end ************/
		if(rs!=null){
			log.debug("************[ABE] 电子客票信息查询:************:ETicketNo="+scheduleRQ.getETicketNo()+",result="+rs.getResult()+",message="+rs.getMessage());
		}
		return rs;
	}

	public ScheduleRS planPolicySchedule(ScheduleRQ scheduleRQ){
		ScheduleRS rs=new ScheduleRS();
		rs.setResult("success");
		rs.setMessage("success");
		try
		{
			//rs=delegate.preparePolicySchedule(scheduleRQ);
		} catch (Exception e)
		{
			
		}
		return rs;
	}
	
	public ScheduleRS cancelOrder(ScheduleRQ scheduleRQ) {
		ScheduleRS rs=new ScheduleRS();
		rs.setResult("success");
		rs.setMessage("success");
		try
		{
			//rs=delegate.cancelOrder(scheduleRQ);
		} catch (Exception e)
		{
			
		}
		return rs;
	}
	
	public ScheduleRS orderMaintainInit(ScheduleRQ scheduleRQ) throws Exception {
		return delegate.orderMaintainInit(scheduleRQ);
	}
	
	public ScheduleRS orderMaintainMergeInit(ScheduleRQ scheduleRQ) throws Exception {
		return delegate.orderMaintainMergeInit(scheduleRQ);
	}
	
	public ScheduleRS orderMaintain(ScheduleRQ scheduleRQ) throws Exception {
		return delegate.orderMaintain(scheduleRQ);
	}

	public ScheduleRS searchOrders(ScheduleRQ scheduleRQ) throws Exception {
		return delegate.searchOrders(scheduleRQ);
	}

}
