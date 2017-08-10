package com.szcares.bean;

import java.util.HashMap;

public interface IGrabTicket {
	public HashMap<String, Object> grabTicketNotice(HashMap<String, Object> request);
	public HashMap<String, Object> addGrabTicketPlan(HashMap<String, Object> request);
}
