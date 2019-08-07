package com.github.devonsparks.ifcobjasm;

import java.io.File;
import java.io.InputStream;

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
		this.setName("objectify");
	}
	
	@Override
	public CommandResponse execute(String[] args)  {
		
		assert(args[0] == this.getName());
		
		if(args.length < 2) {
			return new CommandResponse(this, 
						CommandResponse.states.FAIL, 
						"Missing source xml document in argument list");
		}
		
		File input = new File(args[2]);
		if(!input.exists()) {
			return new CommandResponse(this,
						CommandResponse.states.FAIL,
						String.format("Source xml %s document not found", args[2]));
		}
		
		
		InputStream disassemble = loader.getResourceAsStream("disassemble.xsl");
		InputStream scatter = loader.getResourceAsStream("scatter.xsl");
		InputStream report = loader.getResourceAsStream("report.xsl");
		
		Processor proc = new Processor(false);
        XsltCompiler comp = proc.newXsltCompiler();
        XsltTransformer trans1, trans2, trans3;
        XdmDestination resultTree = new XdmDestination();
        
        try {
        	/* disassemble step */
	        XsltExecutable templates1 = comp.compile(new StreamSource(disassemble));
	        XdmNode source = proc.newDocumentBuilder().build(new StreamSource(input));
	        trans1 = templates1.load();
	        trans1.setInitialContextNode(source);
	        
        } catch(SaxonApiException e) {
        	return new CommandResponse(this,
        			CommandResponse.states.FAIL,
        			"Failed to load internal disassemble step. This is likely a bug.");
        }
        
        try {
	        /* scatter step */
	        XsltExecutable templates2 = comp.compile(new StreamSource(scatter));
	        trans2 = templates2.load();
	        trans2.setBaseOutputURI(baseUri);
	        
        } catch(SaxonApiException e) {
        	return new CommandResponse(this,
        			CommandResponse.states.FAIL,
        			"Failed to load internal scatter step. This is likely a bug.");
        }
        
        try {
	        /* report step */
	        XsltExecutable templates3 = comp.compile(new StreamSource(report));
	        trans3 = templates3.load();
	        
        } catch(SaxonApiException e) {
        	return new CommandResponse(this,
        			CommandResponse.states.FAIL,
        			"Failed to load internal report step. This is likely a bug.");
        }
        
        
        try {
	        /* step linking */
	        trans1.setDestination(trans2);
	        trans2.setDestination(trans3);	
	        trans3.setParameter(new QName("objectsdir"), new XdmAtomicValue("objects"));
	        trans3.setDestination(resultTree);
	        
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
