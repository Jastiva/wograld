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

import java.io.File;
import net.sf.gridarta.model.data.NamedObjects;
import net.sf.gridarta.model.errorview.ErrorView;
import org.jetbrains.annotations.NotNull;

/**
 * FaceObjects is a container for {@link FaceObject FaceObjects}.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 */
public interface FaceObjects extends NamedObjects<FaceObject> {

    /**
     * Loads all faces from a png collection file.
     * @param errorView the error view for reporting errors
     * @param collectedDirectory directory to load from
     * @return the face provider for accessing the read faces
     */
    @NotNull
    FaceProvider loadFacesCollection(@NotNull ErrorView errorView, @NotNull File collectedDirectory);

    /**
     * Adds a new face object.
     * @param faceName the name of the face, e.g. <samp>"robe.101"</samp>
     * @param originalFilename the original filename, e.g.
     * <samp>"arch/objects/misc/robe.101.png"</samp>
     * @param offset the offset in the file denoted by <var>actualFilename</var>,
     * e.g. <samp>148676</samp>
     * @param size the size in the file denoted by <var>actualFilename</var>,
     * e.g. <samp>567</samp>,
     * @throws DuplicateFaceException in case the face was not unique
     * @throws IllegalFaceException if the face cannot be added
     */
    void addFaceObject(String faceName, String originalFilename, int offset, int size) throws DuplicateFaceException, IllegalFaceException;

    /**
     * Returns whether the images file contains face numbers.
     * @return whether the images file contains face numbers
     */
    boolean isIncludeFaceNumbers();

}
