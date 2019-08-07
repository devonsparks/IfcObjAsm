<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" 
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:ifc="http://www.iai-tech.org/ifcXML/IFC2x2/FINAL"
    xmlns:xi="http://www.w3.org/2001/XInclude">
    
    <xsl:param name="objectsdir"/> 
    
    <xsl:template match="/">
		
		<doc:iso_10303_28 xmlns:exp="urn:oid:1.0.10303.28.2.1.1" 
		xmlns:doc="urn:oid:1.0.10303.28.2.1.3"
		xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
		xsi:schemaLocation="urn:oid:1.0.10303.28.2.1.1 ex.xsd" version="2.0">

		<exp:iso_10303_28_header/>
		<doc:express id="exp_1" external="" schema_name="IFC4"/>
		<doc:schema_population governing_schema="exp_1" determination_method="SECTION_BOUNDARY" governed_sections="uos_1"/>
		<ifc:uos 
				 id="uos_1" description=""
				 schema="exp_1" configuration="IFCXML_Official" edo=""
				  xsi:schemaLocation="http://www.iai-tech.org/ifcXML/IFC2x2/FINAL">
			<xsl:apply-templates/>
		</ifc:uos>
		</doc:iso_10303_28>
    
    </xsl:template>
    
 
    <xsl:template match="@*|node()">
  
            <xsl:apply-templates select="@*|node()"/>
  
    </xsl:template>
    
    
    <xsl:template match="*[@ref]">
    		<xi:include href="{concat($objectsdir, '/', @ref)}"/>
    </xsl:template>
    
    </xsl:stylesheet>
    