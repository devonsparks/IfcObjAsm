package com.github.devonsparks.ifcobjasm;

import java.io.File;
import java.io.InputStream;
import java.net.URI;

import javax.xml.transform.stream.StreamSource;

import net.sf.saxon.lib.FeatureKeys;
import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.XdmNode;
import net.sf.saxon.s9api.XsltCompiler;
import net.sf.saxon.s9api.XsltExecutable;
import net.sf.saxon.s9api.XsltTransformer;

/**
 * Helper for building XslTransformers from raw stylesheets
 */
public class XsltBuilder {

	private final Processor proc;
	private final XsltCompiler comp;
	private URI baseUri;

	XsltBuilder(URI baseOutputUri) {
		baseUri = baseOutputUri;
		proc = new Processor(false);
		comp = proc.newXsltCompiler();
	}

	public XsltTransformer build(InputStream stylesheet, File input) throws SaxonApiException {
		XsltTransformer trans = this.build(stylesheet);
		XdmNode source = proc.newDocumentBuilder().build(new StreamSource(input));
		trans.setInitialContextNode(source);
		return trans;
	}

	public XsltTransformer build(InputStream stylesheet) throws SaxonApiException {

		XsltExecutable template = comp.compile(new StreamSource(stylesheet));
		XsltTransformer trans = template.load();
		trans.setBaseOutputURI(baseUri.toString());
		return trans;
	}
	
	public void useXInclude(Boolean yn) {
		proc.setConfigurationProperty(FeatureKeys.XINCLUDE, yn);
	}

}
