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

import net.sf.gridarta.model.data.AbstractNamedObjects;
import net.sf.gridarta.model.data.IllegalNamedObjectException;
import org.jetbrains.annotations.NotNull;

/**
 * Abstract base class for {@link FaceObjects} implementations.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 * @author Andreas Kirschbaum
 */
public abstract class AbstractFaceObjects extends AbstractNamedObjects<FaceObject> implements FaceObjects {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Creates a new instance.
     * @param name localized name of the object type, e.g. used in dialogs
     */
    protected AbstractFaceObjects(@NotNull final String name) {
        super(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addFaceObject(@NotNull final String faceName, @NotNull final String originalFilename, final int offset, final int size) throws DuplicateFaceException, IllegalFaceException {
        final FaceObject faceObject = new DefaultFaceObject(faceName, originalFilename, offset, size);
        final FaceObject otherFaceObject = get(faceName);
        if (otherFaceObject != null) {
            throw new DuplicateFaceException(faceObject, otherFaceObject);
        }
        try {
            put(faceObject);
        } catch (final IllegalNamedObjectException ex) {
            throw new IllegalFaceException(faceObject, ex);
        }
    }

}
