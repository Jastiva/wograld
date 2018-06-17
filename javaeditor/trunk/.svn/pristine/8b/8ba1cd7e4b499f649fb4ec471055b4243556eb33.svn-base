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

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import net.sf.gridarta.model.face.FaceObject;
import net.sf.gridarta.model.face.FaceObjects;
import net.sf.gridarta.model.gameobject.GameObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Collection of all smoothing information.
 * @author Andreas Kirschbaum
 */
public class SmoothFaces implements Iterable<Map.Entry<String, SmoothFace>> {

    /**
     * The defined {@link SmoothFaces}.
     */
    @NotNull
    private final Map<String, SmoothFace> smoothFaces = new TreeMap<String, SmoothFace>();

    /**
     * The {@link FaceObjects} for looking up faces.
     */
    @NotNull
    private final FaceObjects faceObjects;

    /**
     * Creates a new instance.
     * @param faceObjects the face objects for looking up faces
     */
    public SmoothFaces(@NotNull final FaceObjects faceObjects) {
        this.faceObjects = faceObjects;
    }

    /**
     * Adds a {@link SmoothFace} instance.
     * @param smoothFace the smooth face instance
     * @throws DuplicateSmoothFaceException if the smooth face is not unique
     */
    public void add(@NotNull final SmoothFace smoothFace) throws DuplicateSmoothFaceException {
        final SmoothFace oldSmoothFace = smoothFaces.get(smoothFace.getFace());
        if (oldSmoothFace != null) {
            if (oldSmoothFace.getValue().equals(smoothFace.getValue())) {
                return;
            }

            throw new DuplicateSmoothFaceException(smoothFace.getFace(), smoothFace.getValue(), oldSmoothFace.getValue());
        }

        smoothFaces.put(smoothFace.getFace(), smoothFace);
    }

    /**
     * Returns the smooth faces for a {@link GameObject}.
     * @param gameObject the game object
     * @return the smooth face or <code>null</code> if the game object has no
     *         attached smooth face
     */
    @Nullable
    public FaceObject getSmoothFace(@NotNull final GameObject<?, ?, ?> gameObject) {
        String faceName = gameObject.getFaceName();
        if (faceName == null) {
            faceName = gameObject.getArchetype().getFaceName();
        }
        if (faceName == null) {
            return null;
        }
        final SmoothFace smoothFace = smoothFaces.get(faceName);
        if (smoothFace == null) {
            return null;
        }
        return faceObjects.get(smoothFace.getValue());
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public Iterator<Map.Entry<String, SmoothFace>> iterator() {
        return Collections.unmodifiableMap(smoothFaces).entrySet().iterator();
    }

}
