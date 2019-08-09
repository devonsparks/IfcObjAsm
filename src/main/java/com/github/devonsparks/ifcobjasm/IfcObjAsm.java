/*
 * IfcObjAsm - An IFC Object Assembler
 * Copyright (C) 2019  Devon D. Sparks
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details. 
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.devonsparks.ifcobjasm;

import java.util.HashMap;


public class IfcObjAsm {

	private static final HashMap<String, XsltCommand> commands = new HashMap<String, XsltCommand>();
	
	public static void main(String[] args) {
		
		new ObjectifyCommand().register(commands);
		new SubgraphCommand().register(commands);
		new ExpandCommand().register(commands);
		
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
	
	/**
	 * 
	 * @param args  The full argument list passed to main()
	 * @return      The Commannd's string response or an error message
	 */
	public static CommandResponse dispatch(String[] args) {
		return commands.get(args[0]).execute(args);
	}

	public static void err(String msg) {
		System.err.println(msg);
		System.exit(-1);
	}

	

}
