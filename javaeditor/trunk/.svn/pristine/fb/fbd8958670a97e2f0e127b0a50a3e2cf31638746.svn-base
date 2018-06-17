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
import java.io.IOException;
import org.jetbrains.annotations.NotNull;

/**
 * Exception thrown if a map file cannot be saved.
 * @author Andreas Kirschbaum
 */
public class CannotSaveMapFileException extends IOException {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The map file that could not be saved.
     * @serial
     */
    @NotNull
    private final File mapFile;

    /**
     * Creates a new instance.
     * @param mapFile the map file that could not be saved
     * @param cause the cause why the map file could not be saved
     */
    public CannotSaveMapFileException(@NotNull final File mapFile, @NotNull final Throwable cause) {
        super(mapFile.getPath(), cause);
        this.mapFile = mapFile;
    }

    /**
     * Returns the map file that could not be saved.
     * @return the map file
     */
    @NotNull
    public File getMapFile() {
        return mapFile;
    }

}
