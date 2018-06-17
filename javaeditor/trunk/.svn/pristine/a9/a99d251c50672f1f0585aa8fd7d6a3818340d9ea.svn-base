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
import java.util.Map;
import javax.swing.tree.DefaultMutableTreeNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Stores all defined treasure lists.
 * @author Andreas Kirschbaum
 */
public class TreasureTree {

    /**
     * Maps treasure name to {@link TreasureTreeNode} instance.
     */
    @NotNull
    private final Map<String, TreasureTreeNode> treasures = new HashMap<String, TreasureTreeNode>();

    /**
     * The root node for normal treasure lists.
     */
    @NotNull
    private final DefaultMutableTreeNode root;

    /**
     * Creates a new instance.
     * @param root the root node for normal treasure lists
     */
    public TreasureTree(@NotNull final DefaultMutableTreeNode root) {
        this.root = root;
    }

    /**
     * Returns a treasure list by name.
     * @param name the name
     * @return the treasure name or <code>null</code> if it does not exist
     */
    @Nullable
    public TreasureTreeNode get(@NotNull final String name) {
        return treasures.get(name);
    }

    /**
     * Adds a {@link TreasureTreeNode}.
     * @param treasureTreeNode the treasure tree node to add
     */
    private void put(@NotNull final TreasureTreeNode treasureTreeNode) {
        treasures.put(treasureTreeNode.getTreasureObj().getName(), treasureTreeNode);
    }

    /**
     * Adds all {@link TreasureTreeNode}.
     * @param treasureTreeNodes the treasure tree node to add
     */
    public void putAll(final Iterable<TreasureTreeNode> treasureTreeNodes) {
        for (final TreasureTreeNode node : treasureTreeNodes) {
            put(node);
        }
    }

    /**
     * Returns the root node for normal treasure lists.
     * @return the root node
     */
    @NotNull
    public DefaultMutableTreeNode getRoot() {
        return root;
    }

}
