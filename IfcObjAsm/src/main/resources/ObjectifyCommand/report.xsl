<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xi="http://www.w3.org/2001/XInclude"
    xmlns:exp="urn:oid:1.0.10303.28.2.1.1" xmlns:doc="urn:oid:1.0.10303.28.2.1.3">

    <xsl:param name="objectsdir"/>


    <xsl:template match="@* | node()">
        <xsl:apply-templates select="@* | node()"/>
    </xsl:template>


    <xsl:template match="doc:iso_10303_28|doc:express|doc:schema_population|*:uos">
        <xsl:copy>
            <xsl:copy-of select="@*"/>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>
    
    
    <xsl:template match="*[@ref]">
        <xsl:element name="xi:include">
            <xsl:attribute name="href">
                <xsl:value-of select="concat($objectsdir, '/', @ref)"/>
            </xsl:attribute>

        </xsl:element>
    </xsl:template>

</xsl:stylesheet>
