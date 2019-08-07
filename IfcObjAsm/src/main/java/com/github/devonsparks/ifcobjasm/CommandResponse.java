package com.github.devonsparks.ifcobjasm;

public class CommandResponse {
	private String resp;
	private states state;
	public enum  states {OK, FAIL};
	
	CommandResponse(XsltCommand command, CommandResponse.states state, String resp) {
		this.resp = String.format("%s: %s", command.getName(), resp);		
		
	}
	
	public String getResponse() {
		return resp;
	}
	
	public states getState() {
		return state;
	}
	

}
