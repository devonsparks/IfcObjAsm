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

		XsltBuilder builder = new XsltBuilder(this.getBaseUri());
		XsltTransformer gather;
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

}
