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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.utils.Size2D;
import org.jetbrains.annotations.NotNull;

/**
 * A rectangular grid of {@link MapSquare} instances.
 * @author Andreas Kirschbaum
 */
public class MapSquareGrid<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> implements Serializable {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The associated {@link MapModel}.
     * @serial
     */
    @NotNull
    private final MapModel<G, A, R> mapModel;

    /**
     * The size of {@link #mapGrid}.
     * @serial
     */
    @NotNull
    private Size2D mapSize;

    /**
     * The {@link MapSquare} of this grid.
     * @serial
     */
    @NotNull
    private List<List<MapSquare<G, A, R>>> mapGrid;

    /**
     * Creates a new instance.
     * @param mapModel the associated map model
     * @param mapSize the map size
     */
    public MapSquareGrid(@NotNull final MapModel<G, A, R> mapModel, @NotNull final Size2D mapSize) {
        this.mapModel = mapModel;
        this.mapSize = mapSize;
        mapGrid = newMapGrid(mapSize);
        for (int y = 0; y < mapSize.getHeight(); y++) {
            for (int x = 0; x < mapSize.getWidth(); x++) {
                mapGrid.get(x).set(y, new MapSquare<G, A, R>(mapModel, x, y));
            }
        }
    }

    /**
     * Allocates a new 2-dimensional {@link MapSquare} array of the given size.
     * @param mapSize the size
     * @return the array
     */
    @NotNull
    private List<List<MapSquare<G, A, R>>> newMapGrid(@NotNull final Size2D mapSize) {
        final ArrayList<List<MapSquare<G, A, R>>> result = new ArrayList<List<MapSquare<G, A, R>>>(mapSize.getWidth());
        for (int x = 0; x < mapSize.getWidth(); x++) {
            final ArrayList<MapSquare<G, A, R>> column = new ArrayList<MapSquare<G, A, R>>(mapSize.getHeight());
            for (int y = 0; y < mapSize.getHeight(); y++) {
                column.add(null);
            }
            column.trimToSize();
            result.add(column);
        }
        result.trimToSize();
        return result;
    }

    /**
     * Returns the {@link MapSquare} at a given location.
     * @param x the location's x coordinate
     * @param y the location's y coordinate
     * @return the map square
     * @throws IndexOutOfBoundsException if the location is not within bounds
     */
    @NotNull
    public MapSquare<G, A, R> getMapSquare(final int x, final int y) {
        return mapGrid.get(x).get(y);
    }

    /**
     * This implementation is very safe by recreating every single MapSquare as
     * new empty square with the trade-off of some strain of the
     * garbage-collector. An alternative implementation would manually clean
     * every existing MapSquare but that probably wouldn't really be faster
     * until we have reuse of GameObject instances similar to J2EE.
     */
    public void clearMap() {
        for (int x = 0; x < mapSize.getWidth(); x++) {
            for (int y = 0; y < mapSize.getHeight(); y++) {
                final List<MapSquare<G, A, R>> column = mapGrid.get(x);
                if (column.get(y) == null) {
                    column.set(y, new MapSquare<G, A, R>(mapModel, x, y));
                } else if (!column.get(y).isEmpty()) {
                    column.get(y).beginSquareChange();
                    try {
                        column.set(y, new MapSquare<G, A, R>(mapModel, x, y));
                    } finally {
                        column.get(y).endSquareChange();
                    }
                }
            }
        }
    }

    /**
     * Returns whether the map is empty.
     * @return <code>true</code> if the map is empty, or <code>false</code> if
     *         the map is not empty
     */
    public boolean isEmpty() {
        for (int x = 0; x < mapSize.getWidth(); x++) {
            for (int y = 0; y < mapSize.getHeight(); y++) {
                if (!mapGrid.get(x).get(y).isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Resizes the map grid to a new size.
     * @param newSize the new size
     */
    public void resize(@NotNull final Size2D newSize) {
        final List<List<MapSquare<G, A, R>>> newGrid = newMapGrid(newSize);

        // relink all arches to the new grid
        for (int x = 0; x < newSize.getWidth(); x++) {
            for (int y = 0; y < newSize.getHeight(); y++) {
                if (x < mapSize.getWidth() && y < mapSize.getHeight()) {
                    newGrid.get(x).set(y, mapGrid.get(x).get(y));
                } else {
                    newGrid.get(x).set(y, new MapSquare<G, A, R>(mapModel, x, y));
                }
            }
        }

        // replace old grid by new one
        mapSize = newSize;
        mapGrid = newGrid;
    }

    /**
     * Adds all head parts for game object within an area to a collection.
     * @param minX the x coordinate of the left border of the area
     * @param minY the y coordinate of the top border of the area
     * @param maxX the x coordinate of the right border of the area
     * @param maxY the y coordinate of the bottom border of the area
     * @param objectsToDelete the collection where the heads are added to
     */
    public void collectHeads(final int minX, final int minY, final int maxX, final int maxY, @NotNull final Collection<GameObject<G, A, R>> objectsToDelete) {
        for (int x = minX; x < maxX; x++) {
            for (int y = minY; y < maxY; y++) {
                for (final GameObject<G, A, R> node : mapGrid.get(x).get(y)) {
                    objectsToDelete.add(node.getHead());
                }
            }
        }
    }

    /**
     * Returns the size of this map grid in map squares.
     * @return the size
     */
    @NotNull
    public Size2D getMapSize() {
        return mapSize;
    }

}
