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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This class manages NamedObjects, managing their tree as well as providing a
 * method for showing a dialog for browsing their tree and selecting one from
 * it. NamedObjects are iterated in Unicode (=ASCII) order.
 * @author <a href="mailto:michael.toennies@nord-com.net">Michael Toennies</a>
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 */
public abstract class AbstractNamedObjects<E extends NamedObject> implements NamedObjects<E> {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The localized name of the object type, e.g. used in dialogs.
     * @serial
     */
    private final String name;

    /**
     * The objects that are mapped.
     * @serial
     */
    private final Map<String, E> objectMap = new HashMap<String, E>();

    /**
     * The tree of defined objects.
     * @serial
     */
    @NotNull
    private final NamedTreeNode<E> treeRoot = new NamedTreeNode<E>();

    /**
     * Create a NamedObjects instance.
     * @param name the localized name of the object type, e.g. used in dialogs
     */
    protected AbstractNamedObjects(final String name) {
        this.name = name;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public NamedTreeNode<E> getTreeRoot() {
        return treeRoot;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public String getName() {
        return name;
    }

    /**
     * Store a AbstractNamedObject.
     * @param object AbstractNamedObject to store
     * @throws IllegalNamedObjectException if the named object cannot be added
     */
    protected void put(@NotNull final E object) throws IllegalNamedObjectException {
        objectMap.put(object.getName(), object);
        treeRoot.append(object);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int size() {
        return objectMap.size();
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public E get(@NotNull final String objectName) {
        return objectMap.get(objectName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean containsKey(@NotNull final String name) {
        return objectMap.containsKey(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<E> iterator() {
        return new TreeSet<E>(objectMap.values()).iterator();
    }

}
