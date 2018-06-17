<?xml version="1.0" encoding="utf-8"?><!-- Transformation to join multiple metrics into a single file. -->
<xsl:transform version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:output method="xml" indent="yes"/>

    <xsl:template match="join">
        <xsl:variable name="jointInput">
            <xsl:for-each select="source">
                <xsl:copy-of select="document(@href)"/>
            </xsl:for-each>
        </xsl:variable>
        <xsl:variable name="allMetricNames" select="$jointInput//METRIC[not(@name=preceding::METRIC/@name)]/@name"/>
        <JOINTMETRICS>
            <SOURCES>
                <xsl:apply-templates select="source"/>
            </SOURCES>
            <METRICS>
                <xsl:variable name="source" select="source"/>
                <xsl:for-each select="$allMetricNames">
                    <xsl:variable name="currentMetricName" select="."/>
                    <METRIC category="project" name="{$currentMetricName}" abbreviation="{($jointInput//METRIC[@name=$currentMetricName]/@abbreviation)[last()]}">
                        <xsl:for-each select="$source">

                            <xsl:if test="document(@href)/METRICS/METRIC[@name=$currentMetricName]">
                                <VALUE sourceid="{@id}" measured="project" value="{document(@href)//METRIC[@name=$currentMetricName]/VALUE/@value}"/>
                            </xsl:if>
                        </xsl:for-each>
                    </METRIC>
                </xsl:for-each>
            </METRICS>
        </JOINTMETRICS>
    </xsl:template>

    <xsl:template match="source">
        <SOURCE id="{@id}" href="{@href}"/>
    </xsl:template>
</xsl:transform>
