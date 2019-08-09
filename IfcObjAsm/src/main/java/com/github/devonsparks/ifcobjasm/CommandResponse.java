package com.github.devonsparks.ifcobjasm;

public class CommandResponse {
	private String resp;
	private states state;
	public enum  states {OK, FAIL};
	/** 
	 * A container for responses and failure modes of {@link com.github.devonsparks.ifcobjasm.XsltCommand}
	 * 
	 * @param command  the owning {@link com.github.devonsparks.ifcobjasm.XsltCommand}
	 * @param state    OK or FAIL
	 * @param resp     Response  String content. On FAIL, this is an error message. On OK, XSLT default output.
	 */
	CommandResponse(XsltCommand command, CommandResponse.states state, String resp) {
		this.resp = state == states.OK ? resp :
					String.format("%s: %s", command.getName(), resp);		
		this.state = state;
	}
	
	public String getResponse() {
		return resp;
	}
	
	public states getState() {
		return state;
	}
	

}
