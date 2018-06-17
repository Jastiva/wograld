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

package net.sf.gridarta.model.treasurelist;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import net.sf.japi.xml.NodeListIterator;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Utility class for loadings the TreasureLists.xml file.
 * @author Andreas Kirschbaum
 */
public class TreasureListsParser {

    /**
     * Private constructor to prevent instantiation.
     */
    private TreasureListsParser() {
    }

    /**
     * This method fills the 'specialTreasureLists' hash table with the names of
     * all treasurelists which are special and belong into a special
     * sub-folder.
     * @param specialTreasureListsDocument the special treasure lists
     * definitions
     * @return the hash table
     */
    @NotNull
    public static Map<String, TreasureTreeNode> parseTreasureLists(@NotNull final Document specialTreasureListsDocument) {
        final Map<String, TreasureTreeNode> specialTreasureLists = new HashMap<String, TreasureTreeNode>();

        final Element rootElement = specialTreasureListsDocument.getDocumentElement();
        assert rootElement != null && rootElement.getNodeName().equalsIgnoreCase("lists");

        final NodeListIterator<Element> it = new NodeListIterator<Element>(rootElement, "list");
        while (it.hasNext()) {
            final Element list = it.next();
            final String listName = list.getAttribute("name");
            assert listName != null;

            final TreasureTreeNode folder = new TreasureTreeNode(new FolderTreasureObj(listName));

            final Iterator<Element> it2 = new NodeListIterator<Element>(list, "entry");
            while (it2.hasNext()) {
                final Element entry = it2.next();
                final String entryName = entry.getAttribute("name");
                assert entryName != null;

                specialTreasureLists.put(entryName, folder);
            }
        }

        return specialTreasureLists;
    }

}
