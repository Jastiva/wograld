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

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import net.sf.gridarta.gui.map.renderer.AbstractMapRenderer;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.baseobject.BaseObject;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapgrid.MapGrid;
import net.sf.gridarta.model.mapmodel.MapModel;
import net.sf.gridarta.model.mapmodel.MapModelListener;
import net.sf.gridarta.model.mapmodel.MapSquare;
import net.sf.gridarta.model.validation.ErrorCollector;
import net.sf.gridarta.model.validation.errors.ValidationError;
import net.sf.gridarta.utils.Size2D;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Tracks a {@link MapModel} for changed erroneous map squares and updates a
 * {@link MapGrid} and an {@link AbstractMapRenderer} accordingly.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 * @author Andreas Kirschbaum
 */
public class ErroneousMapSquares<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> {

    /**
     * The {@link MapModel} to track.
     */
    @NotNull
    private final MapModel<G, A, R> mapModel;

    /**
     * The {@link MapGrid} to mark.
     */
    @NotNull
    private final MapGrid mapGrid;

    /**
     * The {@link AbstractMapRenderer} to notify.
     */
    @NotNull
    private final AbstractMapRenderer<G, A, R> renderer;

    /**
     * The erroneous {@link MapSquare MapSquares}.
     */
    @NotNull
    private final Map<MapSquare<G, A, R>, ValidationError<G, A, R>> erroneousMapSquares = new HashMap<MapSquare<G, A, R>, ValidationError<G, A, R>>();

    /**
     * The {@link MapModelListener} attached to {@link #mapModel}.
     */
    @NotNull
    private final MapModelListener<G, A, R> mapModelListener = new MapModelListener<G, A, R>() {

        @Override
        public void mapSizeChanged(@NotNull final Size2D newSize) {
            mapGrid.resize(newSize);
        }

        @Override
        public void mapSquaresChanged(@NotNull final Set<MapSquare<G, A, R>> mapSquares) {
            // ignore
        }

        @Override
        public void mapObjectsChanged(@NotNull final Set<G> gameObjects, @NotNull final Set<G> transientGameObjects) {
            // ignore
        }

        @Override
        public void errorsChanged(@NotNull final ErrorCollector<G, A, R> errors) {
            ErroneousMapSquares.this.errorsChanged(errors);
        }

        @Override
        public void mapFileChanged(@Nullable final File oldMapFile) {
            // ignore
        }

        @Override
        public void modifiedChanged() {
            // ignore
        }

    };

    /**
     * Creates a new instance.
     * @param mapModel the map model to track
     * @param mapGrid the map grid to mark
     * @param renderer the renderer to notify
     */
    public ErroneousMapSquares(@NotNull final MapModel<G, A, R> mapModel, @NotNull final MapGrid mapGrid, @NotNull final AbstractMapRenderer<G, A, R> renderer) {
        this.mapModel = mapModel;
        this.mapGrid = mapGrid;
        this.renderer = renderer;
        this.renderer.setErroneousMapSquares(erroneousMapSquares);
        this.mapModel.addMapModelListener(mapModelListener);
    }

    /**
     * Must be called when this instance is not used anymore. It un-registers
     * all listeners.
     */
    public void closeNotify() {
        mapModel.removeMapModelListener(mapModelListener);
    }

    /**
     * Updates the erroneous map squares.
     * @param errors the errors to display
     */
    public void errorsChanged(@NotNull final ErrorCollector<G, A, R> errors) {
        erroneousMapSquares.clear();
        mapGrid.beginTransaction();
        try {
            mapGrid.clearErrors();
            for (final ValidationError<G, A, R> validationError : errors.getErrors()) {
                for (final MapSquare<G, A, R> mapSquare : validationError.getMapSquares()) {
                    erroneousMapSquares.put(mapSquare, validationError);
                    mapGrid.setError(mapSquare.getMapX(), mapSquare.getMapY());
                }
                for (final G gameObject : validationError.getGameObjects()) {
                    final BaseObject<G, A, R, ?> topContainer = gameObject.getTopContainer();
                    mapGrid.setError(topContainer.getMapX(), topContainer.getMapY());
                }
            }
        } finally {
            mapGrid.endTransaction();
        }
        renderer.setErroneousMapSquares(erroneousMapSquares);
    }

}
