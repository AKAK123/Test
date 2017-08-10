package com.szcares.abe;

import com.travelsky.tdp.insure.eterm.service.client.model.ScheduleRQ;
import com.travelsky.tdp.insure.eterm.service.client.model.ScheduleRS;

/**
 * 航意险ABE交易对接接口
 * @author AKin
 *
 */
public interface InsuranceABEService {
	/**
	 * 电子客票查询 preparePolicySchedule
	 * 
	 * @param scheduleRQ
	 * @return
	 */
	public ScheduleRS preparePolicySchedule(ScheduleRQ scheduleRQ) throws Exception;

	/**
	 * 保单生成：planPolicySchedule
	 * 
	 * @param scheduleRQ
	 * @return
	 */
	public ScheduleRS planPolicySchedule(ScheduleRQ scheduleRQ) throws Exception;

	/**
	 * 保单取消：cancelOrder
	 * 
	 * @param scheduleRQ
	 * @return
	 */
	public ScheduleRS cancelOrder(ScheduleRQ scheduleRQ) throws Exception;

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
