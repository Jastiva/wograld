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

import net.sf.gridarta.model.data.NamedObject;
import org.jetbrains.annotations.Nullable;

/**
 * Common interface for FaceObject.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 */
public interface FaceObject extends NamedObject {

    /**
     * Get the faceName, which is the name of the face as usable by the "face"
     * attribute.
     * @return faceName
     */
    String getFaceName();

    /**
     * Get the original filename of this face.
     * @return filename where this face was originally found
     */
    String getOriginalFilename();

    /**
     * Return whether this face is an up face.
     * @return upFace
     */
    boolean isUp();

    /**
     * Return whether this face is a double face.
     * @return doubleFace
     */
    boolean isDouble();

    /**
     * Returns the alternative face name for image.a.nnn faces.
     * @return the alternative face name (image.b.nnn) or <code>null</code>
     */
    @Nullable
    String getAlternativeFaceName();
    
    boolean setVisibility(String vis);
    
    boolean setMagicmap(String magicmap);
    
    boolean setForegroundColor(String colorFg);
    
    boolean setBackgroundColor(String colorBg);
    
    boolean setIsFloor(String isFl);
    
    boolean setQuad(String quadrant);
    
    String getVisibility();
    
    String getMagicmap();
    
    String getForegroundColor();
    
    String getBackgroundColor();
    
    String getIsFloor();
    
    String getFaceQuad();


}
