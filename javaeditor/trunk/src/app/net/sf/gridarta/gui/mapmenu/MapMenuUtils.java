/*
 * Gridarta MMORPG map editor for Crossfire, Daimonin and similar games.
 * Copyright (C) 2000-2011 The Gridarta Developers.
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

package net.sf.gridarta.gui.mapmenu;

import java.io.File;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import org.jetbrains.annotations.NotNull;

/**
 * Utility class for {@link MapMenu} related functions.
 * @author Andreas Kirschbaum
 */
public class MapMenuUtils {

    /**
     * Private constructor to prevent instantiation.
     */
    private MapMenuUtils() {
    }

    /**
     * Removes all entries for a given map file. Does nothing if no such entries
     * do exist.
     * @param treeNode the tree node to process
     * @param mapFile the map file
     */
    public static void removeMap(@NotNull final MutableTreeNode treeNode, @NotNull final File mapFile) {
        for (int i = treeNode.getChildCount() - 1; i >= 0; i--) {
            final DefaultMutableTreeNode treeNode2 = (DefaultMutableTreeNode) treeNode.getChildAt(i);
            final MapMenuEntry mapMenuEntry = (MapMenuEntry) treeNode2.getUserObject();
            if (mapMenuEntry instanceof MapMenuEntryMap) {
                final MapMenuEntryMap mapMenuEntryMap = (MapMenuEntryMap) mapMenuEntry;
                if (mapMenuEntryMap.getMapFile().equals(mapFile)) {
                    treeNode.remove(i);
                }
            }
        }
    }

}
