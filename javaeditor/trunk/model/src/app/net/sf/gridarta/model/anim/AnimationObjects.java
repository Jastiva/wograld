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

import net.sf.gridarta.model.data.NamedObjects;
import org.jetbrains.annotations.NotNull;

/**
 * AnimationObjects is a container for {@link AnimationObject
 * AnimationObjects}.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 */
public interface AnimationObjects extends NamedObjects<AnimationObject> {

    /**
     * Add an animation object.
     * @param animName name of the animation object
     * @param list list with individual frames
     * @param path the path for the animation object
     * @throws DuplicateAnimationException in case the animation was not unique
     * @throws IllegalAnimationException if the animation cannot be added
     */
    void addAnimationObject(@NotNull String animName, @NotNull String list, @NotNull String path) throws DuplicateAnimationException, IllegalAnimationException;

}
