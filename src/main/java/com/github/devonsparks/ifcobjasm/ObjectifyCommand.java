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

public class ObjectifyCommand extends XsltCommand {

	ObjectifyCommand() {
		super("objectify");
	}

	/**
	 * Given an ifcxml file, rewrite refs to Resource Layer IFC Entities as inline
	 * literals, write each IfcRoot instance to its own file, and return a list of
	 * xi:includes to all parsed IfcRoot instance objects. See rewrite.xsl,
	 * scatter.xsl, report.xsl in resources folder for details.
	 */
	@Override
	public CommandResponse execute(String[] args) {
		assert (args[0] == this.getName());

		CommandResponse argcheck = this.parseargs(args);

		if (argcheck.getState() != CommandResponse.states.OK)
			return argcheck;

		XsltBuilder builder = new XsltBuilder(this.getBaseUri());
		XsltTransformer rewrite, scatter, report;
		XdmDestination resultTree = new XdmDestination();
		
		try {
		rewrite = builder.build(this.getXSL("ObjectifyCommand/rewrite.xsl"), new File(args[1]));
			scatter = builder.build(this.getXSL("ObjectifyCommand/scatter.xsl"));
			report  = builder.build(this.getXSL("ObjectifyCommand/report.xsl"));

		} catch (SaxonApiException e) {
			return new CommandResponse(this, CommandResponse.states.FAIL,
					"Unable to load in-package XSLT steps. This is likely a bug.");
		}

		try {
			URI objectsdir = this.getBaseUri().resolve("objects");
			rewrite.setDestination(scatter);
			scatter.setDestination(report);
			report.setParameter(new QName("objectsdir"), new XdmAtomicValue(objectsdir.toString()));
			report.setDestination(resultTree);

			// kickstart
			rewrite.transform();

		} catch (SaxonApiException e) {
			return new CommandResponse(this, CommandResponse.states.FAIL, "Failed to execute transformation.");
		}

		return new CommandResponse(this, CommandResponse.states.OK, resultTree.getXdmNode().toString());
	}

	@Override
	public String help() {
		return "Given an ifcxml file, output all of its IfcRoot-descended entities"
				+ " into an 'objects' archive. Use the '-b' to set the base directory"
				+ " for the archive location";
	}

}
