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

package net.sf.gridarta.gui.dialog.find;

import java.awt.Component;
import net.sf.gridarta.gui.map.mapview.MapView;
import net.sf.gridarta.gui.map.mapview.MapViewManager;
import net.sf.gridarta.gui.map.mapview.MapViewManagerListener;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Creates and displays the find dialog.
 * @author Andreas Kirschbaum
 */
public class FindDialogManager<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> {

    /**
     * Singleton Instance.
     */
    @Nullable
    private FindDialog<G, A, R> instance;

    /**
     * The parent component for dialogs.
     */
    @NotNull
    private final Component parent;

    /**
     * Creates a new instance.
     * @param parent the parent component for dialogs
     * @param mapViewManager the map view manager to use
     */
    public FindDialogManager(@NotNull final Component parent, @NotNull final MapViewManager<G, A, R> mapViewManager) {
        this.parent = parent;

        final MapViewManagerListener<G, A, R> mapViewManagerListener = new MapViewManagerListener<G, A, R>() {

            @Override
            public void activeMapViewChanged(@Nullable final MapView<G, A, R> mapView) {
                // ignore
            }

            @Override
            public void mapViewCreated(@NotNull final MapView<G, A, R> mapView) {
                // ignore
            }

            @Override
            public void mapViewClosing(@NotNull final MapView<G, A, R> mapView) {
                disposeDialog(mapView);
            }

        };
        mapViewManager.addMapViewManagerListener(mapViewManagerListener);
    }

    /**
     * Dispose the replace dialog.
     * @param mapView the map view to dispose the dialog of; do nothing if no
     * dialog exists
     */
    private void disposeDialog(@NotNull final MapView<G, A, R> mapView) {
        if (instance != null) {
            instance.dispose(mapView);
        }
    }

    /**
     * Displays the replace dialog.
     * @param mapView the map view to operate on
     */
    public void showDialog(@NotNull final MapView<G, A, R> mapView) {
        if (instance == null) {
            instance = new FindDialog<G, A, R>(parent);
        }
        instance.display(mapView);
    }

    /**
     * Executes the "find next" action.
     * @param mapView the map view to operate on
     * @param performAction whether the action should be performed
     * @return whether the action was or can be performed
     */
    public boolean findNext(@NotNull final MapView<G, A, R> mapView, final boolean performAction) {
        return instance != null && instance.findAgain(mapView, true, performAction);
    }

    /**
     * Executes the "find next" action.
     * @param mapView the map view to operate on
     * @param performAction whether the action should be performed
     * @return whether the action was or can be performed
     */
    public boolean findPrev(@NotNull final MapView<G, A, R> mapView, final boolean performAction) {
        return instance != null && instance.findAgain(mapView, false, performAction);
    }

}
