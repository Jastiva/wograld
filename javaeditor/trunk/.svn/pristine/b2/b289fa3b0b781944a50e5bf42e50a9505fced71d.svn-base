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

import java.io.IOException;
import org.jetbrains.annotations.NotNull;

/**
 * Exception thrown if a map file cannot be loaded.
 * @author Andreas Kirschbaum
 */
public class CannotLoadMapFileException extends IOException {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The map path that could not be loaded.
     * @serial
     */
    @NotNull
    private final String mapPath;

    /**
     * Creates a new instance.
     * @param mapPath the map path that could not be loaded
     * @param cause the cause why the map file could not be loaded
     */
    public CannotLoadMapFileException(@NotNull final String mapPath, @NotNull final Throwable cause) {
        super(mapPath, cause);
        this.mapPath = mapPath;
    }

    /**
     * Returns the map path that could not be loaded.
     * @return the map path
     */
    @NotNull
    public String getMapPath() {
        return mapPath;
    }

}
