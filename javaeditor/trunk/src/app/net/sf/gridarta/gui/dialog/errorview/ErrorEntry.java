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

package net.sf.gridarta.gui.dialog.errorview;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import org.jetbrains.annotations.NotNull;

/**
 * An entry in a {@link net.sf.gridarta.gui.dialog.errorview.DefaultErrorView}.
 * @author Andreas Kirschbaum
 */
public class ErrorEntry extends DefaultMutableTreeNode {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The {@link DefaultTreeModel} this instance belongs to.
     */
    @NotNull
    private final DefaultTreeModel treeModel;

    /**
     * Creates a new instance.
     * @param treeModel the tree model this instance belongs to
     * @param name the name
     * @param allowsChildren whether this node allows children
     */
    public ErrorEntry(@NotNull final DefaultTreeModel treeModel, @NotNull final String name, final boolean allowsChildren) {
        super(name, allowsChildren);
        this.treeModel = treeModel;
    }

    @Override
    public void add(@NotNull final MutableTreeNode newChild) {
        super.add(newChild);
        treeModel.nodesWereInserted(this, new int[] { getChildCount() - 1, });
    }

}
