<?xml version="1.0" encoding="utf-8"?><!-- Converts a single metrics file to XHTML. -->
<xsl:transform version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns="http://www.w3.org/1999/xhtml">

    <xsl:output method="xml" doctype-system="http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd" doctype-public="-//W3C//DTD XHTML 1.1//EN" encoding="utf-8" omit-xml-declaration="no" indent="yes"/>

    <xsl:variable name="sourceIds" select="JOINTMETRICS/SOURCES/SOURCE/@id"/>

    <xsl:template match="/JOINTMETRICS">
        <html xml:lang="en">
            <head>
                <title>Metrics of Project Daimonin</title>
            </head>
            <body>
                <h1>Metrics of Project Daimonin</h1>
                <table>
                    <thead>
                        <tr>
                            <th>Metric</th>
                            <th>Name</th>
                            <xsl:for-each select="SOURCES/SOURCE">
                                <th>
                                    <xsl:value-of select="@href"/>
                                </th>
                            </xsl:for-each>
                        </tr>
                    </thead>
                    <tbody>
                        <xsl:apply-templates select="METRICS/METRIC"/>
                    </tbody>
                </table>
            </body>
        </html>
    </xsl:template>

    <xsl:template match="METRIC">
        <tr>
            <td>
                <xsl:value-of select="@abbreviation"/>
            </td>
            <td>
                <xsl:value-of select="@name"/>
            </td>
            <xsl:variable name="currentMetric" select="."/>
            <xsl:for-each select="$sourceIds">
                <td>
                    <xsl:value-of select="$currentMetric/VALUE[@sourceid=current()]/@value"/>
                </td>
            </xsl:for-each>
        </tr>
    </xsl:template>

</xsl:transform>
