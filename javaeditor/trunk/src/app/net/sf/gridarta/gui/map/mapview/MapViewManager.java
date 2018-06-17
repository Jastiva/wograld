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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Maintains all map views.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 * @author Andreas Kirschbaum
 */
public class MapViewManager<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> implements Iterable<MapView<G, A, R>> {

    /**
     * All open map views as a list. The first entry is the top-most map view,
     * the last one is the bottom-most map view.
     */
    @NotNull
    private final List<MapView<G, A, R>> mapViewsList = Collections.synchronizedList(new ArrayList<MapView<G, A, R>>());

    /**
     * The active map view. Set to <code>null</code> only if no map view is
     * open.
     */
    @Nullable
    private MapView<G, A, R> activeMapView;

    /**
     * The registered {@link MapViewManagerListener MapViewManagerListeners}.
     */
    @NotNull
    private final Collection<MapViewManagerListener<G, A, R>> listeners = new ArrayList<MapViewManagerListener<G, A, R>>();

    /**
     * Sets the active map view. The map view will be moved to the front.
     * @param mapView the map view to activate
     */
    @SuppressWarnings("NullableProblems")
    public void setActiveMapView(@NotNull final MapView<G, A, R> mapView) {
        assert mapViewsList.contains(mapView);
        mapViewsList.remove(mapView);
        mapViewsList.add(0, mapView);
        updateActiveMapView();
    }

    /**
     * Removes a map view. Activates another map view if the active map view is
     * removed.
     * @param mapView the map view to remove
     */
    public void removeMapView(@NotNull final MapView<G, A, R> mapView) {
        assert mapViewsList.contains(mapView);
        if (!mapViewsList.contains(mapView)) {
            return;
        }

        mapViewsList.remove(mapView);
        for (final MapViewManagerListener<G, A, R> listener : listeners) {
            listener.mapViewClosing(mapView);
        }
        updateActiveMapView();
    }

    /**
     * Adds a map view. The map view becomes the active map view.
     * @param mapView the map view to add
     */
    public void addMapView(@NotNull final MapView<G, A, R> mapView) {
        assert !mapViewsList.contains(mapView);
        mapViewsList.add(0, mapView);

        for (final MapViewManagerListener<G, A, R> listener : listeners) {
            listener.mapViewCreated(mapView);
        }
    }

    /**
     * Deactivates a map view.
     * @param mapView the map view to deactivate
     */
    public void deactivateMapView(@NotNull final MapView<G, A, R> mapView) {
        assert mapViewsList.contains(mapView);
        if (mapViewsList.size() <= 1) {
            return;
        }

        mapViewsList.remove(mapView);
        mapViewsList.add(mapView);
        updateActiveMapView();
    }

    /**
     * Activates a map view.
     * @param mapView the map view to activate
     */
    public void activateMapView(@NotNull final MapView<G, A, R> mapView) {
        assert mapViewsList.contains(mapView);
        mapViewsList.remove(mapView);
        mapViewsList.add(0, mapView);
        updateActiveMapView();
    }

    /**
     * Returns all opened map views.
     * @return all opened map views; the result cannot be modified
     */
    @NotNull
    @Override
    public Iterator<MapView<G, A, R>> iterator() {
        return Collections.unmodifiableList(mapViewsList).iterator();
    }

    /**
     * Updates the active map view and notifies all listeners of changes.
     */
    private void updateActiveMapView() {
        final MapView<G, A, R> newActiveMapView = mapViewsList.isEmpty() ? null : mapViewsList.get(0);
        if (activeMapView == newActiveMapView) {
            return;
        }

        activeMapView = newActiveMapView;
        for (final MapViewManagerListener<G, A, R> listener : listeners) {
            listener.activeMapViewChanged(activeMapView);
        }
    }

    /**
     * Adds a listener to be notified.
     * @param listener the listener to add
     */
    public void addMapViewManagerListener(@NotNull final MapViewManagerListener<G, A, R> listener) {
        listeners.add(listener);
    }

    /**
     * Removes a listener to be notified.
     * @param listener the listener to remove
     */
    public void removeMapViewManagerListener(@NotNull final MapViewManagerListener<G, A, R> listener) {
        listeners.remove(listener);
    }

    /**
     * Returns the active top map view we are working with.
     * @return the active top map view we are working with or <code>null</code>
     *         if no map view is open
     */
    @Nullable
    public MapView<G, A, R> getActiveMapView() {
        return activeMapView;
    }

    /**
     * Executes the "prev window" action.
     * @param performAction whether the action should be performed
     * @return whether the action was or can be performed
     */
    public boolean doPrevWindow(final boolean performAction) {
        if (mapViewsList.size() <= 1) {
            return false;
        }

        if (performAction) {
            // XXX I might work I might not work, use AWT/Swing focus traversal
            final MapView<G, A, R> mapView = mapViewsList.remove(0);
            mapViewsList.add(mapView);
            updateActiveMapView();
        }

        return true;
    }

    /**
     * Executes the "next window" action.
     * @param performAction whether the action should be performed
     * @return whether the action was or can be performed
     */
    public boolean doNextWindow(final boolean performAction) {
        final int size = mapViewsList.size();
        if (size <= 1) {
            return false;
        }

        if (performAction) {
            // XXX I might work I might not work, use AWT/Swing focus traversal
            final MapView<G, A, R> mapView = mapViewsList.remove(size - 1);
            mapViewsList.add(0, mapView);
            updateActiveMapView();
        }

        return true;
    }

}
