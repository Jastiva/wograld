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

package net.sf.gridarta.actions;

import java.awt.Point;
import java.io.File;
import java.io.IOException;
import javax.swing.Action;
import net.sf.gridarta.gui.map.mapview.MapView;
import net.sf.gridarta.gui.map.mapview.MapViewManager;
import net.sf.gridarta.gui.map.mapview.MapViewManagerListener;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.io.PathManager;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapcontrol.MapControl;
import net.sf.gridarta.model.mapmanager.FileControl;
import net.sf.gridarta.model.mapmodel.MapModel;
import net.sf.gridarta.utils.ActionUtils;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import net.sf.japi.swing.action.ActionMethod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Actions that require a connection to a game server.
 * @author Andreas Kirschbaum
 * @noinspection AbstractClassWithOnlyOneDirectInheritor
 */
public abstract class AbstractServerActions<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> {

    /**
     * Action Builder to create Actions.
     */
    @NotNull
    private static final ActionBuilder ACTION_BUILDER = ActionBuilderFactory.getInstance().getActionBuilder("net.sf.gridarta");

    /**
     * The currently active map or <code>null</code> if no map is active.
     */
    @Nullable
    private MapView<G, A, R> currentMapView;

    /**
     * The file control for saving maps.
     */
    @NotNull
    private final FileControl<G, A, R> fileControl;

    /**
     * The {@link PathManager} for converting path names.
     */
    @NotNull
    private final PathManager pathManager;

    /**
     * The action for "open in client".
     * @noinspection ThisEscapedInObjectConstruction
     */
    @NotNull
    private final Action aOpenInClient = ActionUtils.newAction(ACTION_BUILDER, "Map", this, "openInClient");

    /**
     * The map manager listener which is attached to the current map if the
     * current map is tracked. Otherwise it is unused.
     * @noinspection FieldCanBeLocal
     */
    @NotNull
    private final MapViewManagerListener<G, A, R> mapViewManagerListener = new MapViewManagerListener<G, A, R>() {

        @Override
        public void activeMapViewChanged(@Nullable final MapView<G, A, R> mapView) {
            currentMapView = mapView;
            updateActions();
        }

        @Override
        public void mapViewCreated(@NotNull final MapView<G, A, R> mapView) {
            // ignore
        }

        @Override
        public void mapViewClosing(@NotNull final MapView<G, A, R> mapView) {
            // ignore
        }

    };

    /**
     * Creates a new instance.
     * @param mapViewManager the map view manager for tracking the current map
     * view
     * @param fileControl the file control for saving maps
     * @param pathManager the path manager for converting path names
     */
    protected AbstractServerActions(@NotNull final MapViewManager<G, A, R> mapViewManager, @NotNull final FileControl<G, A, R> fileControl, @NotNull final PathManager pathManager) {
        this.fileControl = fileControl;
        this.pathManager = pathManager;
        mapViewManager.addMapViewManagerListener(mapViewManagerListener);
        currentMapView = mapViewManager.getActiveMapView();
        updateActions();
    }

    /**
     * Action method for "open in client".
     */
    @ActionMethod
    public void openInClient() {
        doOpenInClient(true);
    }

    /**
     * Update the enabled/disabled state of all actions.
     */
    private void updateActions() {
        aOpenInClient.setEnabled(doOpenInClient(false));
    }

    /**
     * Executes the "open in client" action.
     * @param performAction whether the action should be performed
     * @return whether the action was or can be performed
     */
    private boolean doOpenInClient(final boolean performAction) {
        final MapView<G, A, R> mapView = currentMapView;
        if (mapView == null) {
            return false;
        }

        final MapControl<G, A, R> mapControl = mapView.getMapControl();
        final MapModel<G, A, R> mapModel = mapControl.getMapModel();
        final File mapFile = mapModel.getMapFile();
        if (mapFile == null) {
            return false;
        }

        final String mapPath = pathManager.getMapPath2(mapFile);
        if (mapPath == null) {
            return false;
        }

        if (performAction) {
            if (mapModel.isModified() && !fileControl.save(mapControl)) {
                return false;
            }

            final Point cursor = mapView.getMapCursor().getLocation();
            final int mapX;
            final int mapY;
            if (cursor == null) {
                final A mapArchObject = mapModel.getMapArchObject();
                mapX = mapArchObject.getEnterX();
                mapY = mapArchObject.getEnterY();
            } else {
                mapX = cursor.x;
                mapY = cursor.y;
            }
            try {
                teleportCharacterToMap(mapPath, mapX, mapY);
            } catch (final IOException ex) {
                fileControl.reportTeleportCharacterError(mapPath, ex.getMessage());
            }
        }

        return true;
    }

    /**
     * Teleports the character to the given map path.
     * @param mapPath the map path to teleport to
     * @param mapX the x coordinate to teleport to
     * @param mapY the y coordinate to teleport to
     * @throws IOException if teleportation fails
     */
    protected abstract void teleportCharacterToMap(@NotNull final String mapPath, final int mapX, final int mapY) throws IOException;

}
