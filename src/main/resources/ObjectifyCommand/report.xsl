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
