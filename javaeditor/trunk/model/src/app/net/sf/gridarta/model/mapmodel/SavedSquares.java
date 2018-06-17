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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.baseobject.GameObjectContainer;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.gameobject.GameObjectFactory;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.match.GameObjectMatchers;
import net.sf.gridarta.utils.Size2D;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Records a set of changed map squares.
 * @author Andreas Kirschbaum
 */
public class SavedSquares<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> implements Serializable {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The saved squares. Maps x-coordinate to y-coordinate to map squares
     * contents. Not saved map squares (or map columns) are null.
     */
    @NotNull
    private final ArrayList<ArrayList<ArrayList<G>>> savedSquares = new ArrayList<ArrayList<ArrayList<G>>>();

    /**
     * The {@link GameObjectFactory} to use.
     */
    @NotNull
    private final GameObjectFactory<G, A, R> gameObjectFactory;

    /**
     * The {@link GameObjectMatchers} to use.
     */
    @NotNull
    private final GameObjectMatchers gameObjectMatchers;

    /**
     * Creates a new instance.
     * @param gameObjectFactory the game object factory to use
     * @param gameObjectMatchers the game object matchers to use
     */
    public SavedSquares(@NotNull final GameObjectFactory<G, A, R> gameObjectFactory, @NotNull final GameObjectMatchers gameObjectMatchers) {
        this.gameObjectFactory = gameObjectFactory;
        this.gameObjectMatchers = gameObjectMatchers;
    }

    /**
     * Records a map square as changed. Does nothing if the square already has
     * been saved.
     * @param mapSquare the map square to save
     */
    public void recordMapSquare(@NotNull final MapSquare<G, A, R> mapSquare) {
        final int x = mapSquare.getMapX();
        final int y = mapSquare.getMapY();
        final ArrayList<G> list = allocateMapSquare(x, y);
        if (list == null) {
            return;
        }

        for (final G content : mapSquare) {
            if (content.isHead()) {
                list.add(gameObjectFactory.cloneMultiGameObject(content));
            }
        }
        list.trimToSize();
    }

    /**
     * Allocates a saved map square.
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @return the newly allocated map square or <code>null</code> if the map
     *         square already did exist
     */
    @Nullable
    private ArrayList<G> allocateMapSquare(final int x, final int y) {
        final ArrayList<ArrayList<G>> col;
        if (x >= savedSquares.size()) {
            while (savedSquares.size() < x) {
                savedSquares.add(null);
            }
            col = new ArrayList<ArrayList<G>>();
            savedSquares.add(col);
        } else {
            final ArrayList<ArrayList<G>> tmp = savedSquares.get(x);
            if (tmp != null) {
                col = tmp;
            } else {
                col = new ArrayList<ArrayList<G>>();
                savedSquares.set(x, col);
            }
        }

        final ArrayList<G> result;
        if (y >= col.size()) {
            while (col.size() < y) {
                col.add(null);
            }
            result = new ArrayList<G>();
            col.add(result);
        } else {
            final Collection<G> tmp = col.get(y);
            if (tmp != null) {
                return null;
            }

            result = new ArrayList<G>();
            col.set(y, result);
        }

        return result;
    }


    /**
     * Returns whether no saved squares exist.
     * @return whether no saved squares exist
     */
    public boolean isEmpty() {
        return savedSquares.isEmpty();
    }

    /**
     * Forgets all saved squares.
     */
    public void clear() {
        savedSquares.clear();
    }

    /**
     * Creates a new instance having the same contents as this instance, then
     * forgets all saves squares in this instance.
     * @return the new instance
     */
    @NotNull
    public SavedSquares<G, A, R> cloneAndClear() {
        final SavedSquares<G, A, R> clone = new SavedSquares<G, A, R>(gameObjectFactory, gameObjectMatchers);
        clone.savedSquares.addAll(savedSquares);
        savedSquares.clear();
        return clone;
    }

    /**
     * Applies the saved squares to the given map model.
     * @param mapModel the map model to change
     */
    public void applyChanges(@NotNull final MapModel<G, A, R> mapModel) {
        final Collection<G> objectsToDelete = new ArrayList<G>();

        final Point point = new Point();
        point.x = 0;
        for (final Iterable<ArrayList<G>> col : savedSquares) {
            if (col != null) {
                point.y = 0;
                for (final Iterable<G> square : col) {
                    if (square != null) {
                        final GameObjectContainer<G, A, R> mapSquare = mapModel.getMapSquare(point);
                        for (final G gameObject : mapSquare) {
                            if (gameObject.isHead()) {
                                objectsToDelete.add(gameObject);
                            }
                        }
                        for (final G gameObject : objectsToDelete) {
                            mapModel.removeGameObject(gameObject, false);
                        }
                        objectsToDelete.clear();
                        for (final G gameObject : square) {
                            mapSquare.addLast(gameObject);
                        }
                    }
                    point.y++;
                }
            }
            point.x++;
        }

        final Point point2 = new Point();
        point.x = 0;
        for (final Iterable<ArrayList<G>> col : savedSquares) {
            if (col != null) {
                point.y = 0;
                for (final Iterable<G> square : col) {
                    if (square != null) {
                        for (final GameObject<G, A, R> gameObject : square) {
                            final Point map = gameObject.getMapLocation();
                            for (G tailGameObject = gameObject.getMultiNext(); tailGameObject != null; tailGameObject = tailGameObject.getMultiNext()) {
                                point2.x = map.x + tailGameObject.getArchetype().getMultiX();
                                point2.y = map.y + tailGameObject.getArchetype().getMultiY();
                                final GameObjectContainer<G, A, R> mapSquare = mapModel.getMapSquare(point2);
                                mapSquare.addLast(tailGameObject);
                            }
                        }
                    }
                    point.y++;
                }
            }
            point.x++;
        }
    }

    /**
     * Removes empty squares outside a given area.
     * @param size the area
     */
    public void removeEmptySquares(@NotNull final Size2D size) {
        final int width = size.getWidth();
        final int height = size.getHeight();

        if (!isOutsideEmpty(width, height)) {
            throw new IllegalArgumentException();
        }

        for (int x = 0; x < width && x < savedSquares.size(); x++) {
            final List<ArrayList<G>> col = savedSquares.get(x);
            if (col != null) {
                for (int y = col.size() - 1; y >= height; y--) {
                    col.remove(y);
                }
            }
        }
        for (int x = savedSquares.size() - 1; x >= width; x--) {
            savedSquares.remove(x);
        }
    }

    /**
     * Returns whether all squares outside a given area are empty or unchanged.
     * @param width the width of the area
     * @param height the height of the area
     * @return whether no non-empty squares exist
     */
    private boolean isOutsideEmpty(final int width, final int height) {
        for (int x = 0; x < width && x < savedSquares.size(); x++) {
            final ArrayList<ArrayList<G>> col = savedSquares.get(x);
            if (col != null) {
                for (int y = height; y < col.size(); y++) {
                    final Collection<G> square = col.get(y);
                    if (!square.isEmpty()) {
                        return false;
                    }
                }
            }
        }

        for (int x = width; x < savedSquares.size(); x++) {
            final Iterable<ArrayList<G>> col = savedSquares.get(x);
            if (col != null) {
                for (final Collection<G> square : col) {
                    if (square != null && !square.isEmpty()) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

}
