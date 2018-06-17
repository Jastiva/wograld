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

package net.sf.gridarta.gui.dialog.bookmarks;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import net.sf.gridarta.gui.mapimagecache.MapImageCache;
import net.sf.gridarta.gui.mapmenu.MapMenuEntry;
import net.sf.gridarta.gui.mapmenu.MapMenuEntryDir;
import net.sf.gridarta.gui.mapmenu.MapMenuEntryMap;
import net.sf.gridarta.gui.mapmenu.MapMenuEntryVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Generates icons for {@link MapMenuEntry} instances.
 * @author Andreas Kirschbaum
 */
public class MapMenuEntryIcons implements MapMenuEntryVisitor {

    /**
     * The {@link MapImageCache} for looking up icons.
     */
    @NotNull
    private final MapImageCache<?, ?, ?> mapImageCache;

    /**
     * The result icon.
     */
    @Nullable
    private Icon icon;

    /**
     * Creates a new instance.
     * @param mapImageCache the map image cache for looking up icons
     */
    public MapMenuEntryIcons(@NotNull final MapImageCache<?, ?, ?> mapImageCache) {
        this.mapImageCache = mapImageCache;
    }

    /**
     * Returns an {@link Icon} for a {@link MapMenuEntry} instance.
     * @param mapMenuEntry the instance
     * @return the icon or <code>null</code>
     */
    @Nullable
    public Icon getIcon(@NotNull final MapMenuEntry mapMenuEntry) {
        mapMenuEntry.visit(this);
        final Icon result = icon;
        icon = null;
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void visit(@NotNull final MapMenuEntryDir mapMenuEntry) {
        icon = null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void visit(@NotNull final MapMenuEntryMap mapMenuEntry) {
        icon = new ImageIcon(mapImageCache.getOrCreatePreview(mapMenuEntry.getMapFile()));
    }

}
