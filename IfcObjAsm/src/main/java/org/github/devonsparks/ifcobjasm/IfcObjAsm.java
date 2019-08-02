package org.github.devonsparks.ifcobjasm;

import java.io.File;

import javax.xml.transform.stream.StreamSource;

import net.sf.saxon.s9api.*;

public class IfcObjAsm {

	private static final String baseUri = "file://" + System.getProperty("user.dir");
	
	public static void main(String[] args) {

		Processor processor = new Processor(false);
		XsltCompiler compiler = processor.newXsltCompiler();
		
		File disassembler = new File(IfcObjAsm.class.getClassLoader().getResource("disassemble.xsl").getFile());
		File input = new File(args[0]);
		
		try {
			XsltExecutable exp = compiler.compile(new StreamSource(disassembler));
			Serializer out = processor.newSerializer(System.out);
			out.setOutputProperty(Serializer.Property.METHOD, "xml");
			out.setOutputProperty(Serializer.Property.INDENT, "yes");
			Xslt30Transformer trans = exp.load30();
			trans.setBaseOutputURI(baseUri);
			trans.transform(new StreamSource(input), out);
			

		} catch (SaxonApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Output written to out.xml");
	}

}
