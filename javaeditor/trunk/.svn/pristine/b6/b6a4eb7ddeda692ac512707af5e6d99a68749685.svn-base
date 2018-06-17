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
import java.io.File;
import java.io.IOException;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapcontrol.MapControl;
import net.sf.gridarta.model.mapmanager.FileControl;
import net.sf.gridarta.model.mapmanager.MapManager;
import net.sf.gridarta.model.mapmanager.MapManagerListener;
import net.sf.gridarta.model.mapviewsettings.MapViewSettings;
import net.sf.gridarta.model.mapviewsettings.MapViewSettingsListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Stores all existing {@link MapView MapViews}.
 * @author Andreas Kirschbaum
 */
public class MapViewsManager<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> {

    /**
     * The {@link MapManager} to use.
     */
    @NotNull
    private final MapManager<G, A, R> mapManager;

    /**
     * The {@link FileControl} to use.
     */
    @NotNull
    private FileControl<G, A, R> fileControl;

    /**
     * The {@link MapViewFactory} to use.
     */
    @NotNull
    private final MapViewFactory<G, A, R> mapViewFactory;

    /**
     * All existing {@link MapView MapViews}. Maps {@link MapControl} instance
     * to {@link MapViewsManager} showing this instance.
     */
    @NotNull
    private final Map<MapControl<G, A, R>, MapViews<G, A, R>> mapViews = new IdentityHashMap<MapControl<G, A, R>, MapViews<G, A, R>>();

    /**
     * The listener tracking alpha types changes to repaint map views after
     * changes.
     */
    private final MapViewSettingsListener mapViewSettingsListener = new MapViewSettingsListener() {

        @Override
        public void gridVisibleChanged(final boolean gridVisible) {
            // ignore
        }

        @Override
        public void lightVisibleChanged(final boolean lightVisible) {
            // ignore
        }

        @Override
        public void smoothingChanged(final boolean smoothing) {
            // ignore
        }

        @Override
        public void doubleFacesChanged(final boolean doubleFaces) {
            // ignore
        }

        @Override
        public void alphaTypeChanged(final int alphaType) {
            // XXX THIS IS A BAD HACK
            addEditType(alphaType);
        }

        @Override
        public void editTypeChanged(final int editType) {
            addEditType(editType);
        }

        @Override
        public void autojoinChanged(final boolean autojoin) {
            // ignore
        }

    };

    /**
     * The {@link MapManagerListener} attached to {@link #mapManager}.
     */
    @NotNull
    private final MapManagerListener<G, A, R> mapManagerListener = new MapManagerListener<G, A, R>() {

        @Override
        public void currentMapChanged(@Nullable final MapControl<G, A, R> mapControl) {
        }

        @Override
        public void mapCreated(@NotNull final MapControl<G, A, R> mapControl, final boolean interactive) {
        }

        @Override
        public void mapClosing(@NotNull final MapControl<G, A, R> mapControl) {
            getMapViewsInt(mapControl).closeAllViews();
        }

        @Override
        public void mapClosed(@NotNull final MapControl<G, A, R> mapControl) {
            assert getMapViews(mapControl) <= 0;
            mapViews.remove(mapControl);
        }

    };

    /**
     * Creates a new instance.
     * @param mapViewSettings the map view settings instance
     * @param mapViewFactory the map view factory to use
     * @param mapManager the map manager to use
     * @param pickmapManager the pickmap manager to use
     */
    public MapViewsManager(@NotNull final MapViewSettings mapViewSettings, @NotNull final MapViewFactory<G, A, R> mapViewFactory, @NotNull final MapManager<G, A, R> mapManager, @NotNull final MapManager<G, A, R> pickmapManager) {
        this.mapViewFactory = mapViewFactory;
        this.mapManager = mapManager;
        mapViewSettings.addMapViewSettingsListener(mapViewSettingsListener);
        mapManager.addMapManagerListener(mapManagerListener);
        pickmapManager.addMapManagerListener(mapManagerListener);
    }

    @Deprecated
    public void setFileControl(@NotNull final FileControl<G, A, R> fileControl) {
        this.fileControl = fileControl;
    }

    /**
     * Adds a {@link MapViewsListener} to be notified of events.
     * @param mapControl the map control to attach to
     * @param listener the listener
     */
    public void addMapViewsListener(@NotNull final MapControl<G, A, R> mapControl, @NotNull final MapViewsListener<G, A, R> listener) {
        getMapViewsInt(mapControl).addMapViewsListener(listener);
    }

    /**
     * Removes a {@link MapViewsListener} to be notified of events.
     * @param mapControl the map control to attach to
     * @param listener the listener
     */
    public void removeMapViewsListener(@NotNull final MapControl<G, A, R> mapControl, @NotNull final MapViewsListener<G, A, R> listener) {
        getMapViewsInt(mapControl).removeMapViewsListener(listener);
    }

    /**
     * Returns the last used view of a {@link MapControl}.
     * @param mapControl the map control
     * @return the last used view
     */
    @Nullable
    public MapView<G, A, R> getMapViewFrame(@NotNull final MapControl<G, A, R> mapControl) {
        return getMapViewsInt(mapControl).getMapViewFrame();
    }

    /**
     * Returns the number of views of a {@link MapControl}.
     * @param mapControl the map control
     * @return the number of views
     */
    public int getMapViews(@NotNull final MapControl<G, A, R> mapControl) {
        return getMapViewsInt(mapControl).getMapViews();
    }

    /**
     * Creates a new map view.
     * @param mapControl the map control
     * @param viewPosition the view position to show initially; may be
     * <code>null</code> to show the top left corner
     * @param centerSquare the map square that should be in the center of the
     * view; ignored if <code>viewPosition</code> is set
     * @return the map view
     */
    @NotNull
    public MapView<G, A, R> newMapView(@NotNull final MapControl<G, A, R> mapControl, @Nullable final Point viewPosition, @Nullable final Point centerSquare) {
        return getMapViewsInt(mapControl).newMapView(mapControl, viewPosition, centerSquare, mapViewFactory);
    }

    /**
     * Closes a view of a {@link MapControl}.
     * @param mapView the view to be closed
     */
    public void closeView(@NotNull final MapView<G, A, R> mapView) {
        getMapViewsInt(mapView.getMapControl()).closeView(mapView);
    }

    /**
     * Sets a {@link MapView} as the main view.
     * @param mapView the map view
     */
    public void setFocus(@NotNull final MapView<G, A, R> mapView) {
        getMapViewsInt(mapView.getMapControl()).setFocus(mapView);
    }

    /**
     * Returns an {@link Iterator} returning all {@link MapView MapViews} of a
     * {@link MapControl}. The map views are returns top to bottom.
     * @param mapControl the map control
     * @return the iterator
     */
    public Iterator<MapView<G, A, R>> getMapViewIterator(@NotNull final MapControl<G, A, R> mapControl) {
        return getMapViewsInt(mapControl).getMapViewIterator();
    }

    /**
     * Repaints all {@link MapView MapViews} of a {@link MapControl}.
     * @param mapControl the map control
     */
    public void repaintAllViews(@NotNull final MapControl<G, A, R> mapControl) {
        getMapViewsInt(mapControl).repaintAllViews();
    }

    /**
     * Returns the {@link MapViews} instance for a {@link MapControl}.
     * @param mapControl the map control
     * @return the map views
     */
    @NotNull
    private MapViews<G, A, R> getMapViewsInt(@NotNull final MapControl<G, A, R> mapControl) {
        final MapViews<G, A, R> existingMapViews = mapViews.get(mapControl);
        if (existingMapViews != null) {
            return existingMapViews;
        }

        final MapViews<G, A, R> newMapViews = new MapViews<G, A, R>();
        mapViews.put(mapControl, newMapViews);
        return newMapViews;
    }

    /**
     * Create a new map control and an initial map view.
     * @param objects the game objects to insert; may be <code>null</code>
     * @param mapArchObject the map arch object for the new map
     * @param viewPosition the view position to show initially; may be
     * <code>null</code> to show the top left corner
     * @param file the map file, or <code>null</code> if the map was not yet
     * saved
     * @return the newly created map view
     */
    @NotNull
    public MapView<G, A, R> newMapWithView(@Nullable final List<G> objects, @NotNull final A mapArchObject, @Nullable final Point viewPosition, @Nullable final File file) {
        final MapControl<G, A, R> mapControl = mapManager.newMap(objects, mapArchObject, file, true);
        try {
            return newMapView(mapControl, viewPosition, null);
        } finally {
            mapManager.release(mapControl);
        }
    }

    /**
     * Invoked when the user wants to close a map view. If there is only 1 view
     * left the map will be closed.
     * @param mapView the map view to close
     */
    public void closeMapView(@NotNull final MapView<G, A, R> mapView) {
        final MapControl<G, A, R> mapControl = mapView.getMapControl();
        if (getMapViews(mapControl) <= 1) {
            if (!fileControl.confirmSaveChanges(mapControl)) {
                return;
            }
            closeView(mapView);
            assert mapControl.getUseCounter() <= 0;
            mapManager.closeMap(mapControl);
        } else {
            closeView(mapView);
            assert mapControl.getUseCounter() > 0;
        }
    }

    /**
     * Load a map file and create a map view.
     * @param file the map file to load
     * @param viewPosition the view position to show initially; may be
     * <code>null</code> to show the top left corner
     * @param centerSquare the map square that should be in the center of the
     * view; ignored if <code>viewPosition</code> is set
     * @return the map view
     * @throws IOException if an I/O error occurs
     */
    @NotNull
    public MapView<G, A, R> openMapFileWithView(@NotNull final File file, @Nullable final Point viewPosition, @Nullable final Point centerSquare) throws IOException {
        final MapControl<G, A, R> mapControl = mapManager.openMapFile(file, true);
        try {
            return newMapView(mapControl, viewPosition, centerSquare);
        } finally {
            mapManager.release(mapControl);
        }
    }

    /**
     * Refreshes all map views. Does nothing if no maps are opened.
     */
    public void refreshAllMaps() {
        for (final MapControl<G, A, R> mapControl : mapManager.getOpenedMaps()) {
            repaintAllViews(mapControl);
        }
    }

    /**
     * Calculate a new type for all opened maps.
     * @param newType the new type to add
     */
    public void addEditType(final int newType) {
        for (final MapControl<G, A, R> mapControl : mapManager.getOpenedMaps()) {
            mapControl.getMapModel().addActiveEditType(newType); // calculate new type
        }
        refreshAllMaps();
    }

}
