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

package net.sf.gridarta.model.mapmodel;

import java.util.Iterator;
import java.util.NoSuchElementException;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.match.GameObjectMatcher;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * An {@link Iterator} that filters another iterator according to a {@link
 * GameObjectMatcher}. Only game objects matching the matcher are returned.
 * @author Andreas Kirschbaum
 */
public class FilterGameObjectIterator<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> implements Iterator<G> {

    /**
     * The {@link Iterator} being filtered.
     */
    @NotNull
    private final Iterator<G> iterator;

    /**
     * The {@link GameObjectMatcher} for filtering returned game objects.
     */
    @NotNull
    private final GameObjectMatcher matcher;

    /**
     * The next element to return from {@link #next()} or <code>null</code> if
     * no next element exists. Only valid if {@link #findNextPending} is set.
     */
    @Nullable
    private G next;

    /**
     * Whether {@link #findNext()} has to be called.
     */
    private boolean findNextPending = true;

    /**
     * Creates a new instance.
     * @param iterator the iterator to filter
     * @param matcher the game object matcher to filter with
     */
    public FilterGameObjectIterator(@NotNull final Iterator<G> iterator, @NotNull final GameObjectMatcher matcher) {
        this.iterator = iterator;
        this.matcher = matcher;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasNext() {
        findNext();
        return next != null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public G next() {
        findNext();
        if (next == null) {
            throw new NoSuchElementException();
        }
        findNextPending = true;
        return next;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove() {
        if (findNextPending) {
            throw new IllegalStateException();
        }
        findNextPending = true;
        iterator.remove();
    }

    /**
     * Updates {@link #next} to hold the next matching game object. Does nothing
     * unless {@link #findNextPending} is set.
     */
    private void findNext() {
        if (!findNextPending) {
            return;
        }
        findNextPending = false;

        while (true) {
            if (!iterator.hasNext()) {
                next = null;
                return;
            }

            final G tmp = iterator.next();
            if (matcher.isMatching(tmp)) {
                next = tmp;
                return;
            }
        }
    }

}
