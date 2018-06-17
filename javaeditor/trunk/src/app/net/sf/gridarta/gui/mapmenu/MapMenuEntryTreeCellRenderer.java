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

import java.awt.Component;
import java.awt.Image;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import net.sf.gridarta.gui.mapimagecache.MapImageCache;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link DefaultTreeCellRenderer} for rendering {@link MapMenuEntry}
 * instances.
 * @author Andreas Kirschbaum
 */
public class MapMenuEntryTreeCellRenderer extends DefaultTreeCellRenderer implements MapMenuEntryVisitor {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The {@link MapImageCache} queried for icons.
     */
    @NotNull
    private final MapImageCache<?, ?, ?> mapImageCache;

    /**
     * Parameter for passing the expanded state into the {@link
     * MapMenuEntryVisitor} function.
     */
    private boolean expanded;

    /**
     * Creates a new instance.
     * @param mapImageCache the map image cache to query for icons
     */
    public MapMenuEntryTreeCellRenderer(@NotNull final MapImageCache<?, ?, ?> mapImageCache) {
        this.mapImageCache = mapImageCache;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Component getTreeCellRendererComponent(final JTree tree, final Object value, final boolean sel, final boolean expanded, final boolean leaf, final int row, final boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        final DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) value;
        final MapMenuEntry mapMenuEntry = (MapMenuEntry) treeNode.getUserObject();
        setText(mapMenuEntry.getTitle());
        this.expanded = expanded;
        mapMenuEntry.visit(this);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void visit(@NotNull final MapMenuEntryDir mapMenuEntry) {
        setIcon(expanded ? getOpenIcon() : getClosedIcon());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void visit(@NotNull final MapMenuEntryMap mapMenuEntry) {
        final File mapFile = mapMenuEntry.getMapFile();
        setToolTipText(mapFile.getPath());
        final Image icon = mapImageCache.getOrCreateIcon(mapFile);
        if (icon != null) {
            setIcon(new ImageIcon(icon));
        }
    }

}
