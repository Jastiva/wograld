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

package net.sf.gridarta.actions;

import java.io.File;
import net.sf.gridarta.utils.Size2D;
import org.jetbrains.annotations.NotNull;

/**
 * Exception thrown if the size of a map file is unexpected.
 * @author Andreas Kirschbaum
 */
public class MapSizeMismatchException extends Exception {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The map {@link File} of the first map.
     * @serial
     */
    @NotNull
    private final File mapFile;

    /**
     * The size of the first map.
     * @serial
     */
    @NotNull
    private final Size2D mapSize;

    /**
     * The size of the second map.
     * @serial
     */
    @NotNull
    private final Size2D otherMapSize;

    /**
     * Creates a new instance.
     * @param mapFile the map file of the first map
     * @param mapSize the size of the first map
     * @param otherMapSize the size of the second map
     */
    public MapSizeMismatchException(@NotNull final File mapFile, @NotNull final Size2D mapSize, @NotNull final Size2D otherMapSize) {
        this.mapFile = mapFile;
        this.mapSize = mapSize;
        this.otherMapSize = otherMapSize;
    }

    /**
     * Returns the map {@link File} of the first map.
     * @return the map control
     */
    @NotNull
    public File getMapFile() {
        return mapFile;
    }

    /**
     * Returns the size of the first map.
     * @return the size
     */
    @NotNull
    public Size2D getMapSize() {
        return mapSize;
    }

    /**
     * Returns the size of the second map.
     * @return the size
     */
    @NotNull
    public Size2D getOtherMapSize() {
        return otherMapSize;
    }

}
