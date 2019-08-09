<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" 
 xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<!-- TODO - alternative to IFC namespace wildcard on GlobalId ? -->
	
	<!-- *** GLOBALS *** -->
	<xsl:strip-space elements=""/>

	<xsl:key name="tags" match="*[@id]" use="@id"/>
	<xsl:key name="objects" match="*[*:GlobalId]" use="@id"/>


	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()"/>
		</xsl:copy>
	</xsl:template>

	
	<xsl:template match="@id">
		
		<xsl:if test="../*:GlobalId">
		<xsl:attribute name="id">
			<xsl:value-of select="../*:GlobalId"/>
		</xsl:attribute>
		</xsl:if>
	</xsl:template>


	<xsl:template match="*[@ref]">

		<xsl:variable name="obj" select="key('objects', @ref)"/>
		<xsl:variable name="tag" select="key('tags', @ref)"/>
		
		<xsl:choose>
			<xsl:when test="$obj">
				<!-- rewrite refs as globals -->
				<xsl:copy>
					<xsl:copy-of select="@*[name()!='id' and name()!='ref']"/>
					<xsl:attribute name="ref">
						<xsl:value-of select="$obj/*:GlobalId"/>
					</xsl:attribute>
				</xsl:copy>
			</xsl:when>
			<xsl:otherwise>
				<!-- replace Resource Layer nodes refs with copies -->
				<xsl:for-each select="$tag">			
					<xsl:copy>
						<xsl:copy-of select="@*[name(.)!='id']"/>
						<xsl:apply-templates/>
					</xsl:copy>
				</xsl:for-each>
			</xsl:otherwise>	
		</xsl:choose>
	
	</xsl:template>
	


</xsl:stylesheet>