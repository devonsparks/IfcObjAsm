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

		try {
			disassemble1(new File(args[0]));
			disassemble2();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void disassemble1(File input) throws SaxonApiException {
		Processor processor = new Processor(false);
		XsltCompiler compiler = processor.newXsltCompiler();

		InputStream xsl = loader.getResourceAsStream("disassemble.xsl");

		XsltExecutable exp = compiler.compile(new StreamSource(xsl));
		Serializer out = processor.newSerializer(System.out);

		out.setOutputProperty(Serializer.Property.METHOD, "xml");
		out.setOutputProperty(Serializer.Property.INDENT, "yes");
		Xslt30Transformer trans = exp.load30();
		trans.setBaseOutputURI(baseUri);
		trans.transform(new StreamSource(input), out);
		System.out.println("Done with step 1");

	}

	public static void disassemble2() throws SaxonApiException, FileNotFoundException {

		InputStream xsl = loader.getResourceAsStream("identity.xsl");
		Processor processor = new Processor(false);
		processor.setConfigurationProperty(FeatureKeys.XINCLUDE, true);

		XsltCompiler compiler = processor.newXsltCompiler();
		XsltExecutable exp = compiler.compile(new StreamSource(xsl));

		Xslt30Transformer trans = exp.load30();
		trans.setBaseOutputURI(baseUri);

		Path baseDir = Paths.get(System.getProperty("user.dir")).getParent();

		File objectsDir = new File(Paths.get(baseDir.toString(), "objects").toString());
		File idsDir = new File(Paths.get(baseDir.toString(), "objects", "ids").toString());

		File[] objects = objectsDir.listFiles(File::isFile);

		int c = 0;
		int size = objects.length;

		for (File child : objects) {
			System.out.println(c + "/" + size + "(" + child.getName() + ")");
			c++;
			try {
				Serializer out = processor.newSerializer(child);
				trans.transform(new StreamSource(child), out);
			} catch (SaxonApiException e) {
				System.err.println("Couldn't process " + child.getName());
			}

		}

		/* remove the 'ids' temporaries */
		for (File child : idsDir.listFiles()) {
			System.out.println(child.getName());
			child.delete();
		}
		idsDir.delete();

	}

}
