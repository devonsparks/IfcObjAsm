package com.github.devonsparks.ifcobjasm;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.util.HashMap;

public abstract class XsltCommand {

	private URI baseUri;
	final ClassLoader loader = IfcObjAsm.class.getClassLoader();
	private String name;

	XsltCommand(String name) {
		this.name = name;
		this.baseUri = new File(System.getProperty("user.dir")).toURI();
	}

	public String getName() {
		return name;
	}

	/**
	 * The Command's name as used on the command line
	 * 
	 * @param name
	 */
	protected void setName(final String name) {
		this.name = name;
	}

	public URI getBaseUri() {
		return this.baseUri;
	}

	/**
	 * The base output directory for all XSLT processing
	 */
	public void setBaseUri(URI baseUri) {
		this.baseUri = baseUri;
	}

	public InputStream getXSL(String path) {
		return loader.getResourceAsStream(path);
	}

	/**
	 * Execute the transformations associated with this command. All commands
	 * receive the full argument list for processing.
	 * 
	 * @param args the full arglist. args[0] is the command name.
	 * @return
	 */
	public abstract CommandResponse execute(String[] args);

	public abstract String help();

	/**
	 * Register a command name with its implementation
	 *
	 * @param commands map command names to implementations
	 * @return the updated command map
	 */
	public HashMap<String, XsltCommand> register(HashMap<String, XsltCommand> commands) {
		commands.put(this.getName(), this);
		return commands;
	}

	protected CommandResponse parseargs(String args[]) {

		if (args.length < 2) {
			return new CommandResponse(this, CommandResponse.states.FAIL,
					"Missing source xml document in argument list");
		}
		

		if (args.length == 2 && args[1].equals("-h")) {
			return new CommandResponse(this, CommandResponse.states.FAIL, this.help());
		}
		
		File input = new File(args[1]);
		if (!input.exists()) {
			return new CommandResponse(this, CommandResponse.states.FAIL,
					String.format("Source xml %s document not found", args[1]));
		}

		if (args.length > 3 && args[2].equals("-b")) {
			File base = new File(args[3]);
			if (!base.exists())
				return new CommandResponse(this, CommandResponse.states.FAIL,
						String.format("Base directory not found. Create it and try again.", args[3]));
			this.setBaseUri(base.toURI());

		}

		return new CommandResponse(this, CommandResponse.states.OK, "All args ok");

	}
}
