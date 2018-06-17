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

package net.sf.gridarta.gui.dialog.replace;

import java.awt.Component;
import net.sf.gridarta.gui.copybuffer.CopyBuffer;
import net.sf.gridarta.gui.map.mapview.MapView;
import net.sf.gridarta.gui.map.mapview.MapViewManager;
import net.sf.gridarta.gui.map.mapview.MapViewManagerListener;
import net.sf.gridarta.gui.panel.objectchooser.ObjectChooser;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.face.FaceObjectProviders;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapmodel.InsertionModeSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Creates and displays the replace dialog.
 * @author Andreas Kirschbaum
 */
public class ReplaceDialogManager<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> {

    /**
     * Singleton Instance.
     */
    @Nullable
    private ReplaceDialog<G, A, R> instance;

    /**
     * The parent component for dialogs.
     */
    @NotNull
    private final Component parent;

    /**
     * The {@link CopyBuffer}.
     */
    @NotNull
    private final CopyBuffer<G, A, R> copyBuffer;

    /**
     * The {@link ObjectChooser} to use.
     */
    @NotNull
    private final ObjectChooser<G, A, R> objectChooser;

    /**
     * The {@link FaceObjectProviders} for looking up faces.
     */
    @NotNull
    private final FaceObjectProviders faceObjectProviders;

    /**
     * The {@link InsertionModeSet} to use.
     */
    @NotNull
    private final InsertionModeSet<G, A, R> insertionModeSet;

    /**
     * Creates a new instance.
     * @param parent the parent component for dialogs
     * @param copyBuffer the copy buffer's
     * @param objectChooser the object chooser to use
     * @param mapViewManager the map view manager to use
     * @param faceObjectProviders the face object providers for looking up
     * faces
     * @param insertionModeSet the insertion mode set to use
     */
    public ReplaceDialogManager(@NotNull final Component parent, @NotNull final CopyBuffer<G, A, R> copyBuffer, @NotNull final ObjectChooser<G, A, R> objectChooser, @NotNull final MapViewManager<G, A, R> mapViewManager, @NotNull final FaceObjectProviders faceObjectProviders, @NotNull final InsertionModeSet<G, A, R> insertionModeSet) {
        this.parent = parent;
        this.copyBuffer = copyBuffer;
        this.objectChooser = objectChooser;
        this.faceObjectProviders = faceObjectProviders;
        this.insertionModeSet = insertionModeSet;

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
            instance = new ReplaceDialog<G, A, R>(parent, copyBuffer, objectChooser, faceObjectProviders, insertionModeSet);
        }
        instance.display(mapView);
    }

}
