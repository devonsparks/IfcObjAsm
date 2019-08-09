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

import java.io.File;
import java.net.URI;

import net.sf.saxon.s9api.QName;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.XdmAtomicValue;
import net.sf.saxon.s9api.XdmDestination;
import net.sf.saxon.s9api.XsltTransformer;

public class SubgraphCommand extends XsltCommand {

	SubgraphCommand() {
		super("subgraph");
	}

	@Override
	public CommandResponse execute(String[] args) {
		assert (args[0] == this.getName());

		CommandResponse argcheck = this.parseargs(args);
		if (argcheck.getState() != CommandResponse.states.OK)
			return argcheck;

		XsltTransformer gather;
		XsltBuilder builder = new XsltBuilder(this.getBaseUri());
		XdmDestination resultTree = new XdmDestination();

		try {
			gather = builder.build(this.getXSL("SubgraphCommand/gather.xsl"), new File(args[1]));

		} catch (SaxonApiException e) {
			return new CommandResponse(this, CommandResponse.states.FAIL,
					"Failed to load internal gather step. This is likely a bug.");
		}

		try {
			URI objectsdir = this.getBaseUri().resolve("objects");
			gather.setParameter(new QName("objectsdir"), new XdmAtomicValue(objectsdir.toString()));
			gather.setDestination(resultTree);

			// kickstart
			gather.transform();

		} catch (SaxonApiException e) {
			return new CommandResponse(this, CommandResponse.states.FAIL, "Failed to execute transformation.");
		}

		return new CommandResponse(this, CommandResponse.states.OK, resultTree.getXdmNode().toString());
	}

	@Override
	public String help() {
		return "Given an input ifcxml file with @ref attributes, produce an ifcxml"
				+ " document that references all other IFCRoot objects connected to it.";
	}

}
