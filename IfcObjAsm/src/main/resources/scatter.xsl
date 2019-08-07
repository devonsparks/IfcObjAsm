<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" 
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    
    
    <!-- *** IDENTITY *** -->
    <xsl:template match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>
  
    
    <xsl:template match="*[@id]">
        <!-- output the persistent object to its own file -->
        
        <xsl:result-document href="{concat('objects', '/', @id)}">
            <xsl:copy>
                <xsl:copy-of select="@*"/>
                <xsl:apply-templates/>
            </xsl:copy>
        </xsl:result-document>
        
        <!-- replace with a ref to it -->
        <xsl:copy>
            <xsl:attribute name="ref">
                <xsl:value-of select="@id"/>
            </xsl:attribute>
        </xsl:copy>
    </xsl:template>
    
   
</xsl:stylesheet>
