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

import net.sf.gridarta.model.data.AbstractNamedObjects;
import net.sf.gridarta.model.data.IllegalNamedObjectException;
import org.jetbrains.annotations.NotNull;

/**
 * Abstract base class for {@link AnimationObjects} implementations.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 * @author Andreas Kirschbaum
 */
public abstract class AbstractAnimationObjects extends AbstractNamedObjects<AnimationObject> implements AnimationObjects {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Creates a new instance.
     * @param name the localized name of the object type, e.g. used in dialogs
     */
    protected AbstractAnimationObjects(final String name) {
        super(name);
    }

    /**
     * Adds a new animation object.
     * @param animName name of the animation object to add
     * @param list String with animation list data
     * @param path Path relative to the arch directory
     * @throws DuplicateAnimationException in case the animation was not unique
     * @throws IllegalAnimationException if the animation cannot be added
     */
    @Override
    public void addAnimationObject(@NotNull final String animName, @NotNull final String list, @NotNull final String path) throws DuplicateAnimationException, IllegalAnimationException {
        final AnimationObject animationObject = new DefaultAnimationObject(animName, list, path + '/' + animName);
        if (containsKey(animName)) {
            throw new DuplicateAnimationException(animationObject);
        }
        try {
            put(animationObject);
        } catch (final IllegalNamedObjectException ex) {
            throw new IllegalAnimationException(animationObject, ex);
        }
    }

}
