/*
 * Gridarta MMORPG map editor for Crossfire, Daimonin and similar games.
 * Copyright (C) 2000-2011 The Gridarta Developers.
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

package net.sf.gridarta.gui.panel.connectionview;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import net.sf.gridarta.model.gameobject.GameObject;
import org.jetbrains.annotations.NotNull;

/**
 * Stores {@link GameObject GameObjects} related to key values. A connection
 * consists of a key object, and a set of <code>GameObject</code>s.
 * @author Andreas Kirschbaum
 */
public class Connection<K> implements Iterable<GameObject<?, ?, ?>> {

    /**
     * The key.
     */
    @NotNull
    private final K key;

    /**
     * The game objects for this connection.
     * @note a list is used to allow accessing the game objects by index
     * @note the initial capacity of 2 assumes that most connection values have
     * only two game objects: the triggering object and the triggered object
     */
    @NotNull
    private final List<GameObject<?, ?, ?>> gameObjects = new ArrayList<GameObject<?, ?, ?>>(2);

    /**
     * Create a new instance.
     * @param key the key
     */
    public Connection(@NotNull final K key) {
        this.key = key;
    }

    /**
     * Return the key.
     * @return the key
     */
    @NotNull
    @SuppressWarnings("TypeMayBeWeakened")
    public K getKey() {
        return key;
    }

    /**
     * Add a game object.
     * @param gameObject the game object to add
     */
    public void addGameObject(@NotNull final GameObject<?, ?, ?> gameObject) {
        if (!gameObjects.contains(gameObject)) {
            gameObjects.add(gameObject);
        }
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public Iterator<GameObject<?, ?, ?>> iterator() {
        return Collections.unmodifiableList(gameObjects).iterator();
    }

}
