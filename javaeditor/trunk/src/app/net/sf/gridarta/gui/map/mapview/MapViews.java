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

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.swing.JScrollPane;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapcontrol.MapControl;
import net.sf.gridarta.utils.EventListenerList2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A list of {@link MapView MapViews} for one {@link MapControl} instance.
 * @author Andreas Kirschbaum
 */
public class MapViews<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> {

    /**
     * The registered {@link MapViewsListener MapViewsListeners}.
     */
    @NotNull
    private final EventListenerList2<MapViewsListener<G, A, R>> listeners = new EventListenerList2<MapViewsListener<G, A, R>>(MapViewsListener.class);

    /**
     * The list contains all views of the map. Maps opened in the editor have
     * one or more views; if the last view is closed, the map is closed, too.
     * Maps opened by scripts or used internally may have zero or more views.
     * Pickmaps always have exactly one view.
     */
    @NotNull
    private final List<MapView<G, A, R>> mapViews = new ArrayList<MapView<G, A, R>>();

    /**
     * The map view counter for the next created {@link MapView}.
     */
    private int nextMapViewCounter;

    /**
     * Adds a {@link MapViewsListener} to be notified of events.
     * @param listener the listener
     */
    public void addMapViewsListener(@NotNull final MapViewsListener<G, A, R> listener) {
        listeners.add(listener);
    }

    /**
     * Removes a {@link MapViewsListener} to be notified of events.
     * @param listener the listener
     */
    public void removeMapViewsListener(@NotNull final MapViewsListener<G, A, R> listener) {
        listeners.remove(listener);
    }

    /**
     * Returns the last used view of a {@link MapControl}.
     * @return the last used view
     */
    @Nullable
    public MapView<G, A, R> getMapViewFrame() {
        return mapViews.isEmpty() ? null : mapViews.get(0);
    }

    /**
     * Returns the number of views of a {@link MapControl}.
     * @return the number of views
     */
    public int getMapViews() {
        return mapViews.size();
    }

    /**
     * Creates a new {@link MapView}.
     * @param mapControl the map control new map view is part of
     * @param viewPosition the view position to show initially; may be
     * <code>null</code> to show the top left corner
     * @param centerSquare the map square that should be in the center of the
     * view; ignored if <code>viewPosition</code> is set
     * @param mapViewFactory the map view factory to use
     * @return the map view
     */
    @NotNull
    public MapView<G, A, R> newMapView(@NotNull final MapControl<G, A, R> mapControl, @Nullable final Point viewPosition, @Nullable final Point centerSquare, @NotNull final MapViewFactory<G, A, R> mapViewFactory) {
        mapControl.acquire();
        try {
            final MapView<G, A, R> mapView = mapViewFactory.newMapView(mapControl, viewPosition, ++nextMapViewCounter);
            mapViews.add(mapView);
            mapControl.acquire();
            for (final MapViewsListener<G, A, R> listener : listeners.getListeners()) {
                listener.mapViewCreated(mapView);
            }
            if (viewPosition == null && centerSquare != null) {
                final Rectangle squareBounds = mapView.getRenderer().getSquareBounds(centerSquare);
                final JScrollPane scrollPane = mapView.getScrollPane();
                final Dimension extentSize = scrollPane.getViewport().getExtentSize();
                final Point centerPoint = new Point(Math.max(0, squareBounds.x + squareBounds.width / 2 - extentSize.width / 2), Math.max(0, squareBounds.y + squareBounds.height / 2 - extentSize.height / 2));
                scrollPane.getViewport().setViewPosition(centerPoint);
            }
            return mapView;
        } finally {
            mapControl.release();
        }
    }

    /**
     * Returns the current view positions of all views of a {@link MapControl}.
     * @return the current view positions of all views
     */
    @NotNull
    public Point[] getViewPositions() {
        final Point[] result = new Point[mapViews.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = mapViews.get(i).getScrollPane().getViewport().getViewPosition();
        }
        return result;
    }

    /**
     * Closes a view of a {@link MapControl}.
     * @param mapView the view to be closed
     */
    public void closeView(@NotNull final MapView<G, A, R> mapView) {
        for (final MapViewsListener<G, A, R> listener : listeners.getListeners()) {
            listener.mapViewClosing(mapView);
        }
        mapView.closeNotify();
        mapViews.remove(mapView);
        mapView.getMapControl().release();
    }

    /**
     * Closes all views of a {@link MapControl}.
     */
    public void closeAllViews() {
        while (!mapViews.isEmpty()) {
            closeView(mapViews.get(0));
        }
    }

    /**
     * Sets a {@link MapView} as the main view.
     * @param mapView the map view
     */
    public void setFocus(@NotNull final MapView<G, A, R> mapView) {
        assert mapViews.contains(mapView);
        mapViews.remove(mapView);
        mapViews.add(0, mapView);
    }

    /**
     * Returns an {@link Iterator} returning all {@link MapView MapViews}. The
     * map views are returns top to bottom.
     * @return the iterator
     */
    @NotNull
    public Iterator<MapView<G, A, R>> getMapViewIterator() {
        return Collections.unmodifiableList(mapViews).iterator();
    }

    /**
     * Repaints all {@link MapView MapViews}.
     */
    public void repaintAllViews() {
        for (final MapView<G, A, R> mapView : mapViews) {
            mapView.getRenderer().forceRepaint();
        }
    }

}
