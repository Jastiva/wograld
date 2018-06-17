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

package net.sf.gridarta.gui.copybuffer;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import net.sf.gridarta.gui.map.mapview.MapView;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.baseobject.BaseObject;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.gameobject.GameObjectFactory;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.maparchobject.MapArchObjectFactory;
import net.sf.gridarta.model.mapmodel.InsertionModeSet;
import net.sf.gridarta.model.mapmodel.MapModel;
import net.sf.gridarta.model.mapmodel.MapModelFactory;
import net.sf.gridarta.model.mapmodel.MapModelListener;
import net.sf.gridarta.model.mapmodel.MapSquare;
import net.sf.gridarta.model.mapviewsettings.MapViewSettings;
import net.sf.gridarta.utils.MathUtils;
import net.sf.gridarta.utils.Size2D;
import org.jetbrains.annotations.NotNull;

/**
 * Common base implementation of CopyBuffer. The CopyBuffer is responsible for
 * cut, copy, paste and related operations like filling. It is backed by an
 * invisible MapModel.
 * @author <a href="mailto:andi.vogl@gmx.net">Andreas Vogl</a>
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 * @author Andreas Kirschbaum
 */
public class CopyBuffer<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> {

    /**
     * The map view settings instance.
     */
    @NotNull
    private final MapViewSettings mapViewSettings;

    /**
     * Internal map model to store the cut / copied game objects.
     */
    @NotNull
    private final MapModel<G, A, R> mapModel;

    /**
     * The {@link GameObjectFactory} to use.
     */
    @NotNull
    private final GameObjectFactory<G, A, R> gameObjectFactory;

    /**
     * The {@link InsertionModeSet} to use.
     */
    @NotNull
    private final InsertionModeSet<G, A, R> insertionModeSet;

    /**
     * Create the copy buffer.
     * @param mapViewSettings the map view settings instance
     * @param gameObjectFactory the game object factory to use
     * @param mapArchObjectFactory the map arch object factory to use
     * @param mapModelFactory the map model factory to use
     * @param insertionModeSet the insertion mode set to use
     */
    public CopyBuffer(@NotNull final MapViewSettings mapViewSettings, @NotNull final GameObjectFactory<G, A, R> gameObjectFactory, @NotNull final MapArchObjectFactory<A> mapArchObjectFactory, @NotNull final MapModelFactory<G, A, R> mapModelFactory, @NotNull final InsertionModeSet<G, A, R> insertionModeSet) {
        this.mapViewSettings = mapViewSettings;
        final A mapArchObject = mapArchObjectFactory.newMapArchObject(false);
        mapArchObject.setMapName("cb");
        mapModel = mapModelFactory.newMapModel(mapArchObject);
        this.gameObjectFactory = gameObjectFactory;
        this.insertionModeSet = insertionModeSet;
    }

    /**
     * Adds a {@link MapModelListener} to be notified about changes of the
     * cut/copied game objects.
     * @param listener the listener
     */
    public void addMapModelListener(@NotNull final MapModelListener<G, A, R> listener) {
        mapModel.addMapModelListener(listener);
    }

    /**
     * Removes a {@link MapModelListener} to be notified about changes of the
     * cut/copied game objects.
     * @param listener the listener
     */
    public void removeMapModelListener(@NotNull final MapModelListener<G, A, R> listener) {
        mapModel.removeMapModelListener(listener);
    }

    /**
     * Executing the Clear command.
     * @param mapView the map view to clear
     * @param selectedRec the rectangle to operate on
     */
    public void clear(@NotNull final MapView<G, A, R> mapView, @NotNull final Rectangle selectedRec) {
        copyNCut(mapView, selectedRec, CopyMode.DO_CLEAR);
    }

    /**
     * Executing the Cut command.
     * @param mapView the map view to cut from
     * @param selectedRec the rectangle to operate on
     */
    public void cut(@NotNull final MapView<G, A, R> mapView, @NotNull final Rectangle selectedRec) {
        copyNCut(mapView, selectedRec, CopyMode.DO_CUT);
    }

    /**
     * Executing the Copy command.
     * @param mapView the map view to copy from
     * @param selectedRec the rectangle to operate on
     */
    public void copy(@NotNull final MapView<G, A, R> mapView, @NotNull final Rectangle selectedRec) {
        copyNCut(mapView, selectedRec, CopyMode.DO_COPY);
    }

    /**
     * copyNCut implements clear, cut and copy in one function (since they are
     * so similar).
     * @param mapView the map view to operate on
     * @param selectedRec the rectangle to operate on
     * @param copyMode defines if we have a cut, copy or paste action
     */
    private void copyNCut(@NotNull final MapView<G, A, R> mapView, @NotNull final Rectangle selectedRec, @NotNull final CopyMode copyMode) {
        mapModel.beginTransaction("Cut / Clear");
        try {
            copyMode.prepare(mapModel, new Size2D(selectedRec.width, selectedRec.height));

            final MapModel<G, A, R> mapModel2 = mapView.getMapControl().getMapModel();
            mapModel2.beginTransaction("Cut / Clear"); // TODO: I18N/L10N
            try {
                final Collection<G> gameObjectsToDelete = new HashSet<G>();
                final Point pos = new Point();
                for (final MapSquare<G, A, R> square : mapView.getSelectedSquares()) {
                    square.getMapLocation(pos, -selectedRec.x, -selectedRec.y);
                    for (final G gameObject : square) {
                        copyMode.process(mapModel, gameObject, mapViewSettings.isEditType(gameObject), gameObjectsToDelete, pos, gameObjectFactory, insertionModeSet);
                    }
                }

                for (final GameObject<G, A, R> gameObject : gameObjectsToDelete) {
                    gameObject.remove();
                }
            } finally {
                mapModel2.endTransaction();
            }
        } finally {
            mapModel.endTransaction();
        }
    }

    /**
     * Executing the Paste command.
     * @param mapView the map view to paste on
     * @param startLocation the location to paste into
     */
    public void paste(@NotNull final MapView<G, A, R> mapView, @NotNull final Point startLocation) {
        final MapModel<G, A, R> mapModel2 = mapView.getMapControl().getMapModel();
        final MapArchObject<A> mapArchObject = mapModel2.getMapArchObject();
        mapModel2.beginTransaction("Paste"); // TODO: I18N/L10N
        try {
            final Point pos = new Point();
            for (final MapSquare<G, A, R> square : mapModel) {
                square.getMapLocation(pos);
                pos.translate(startLocation.x, startLocation.y);
                if (mapArchObject.isPointValid(pos)) {
                    for (final BaseObject<G, A, R, ?> gameObject : square) {
                        if (!gameObject.isMulti()) {
                            mapModel2.insertBaseObject(gameObject, pos, true, false, insertionModeSet.getTopmostInsertionMode());
                        }
                    }
                }
            }

            for (final MapSquare<G, A, R> square : mapModel) {
                square.getMapLocation(pos);
                pos.translate(startLocation.x, startLocation.y);
                if (mapArchObject.isPointValid(pos)) {
                    for (final BaseObject<G, A, R, ?> gameObject : square) {
                        if (gameObject.isMulti()) {
                            mapModel2.insertBaseObject(gameObject, pos, true, false, insertionModeSet.getTopmostInsertionMode());
                        }
                    }
                }
            }
        } finally {
            mapModel2.endTransaction();
        }
    }

    /**
     * Executing the Paste Tiled command.
     * @param mapView the map view to paste on
     * @param selectedSquares the square to paste into
     * @param origin the origin where to start pasting
     */
    public void pasteTiled(@NotNull final MapView<G, A, R> mapView, @NotNull final Iterable<MapSquare<G, A, R>> selectedSquares, @NotNull final Point origin) {
        final Point sourcePoint = new Point();
        final Point destinationPoint = new Point();
        final MapModel<G, A, R> mapModel2 = mapView.getMapControl().getMapModel();
        final Size2D mapModelSize = mapModel.getMapArchObject().getMapSize();
        mapModel2.beginTransaction("Paste"); // TODO: I18N/L10N
        try {
            for (final MapSquare<G, A, R> destinationMapSquare : selectedSquares) {
                sourcePoint.x = MathUtils.mod(destinationMapSquare.getMapX() - origin.x, mapModelSize.getWidth());
                sourcePoint.y = MathUtils.mod(destinationMapSquare.getMapY() - origin.y, mapModelSize.getHeight());
                final MapSquare<G, A, R> sourceMapSquare = mapModel.getMapSquare(sourcePoint);
                for (final BaseObject<G, A, R, ?> gameObject : sourceMapSquare) {
                    if (!gameObject.isMulti()) {
                        destinationPoint.x = destinationMapSquare.getMapX();
                        destinationPoint.y = destinationMapSquare.getMapY();
                        mapModel2.insertBaseObject(gameObject, destinationPoint, true, false, insertionModeSet.getTopmostInsertionMode());
                    }
                }
            }

            for (final MapSquare<G, A, R> destinationMapSquare : selectedSquares) {
                sourcePoint.x = MathUtils.mod(destinationMapSquare.getMapX() - origin.x, mapModelSize.getWidth());
                sourcePoint.y = MathUtils.mod(destinationMapSquare.getMapY() - origin.y, mapModelSize.getHeight());
                final MapSquare<G, A, R> sourceMapSquare = mapModel.getMapSquare(sourcePoint);
                for (final BaseObject<G, A, R, ?> gameObject : sourceMapSquare) {
                    if (gameObject.isMulti()) {
                        destinationPoint.x = destinationMapSquare.getMapX();
                        destinationPoint.y = destinationMapSquare.getMapY();
                        mapModel2.insertBaseObject(gameObject, destinationPoint, true, false, insertionModeSet.getTopmostInsertionMode());
                    }
                }
            }
        } finally {
            mapModel2.endTransaction();
        }
    }

    /**
     * Return all game objects. Only top-level head parts are returned; tail
     * parts are ignored as are objects in inventories.
     * @return all game objects
     */
    @NotNull
    public List<G> getAllGameObjects() {
        return mapModel.getAllGameObjects();
    }

    /**
     * Returns whether this copy buffer contains any game objects.
     * @return whether any game objects exist
     */
    public boolean isEmpty() {
        return mapModel.isEmpty();
    }

}
