<?xml version="1.0" encoding="UTF-8"?>

<!--  
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
-->

<xsl:stylesheet version="3.0" 
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns:map="http://www.w3.org/2005/xpath-functions/map"
    xmlns:xi="http://www.w3.org/2001/XInclude"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:exp="urn:oid:1.0.10303.28.2.1.1" xmlns:doc="urn:oid:1.0.10303.28.2.1.3">

    <xsl:param name="objectsdir" select="'.'"/>

    
    <xsl:template match="/">
	   	<xsl:variable name="result">
	    	<xsl:call-template name="gather"/>
	    </xsl:variable>
	    
		<xsl:copy select="doc:iso_10303_28">
			<xsl:copy select="doc:express">
				<xsl:copy-of select="@*"/>
			</xsl:copy>
			<xsl:copy select="doc:schema_population">
				<xsl:copy-of select="@*"/>
			</xsl:copy>
			
			<xsl:copy select="*:uos">
				<xsl:copy-of select="@*"/>
				<xsl:copy-of select="$result"/>
			</xsl:copy>
		</xsl:copy>
    </xsl:template>

    <!-- 
        * gather *
        Given an input document that contains @refs to GlobalIds, perform a DFS
        traversal of the ref graph to retrieve hrefs to all objects linked to those
        listed in the input document.
        
        This is a standard recursive DFS implementation that uses XSLT 3.0 maps to
        track visited nodes. Assumes we'll get TCO from the compiler.
    -->
    <xsl:template name="gather">
        <xsl:param name="idx" as="map(*)" select="map {}"/>
        <xsl:param name="doc" select="document-uri(.)"/>
      
      <!--  output the current ref -->
        <xsl:text>&#xa;</xsl:text>
        <xsl:element name="xi:include">
            <xsl:attribute name="href"><xsl:value-of select="$doc"/></xsl:attribute>
        </xsl:element>
        
        <!--  and recurse through all the included ("adjacent") refs -->
        <xsl:variable name="newidx" as="map(*)" select="map:put($idx, $doc, $doc)"/>
        <xsl:for-each select="document($doc)//*[@ref]">
          
            <xsl:variable name="nextdoc" select="concat($objectsdir, '/', @ref)"/>
            <xsl:if test="not(map:contains($newidx, $nextdoc))">
                <xsl:call-template name="gather">
                    <xsl:with-param name="idx" select="$newidx"/>
                    <xsl:with-param name="doc" select="$nextdoc"/>
                </xsl:call-template>
            </xsl:if>

        </xsl:for-each>

    </xsl:template>
</xsl:stylesheet>
