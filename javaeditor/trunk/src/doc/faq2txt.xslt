<?xml version="1.0" encoding="utf-8"?><!--
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

<!-- Transformation to convert a FAQ to plain text. -->
<xsl:transform version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:html="http://www.w3.org/1999/xhtml">

    <xsl:strip-space elements="html:*"/>

    <xsl:output method="text" encoding="utf-8"/>

    <xsl:template match="html:title"/>

    <xsl:template match="html:h1">
        <xsl:apply-templates/>
        <xsl:text>&#xa;&#xa;</xsl:text>
        <xsl:text>Note: This file is auto-generated from src/doc/faq.xhtml using
            src/doc/faq2txt.xslt.&#xa;</xsl:text>
        <xsl:text>If you want to change the faq, please make your changes in
            src/doc/faq.xhtml.&#xa;</xsl:text>
        <xsl:text>&#xa;&#xa;</xsl:text>
    </xsl:template>

    <xsl:template match="html:dt">
        <xsl:text>Question:</xsl:text>
        <xsl:text>&#xa;</xsl:text>
        <xsl:apply-templates/>
        <xsl:text>&#xa;&#xa;</xsl:text>
    </xsl:template>

    <xsl:template match="html:dd">
        <xsl:text>Answer:</xsl:text>
        <xsl:apply-templates/>
        <xsl:text>&#xa;&#xa;&#xa;</xsl:text>
    </xsl:template>

    <xsl:template match="html:a">
        <xsl:apply-templates/>
        <xsl:text> &lt;</xsl:text>
        <xsl:value-of select="@href"/>
        <xsl:text>&gt;</xsl:text>
    </xsl:template>

    <xsl:template match="text()">
        <xsl:if test="starts-with(., '&#xA;')">
            <xsl:text>&#xA;</xsl:text>
        </xsl:if>
        <xsl:if test="starts-with(., ' ')">
            <xsl:text/>
        </xsl:if>
        <xsl:value-of select="normalize-space(.)"/>
        <xsl:if test="ends-with(., '&#xA;')">
            <xsl:text>&#xA;</xsl:text>
        </xsl:if>
        <xsl:if test="ends-with(., ' ')">
            <xsl:text/>
        </xsl:if>
    </xsl:template>
</xsl:transform>
