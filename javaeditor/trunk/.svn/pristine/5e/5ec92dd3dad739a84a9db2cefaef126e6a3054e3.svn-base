/*
 * Gridarta MMORPG map editor for Crossfire, Daimonin and similar games.
 * Copyright (C) 2000-2010 The Gridarta Developers.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package net.sf.gridarta.model.match;

import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.xpath.XPath;
import net.sf.gridarta.model.errorview.ErrorViewCategory;
import net.sf.gridarta.model.errorview.ErrorViewCollector;
import net.sf.japi.xml.NodeListIterator;
import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Parser for the GameObjectMatchers.xml file.
 * @author Andreas Kirschbaum
 */
public class GameObjectMatchersParser {

    /**
     * The Logger for printing log messages.
     */
    @NotNull
    private static final Category log = Logger.getLogger(GameObjectMatchersParser.class);

    /**
     * DocumentBuilder.
     */
    @NotNull
    private final DocumentBuilder documentBuilder;

    /**
     * The XPath for using XPath.
     */
    @NotNull
    private final XPath xpath;

    /**
     * Creates a new instance.
     * @param documentBuilder the <code>DocumentBuilder</code> to use for
     * parsing XML
     * @param xpath the XPath to use for applying for XPath expressions
     */
    public GameObjectMatchersParser(@NotNull final DocumentBuilder documentBuilder, @NotNull final XPath xpath) {
        this.documentBuilder = documentBuilder;
        this.xpath = xpath;
    }

    /**
     * Parses a .xml file defining game object matchers.
     * @param url the URL to parse
     * @param gameObjectMatchers the game object matchers to update
     * @param errorViewCollector the error view collector to use
     * @throws IOException if the file cannot be parsed
     */
    public void readGameObjectMatchers(@NotNull final URL url, @NotNull final GameObjectMatchers gameObjectMatchers, @NotNull final ErrorViewCollector errorViewCollector) throws IOException {
        final GameObjectMatcherParser aom = new GameObjectMatcherParser(xpath);
        final Document doc;
        try {
            doc = documentBuilder.parse(new InputSource(url.toString()));
        } catch (final SAXException ex) {
            throw new IOException("sax exception: " + ex.getMessage(), ex);
        }
        int count = 0;
        int editType = 1;
        final Iterator<Element> it = new NodeListIterator<Element>(doc.getElementsByTagName("GameObjectMatcher"));
        while (it.hasNext()) {
            final Element node = it.next();
            final NamedGameObjectMatcher archObjectMatcher;
            try {
                archObjectMatcher = aom.parseMatcher(node, editType);
            } catch (final ParsingException ex) {
                errorViewCollector.addWarning(ErrorViewCategory.GAMEOBJECTMATCHERS_ENTRY_INVALID, node.getAttribute("id") + ": " + ex.getMessage());
                continue;
            }
            gameObjectMatchers.addGameObjectMatcher(archObjectMatcher);
            count++;
            if (editType != 0 && !archObjectMatcher.isSystemMatcher()) {
                editType <<= 1;
                if (editType == 0) {
                    errorViewCollector.addWarning(ErrorViewCategory.GAMEOBJECTMATCHERS_ENTRY_INVALID, "too many GameObjectMatchers, ignoring rest");
                }
            }
        }
        if (log.isInfoEnabled()) {
            log.info("Loaded " + count + " GameObjectMatchers from '" + url + "'.");
        }
    }

}
