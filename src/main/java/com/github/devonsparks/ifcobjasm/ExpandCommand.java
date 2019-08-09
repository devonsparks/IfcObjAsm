package com.github.devonsparks.ifcobjasm;

import java.io.File;
import java.net.URI;

import net.sf.saxon.s9api.QName;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.XdmAtomicValue;
import net.sf.saxon.s9api.XdmDestination;
import net.sf.saxon.s9api.XsltTransformer;

public class ExpandCommand extends XsltCommand {

	ExpandCommand() {
		super("expand");
	}
	
	@Override
	public CommandResponse execute(String[] args) {
		assert (args[0] == this.getName());

		CommandResponse argcheck = this.parseargs(args);

		if (argcheck.getState() != CommandResponse.states.OK)
			return argcheck;

		XsltBuilder builder = new XsltBuilder(this.getBaseUri());
		builder.useXInclude(Boolean.TRUE);
		XsltTransformer expand;
		XdmDestination resultTree = new XdmDestination();
		
		try {
			expand = builder.build(this.getXSL("ExpandCommand/identity.xsl"), new File(args[1]));

		} catch (SaxonApiException e) {
			return new CommandResponse(this, CommandResponse.states.FAIL,
					"Unable to load in-package XSLT steps. This is likely a bug.");
		}

		try {
			expand.setDestination(resultTree);
			expand.transform();

		} catch (SaxonApiException e) {
			return new CommandResponse(this, CommandResponse.states.FAIL, "Failed to execute transformation.");
		}

		return new CommandResponse(this, CommandResponse.states.OK, resultTree.getXdmNode().toString());

	}

	@Override
	public String help() {
		return "Given an ifcxml file, perform an XSLT identity transform"
				+" with XInclude processing. Useful for reconstituting 'objectify'-ied"
				+ " object archives.";
	}

}
