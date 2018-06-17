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
import org.jetbrains.annotations.NotNull;

/**
 * A {@link MapMenuEntry} that represents a map.
 * @author Andreas Kirschbaum
 */
public class MapMenuEntryMap extends MapMenuEntry {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The map {@link File}.
     */
    @NotNull
    private final File mapFile;

    /**
     * Creates a new instance.
     * @param mapFile the map file
     * @param title the entry's title
     */
    public MapMenuEntryMap(@NotNull final File mapFile, @NotNull final String title) {
        super(title);
        this.mapFile = mapFile;
    }

    /**
     * Returns the map file.
     * @return the map file
     */
    @NotNull
    public File getMapFile() {
        return mapFile;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean allowsChildren() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void visit(@NotNull final MapMenuEntryVisitor visitor) {
        visitor.visit(this);
    }

}
