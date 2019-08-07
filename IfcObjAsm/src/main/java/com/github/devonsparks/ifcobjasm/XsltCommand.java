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
	
	/**
	 * All commands receive the full argument for further processing
	 * 
	 * @param args  the full arglist. args[0] is the command name.
	 * @return
	 */
	public abstract CommandResponse execute(String[] args);
	
	/**
	 * Register a command name with its implementation
     *
	 * @param commands  map command names to implementations
	 * @return          the updated command map
	 */
	public HashMap<String, XsltCommand> register(HashMap<String, XsltCommand> commands) {
		commands.put(this.getName(), this);
		return commands;
	}
} 
