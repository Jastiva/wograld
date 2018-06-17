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

package net.sf.gridarta.gui.map.mapview;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapcursor.MapCursor;
import net.sf.gridarta.model.mapgrid.MapGrid;
import net.sf.gridarta.model.mapmodel.MapModel;
import net.sf.gridarta.model.mapmodel.MapSquare;
import net.sf.gridarta.utils.RandomUtils;
import net.sf.gridarta.utils.Size2D;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Abstract base class for {@link MapView} implementations.
 * @author Andreas Kirschbaum
 */
public abstract class AbstractMapView<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> implements MapView<G, A, R> {

    /**
     * The {@link MapModel} of this map view.
     */
    @NotNull
    private final MapModel<G, A, R> mapModel;

    /**
     * The {@link MapGrid} of this map view.
     */
    @NotNull
    private final MapGrid mapGrid;

    /**
     * The {@link MapCursor} of this map view.
     */
    @NotNull
    private final MapCursor<G, A, R> mapCursor;

    /**
     * Creates a new instance.
     * @param mapModel the map model of this map view
     * @param mapGrid the map grid of this map view
     * @param mapCursor the map cursor of this map view
     */
    protected AbstractMapView(@NotNull final MapModel<G, A, R> mapModel, @NotNull final MapGrid mapGrid, @NotNull final MapCursor<G, A, R> mapCursor) {
        this.mapModel = mapModel;
        this.mapGrid = mapGrid;
        this.mapCursor = mapCursor;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public List<MapSquare<G, A, R>> getSelectedSquares() {
        final Point[] selectedMapSquares = mapGrid.getSelection();
        final List<MapSquare<G, A, R>> selection = new ArrayList<MapSquare<G, A, R>>();
        for (final Point pos : selectedMapSquares) {
            selection.add(mapModel.getMapSquare(pos));
        }
        return selection;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public List<G> getSelectedGameObjects() {
        final Collection<MapSquare<G, A, R>> selectedMapSquares = getSelectedSquares();
        if (selectedMapSquares.isEmpty()) {
            return mapModel.getAllGameObjects();
        }

        final List<G> objects = new ArrayList<G>();
        for (final Iterable<G> mapSquare : selectedMapSquares) {
            for (final GameObject<G, A, R> gameObject : mapSquare) {
                objects.add(gameObject.getHead());
            }
        }
        return objects;
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public G getSelectedGameObject() {
        final List<G> objects = getSelectedGameObjects();
        final int objectSize = objects.size();
        if (objectSize == 0) {
            return null;
        }
        if (objectSize == 1) {
            return objects.get(0);
        }
        return objects.get(RandomUtils.rnd.nextInt(objects.size()));
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public MapGrid getMapGrid() {
        return mapGrid;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public MapCursor<G, A, R> getMapCursor() {
        return mapCursor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCursorLocation(@Nullable final Point point) {
        if (point != null) {
            final Size2D mapSize = getMapControl().getMapModel().getMapArchObject().getMapSize();
            if (point.x >= mapSize.getWidth()) {
                point.x = mapSize.getWidth() - 1;
            } else if (point.x < 0) {
                point.x = 0;
            }
            if (point.y >= mapSize.getHeight()) {
                point.y = mapSize.getHeight() - 1;
            } else if (point.y < 0) {
                point.y = 0;
            }
        }
        mapCursor.setLocation(point);
    }

}
