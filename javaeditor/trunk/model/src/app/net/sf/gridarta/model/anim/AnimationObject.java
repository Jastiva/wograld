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

package net.sf.gridarta.model.anim;

import net.sf.gridarta.model.data.NamedObject;
import org.jetbrains.annotations.NotNull;

/**
 * An AnimationObject reflects the animation ("<code>anim\n</code>" ...
 * "<code>mina\n</code>") of a GameObject or Archetype.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 */
public interface AnimationObject extends NamedObject {

    /**
     * Get the animName, which is the name of the animation as usable by the
     * "animations" attribute.
     * @return the name of this animation
     */
    @NotNull
    String getAnimName();

    /**
     * Returns the animation list of this animation. The individual entries are
     * all suffixed with '\n'.
     * @return the animation list of this animation
     */
    @NotNull
    String getAnimList();

    /**
     * Get the facings, which is the number of different sub-animations, for
     * instance for different directions.
     * @return facings
     */
    int getFacings();

    /**
     * Get the first frame.
     * @param facing facing to get frame for, usually a direction
     * @return frame
     */
    @NotNull
    String getFirstFrame(int facing);
    
    int getFrameCount();
    
    String getFrame(final int facing, final int frame) throws IndexOutOfBoundsException;

}
