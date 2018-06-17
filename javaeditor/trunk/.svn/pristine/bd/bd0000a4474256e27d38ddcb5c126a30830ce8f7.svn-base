<?xml version="1.0" encoding="utf-8"?>
<!--
  ~ Gridarta MMORPG map editor for Crossfire, Daimonin and similar games.
  ~ Copyright (C) 2000-2011 The Gridarta Developers.
  ~
  ~ This program is free software; you can redistribute it and/or modify
  ~ it under the terms of the GNU General Public License as published by
  ~ the Free Software Foundation; either version 2 of the License, or
  ~ (at your option) any later version.
  ~
  ~ This program is distributed in the hope that it will be useful,
  ~ but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~ MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~ GNU General Public License for more details.
  ~
  ~ You should have received a copy of the GNU General Public License along
  ~ with this program; if not, write to the Free Software Foundation, Inc.,
  ~ 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
  -->

<xsl:transform version="2.0" xmlns:html="http://www.w3.org/1999/xhtml" xmlns:xml="http://www.w3.org/XML/1998/namespace" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xi="http://www.w3.org/2001/XInclude" xmlns="http://www.w3.org/1999/xhtml" exclude-result-prefixes="xi html xsl">

    <xsl:output cdata-section-elements="html:script html:style" doctype-public="-//W3C//DTD XHTML 1.1//EN" doctype-system="http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd" encoding="utf-8" indent="no" method="xml" omit-xml-declaration="no"/>

    <xsl:strip-space elements="html:body html:head html:html html:title html:h1 html:h2 html:h3 html:h4 html:h5 html:h6 html:address html:blockquote html:div html:p html:pre html:dl html:dt html:dd html:ol html:ul html:li html:applet html:form html:fieldset html:caption html:table html:td html:th html:tr html:col html:colgroup html:tbody html:thead html:tfoot html:object html:frameset html:param html:img html:frame html:noframes html:iframe html:meta html:link html:input html:select html:optgroup html:option html:button html:label html:legend"/>

    <xsl:template match="html:head">
        <xsl:copy>
            <xsl:apply-templates select="@*"/>
            <xsl:apply-templates select="node()"/>
            <link rel="Stylesheet" type="text/css" href="/sitestyle.css"/>
            <link rel="Start" href="/"/>
            <xsl:if test="not(html:link[@rel='Copyright'])">
                <link rel="Copyright" href="/copyright" title="Copyright"/>
            </xsl:if>
            <xsl:apply-templates select="/html:html/html:body//html:*" mode="generateChapterLinks"/>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="html:h2|html:h3|html:h4" mode="generateChapterLinks">
        <link rel="Bookmark" href="#{generate-id()}">
            <xsl:attribute name="title">
                <xsl:apply-templates select=".//text()"/>
            </xsl:attribute>
        </link>
    </xsl:template>
    <xsl:template match="html:*" mode="generateChapterLinks"/>

    <xsl:template match="html:body">
        <xsl:copy>
            <xsl:apply-templates select="@*"/>
            <div class="header">
                <a accesskey="h" href="/">Home</a>
                <a accesskey="s" href="http://sourceforge.net/projects/gridarta/">
                    SF Project Home
                </a>
            </div>
            <div class="content">
                <xsl:if test="not(html:h1)">
                    <h1>
                        <xsl:apply-templates select="/html:html/html:head/html:title/node()"/>
                    </h1>
                </xsl:if>
                <xsl:apply-templates select="node()"/>
            </div>
            <address>
                <a href="http://sourceforge.net/">
                    <img src="http://sflogo.sourceforge.net/sflogo.php?group_id=166996&amp;type=1" alt="SourceForge.net Logo" width="88" height="31" class="now"/>
                </a>
                <a href="http://sourceforge.net/donate/index.php?group_id=166996">
                    <img src="http://sourceforge.net/images/project-support.jpg" width="88" height="32" alt="Support This Project" class="now"/>
                </a>
                <a href="http://freshmeat.net/projects/gridarta/">
                    <img src="http://images.freshmeat.net/img/link_button_4.gif" width="88" height="31" alt="freshmeat.net" class="now"/>
                </a>
                <a href="http://validator.w3.org/check?uri=referer">
                    <img src="http://www.w3.org/Icons/valid-xhtml11" alt="Valid XHTML 1.1" height="31" width="88" class="now"/>
                </a>
                <a href="http://jigsaw.w3.org/css-validator/check/referer">
                    <img src="http://jigsaw.w3.org/css-validator/images/vcss" width="88" height="31" alt="Valid CSS!" class="now"/>
                </a>
                <!--<a href="http://www.jetbrains.com/idea/"><img src="http://www.jetbrains.com/idea/opensource/img/banners/idea88x31_blue.gif" alt="The best Java IDE" width="88" height="31" /></a>-->
                Feedback:
                <a href="mailto:cher@riedquat.de">webmaster</a>
                <xsl:if test="/html:html/html:head/html:meta[@name='Date']">
                    <br/>
                    <xsl:value-of select="/html:html/html:head/html:meta[@name='Date']/@content"/>
                </xsl:if>
            </address>
            <p class="copyright">
                <xsl:choose>
                    <xsl:when test="/html:html/html:head/html:meta[@name='Copyright']">
                        <xsl:value-of select="/html:html/html:head/html:meta[@name='Copyright']"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:text>Copyright © 2006 - 2008 The Gridarta
                            developers. All rights reserved.
                        </xsl:text>
                    </xsl:otherwise>
                </xsl:choose>
            </p>
        </xsl:copy>
    </xsl:template>
    <xsl:template match="html:h2|html:h3|html:h4">
        <xsl:copy>
            <xsl:apply-templates select="@*"/>
            <xsl:attribute name="id">
                <xsl:value-of select="generate-id()"/>
            </xsl:attribute>
            <xsl:apply-templates select="node()"/>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="html:head/@profile"/>
    <xsl:template match="html:html/@version"/>
    <xsl:template match="html:td/@colspan[.='1']|html:td/@rowspan[.='1']|html:th/@colspan[.='1']|html:th/@rowspan[.='1']"/>
    <xsl:template match="html:a/@shape[.='rect']"/>
    <xsl:template match="html:input/@type[.='text']"/>
    <xsl:template match="html:*/@xml:space[.='preserve']"/>
    <xsl:template match="@xml:base"/>

    <xsl:template match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>

</xsl:transform>
