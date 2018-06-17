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

package net.sf.gridarta.gui.map;

import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.util.Set;
import javax.swing.Action;
import net.sf.gridarta.gui.map.mapview.MapView;
import net.sf.gridarta.gui.map.mapview.MapViewManager;
import net.sf.gridarta.gui.map.mapview.MapViewManagerListener;
import net.sf.gridarta.gui.map.mapview.MapViewsManager;
import net.sf.gridarta.gui.map.renderer.ImageCreator2;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapcontrol.MapControl;
import net.sf.gridarta.model.mapmanager.FileControl;
import net.sf.gridarta.model.mapmanager.MapManager;
import net.sf.gridarta.model.mapmanager.MapManagerListener;
import net.sf.gridarta.model.mapmodel.MapModel;
import net.sf.gridarta.model.mapmodel.MapModelListener;
import net.sf.gridarta.model.mapmodel.MapSquare;
import net.sf.gridarta.model.validation.ErrorCollector;
import net.sf.gridarta.utils.ActionUtils;
import net.sf.gridarta.utils.Size2D;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import net.sf.japi.swing.action.ActionMethod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The class <code>MapFileActions</code> implements actions for the "file" menu
 * attached to maps.
 * @author Andreas Kirschbaum
 */
public class MapFileActions<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> {

    /**
     * Action Builder to create Actions.
     */
    @NotNull
    private static final ActionBuilder ACTION_BUILDER = ActionBuilderFactory.getInstance().getActionBuilder("net.sf.gridarta");

    /**
     * The {@link ImageCreator2} to forwards actions to.
     */
    @NotNull
    private final ImageCreator2<G, A, R> imageCreator2;

    /**
     * The file control to forward actions to.
     */
    @NotNull
    private final FileControl<G, A, R> fileControl;

    /**
     * The main view {@link Component}.
     */
    @NotNull
    private final Component mainView;

    /**
     * The map manager.
     */
    @NotNull
    private final MapManager<G, A, R> mapManager;

    /**
     * The {@link MapViewsManager}.
     */
    @NotNull
    private final MapViewsManager<G, A, R> mapViewsManager;

    /**
     * The action for "save map".
     */
    @NotNull
    private final Action aSaveMap = ActionUtils.newAction(ACTION_BUILDER, "Map", this, "saveMap");

    /**
     * The action for "save map as".
     */
    @NotNull
    private final Action aSaveMapAs = ActionUtils.newAction(ACTION_BUILDER, "Map", this, "saveMapAs");

    /**
     * The action for "create image".
     */
    @NotNull
    private final Action aCreateImage = ActionUtils.newAction(ACTION_BUILDER, "Map,Image", this, "createImage");

    /**
     * The action for "reload map".
     */
    @NotNull
    private final Action aReloadMap = ActionUtils.newAction(ACTION_BUILDER, "Map", this, "reloadMap");

    /**
     * The action for "close map".
     */
    @NotNull
    private final Action aCloseMap = ActionUtils.newAction(ACTION_BUILDER, "Map", this, "closeMap");

    /**
     * The currently tracked map, or <code>null</code> if no map is tracked.
     * This map has the {@link #mapModelListener} attached.
     */
    @Nullable
    private MapControl<G, A, R> currentMapControl;

    /**
     * The current map view, or <code>null</code> if no map view is active.
     */
    @Nullable
    private MapView<G, A, R> currentMapView;

    /**
     * The {@link MapModelListener} which is attached to {@link
     * #currentMapControl}'s map model.
     */
    @NotNull
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
            updateActions();
        }

    };

    /**
     * The map manager listener which is attached to the current map if the
     * current map is tracked. Otherwise it is unused.
     */
    @NotNull
    private final MapManagerListener<G, A, R> mapManagerListener = new MapManagerListener<G, A, R>() {

        @Override
        public void currentMapChanged(@Nullable final MapControl<G, A, R> mapControl) {
            if (currentMapControl != null) {
                currentMapControl.getMapModel().removeMapModelListener(mapModelListener);
            }
            currentMapControl = mapControl;
            if (currentMapControl != null) {
                currentMapControl.getMapModel().addMapModelListener(mapModelListener);
            }
            updateActions();
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
     * Create a new instance that tracks the map state.
     * @param imageCreator2 the image creator to forwards actions to
     * @param mapManager the map manager
     * @param mapViewsManager the map views manager
     * @param mapViewManager the map view manager
     * @param fileControl the file control to forward actions to
     * @param mainView the main view component
     */
    public MapFileActions(@NotNull final ImageCreator2<G, A, R> imageCreator2, @NotNull final MapManager<G, A, R> mapManager, @NotNull final MapViewsManager<G, A, R> mapViewsManager, @NotNull final MapViewManager<G, A, R> mapViewManager, @NotNull final FileControl<G, A, R> fileControl, @NotNull final Component mainView) {
        this.imageCreator2 = imageCreator2;
        this.mapManager = mapManager;
        this.mapViewsManager = mapViewsManager;
        this.fileControl = fileControl;
        this.mainView = mainView;
        mapManager.addMapManagerListener(mapManagerListener);
        currentMapControl = mapManager.getCurrentMap();
        if (currentMapControl != null) {
            currentMapControl.getMapModel().addMapModelListener(mapModelListener);
        }
        final MapViewManagerListener<G, A, R> mapViewManagerListener = new MapViewManagerListener<G, A, R>() {

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
        mapViewManager.addMapViewManagerListener(mapViewManagerListener);
        currentMapView = mapViewManager.getActiveMapView();
        updateActions();
    }

    /**
     * Unregister all registered listeners. Must be called when this instance is
     * freed.
     */
    public void closeNotify() {
        mapManager.removeMapManagerListener(mapManagerListener);
        if (currentMapControl != null) {
            currentMapControl.getMapModel().removeMapModelListener(mapModelListener);
        }
    }

    /**
     * Update the enabled/disabled state of all actions.
     */
    private void updateActions() {
        aSaveMap.setEnabled(doSaveMap(false));
        aSaveMapAs.setEnabled(doSaveMapAs(false));
        aCreateImage.setEnabled(doCreateImage(false));
        aReloadMap.setEnabled(doReloadMap(false));
        aCloseMap.setEnabled(doCloseMap(false));
    }

    /**
     * Invoked when the user wants to save the map.
     */
    @ActionMethod
    public void saveMap() {
        doSaveMap(true);
    }

    /**
     * Invoked when the user wants to save the map to a file.
     */
    @ActionMethod
    public void saveMapAs() {
        doSaveMapAs(true);
    }

    /**
     * Invoked when the user wants to create an image file of the map.
     */
    @ActionMethod
    public void createImage() {
        doCreateImage(true);
    }

    /**
     * Invoked when the user wants to reload the map to the previously saved
     * state.
     */
    @ActionMethod
    public void reloadMap() {
        doReloadMap(true);
    }

    /**
     * Invoked when the user wants to close the map.
     */
    @ActionMethod
    public void closeMap() {
        doCloseMap(true);
    }

    /**
     * Executes the "save map" action.
     * @param performAction whether the action should be performed
     * @return whether the action was or can be performed
     */
    private boolean doSaveMap(final boolean performAction) {
        final MapControl<G, A, R> mapControl = currentMapControl;
        if (mapControl == null || !mapControl.getMapModel().isModified()) {
            return false;
        }

        if (performAction) {
            if (!fileControl.save(mapControl)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Executes the "save map as" action.
     * @param performAction whether the action should be performed
     * @return whether the action was or can be performed
     */
    private boolean doSaveMapAs(final boolean performAction) {
        final MapControl<G, A, R> mapControl = currentMapControl;
        if (mapControl == null) {
            return false;
        }

        if (performAction) {
            if (!fileControl.saveAs(mapControl)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Executes the "create image" action.
     * @param performAction whether the action should be performed
     * @return whether the action was or can be performed
     */
    private boolean doCreateImage(final boolean performAction) {
        final MapView<G, A, R> mapView = currentMapView;
        if (mapView == null) {
            return false;
        }

        if (performAction) {
            final MapModel<G, A, R> mapModel = mapView.getMapControl().getMapModel();
            imageCreator2.createImage(mapModel, mainView);
        }

        return true;
    }

    /**
     * Executes the "reload map" action.
     * @param performAction whether the action should be performed
     * @return whether the action was or can be performed
     */
    private boolean doReloadMap(final boolean performAction) {
        final MapControl<G, A, R> mapControl = currentMapControl;
        if (mapControl == null) {
            return false;
        }

        final File mapFile = mapControl.getMapModel().getMapFile();
        if (mapFile == null) {
            return false;
        }

        if (performAction) {
            try {
                mapManager.revert(mapControl);
            } catch (final IOException ex) {
                fileControl.reportLoadError(mapFile, ex.getMessage());
                return false;
            }
        }

        return true;
    }

    /**
     * Executes the "close map" action.
     * @param performAction whether the action should be performed
     * @return whether the action was or can be performed
     */
    private boolean doCloseMap(final boolean performAction) {
        final MapView<G, A, R> mapView = currentMapView;
        if (mapView == null) {
            return false;
        }

        if (performAction) {
            mapViewsManager.closeMapView(mapView);
        }

        return true;
    }

}
