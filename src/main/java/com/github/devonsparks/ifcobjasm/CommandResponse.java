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
