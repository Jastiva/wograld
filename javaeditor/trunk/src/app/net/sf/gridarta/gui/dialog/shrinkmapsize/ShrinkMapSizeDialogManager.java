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

package net.sf.gridarta.gui.dialog.shrinkmapsize;

import java.util.HashMap;
import java.util.Map;
import net.sf.gridarta.gui.map.mapview.MapView;
import net.sf.gridarta.gui.map.mapview.MapViewManager;
import net.sf.gridarta.gui.map.mapview.MapViewManagerListener;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Manager for {@link ShrinkMapSizeDialog} instances. This class makes sure that
 * for each map view no more than one shrink map size dialog exists.
 * @author Andreas Kirschbaum
 */
public class ShrinkMapSizeDialogManager<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> {

    /**
     * Dialog for each map view.
     */
    @NotNull
    private final Map<MapView<G, A, R>, ShrinkMapSizeDialog<G, A, R>> dialogs = new HashMap<MapView<G, A, R>, ShrinkMapSizeDialog<G, A, R>>();

    /**
     * The map view manager listener to detect closed map views.
     */
    @NotNull
    private final MapViewManagerListener<G, A, R> mapViewManagerListener = new MapViewManagerListener<G, A, R>() {

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

    /**
     * Creates a new instance.
     * @param mapViewManager the map view manager
     */
    public ShrinkMapSizeDialogManager(@NotNull final MapViewManager<G, A, R> mapViewManager) {
        mapViewManager.addMapViewManagerListener(mapViewManagerListener);
    }

    /**
     * Shows a dialog to shrink the map size.
     * @param mapView the view map
     */
    public void showDialog(@NotNull final MapView<G, A, R> mapView) {
        synchronized (dialogs) {
            final ShrinkMapSizeDialog<G, A, R> oldDialog = dialogs.get(mapView);
            if (oldDialog != null) {
                oldDialog.getDialog().toFront();
                return;
            }

            final ShrinkMapSizeDialog<G, A, R> pane = new ShrinkMapSizeDialog<G, A, R>(this, mapView);
            dialogs.put(mapView, pane);
        }
    }

    /**
     * Disposes a dialog.
     * @param mapView the map view to dispose the dialog of; does nothing if no
     * dialog exists
     */
    public void disposeDialog(@NotNull final MapView<G, A, R> mapView) {
        final ShrinkMapSizeDialog<G, A, R> dialog;
        synchronized (dialogs) {
            dialog = dialogs.remove(mapView);
            if (dialog == null) {
                return;
            }
        }

        dialog.getDialog().dispose();
    }

}
