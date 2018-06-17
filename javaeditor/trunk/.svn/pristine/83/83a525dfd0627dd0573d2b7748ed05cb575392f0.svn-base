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

package net.sf.gridarta.model.mapmodel;

import java.awt.Point;
import java.util.Iterator;
import java.util.NoSuchElementException;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Iterator for iterating over top-level game object of a map model.
 * @author Andreas Kirschbaum
 */
public class TopLevelGameObjectIterator<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> implements Iterator<G> {

    /**
     * The {@link Iterator} returning all {@link MapSquare MapSquares} to
     * consider.
     */
    @NotNull
    private final Iterator<MapSquare<G, A, R>> mapSquareIterator;

    /**
     * The {@link Iterator} returning all top-level game objects in the current
     * map square or <code>null</code> if no more top-level game objects exist.
     */
    @Nullable
    private Iterator<G> gameObjectIterator;

    /**
     * The {@link GameObject} to return from the next call to {@link #next()} or
     * <code>null</code> if no next game object exists.
     */
    @Nullable
    private G gameObject;

    /**
     * Creates a new instance.
     * @param mapModel the map model to iterate over
     * @param start the starting point or <code>null</code> for default
     * @param direction the direction to iterate: <code>+1</code> for forward,
     * <code>-1</code> for backward
     * @param skipFirst whether to skip the first map square and return it at
     * the end
     */
    public TopLevelGameObjectIterator(@NotNull final MapModel<G, A, R> mapModel, @Nullable final Point start, final int direction, final boolean skipFirst) {
        mapSquareIterator = new MapSquareIterator<G, A, R>(mapModel, start, direction, skipFirst);
        findNext();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasNext() {
        return gameObject != null;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public G next() {
        final G result = gameObject;
        if (result == null) {
            throw new NoSuchElementException();
        }
        findNext();
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    /**
     * Updates {@link #gameObject} to contain the next top-level game object.
     */
    private void findNext() {
        while (true) {
            if (gameObjectIterator != null) {
                if (gameObjectIterator.hasNext()) {
                    assert gameObjectIterator != null;
                    gameObject = gameObjectIterator.next();
                    return;
                }
                gameObjectIterator = null;
            }

            if (!mapSquareIterator.hasNext()) {
                gameObject = null;
                return;
            }

            final MapSquare<G, A, R> mapSquare = mapSquareIterator.next();
            if (!mapSquare.isEmpty()) {
                gameObjectIterator = mapSquare.iterator();
            }
        }
    }

}
