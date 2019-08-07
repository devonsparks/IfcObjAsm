package com.github.devonsparks.ifcobjasm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.xml.transform.stream.StreamSource;

import net.sf.saxon.lib.FeatureKeys;
import net.sf.saxon.s9api.*;

public class IfcObjAsm {

	private static final String baseUri = "file://" + System.getProperty("user.dir");
	private static final ClassLoader loader = IfcObjAsm.class.getClassLoader();
	
	public static void main(String[] args) {

		if(args.length < 1) {
			usage("Missing arguments");
		}
		
		File input = new File(args[0]);
		if(!input.exists()) {
			usage("Input file not found");
		}
		
		try {
			disassemble1(new File(args[0]));

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.exit(0);

	}
	
	public static void usage(String msg) {
		System.err.println(msg);
		System.err.println("***");
		System.err.println("[scatter [file]]");
		System.exit(-1);
	}

	public static void disassemble1(File input) throws SaxonApiException {


		InputStream disassemble = loader.getResourceAsStream("disassemble.xsl");
		InputStream scatter = loader.getResourceAsStream("scatter.xsl");
		InputStream report = loader.getResourceAsStream("report.xsl");
		
		Processor proc = new Processor(false);
        XsltCompiler comp = proc.newXsltCompiler();
        
        /* disassemble step */
        XsltExecutable templates1 = comp.compile(new StreamSource(disassemble));
        XdmNode source = proc.newDocumentBuilder().build(new StreamSource(input));
        XsltTransformer trans1 = templates1.load();
        trans1.setInitialContextNode(source);

        /* scatter step */
        XsltExecutable templates2 = comp.compile(new StreamSource(scatter));
        XsltTransformer trans2 = templates2.load();
        trans2.setBaseOutputURI(baseUri);
        
        /* report step */
        XsltExecutable templates3 = comp.compile(new StreamSource(report));
        XsltTransformer trans3 = templates3.load();

        XdmDestination resultTree = new XdmDestination();
        
        
        /* step linking */
        trans1.setDestination(trans2);
        trans2.setDestination(trans3);	
        trans3.setParameter(new QName("objectsdir"), new XdmAtomicValue("objects"));
        trans3.setDestination(resultTree);
        
        /* kickstart */
        trans1.transform();
		
        System.out.println(resultTree.getXdmNode());

	}

	

}
