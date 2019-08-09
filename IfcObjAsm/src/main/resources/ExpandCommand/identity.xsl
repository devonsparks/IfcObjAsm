<?xml version="1.0"?>

<!-- identity --> 
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"  xmlns:saxon="http://saxon.sf.net/">

<xsl:strip-space elements="*"/>
<xsl:output method="xml" version="1.0" encoding="UTF-8"/> <!--  saxon:canonical="yes"/>  -->

    <xsl:template match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>
	
</xsl:stylesheet>
