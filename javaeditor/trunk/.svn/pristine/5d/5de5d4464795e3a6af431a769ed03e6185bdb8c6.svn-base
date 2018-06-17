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
import net.sf.gridarta.utils.Size2D;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Iterator for iterating over all squares of a model.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 * @author Andreas Kirschbaum
 */
public class MapSquareIterator<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> implements Iterator<MapSquare<G, A, R>> {

    /**
     * The {@link MapModel} to iterate over.
     */
    @NotNull
    private final MapModel<G, A, R> mapModel;

    /**
     * The direction to iterate: <code>+1</code> for forward, <code>-1</code>
     * for backward.
     */
    private final int direction;

    /**
     * The {@link #mapModel}'s map width.
     */
    private final int mapWidth;

    /**
     * The {@link #mapModel}'s map height.
     */
    private final int mapHeight;

    /**
     * Index of current map square. The point is used as single value index to a
     * two dimensional array. That works fine because the two dimensional array
     * is a rectangular array.
     */
    private int point;

    /**
     * The number of remaining map square to return from {@link #next()}.
     */
    private int remainingMapSquares;

    /**
     * Creates a new instance.
     * @param mapModel the map model to iterate over
     * @param start the starting point or <code>null</code> for default
     * @param direction the direction to iterate: <code>+1</code> for forward,
     * <code>-1</code> for backward
     * @param skipFirst whether to skip the first map square and return it at
     * the end
     */
    public MapSquareIterator(@NotNull final MapModel<G, A, R> mapModel, @Nullable final Point start, final int direction, final boolean skipFirst) {
        if (direction != -1 && direction != +1) {
            throw new IllegalArgumentException();
        }
        this.mapModel = mapModel;
        this.direction = direction;
        final Size2D mapSize = mapModel.getMapArchObject().getMapSize();
        mapWidth = mapSize.getWidth();
        mapHeight = mapSize.getHeight();
        if (start != null) {
            point = start.x + start.y * mapWidth;
        } else if (direction > 0) {
            point = 0;
        } else {
            point = mapWidth * mapHeight - 1;
        }
        remainingMapSquares = mapWidth * mapHeight;
        if (skipFirst) {
            nextMapSquare();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasNext() {
        return remainingMapSquares > 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MapSquare<G, A, R> next() {
        if (remainingMapSquares <= 0) {
            throw new NoSuchElementException();
        }
        remainingMapSquares--;

        final MapSquare<G, A, R> square = mapModel.getMapSquare(new Point(point % mapWidth, point / mapWidth));
        nextMapSquare();
        return square;
    }

    /**
     * Updates {@link #point} to the next map square.
     */
    private void nextMapSquare() {
        point += direction;
        if (point < 0) {
            point += mapWidth * mapHeight;
        } else if (point >= mapWidth * mapHeight) {
            point -= mapWidth * mapHeight;
        }
    }

}
