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

package net.sf.gridarta.gui.misc;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import net.sf.gridarta.gui.map.mapview.MapView;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.direction.Direction;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapgrid.MapGrid;
import net.sf.gridarta.model.mapgrid.SelectionMode;
import net.sf.gridarta.model.mapmodel.InsertionModeSet;
import net.sf.gridarta.model.mapmodel.MapModel;
import net.sf.gridarta.model.mapviewsettings.MapViewSettings;
import net.sf.gridarta.utils.Size2D;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Performs a "shift" operation in a map: shift all selected squares into the
 * given direction.
 * @author Andreas Kirschbaum
 */
public class ShiftProcessor<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> {

    /**
     * The map view settings instance.
     */
    @NotNull
    private final MapViewSettings mapViewSettings;

    /**
     * The map view to operate on.
     */
    @NotNull
    private final MapView<G, A, R> mapView;

    /**
     * The map model to operate on.
     */
    @NotNull
    private final MapModel<G, A, R> mapModel;

    /**
     * The {@link InsertionModeSet} to use.
     */
    @NotNull
    private final InsertionModeSet<G, A, R> insertionModeSet;

    /**
     * The map grid to operate on.
     */
    @Nullable
    private MapGrid mapGrid;

    /**
     * The selection rectangle to operate on.
     */
    @Nullable
    private Rectangle selRec;

    /**
     * The x offset to shift.
     */
    private int dx;

    /**
     * The y offset to shift.
     */
    private int dy;

    /**
     * Create a new instance.
     * @param mapViewSettings the map view settings instance
     * @param mapView the map view to operate on
     * @param mapModel the map model to operate on
     * @param insertionModeSet the insertion mode set to use
     */
    public ShiftProcessor(@NotNull final MapViewSettings mapViewSettings, @NotNull final MapView<G, A, R> mapView, @NotNull final MapModel<G, A, R> mapModel, @NotNull final InsertionModeSet<G, A, R> insertionModeSet) {
        this.mapViewSettings = mapViewSettings;
        this.mapView = mapView;
        this.mapModel = mapModel;
        this.insertionModeSet = insertionModeSet;
    }

    /**
     * Check whether shifting is possible.
     * @param dir the direction to shift
     * @return whether shifting is possible
     */
    public boolean canShift(@NotNull final Direction dir) {
        mapGrid = mapView.getMapGrid();
        selRec = mapGrid.getSelectedRec();
        if (selRec == null) {
            return false;
        }

        assert mapGrid != null;
        final Size2D size = mapGrid.getSize();

        dx = dir.getDx();
        dy = dir.getDy();
        if (dx < 0) {
            assert selRec != null;
            if (selRec.x + dx < 0) {
                return false;
            }
        } else if (dx > 0) {
            assert selRec != null;
            if (selRec.x + selRec.width + dx > size.getWidth()) {
                return false;
            }
        }

        if (dy < 0) {
            assert selRec != null;
            if (selRec.y + dy < 0) {
                return false;
            }
        } else if (dy > 0) {
            assert selRec != null;
            if (selRec.y + selRec.height + dy > size.getHeight()) {
                return false;
            }
        }

        return true;
    }

    /**
     * Shift the map contents by one square. If shifting is not possible (see
     * {@link #canShift(Direction)}) nothing is changed.
     * @param dir the direction to shift
     */
    public void shift(@NotNull final Direction dir) {
        if (canShift(dir)) {
            final Point pos = new Point();
            mapModel.beginTransaction("Shift");
            try {
                mapGrid.beginTransaction();
                try {
                    switch (dir) {
                    case NORTH:
                        for (int x = selRec.x; x < selRec.x + selRec.width; x++) {
                            pos.x = x;
                            pos.y = selRec.y;
                            shift(pos, selRec.height);
                        }
                        break;

                    case EAST:
                        for (int y = selRec.y; y < selRec.y + selRec.height; y++) {
                            pos.x = selRec.x + selRec.width - 1;
                            pos.y = y;
                            shift(pos, selRec.width);
                        }
                        break;

                    case SOUTH:
                        for (int x = selRec.x; x < selRec.x + selRec.width; x++) {
                            pos.x = x;
                            pos.y = selRec.y + selRec.height - 1;
                            shift(pos, selRec.height);
                        }
                        break;

                    case WEST:
                        for (int y = selRec.y; y < selRec.y + selRec.height; y++) {
                            pos.x = selRec.x;
                            pos.y = y;
                            shift(pos, selRec.width);
                        }
                        break;

                    case NORTH_EAST:
                        for (int x = selRec.x; x < selRec.x + selRec.width; x++) {
                            pos.x = x;
                            pos.y = selRec.y;
                            shift(pos, Math.min(x - selRec.x + 1, selRec.height));
                        }
                        for (int y = selRec.y + 1; y < selRec.y + selRec.height; y++) {
                            pos.x = selRec.x + selRec.width - 1;
                            pos.y = y;
                            shift(pos, Math.min(selRec.y + selRec.height - y, selRec.width));
                        }
                        break;

                    case SOUTH_EAST:
                        for (int x = selRec.x; x < selRec.x + selRec.width; x++) {
                            pos.x = x;
                            pos.y = selRec.y + selRec.height - 1;
                            shift(pos, Math.min(x - selRec.x + 1, selRec.height));
                        }
                        for (int y = selRec.y + selRec.height - 2; y >= selRec.y; y--) {
                            pos.x = selRec.x + selRec.width - 1;
                            pos.y = y;
                            shift(pos, Math.min(y - selRec.y + 1, selRec.width));
                        }
                        break;

                    case SOUTH_WEST:
                        for (int y = selRec.y; y < selRec.y + selRec.height; y++) {
                            pos.x = selRec.x;
                            pos.y = y;
                            shift(pos, Math.min(y - selRec.y + 1, selRec.width));
                        }
                        for (int x = selRec.x + 1; x < selRec.x + selRec.width; x++) {
                            pos.x = x;
                            pos.y = selRec.y + selRec.height - 1;
                            shift(pos, Math.min(selRec.x + selRec.width - x, selRec.height));
                        }
                        break;

                    case NORTH_WEST:
                        for (int y = selRec.y + selRec.height - 1; y >= selRec.y; y--) {
                            pos.x = selRec.x;
                            pos.y = y;
                            shift(pos, Math.min(selRec.y + selRec.height - y, selRec.width));
                        }
                        for (int x = selRec.x + 1; x < selRec.x + selRec.width; x++) {
                            pos.x = x;
                            pos.y = selRec.y;
                            shift(pos, Math.min(selRec.x + selRec.width - x, selRec.height));
                        }
                        break;

                    default:
                        throw new IllegalArgumentException();
                    }
                } finally {
                    mapGrid.endTransaction();
                }
            } finally {
                mapModel.endTransaction();
            }
        }

        mapGrid = null;
        selRec = null;
        dx = 0;
        dy = 0;
    }

    /**
     * Shift one row. The shift direction is {@link #dx} and {@link #dy}.
     * @param pos the starting position of the row in absolute map coordinates
     * @param len the length of the row in squares
     */
    private void shift(final Point pos, final int len) {
        final Collection<G> startGameObjects = new LinkedList<G>();
        final List<GameObject<G, A, R>> gameObjectsToDelete = new LinkedList<GameObject<G, A, R>>();
        final Collection<G> gameObjectsToInsert = new LinkedList<G>();
        final Point prevPos = new Point(pos.x + dx, pos.y + dy);
        final Point startPos = new Point();
        assert mapGrid != null;
        final boolean startSelection = (mapGrid.getFlags(prevPos) & MapGrid.GRID_FLAG_SELECTION) > 0;
        assert !startSelection;
        boolean isStart = true;
        for (int i = 0; i < len; i++) {
            assert mapGrid != null;
            final boolean thisSelection = (mapGrid.getFlags(pos) & MapGrid.GRID_FLAG_SELECTION) > 0;
            assert mapGrid != null;
            mapGrid.select(prevPos, thisSelection ? SelectionMode.ADD : SelectionMode.SUB);
            gameObjectsToInsert.clear();
            if (thisSelection) {
                if (isStart) {
                    isStart = false;
                    startPos.setLocation(prevPos);
                    // [startGameObjects] = [prevPos]
                    assert startGameObjects.isEmpty();
                    for (final G gameObject : mapModel.getMapSquare(prevPos)) {
                        if (gameObject.isHead() && !gameObject.isInContainer() && mapViewSettings.isEditType(gameObject)) {
                            startGameObjects.add(gameObject);
                            gameObjectsToDelete.add(gameObject);
                        }
                    }
                }
                // [prevPos] = [pos]
                for (final G gameObject : mapModel.getMapSquare(pos)) {
                    if (gameObject.isHead() && !gameObject.isInContainer() && mapViewSettings.isEditType(gameObject)) {
                        gameObjectsToInsert.add(gameObject);
                        gameObjectsToDelete.add(gameObject);
                    }
                }
            } else {
                if (isStart) {
                    // ignore
                } else {
                    isStart = true;
                    // [prevPos] = [startGameObjects]
                    gameObjectsToInsert.addAll(startGameObjects);
                    startGameObjects.clear();
                }
            }

            insertAllAndClear(gameObjectsToInsert, prevPos);

            while (!gameObjectsToDelete.isEmpty()) {
                gameObjectsToDelete.remove(0).remove();
            }

            prevPos.setLocation(pos);
            pos.x -= dx;
            pos.y -= dy;
        }
        if (!isStart) {
            // [prevPos] = [startGameObjects]
            insertAllAndClear(startGameObjects, prevPos);
        }
        assert startGameObjects.isEmpty();
        assert mapGrid != null;
        mapGrid.select(prevPos, startSelection ? SelectionMode.ADD : SelectionMode.SUB);
    }

    /**
     * Inserts a collection of {@link GameObject GameObjects} into the map and
     * clears the list.
     * @param gameObjects the game objects
     * @param point the insertion point
     */
    private void insertAllAndClear(@NotNull final Collection<G> gameObjects, @NotNull final Point point) {
        for (final G gameObject : gameObjects) {
            assert gameObject.isHead() && !gameObject.isInContainer() && mapViewSettings.isEditType(gameObject);
            mapModel.insertBaseObject(gameObject, point, true, false, insertionModeSet.getTopmostInsertionMode());
        }
        gameObjects.clear();
    }

}
