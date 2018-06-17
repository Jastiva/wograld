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

package net.sf.gridarta.model.baseobject;

import java.util.EmptyStackException;
import java.util.Iterator;
import java.util.Stack;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;

/**
 * Iterator for recursively iterating over GameObjectContainers.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 */
public class RecursiveGameObjectIterator<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> implements Iterator<G> {

    /**
     * The Iterator stack.
     */
    private final Stack<Iterator<G>> iteratorStack = new Stack<Iterator<G>>();

    /**
     * The current iterator.
     */
    private Iterator<G> current;

    /**
     * Create a recursive GameObject Iterator.
     * @param container GameObjectContainer to start with
     */
    RecursiveGameObjectIterator(final Iterable<G> container) {
        current = container.iterator();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasNext() {
        return current.hasNext();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public G next() {
        final G gameObject = current.next();
        try {
            return gameObject;
        } finally {
            if (gameObject.isEmpty()) {
                try {
                    //noinspection LoopConditionNotUpdatedInsideLoop
                    while (!current.hasNext()) {
                        current = iteratorStack.pop();
                    }
                } catch (final EmptyStackException ignore) {
                    /* ignore. */
                }
            } else {
                iteratorStack.push(current);
                current = gameObject.iterator();
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove() {
        current.remove();
    }

}
