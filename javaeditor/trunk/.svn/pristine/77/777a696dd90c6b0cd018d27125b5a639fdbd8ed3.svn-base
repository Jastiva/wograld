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

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Set;
import javax.swing.AbstractAction;
import javax.swing.KeyStroke;
import net.sf.gridarta.gui.map.mapview.MapView;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapcontrol.MapControl;
import net.sf.gridarta.model.mapmanager.MapManager;
import net.sf.gridarta.model.mapmanager.MapManagerListener;
import net.sf.gridarta.model.mapmodel.MapModelListener;
import net.sf.gridarta.model.mapmodel.MapSquare;
import net.sf.gridarta.model.validation.ErrorCollector;
import net.sf.gridarta.utils.Size2D;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Action class for selecting this window.
 * @author Andreas Kirschbaum
 */
public class WindowAction<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends AbstractAction {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The map desktop.
     */
    @NotNull
    private final MapDesktop<G, A, R> mapDesktop;

    /**
     * The associated map view.
     */
    @NotNull
    private final MapView<G, A, R> mapView;

    /**
     * The {@link MapManager} to use.
     */
    @NotNull
    private final MapManager<G, A, R> mapManager;

    /**
     * The index of this action.
     */
    private int index = -1;

    /**
     * The {@link MapManagerListener} to detect closed maps.
     */
    private final MapManagerListener<G, A, R> mapManagerListener = new MapManagerListener<G, A, R>() {

        @Override
        public void currentMapChanged(@Nullable final MapControl<G, A, R> mapControl) {
            // ignore
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
            if (mapControl == mapView.getMapControl()) {
                mapView.getMapControl().getMapModel().removeMapModelListener(mapModelListener);
                mapManager.removeMapManagerListener(mapManagerListener);
            }
        }

    };

    /**
     * The {@link MapModelListener} to detect modified maps.
     */
    private final MapModelListener<G, A, R> mapModelListener = new MapModelListener<G, A, R>() {

        @Override
        public void mapSizeChanged(@NotNull final Size2D newSize) {
            // ignore
        }

        @Override
        public void mapSquaresChanged(@NotNull final Set<MapSquare<G, A, R>> mapSquares) {
            // ignore
        }

        @Override
        public void mapObjectsChanged(@NotNull final Set<G> gameObjects, @NotNull final Set<G> transientGameObjects) {
            // ignore
        }

        @Override
        public void errorsChanged(@NotNull final ErrorCollector<G, A, R> errors) {
            // ignore
        }

        @Override
        public void mapFileChanged(@Nullable final File oldMapFile) {
            // ignore
        }

        @Override
        public void modifiedChanged() {
            updateName();
        }

    };

    /**
     * Create a new instance.
     * @param mapDesktop the main view
     * @param mapView the associated map view
     * @param mapManager the map manager to use
     */
    public WindowAction(@NotNull final MapDesktop<G, A, R> mapDesktop, @NotNull final MapView<G, A, R> mapView, @NotNull final MapManager<G, A, R> mapManager) {
        this.mapDesktop = mapDesktop;
        this.mapView = mapView;
        this.mapManager = mapManager;
        putValue(SHORT_DESCRIPTION, "Switches to map " + mapView.getInternalFrame().getTitle());
        mapView.getMapControl().getMapModel().addMapModelListener(mapModelListener);
        mapManager.addMapManagerListener(mapManagerListener);
        setIndex(-1);
    }

    /**
     * Set the index of this action so this Action knows what Mnemonic and
     * Accelerator to use.
     * @param index index (with 1 for first entry)
     */
    public final void setIndex(final int index) {
        this.index = index;
        if (0 <= index && index <= 10) {
            putValue(MNEMONIC_KEY, KeyStroke.getKeyStroke(Integer.toString(index % 10)).getKeyCode());
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl pressed " + index % 10));
        } else {
            putValue(MNEMONIC_KEY, null);
            putValue(ACCELERATOR_KEY, null);
        }
        updateName();
    }

    /**
     * Updates the action's name to the current map state.
     */
    private void updateName() {
        final String windowTitle = mapView.getWindowTitle();
        if (0 <= index && index <= 10) {
            putValue(NAME, index + ": " + windowTitle);
        } else {
            putValue(NAME, windowTitle);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void actionPerformed(@NotNull final ActionEvent e) {
        mapDesktop.setCurrentMapView(mapView);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (final CloneNotSupportedException ex) {
            throw new AssertionError(ex);
        }
    }

}
