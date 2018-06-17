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
import java.util.Iterator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This class manages NamedObjects, managing their tree as well as providing a
 * method for showing a dialog for browsing their tree and selecting one from
 * it. NamedObjects are iterated in Unicode (=ASCII) order.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 */
public interface NamedObjects<E extends NamedObject> extends Iterable<E>, Serializable {

    /**
     * Get the object tree root.
     * @return object tree root
     */
    @NotNull
    NamedTreeNode<E> getTreeRoot();

    /**
     * Returns the tree name.
     * @return the tree name
     */
    @NotNull
    String getName();

    /**
     * Get the number of objects.
     * @return object count
     */
    int size();

    /**
     * Gets a AbstractNamedObject.
     * @param objectName name of object
     * @return named object for name <var>objectName</var> or <code>null</code>
     *         if none found
     */
    @Nullable
    E get(@NotNull String objectName);

    /**
     * Check whether an object is defined.
     * @param name object name to look for
     * @return <code>true</code> if an object with that name is defined,
     *         otherwise <code>false</code>
     */
    boolean containsKey(@NotNull String name);

    /**
     * {@inheritDoc} NamedObjects are iterated in Unicode (=case sensitive
     * ASCII) order.
     */
    @Override
    Iterator<E> iterator();

}
