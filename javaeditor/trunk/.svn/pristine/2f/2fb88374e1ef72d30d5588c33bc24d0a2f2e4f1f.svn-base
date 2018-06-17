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

package net.sf.gridarta.gui.mapfiles;

import java.io.Serializable;
import java.util.Comparator;

/**
 * A {@link Comparator} for comparing {@link MapFile MapFiles} by pickmap name.
 * @author Andreas Kirschbaum
 */
public class MapFileNameComparator implements Comparator<MapFile<?, ?, ?>>, Serializable {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * A {@link Comparator} that compares {@link MapFile} instances by name.
     */
    public static final Comparator<MapFile<?, ?, ?>> INSTANCE = new MapFileNameComparator();

    /**
     * Private constructor to prevent instantiation.
     */
    private MapFileNameComparator() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compare(final MapFile<?, ?, ?> o1, final MapFile<?, ?, ?> o2) {
        return String.CASE_INSENSITIVE_ORDER.compare(o1.getFile().getPath(), o2.getFile().getPath());
    }

}
