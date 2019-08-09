package com.github.devonsparks.ifcobjasm;

import java.io.File;
import java.io.InputStream;
import java.net.URI;

import javax.xml.transform.stream.StreamSource;

import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.QName;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.XdmAtomicValue;
import net.sf.saxon.s9api.XdmDestination;
import net.sf.saxon.s9api.XdmNode;
import net.sf.saxon.s9api.XsltCompiler;
import net.sf.saxon.s9api.XsltExecutable;
import net.sf.saxon.s9api.XsltTransformer;

public class SubgraphCommand extends XsltCommand {

	SubgraphCommand() {
		super();
		this.setName("subgraph");
	}
	
	@Override
	public CommandResponse execute(String[] args) {
		assert(args[0] == this.getName());

		CommandResponse argcheck = this.parseargs(args);
		if(argcheck.getState() != CommandResponse.states.OK)
			return argcheck;
		
		InputStream gather = loader.getResourceAsStream("SubgraphCommand/gather.xsl");
		
		Processor proc = new Processor(false);
        XsltCompiler comp = proc.newXsltCompiler();
        XsltTransformer trans1;
        XdmDestination resultTree = new XdmDestination();
        
        try {
        	/* gather step */
        	File input = new File(args[1]);
	        XsltExecutable templates1 = comp.compile(new StreamSource(gather));
	        XdmNode source = proc.newDocumentBuilder().build(new StreamSource(input));
	        trans1 = templates1.load();
	        trans1.setInitialContextNode(source);
	        trans1.setBaseOutputURI(this.getBaseUri().toString());
	        
        } catch(SaxonApiException e) {
        	return new CommandResponse(this,
        			CommandResponse.states.FAIL,
        			"Failed to load internal gather step. This is likely a bug.");
        }
		
        
        try {
        	URI  objectsdir = this.getBaseUri().resolve("objects");
	        trans1.setParameter(new QName("objectsdir"), 
	        		new XdmAtomicValue(objectsdir.toString()));
	        trans1.setDestination(resultTree);
	        
	        /* kickstart */
	        trans1.transform();
	        
        } catch(SaxonApiException e) {
        	return new CommandResponse(this,
        			CommandResponse.states.FAIL,
        			"Failed to execute transformation.");
        }
		
        return new CommandResponse(this,
        		CommandResponse.states.OK, 
        		resultTree.getXdmNode().toString());
	}
	
	

}
