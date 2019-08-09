package com.github.devonsparks.ifcobjasm;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;


public abstract class XsltCommand {
	
	private URI baseUri;
	final ClassLoader loader = IfcObjAsm.class.getClassLoader();
	private String name = "XsltCommand";

	XsltCommand()
	{
		try {
			baseUri = new URI(System.getProperty("user.dir"));
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public String getName() {
		return name;
	}
	
	protected void setName(final String name) {
		this.name = name;
	}
	
	public URI getBaseUri() {
		return this.baseUri;
	}
	
	public void setBaseUri(URI baseUri) {
		this.baseUri = baseUri;
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
	
	protected CommandResponse parseargs(String args[]) {
		if(args.length < 2) {
			return new CommandResponse(this, 
						CommandResponse.states.FAIL, 
						"Missing source xml document in argument list");
		}
		File input = new File(args[1]);
		if(!input.exists()){
			return new CommandResponse(this,
						CommandResponse.states.FAIL,
						String.format("Source xml %s document not found", args[1]));
		}

		if(args.length > 3 && args[2].equalsIgnoreCase("-b")) {
			System.out.println("Trying to set basedir");
			File base = new File(args[3]);
			if(!base.exists())
				return new CommandResponse(this,
						CommandResponse.states.FAIL,
						String.format("Base directory not found", args[3]));
			this.setBaseUri(base.toURI());
			
		
		}
		
		System.out.println("Basedir now "+this.getBaseUri());
		return new CommandResponse(this,
					CommandResponse.states.OK,
					"All args ok");
				
	
	}
} 
