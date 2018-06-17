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

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import net.sf.gridarta.gui.map.mapview.MapView;
import net.sf.gridarta.gui.map.mapview.MapViewManager;
import net.sf.gridarta.gui.map.mapview.MapViewManagerListener;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapcontrol.MapControl;
import net.sf.gridarta.model.mapmanager.MapManager;
import net.sf.gridarta.model.mapmanager.MapManagerListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Tracks key presses and maps them to actions.
 * @author Andreas Kirschbaum
 */
public class MapKeyListener<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> {

    /**
     * The {@link MapView} being tracked for key actions.
     */
    @Nullable
    private MapView<G, A, R> mapView;

    /**
     * Whether the mouse is inside the window.
     */
    private boolean inside;

    /**
     * Whether the CTRL+SHIFT modifiers are pressed.
     */
    private boolean lightVisible;

    /**
     * The {@link KeyListener} attached to {@link #mapView}.
     */
    @NotNull
    private final KeyListener keyListener = new KeyListener() {

        @Override
        public void keyTyped(@NotNull final KeyEvent e) {
            // ignore
        }

        @Override
        public void keyPressed(@NotNull final KeyEvent e) {
            setModifiers(e.getModifiers());
        }

        @Override
        public void keyReleased(@NotNull final KeyEvent e) {
            setModifiers(e.getModifiers());
        }

    };

    /**
     * The {@link MouseListener} attached to {@link #mapView}. Checks the key
     * modifiers whenever the mouse enters or leaves the window.
     */
    @NotNull
    private final MouseListener mouseListener = new MouseListener() {

        @Override
        public void mouseClicked(@NotNull final MouseEvent e) {
            // ignore
        }

        @Override
        public void mousePressed(@NotNull final MouseEvent e) {
            // ignore
        }

        @Override
        public void mouseReleased(@NotNull final MouseEvent e) {
            // ignore
        }

        @Override
        public void mouseEntered(@NotNull final MouseEvent e) {
            setInside(true);
            setModifiers(e.getModifiers());
        }

        @Override
        public void mouseExited(@NotNull final MouseEvent e) {
            setInside(false);
        }

    };

    /**
     * The listener used to detect map view changes.
     * @noinspection FieldCanBeLocal
     */
    @NotNull
    private final MapViewManagerListener<G, A, R> mapViewManagerListener = new MapViewManagerListener<G, A, R>() {

        @Override
        public void mapViewCreated(@NotNull final MapView<G, A, R> mapView) {
            // ignore
        }

        @Override
        public void mapViewClosing(@NotNull final MapView<G, A, R> mapView) {
            // ignore
        }

        @Override
        public void activeMapViewChanged(@Nullable final MapView<G, A, R> mapView) {
            setMapView(mapView);
        }

    };

    /**
     * The {@link MapManagerListener} for tracking {@link MapControl}
     * instances.
     * @noinspection FieldCanBeLocal
     */
    @NotNull
    private final MapManagerListener<G, A, R> mapManagerListener = new MapManagerListener<G, A, R>() {

        @Override
        public void currentMapChanged(@Nullable final MapControl<G, A, R> mapControl) {
            if (mapView != null) {
                // Hack: mapViewManagerListener.activeMapViewChanged() has been called too early
                setInside(mapView.getInternalFrame().getMousePosition() != null);
            }
        }

        @Override
        public void mapCreated(@NotNull final MapControl<G, A, R> mapControl, final boolean interactive) {
            // ignore
        }

        @Override
        public void mapClosing(@NotNull final MapControl<G, A, R> mapControl) {
            // ignore
        }

        @Override
        public void mapClosed(@NotNull final MapControl<G, A, R> mapControl) {
            // ignore
        }

    };

    /**
     * Creates a new instance.
     * @param mapViewManager the map view manager to track for map views
     * @param mapManager the map manager to track
     */
    public MapKeyListener(@NotNull final MapViewManager<G, A, R> mapViewManager, @NotNull final MapManager<G, A, R> mapManager) {
        mapViewManager.addMapViewManagerListener(mapViewManagerListener);
        mapManager.addMapManagerListener(mapManagerListener);
        setMapView(mapViewManager.getActiveMapView());
    }

    /**
     * Sets the {@link #mapView current map view}.
     * @param mapView the new map view to set
     */
    private void setMapView(@Nullable final MapView<G, A, R> mapView) {
        final MapView<G, A, R> prevMapView = this.mapView;
        if (prevMapView != null) {
            prevMapView.getInternalFrame().removeKeyListener(keyListener);
            prevMapView.getRenderer().removeMouseListener(mouseListener);
            prevMapView.getRenderer().setLightVisible(false);
        }
        this.mapView = mapView;
        if (mapView != null) {
            mapView.getInternalFrame().addKeyListener(keyListener);
            mapView.getRenderer().addMouseListener(mouseListener);
            setModifiers(0);
            setInside(mapView.getInternalFrame().getMousePosition() != null);
        } else {
            setModifiers(0);
            setInside(false);
        }
    }

    /**
     * Updates the active modifiers for the current map view.
     * @param modifiers the new modifiers
     */
    private void setModifiers(final int modifiers) {
        final boolean lightVisible = (modifiers & (InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK)) == (InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK);
        if (this.lightVisible != lightVisible) {
            this.lightVisible = lightVisible;
            updateRenderer();
        }
    }

    /**
     * Updates the "mouse is inside the window" flag for the current map view.
     * @param inside whether the mouse is inside the map view
     */
    private void setInside(final boolean inside) {
        if (this.inside != inside) {
            this.inside = inside;
            updateRenderer();
        }
    }

    /**
     * Updates the current map view's renderer settings. Does nothing if no map
     * view is active.
     */
    private void updateRenderer() {
        if (mapView != null) {
            mapView.getRenderer().setLightVisible(inside && lightVisible);
        }
    }

}
