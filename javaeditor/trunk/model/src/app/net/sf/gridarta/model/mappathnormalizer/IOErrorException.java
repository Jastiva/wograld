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

package net.sf.gridarta.model.mappathnormalizer;

import java.io.File;
import java.io.IOException;
import org.jetbrains.annotations.NotNull;

/**
 * Exception throws if an I/O error occurs.
 * @author Andreas Kirschbaum
 */
public class IOErrorException extends Exception {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The invalid file.
     * @serial
     */
    @NotNull
    private final File file;

    /**
     * Creates a new instance.
     * @param file the invalid file
     * @param exception the I/O exception
     * @noinspection TypeMayBeWeakened
     */
    public IOErrorException(@NotNull final File file, @NotNull final IOException exception) {
        super(file.getPath(), exception);
        this.file = file;
    }

    /**
     * Returns the invalid file.
     * @return the invalid file
     */
    @NotNull
    public File getFile() {
        return file;
    }

}
