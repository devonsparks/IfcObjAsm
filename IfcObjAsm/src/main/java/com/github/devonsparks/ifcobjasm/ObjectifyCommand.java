package com.github.devonsparks.ifcobjasm;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

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


public class ObjectifyCommand extends XsltCommand {

	ObjectifyCommand() {
		super();
		this.setName("objectify");
	}
	
	/**
	 * Given an ifcxml file, rewrite refs to Resource Layer IFC
	 * Entities as inline literals, write each IfcRoot instance to its own file,
	 * and return a list of xi:includes to all parsed IfcRoot instance objects.
	 * See rewrite.xsl, scatter.xsl, report.xsl in resources folder for details.
	 */
	@Override
	public CommandResponse execute(String[] args)  {
		assert(args[0] == this.getName());
	
		CommandResponse argcheck = this.parseargs(args);
	
		
		if(argcheck.getState() != CommandResponse.states.OK)
			return argcheck;

		InputStream rewrite = loader.getResourceAsStream("ObjectifyCommand/rewrite.xsl");
		InputStream scatter = loader.getResourceAsStream("ObjectifyCommand/scatter.xsl");
		InputStream report =  loader.getResourceAsStream("ObjectifyCommand/report.xsl");
		
		Processor proc = new Processor(false);
        XsltCompiler comp = proc.newXsltCompiler();
        XsltTransformer trans1, trans2, trans3;
        XdmDestination resultTree = new XdmDestination();

        try {
        	/* rewrite step */
        	File input = new File(args[1]);
	        XsltExecutable templates1 = comp.compile(new StreamSource(rewrite));
	        XdmNode source = proc.newDocumentBuilder().build(new StreamSource(input));
	        trans1 = templates1.load();
	        trans1.setInitialContextNode(source);
	        
        } catch(SaxonApiException e) {
        	return new CommandResponse(this,
        			CommandResponse.states.FAIL,
        			"Failed to load internal disassemble step. This is likely a bug.");
        }

        try {
	        // scatter step
	        XsltExecutable templates2 = comp.compile(new StreamSource(scatter));
	        trans2 = templates2.load();
	        trans2.setBaseOutputURI(this.getBaseUri().toString());
	        
        } catch(SaxonApiException e) {
        	return new CommandResponse(this,
        			CommandResponse.states.FAIL,
        			"Failed to load internal scatter step. This is likely a bug.");
        }
     
        
        try {
	        // report step
	        XsltExecutable templates3 = comp.compile(new StreamSource(report));
	        trans3 = templates3.load();
	        trans3.setBaseOutputURI(this.getBaseUri().toString());
	        
        } catch(SaxonApiException e) {
        	return new CommandResponse(this,
        			CommandResponse.states.FAIL,
        			"Failed to load internal report step. This is likely a bug.");
        }
        
        try {
	        
	        trans1.setDestination(trans2);
	        trans2.setDestination(trans3);	

		    URI  objectsdir = this.getBaseUri().resolve("objects");
	        trans3.setParameter(new QName("objectsdir"), 
	        		new XdmAtomicValue(objectsdir.toString()));
	        trans3.setDestination(resultTree);
	        
	        // kickstart
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
