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

package net.sf.gridarta.gui.mapuserlistener;

import java.util.IdentityHashMap;
import java.util.Map;
import net.sf.gridarta.gui.map.mapview.MapView;
import net.sf.gridarta.gui.map.mapview.MapViewsListener;
import net.sf.gridarta.gui.map.mapview.MapViewsManager;
import net.sf.gridarta.gui.panel.tools.ToolPalette;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapcontrol.MapControl;
import net.sf.gridarta.model.mapmanager.MapManager;
import net.sf.gridarta.model.mapmanager.MapManagerListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Tracks map views and attaches/detaches {@link MapMouseListener} instances.
 * @author Andreas Kirschbaum
 */
public class MapUserListenerManager<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> {

    /**
     * The {@link ToolPalette} to attach to.
     */
    @NotNull
    private final ToolPalette<G, A, R> toolPalette;

    /**
     * The {@link MapViewsManager}.
     */
    @NotNull
    private final MapViewsManager<G, A, R> mapViewsManager;

    /**
     * Maps {@link MapView} instance to attached {@link MapMouseListener}.
     */
    @NotNull
    private final Map<MapView<G, A, R>, MapMouseListener<G, A, R>> mapUserListeners = new IdentityHashMap<MapView<G, A, R>, MapMouseListener<G, A, R>>();

    /**
     * The {@link MapManagerListener} for tracking {@link MapControl}
     * instances.
     */
    @NotNull
    private final MapManagerListener<G, A, R> mapManagerListener = new MapManagerListener<G, A, R>() {

        @Override
        public void currentMapChanged(@Nullable final MapControl<G, A, R> mapControl) {
            // ignore
        }

        @Override
        public void mapCreated(@NotNull final MapControl<G, A, R> mapControl, final boolean interactive) {
            mapViewsManager.addMapViewsListener(mapControl, mapViewsListener);
        }

        @Override
        public void mapClosing(@NotNull final MapControl<G, A, R> mapControl) {
            // ignore
        }

        @Override
        public void mapClosed(@NotNull final MapControl<G, A, R> mapControl) {
            mapViewsManager.removeMapViewsListener(mapControl, mapViewsListener);
        }

    };

    /**
     * The {@link MapViewsListener} attached to all {@link MapControl}
     * instances.
     */
    @NotNull
    private final MapViewsListener<G, A, R> mapViewsListener = new MapViewsListener<G, A, R>() {

        @Override
        public void mapViewCreated(@NotNull final MapView<G, A, R> mapView) {
            final MapMouseListener<G, A, R> mapMouseListener = new MapMouseListener<G, A, R>(mapView.getRenderer(), toolPalette, mapView);
            mapUserListeners.put(mapView, mapMouseListener);
        }

        @Override
        public void mapViewClosing(@NotNull final MapView<G, A, R> mapView) {
            final MapMouseListener<G, A, R> mapMouseListener = mapUserListeners.remove(mapView);
            assert mapMouseListener != null;
            mapMouseListener.closeNotify();
        }

    };

    /**
     * Creates a new instance.
     * @param toolPalette the tool palette to attach to
     * @param mapViewsManager the map views
     */
    public MapUserListenerManager(@NotNull final ToolPalette<G, A, R> toolPalette, @NotNull final MapViewsManager<G, A, R> mapViewsManager) {
        this.toolPalette = toolPalette;
        this.mapViewsManager = mapViewsManager;
    }

    /**
     * Registers a {@link MapManager} to track.
     * @param mapManager the map manager to track
     */
    public void addMapManager(@NotNull final MapManager<G, A, R> mapManager) {
        mapManager.addMapManagerListener(mapManagerListener);
    }

}
