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

package net.sf.gridarta.gui.map.mapactions;

import java.awt.Point;
import java.util.Iterator;
import java.util.NoSuchElementException;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.exitconnector.ExitMatcher;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapmodel.MapModel;
import net.sf.gridarta.utils.Size2D;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * An {@link Iterator} that returns all map squares containing an exit game
 * object.
 * @author Andreas Kirschbaum
 */
public class ExitIterator<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> implements Iterator<G> {

    /**
     * The {@link ExitMatcher} for selecting exit game objects.
     */
    @NotNull
    private final ExitMatcher<G, A, R> exitMatcher;

    /**
     * The {@link MapModel} begin searched.
     */
    @NotNull
    private final MapModel<G, A, R> mapModel;

    /**
     * The search direction: <code>-1</code> for backward or <code>+1</code> for
     * forward.
     */
    private final int direction;

    /**
     * The current location on the map.
     */
    @NotNull
    private final Point point;

    /**
     * The starting exit game object or <code>null</code>.
     */
    @Nullable
    private final G start;

    /**
     * The number of map squares remaining to be searched.
     */
    private int remainingMapSquares;

    /**
     * Whether {@link #next} is valid.
     */
    private boolean findNextPending = true;

    /**
     * The next exit game object to return from {@link #next()}.
     */
    @Nullable
    private G next;

    /**
     * Creates a new instance.
     * @param exitMatcher the exit matcher for selecting exit game objects
     * @param mapModel the map model to search
     * @param xStart the x coordinate to start seacrhing
     * @param yStart the y coordinate to start seacrhing
     * @param direction the search direction: <code>-1</code> for backward or
     * <code>+1</code> for forward
     */
    public ExitIterator(@NotNull final ExitMatcher<G, A, R> exitMatcher, @NotNull final MapModel<G, A, R> mapModel, final int xStart, final int yStart, final int direction) {
        if (direction != -1 && direction != 1) {
            throw new IllegalArgumentException();
        }
        this.exitMatcher = exitMatcher;
        this.mapModel = mapModel;
        this.direction = direction;
        point = new Point(xStart, yStart);
        start = exitMatcher.getValidExit(mapModel, point);
        final Size2D mapSize = mapModel.getMapArchObject().getMapSize();
        remainingMapSquares = mapSize.getWidth() * mapSize.getHeight();
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
        throw new UnsupportedOperationException();
    }

    /**
     * Updates {@link #next} to the next match.
     */
    private void findNext() {
        if (!findNextPending) {
            return;
        }
        findNextPending = false;

        //noinspection WhileLoopSpinsOnField
        while (remainingMapSquares-- > 0) {
            mapModel.nextPoint(point, direction);
            next = exitMatcher.getValidExit(mapModel, point);
            if (next != null && next != start) {
                return;
            }
        }
        next = null;
    }

}
