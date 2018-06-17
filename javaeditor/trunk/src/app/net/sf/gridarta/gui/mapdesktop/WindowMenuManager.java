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

package net.sf.gridarta.gui.mapdesktop;

import javax.swing.Action;
import javax.swing.JMenu;
import net.sf.gridarta.gui.map.mapview.MapView;
import net.sf.gridarta.gui.map.mapview.MapViewManager;
import net.sf.gridarta.gui.map.mapview.MapViewManagerListener;
import net.sf.gridarta.gui.utils.MenuUtils;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Maintains the window menu. Tracks added, removed, and modified windows.
 * @author Andreas Kirschbaum
 */
public class WindowMenuManager<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> {

    /**
     * The "window" menu.
     */
    @NotNull
    private final JMenu menuWindow;

    /**
     * The {@link MapViewManager} to use.
     */
    @NotNull
    private final Iterable<MapView<G, A, R>> mapViewManager;

    /**
     * The action for "close all map windows".
     */
    @NotNull
    private final Action aCloseAll;

    /**
     * The {@link MapDesktop} to use.
     */
    @NotNull
    private final MapDesktop<G, A, R> mapDesktop;

    /**
     * The listener used to detect map view changes.
     */
    @NotNull
    private final MapViewManagerListener<G, A, R> mapViewManagerListener = new MapViewManagerListener<G, A, R>() {

        @Override
        public void mapViewCreated(@NotNull final MapView<G, A, R> mapView) {
            rebuildWindowMenu();
        }

        @Override
        public void mapViewClosing(@NotNull final MapView<G, A, R> mapView) {
            rebuildWindowMenu();
        }

        @Override
        public void activeMapViewChanged(@Nullable final MapView<G, A, R> mapView) {
            rebuildWindowMenu();
        }

    };

    /**
     * Creates a new instance.
     * @param menuWindow the "window" menu
     * @param mapViewManager the map view manager to track
     * @param aCloseAll the action for "close all map windows"
     * @param mapDesktop the map desktop to use
     */
    public WindowMenuManager(@NotNull final JMenu menuWindow, @NotNull final MapViewManager<G, A, R> mapViewManager, @NotNull final Action aCloseAll, @NotNull final MapDesktop<G, A, R> mapDesktop) {
        this.menuWindow = menuWindow;
        this.mapViewManager = mapViewManager;
        this.aCloseAll = aCloseAll;
        this.mapDesktop = mapDesktop;
        mapViewManager.addMapViewManagerListener(mapViewManagerListener);
    }

    /**
     * Rebuild the window menu.
     */
    private void rebuildWindowMenu() {
        MenuUtils.removeAll(menuWindow);
        menuWindow.add(aCloseAll);

        int index = 0;
        for (final MapView<G, A, R> frame : mapViewManager) {
            if (index == 0) {
                menuWindow.addSeparator();
            }
            mapDesktop.addWindowAction(menuWindow, frame, index++);
        }
    }

}
