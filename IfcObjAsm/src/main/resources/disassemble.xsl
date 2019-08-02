<xsl:stylesheet version="2.0" 
 xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
 xmlns:ifc="http://www.iai-tech.org/ifcXML/IFC2x2/FINAL"
 xmlns:xi="http://www.w3.org/2001/XInclude">

	<!-- *** GLOBALS *** -->
	<xsl:strip-space elements=""/>
	<xsl:key name="guidlookup" match="ifc:GlobalId" use="../@id"/>
	
	
	<!-- *** IGNORES *** -->
	<xsl:template match="@id"/>	<!-- ids are file local, so aren't guaranteed unique -->

	
	<!-- *** IDENTITY *** -->
	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()"/>
		</xsl:copy>
	</xsl:template>



	<!-- *** LINK RULES *** -->
	<xsl:template match="*[@id]">
	
		<xi:include href="{@id}"/>
		<xsl:result-document href="{concat('objects', '/', 'ids', '/', @id)}" method="xml">
			<xsl:copy>
					<xsl:copy-of select="@*[name()!='id']"/>
					<xsl:attribute name="_id"><xsl:value-of select="@id"/></xsl:attribute>
				<xsl:apply-templates select="@*|node()"/>
			</xsl:copy>
		</xsl:result-document>
	</xsl:template>


	<xsl:template match="*[@ref]">
		
		<xsl:choose>
			<xsl:when test="key('guidlookup', @ref)">
				<xsl:copy>
					<xsl:copy-of select="@*[name()!='ref']"/>
					<xsl:attribute name="ref"><xsl:value-of select="key('guidlookup', @ref)"/></xsl:attribute>
					<xsl:attribute name="_ref"><xsl:value-of select="@ref"/></xsl:attribute>
				</xsl:copy>
			</xsl:when>
				<xsl:otherwise>
					<xi:include href="{@ref}"/>
				</xsl:otherwise>
		</xsl:choose>
	</xsl:template>


	<!-- *** SPECIAL CONDITIONS *** -->
	<xsl:template match="ifc:IfcProject[@id]" priority="1">
	
		<!-- place a handle to the ProjectLibrary in the root directory -->
		<xsl:result-document href="{ifc:GlobalId}" method="xml">
			<xi:include href="{concat('objects', '/', ifc:GlobalId)}"/>
		</xsl:result-document>
		
		<!-- process the Project as a ProjectLibrary --> 
		<xsl:result-document href="{concat('objects', '/', 'ids', '/', @id)}" method="xml">
			<ifc:IfcProjectLibrary><xsl:apply-templates select="@*|node()"/></ifc:IfcProjectLibrary>
		</xsl:result-document>
	</xsl:template>


	<xsl:template match="ifc:GlobalId">
		<xsl:variable name="uid">
			<xsl:value-of select="."/>
		</xsl:variable>
		<xsl:result-document href="{concat('objects', '/', $uid)}" method="xml">
			<xi:include href="{concat('ids', '/', ../@id)}"/>
		</xsl:result-document>
		
	</xsl:template>
	




</xsl:stylesheet>