package com.github.devonsparks.ifcobjasm;

import java.util.HashMap;


public abstract class XsltCommand {

	String baseUri = "file://" + System.getProperty("user.dir");
	final ClassLoader loader = IfcObjAsm.class.getClassLoader();
	String name = "XsltCommand";
	
	public String getName() {
		return name;
	}
	
	protected void setName(final String name) {
		this.name = name;
	}
	
	public void setBaseUri(final String baseUri) {
		this.baseUri = baseUri;
	}
	
	public String getBaseUri() {
		return this.baseUri;
	}
	
	public abstract CommandResponse execute(String[] args);
	
	public HashMap<String, XsltCommand> register(HashMap<String, XsltCommand> commands) {
		commands.put(this.getName(), this);
		return commands;
	}
} 
