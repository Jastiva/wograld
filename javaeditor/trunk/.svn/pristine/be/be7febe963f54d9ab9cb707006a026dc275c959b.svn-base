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

package net.sf.gridarta.model.spells;

import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import javax.xml.parsers.DocumentBuilder;
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
 * Load spell definitions from an XML file.
 * @author Andreas Kirschbaum
 */
public class XMLSpellLoader {

    /**
     * The Logger for printing log messages.
     */
    @NotNull
    private static final Category log = Logger.getLogger(XMLSpellLoader.class);

    /**
     * Private constructor to prevent instantiation.
     */
    private XMLSpellLoader() {
    }

    /**
     * Load an XML spell definitions file.
     * @param errorViewCollector the error view collector for reporting errors
     * @param url the URL of the spell definitions file
     * @param documentBuilder the Document Builder to use for parsing
     * @param spells the <code>Spells</code> instance to add the spells to
     */
    public static void load(@NotNull final ErrorViewCollector errorViewCollector, @NotNull final URL url, @NotNull final DocumentBuilder documentBuilder, @NotNull final Spells<NumberSpell> spells) {
        try {
            final Document doc = documentBuilder.parse(new InputSource(url.toString()));

            // retrieve the spell data from the xml
            final Element root = doc.getDocumentElement();
            if (root == null || !"spells".equalsIgnoreCase(root.getNodeName())) {
                errorViewCollector.addWarning(ErrorViewCategory.SPELLS_ENTRY_INVALID, "root element 'spells' is missing");
            } else {
                // initialize array with appropriate size
                int numSpells = 0;
                final Iterator<Element> it = new NodeListIterator<Element>(root, "spell");
                while (it.hasNext()) {
                    final Element spellElem = it.next();
                    if (spellElem.getAttribute("id") == null) {
                        errorViewCollector.addWarning(ErrorViewCategory.SPELLS_ENTRY_INVALID, "found 'spell' element without 'id'");
                    } else if (spellElem.getAttribute("name") == null) {
                        errorViewCollector.addWarning(ErrorViewCategory.SPELLS_ENTRY_INVALID, "found 'spell' element without 'name'");
                    } else {
                        try {
                            // parse spell number and -name
                            spells.add(new NumberSpell(spellElem.getAttribute("name").trim(), Integer.parseInt(spellElem.getAttribute("id"))));
                            numSpells++;
                        } catch (final NumberFormatException ignored) {
                            errorViewCollector.addWarning(ErrorViewCategory.SPELLS_ENTRY_INVALID, "parsing error: spell id '" + spellElem.getAttribute("id") + "' is not an integer.");
                        }
                    }
                }

                // loading successful
                log.info("Loaded " + numSpells + " defined spells from '" + url + "'");
                if (numSpells == 0) {
                    errorViewCollector.addWarning(ErrorViewCategory.SPELLS_FILE_INVALID, "no content");
                }
            }
        } catch (final SAXException ex) {
            errorViewCollector.addWarning(ErrorViewCategory.SPELLS_FILE_INVALID, "parsing error:" + ex.getMessage());
        } catch (final IOException ex) {
            errorViewCollector.addWarning(ErrorViewCategory.SPELLS_FILE_INVALID, ex.getMessage());
        }
    }

}
