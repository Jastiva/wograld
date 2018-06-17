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

package net.sf.gridarta.model.data;

import java.io.Serializable;
import org.jetbrains.annotations.NotNull;

/**
 * A <code>AbstractNamedObject</code> has a name and tree position source and
 * provides an icon for display. <code>AbstractNamedObject</code> is the super
 * class of {@link net.sf.gridarta.model.face.FaceObject} and {@link
 * net.sf.gridarta.model.anim.AnimationObject}.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 */
public interface NamedObject extends Comparable<NamedObject>, Serializable {

    /**
     * Get the name of this AbstractNamedObject. The name should be of user
     * presentable but 100% machine processable nature, means it must be usable
     * for reverse lookup of this AbstractNamedObject in Maps. For faces, the
     * name should be the face name. For animations, the name should be the
     * animation name. For arch objects, the name should be the arch name
     * (definitely not the title).
     * @return name of this AbstractNamedObject
     */
    @NotNull
    String getName();

    /**
     * Get the path of this AbstractNamedObject. The path should be of user
     * presentable but 100% machine processable nature. The path must be usable
     * for creating a tree for this AbstractNamedObject. The path returned by
     * this method does not include the name of the object. The path separator
     * must be "/" independently of the underlying operating system.
     * @return path of this AbstractNamedObject
     * @see #setPath(String)
     */
    @NotNull
    String getPath();

    /**
     * Set the path of this AbstractNamedObject.
     * @param path new path for this AbstractNamedObject
     * @see #getPath()
     */
    void setPath(@NotNull String path);

    /**
     * Returns the face name of the display icon for this AbstractNamedObject.
     * @return the face name
     */
    @NotNull
    String getDisplayIconName();

    /**
     * {@inheritDoc}
     */
    @Override
    String toString();

}
