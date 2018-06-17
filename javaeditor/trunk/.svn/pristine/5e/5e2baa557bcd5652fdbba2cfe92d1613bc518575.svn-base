<?xml version="1.0"?>
<xsl:transform
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    version="2.0"
>

    <xsl:param name="svnversion">unknown</xsl:param>

    <xsl:output
        method="xml"
        doctype-system="http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd"
        doctype-public="-//W3C//DTD XHTML 1.1//EN"
        encoding="utf-8"
        indent="yes"
    />

    <xsl:variable name="doc" select="/" />

    <xsl:variable name="calc">
        <op op="avg" abbreviation="CLOC" />
        <op op="avg" abbreviation="COM_RAT" />
        <op op="avg" abbreviation="Jc" />
        <op op="avg" abbreviation="Jf" />
        <op op="avg" abbreviation="JLOC" />
        <op op="avg" abbreviation="Jm" />
        <op op="avg" abbreviation="LOC" />
        <op op="sum" abbreviation="CLOC" />
        <op op="sum" abbreviation="JLOC" />
        <op op="sum" abbreviation="LOC" />
    </xsl:variable>

    <xsl:template
        match="/METRICS"
        xmlns="http://www.w3.org/1999/xhtml"
    >
        <html>
            <head>
                <title>Javadoc coverage metrics for Gridarta (without textedit) revision <xsl:value-of select="$svnversion" /></title>
                <style type="text/css"><xsl:text><![CDATA[table.metrics td { text-align:right; }]]></xsl:text></style>
            </head>
            <body>
                <h1>Javadoc coverage metrics for Gridarta (without textedit) revision <xsl:value-of select="$svnversion" /></h1>
                <xsl:call-template name="printMetrics">
                    <xsl:with-param name="category" select="'project'" />
                    <xsl:with-param name="title" select="'Project Metrics'" />
                </xsl:call-template>
                <xsl:call-template name="printMetrics">
                    <xsl:with-param name="category" select="'module'" />
                    <xsl:with-param name="title" select="'Module Metrics'" />
                </xsl:call-template>
                <xsl:call-template name="printMetrics">
                    <xsl:with-param name="category" select="'package'" />
                    <xsl:with-param name="title" select="'Package Metrics'" />
                </xsl:call-template>
                <h2>Legend</h2>
                <dl>
                    <xsl:for-each select="METRIC[not(@abbreviation = preceding-sibling::METRIC/@abbreviation)]">
                        <xsl:sort select="@abbreviation" />
                        <dt><xsl:value-of select="@abbreviation" /></dt>
                        <dd><xsl:value-of select="@name" /></dd>
                    </xsl:for-each>
                </dl>
            </body>
        </html>
    </xsl:template>

    <xsl:template
        name="printMetrics"
        xmlns="http://www.w3.org/1999/xhtml"
    >
        <xsl:param name="category" />
        <xsl:param name="title" />
        <h2><xsl:value-of select="$title" /></h2>
        <table border="border" class="metrics">
            <thead>
                <tr>
                    <th><xsl:value-of select="@category"/></th>
                    <xsl:for-each select="METRIC[@category=$category]">
                        <xsl:sort select="@abbreviation" order="ascending" />
                        <th><abbr title="{@name}"><xsl:value-of select="@abbreviation"/></abbr></th>
                    </xsl:for-each>
                </tr>
            </thead>
            <tbody>
                <xsl:for-each select="distinct-values(METRIC[@category=$category]/VALUE/@measured)">
                    <xsl:sort select="." order="ascending" />
                    <tr>
                        <td><xsl:value-of select="." /></td>
                        <xsl:for-each select="$doc/METRICS/METRIC[@category=$category]/VALUE[@measured=current()]">
                            <xsl:sort select="../@abbreviation" order="ascending" />
                            <td><xsl:value-of select="@value" /></td>
                        </xsl:for-each>
                    </tr>
                </xsl:for-each>
                <tr>
                    <th>Total</th>
                    <xsl:for-each select="METRIC[@category=$category]">
                        <xsl:sort select="@abbreviation" order="ascending" />
                        <td>
                            <xsl:if test="$calc/op[@op='sum' and @abbreviation=current()/@abbreviation]">
                                <xsl:value-of select="sum(VALUE/@value)" />
                            </xsl:if>
                        </td>
                    </xsl:for-each>
                </tr>
                <tr>
                    <th>Average</th>
                    <xsl:for-each select="METRIC[@category=$category]">
                        <xsl:sort select="@abbreviation" order="ascending" />
                        <td>
                            <xsl:if test="$calc/op[@op='avg' and @abbreviation=current()/@abbreviation]">
                                <xsl:value-of select="avg(VALUE/@value)" />
                            </xsl:if>
                        </td>
                    </xsl:for-each>
                </tr>
            </tbody>
        </table>
    </xsl:template>
</xsl:transform>
