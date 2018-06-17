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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A <code>AbstractNamedObject</code> has a name and tree position source and
 * provides an icon for display. <code>AbstractNamedObject</code> is the super
 * class of {@link net.sf.gridarta.model.face.FaceObject} and {@link
 * net.sf.gridarta.model.anim.AnimationObject}.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 */
public abstract class AbstractNamedObject implements NamedObject {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The path of this object.
     * @serial
     */
    @NotNull
    private String path;

    /**
     * Create a AbstractNamedObject.
     * @param path Path
     * @see #getPath()
     * @see #setPath(String)
     */
    protected AbstractNamedObject(@NotNull final String path) {
        this.path = path;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public String getPath() {
        return path;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPath(@NotNull final String path) {
        this.path = path;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(@NotNull final NamedObject o) {
        return getName().compareTo(o.getName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return getName().hashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(@Nullable final Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }
        final NamedObject abstractNamedObject = (NamedObject) obj;
        return getName().equals(abstractNamedObject.getName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return getName() + "@" + path;
    }

}
