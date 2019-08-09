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
