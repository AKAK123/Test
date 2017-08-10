package com.szcares.abe;

import com.alibaba.fastjson.JSONObject;
import com.travelsky.tdp.insure.eterm.service.client.model.ScheduleRQ;
import com.travelsky.tdp.insure.eterm.service.client.model.ScheduleRS;

/**
 * 航意险接口
 * @author AKin
 *
 */
public interface InsuranceOrderService {
	/**
	 * 电子客票查询 preparePolicySchedule
	 * 
	 * @param ticketNo
	 * @return
	 */
	public ScheduleRS preparePolicySchedule(final String ticketNo) throws Exception;

	/**
	 * 保单生成：planPolicySchedule
	 *
	 * @param jsObj
	 * @return
	 * 
	 * 1、查询电子客票号信息 
	 * 2、判断是否符合提交保单条件  
	 * 3、提交保单去生成 
	 * 
	 */
	public JSONObject planPolicySchedule(final String ticketNo, String jsonString) throws Exception;

	/**
	 * 保单取消：cancelOrderForEticketNo
	 * 
	 * @param scheduleRQ
	 * @return
	 */
	public JSONObject cancelOrderForEticketNo(final String jsonString) throws Exception;

	/**
	 * 保单维护查询指令：orderMaintainInit
	 * 
	 * @param scheduleRQ
	 * @return
	 */
	public ScheduleRS orderMaintainInit(ScheduleRQ scheduleRQ) throws Exception;

	/**
	 * 新保单维护查询指令：orderMaintainMergeInit
	 * 
	 * @param scheduleRQ
	 * @return
	 */
	public ScheduleRS orderMaintainMergeInit(ScheduleRQ scheduleRQ) throws Exception;

	/**
	 * 保单维护指令：orderMaintain
	 * 
	 * @param scheduleRQ
	 * @return
	 */
	public ScheduleRS orderMaintain(ScheduleRQ scheduleRQ) throws Exception;

	/**
	 * 保单查询指令：searchOrders
	 * 
	 * @param scheduleRQ
	 * @return
	 */
	public ScheduleRS searchOrders(ScheduleRQ scheduleRQ) throws Exception;
}
