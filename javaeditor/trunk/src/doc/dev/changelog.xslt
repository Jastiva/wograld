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

<xsl:transform version="2.0" xmlns:xml="http://www.w3.org/XML/1998/namespace" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xi="http://www.w3.org/2001/XInclude" xmlns="http://www.w3.org/1999/xhtml" xmlns:String="http://saxon.sf.net/java.lang.String" xmlns:System="http://saxon.sf.net/java.lang.System" xmlns:File="http://saxon.sf.net/java.io.File" xmlns:URI="http://saxon.sf.net/java.net.URI" exclude-result-prefixes="xi xs xsl String System File URI">

    <xsl:output doctype-public="-//W3C//DTD XHTML 1.1//EN" doctype-system="http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd" encoding="utf-8" indent="no" method="xml" omit-xml-declaration="no"/>

    <xsl:template match="log">
        <html xml:lang="de">
            <head>
                <meta http-equiv="Content-Type" content="application/xhtml+xml; charset=utf-8"/>
                <meta name="Date" content="$Date: 2013/05/29 19:04:31 $"/>
                <title>Gridarta Changelog</title>
                <style type="text/css">
                    .paths, .hide { display:none; } .logTable td {
                    vertical-align:top; } .show, .hide { color:#00f; } td.toggle
                    { text-align:center; } td.revision { text-align:right; }
                    .paths { text-wrap:unrestricted; /* CSS Level 3, CSS3 Text
                    Effects Module, 5. Text Wrapping */ }
                </style>
                <script type="text/javascript">
                    function show(revision) { document.getElementById("show" +
                    revision).style.display="none";
                    document.getElementById("hide" +
                    revision).style.display="inline";
                    document.getElementById("path" +
                    revision).style.display="block"; } function hide(revision) {
                    document.getElementById("show" +
                    revision).style.display="inline";
                    document.getElementById("hide" +
                    revision).style.display="none";
                    document.getElementById("path" +
                    revision).style.display="none"; }
                </script>
            </head>
            <body>
                <table class="logTable" border="border">
                    <thead>
                        <tr>
                            <th/>
                            <th>Rev</th>
                            <th>Developer</th>
                            <th>Date</th>
                            <th>Commit Message / Change Set Description</th>
                        </tr>
                    </thead>
                    <tbody>
                        <xsl:apply-templates select="logentry"/>
                    </tbody>
                </table>
            </body>
        </html>
    </xsl:template>

    <xsl:template match="logentry">
        <tr>
            <td class="toggle">
                <span class="show" id="show{@revision}" onclick="javascript:show({@revision});">
                    [+]
                </span>
                <span class="hide" id="hide{@revision}" onclick="javascript:hide({@revision});">
                    [-]
                </span>
            </td>
            <td class="revision">
                <xsl:value-of select="@revision"/>
            </td>
            <td>
                <xsl:value-of select="author"/>
            </td>
            <td>
                <xsl:value-of select="date"/>
            </td>
            <td>
                <xsl:value-of select="msg"/>
                <xsl:apply-templates select="paths"/>
            </td>
        </tr>
    </xsl:template>

    <xsl:template match="paths">
        <ul id="path{../@revision}" class="paths">
            <xsl:apply-templates select="path"/>
        </ul>
    </xsl:template>

    <xsl:template match="path">
        <li>
            <xsl:value-of select="@action"/>
            <xsl:text/>
            <code>
                <a href="http://svn.sourceforge.net/viewcvs.cgi/gridarta/{URI:relativize(File:toURI(File:new(System:getProperty('user.dir'))),File:toURI(File:new(substring(., 2))))}">
                    <xsl:value-of select="."/>
                </a>
            </code>
        </li>
    </xsl:template>
</xsl:transform>
