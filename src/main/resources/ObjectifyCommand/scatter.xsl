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
        
        <xsl:result-document href="{concat('objects', '/', @id)}" indent="yes">
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
