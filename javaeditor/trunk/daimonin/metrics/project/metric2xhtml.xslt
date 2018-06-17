<?xml version="1.0" encoding="utf-8"?><!-- Converts a single metrics file to XHTML. -->
<xsl:transform version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns="http://www.w3.org/1999/xhtml">

    <xsl:output method="xml" doctype-system="http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd" doctype-public="-//W3C//DTD XHTML 1.1//EN" encoding="utf-8" omit-xml-declaration="no" indent="yes"/>

    <xsl:template match="/METRICS">
        <html xml:lang="en">
            <head>
                <title>Metrics of Project Daimonin</title>
            </head>
            <body>
                <h1>Metrics of Project Daimonin</h1>
                <p>Calculated on:
                    <xsl:value-of select="@timestamp"/>
                </p>
                <table>
                    <thead>
                        <tr>
                            <th>Metric</th>
                            <th>Name</th>
                            <th>Value</th>
                        </tr>
                    </thead>
                    <tbody>
                        <xsl:apply-templates select="METRIC"/>
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
            <td>
                <xsl:value-of select="VALUE/@value"/>
            </td>
        </tr>
    </xsl:template>

</xsl:transform>
