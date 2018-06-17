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

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import javax.swing.tree.TreePath;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link Transferable} that transfers {@link TreePath} instances.
 * @author Andreas Kirschbaum
 */
public class TransferableTreeNode implements Transferable {

    /**
     * A {@link DataFlavor} for transferring {@link TreePath} instances.
     */
    @NotNull
    private static final DataFlavor TREE_PATH_FLAVOR = new DataFlavor(TreePath.class, "Tree Path");

    /**
     * The {@link TreePath} being transferred.
     */
    @NotNull
    private final TreePath treePath;

    /**
     * Creates a new instance.
     * @param treePath the tree path being transferred
     */
    public TransferableTreeNode(@NotNull final TreePath treePath) {
        this.treePath = treePath;
    }

    @NotNull
    @Override
    public DataFlavor[] getTransferDataFlavors() {
        return new DataFlavor[] { TREE_PATH_FLAVOR, };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isDataFlavorSupported(@NotNull final DataFlavor flavor) {
        return flavor.getRepresentationClass() == TreePath.class;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public TreePath getTransferData(@NotNull final DataFlavor flavor) throws UnsupportedFlavorException {
        if (isDataFlavorSupported(flavor)) {
            return treePath;
        }

        throw new UnsupportedFlavorException(flavor);
    }

}
