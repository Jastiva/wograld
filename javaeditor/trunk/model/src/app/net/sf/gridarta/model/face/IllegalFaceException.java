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

package net.sf.gridarta.model.face;

import org.jetbrains.annotations.NotNull;

/**
 * Exception thrown to indicate that a face object is not acceptable.
 * @author Andreas Kirschbaum
 */
public class IllegalFaceException extends Exception {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The illegal face object.
     * @serial
     */
    @NotNull
    private final FaceObject faceObject;

    /**
     * Creates a new instance.
     * @param faceObject the illegal face object
     * @param ex the source exception
     */
    public IllegalFaceException(@NotNull final FaceObject faceObject, @NotNull final Throwable ex) {
        super(ex);
        this.faceObject = faceObject;
    }

    /**
     * Returns the illegal face object.
     * @return the illegal face object
     */
    @NotNull
    public FaceObject getFaceObject() {
        return faceObject;
    }

}
