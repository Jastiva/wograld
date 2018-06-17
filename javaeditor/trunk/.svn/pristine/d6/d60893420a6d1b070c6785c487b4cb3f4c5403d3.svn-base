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

package net.sf.gridarta.model.smoothface;

import org.jetbrains.annotations.NotNull;

/**
 * Thrown to indicate that a {@link SmoothFace} instance is not unique.
 * @author Andreas Kirschbaum
 */
public class DuplicateSmoothFaceException extends Exception {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The new value.
     * @serial
     */
    @NotNull
    private final String newValue;

    /**
     * The existing value.
     * @serial
     */
    @NotNull
    private final String oldValue;

    /**
     * Creates a new instance.
     * @param face the duplicate smooth face
     * @param newValue the new value
     * @param oldValue the existing value
     */
    public DuplicateSmoothFaceException(@NotNull final String face, @NotNull final String newValue, @NotNull final String oldValue) {
        super(face);
        this.newValue = newValue;
        this.oldValue = oldValue;
    }

    /**
     * Returns the existing value.
     * @return the existing value
     */
    @NotNull
    public String getOldValue() {
        return oldValue;
    }

    /**
     * Returns the new value.
     * @return the new value
     */
    @NotNull
    public String getNewValue() {
        return newValue;
    }

}
