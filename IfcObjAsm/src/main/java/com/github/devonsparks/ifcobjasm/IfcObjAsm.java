package com.github.devonsparks.ifcobjasm;

import java.util.HashMap;


public class IfcObjAsm {

	private static final HashMap<String, XsltCommand> commands = new HashMap<String, XsltCommand>();
	
	public static void main(String[] args) {
		
		new ObjectifyCommand().register(commands);
		new SubgraphCommand().register(commands);
		
		if(args.length < 1) {
			err(String.format("Command required. Options: %s", 
					String.join(" | ",commands.keySet())));
		}
		
		if(!commands.containsKey(args[0])) {
			err(String.format("Unknown command '%s'", args[0]));
		}
		
		CommandResponse resp = dispatch(args);
		
		if(resp.getState() != CommandResponse.states.OK) {
			System.out.println(resp.getResponse());
			System.exit(-1);
		} 
		
		System.out.print(resp.getResponse());


	}
	

	public static CommandResponse dispatch(String[] args) {
		return commands.get(args[0]).execute(args);
	}

	public static void err(String msg) {
		System.err.println(msg);
;
		System.exit(-1);
	}

	

}
