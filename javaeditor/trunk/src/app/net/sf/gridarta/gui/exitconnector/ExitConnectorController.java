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

package net.sf.gridarta.gui.exitconnector;

import java.awt.Point;
import javax.swing.Action;
import net.sf.gridarta.actions.ExitConnectorActions;
import net.sf.gridarta.gui.map.mapview.MapView;
import net.sf.gridarta.gui.map.mapview.MapViewManager;
import net.sf.gridarta.gui.map.mapview.MapViewManagerListener;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.exitconnector.ExitConnectorModel;
import net.sf.gridarta.model.exitconnector.ExitConnectorModelListener;
import net.sf.gridarta.model.exitconnector.ExitLocation;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapcursor.MapCursorListener;
import net.sf.gridarta.model.mapmodel.MapSquare;
import net.sf.gridarta.utils.ActionUtils;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import net.sf.japi.swing.action.ActionMethod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The controller of the exit connector.
 * @author Andreas Kirschbaum
 */
public class ExitConnectorController<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> {

    /**
     * The action builder.
     */
    @NotNull
    private static final ActionBuilder ACTION_BUILDER = ActionBuilderFactory.getInstance().getActionBuilder("net.sf.gridarta");

    /**
     * The actions for this controller.
     */
    @NotNull
    private final ExitConnectorActions<G, A, R> exitConnectorActions;

    /**
     * The action for "copy exit".
     */
    @NotNull
    private final Action aExitCopy = ActionUtils.newAction(ACTION_BUILDER, "Exit Connector", this, "exitCopy");

    /**
     * The action for "paste exit".
     */
    @NotNull
    private final Action aExitPaste = ActionUtils.newAction(ACTION_BUILDER, "Exit Connector", this, "exitPaste");

    /**
     * The action for "connect exits".
     */
    @NotNull
    private final Action aExitConnect = ActionUtils.newAction(ACTION_BUILDER, "Exit Connector", this, "exitConnect");

    /**
     * The active {@link MapView} or <code>null</code> if no map view exists.
     */
    @Nullable
    private MapView<G, A, R> currentMapView;

    /**
     * The {@link ExitConnectorModelListener} registered to the model.
     */
    @NotNull
    private final ExitConnectorModelListener exitConnectorModelListener = new ExitConnectorModelListener() {

        @Override
        public void exitLocationChanged(@Nullable final ExitLocation exitLocation) {
            refreshActions();
        }

        @Override
        public void pasteExitNameChanged(final boolean pasteExitName) {
            // ignore
        }

        @Override
        public void autoCreateExitChanged(final boolean pasteExitName) {
            refreshActions();
        }

        @Override
        public void exitArchetypeNameChanged(@NotNull final String exitArchetypeName) {
            // ignore
        }

    };

    /**
     * The {@link MapViewManagerListener} used to track the current map.
     */
    @NotNull
    private final MapViewManagerListener<G, A, R> mapViewManagerListener = new MapViewManagerListener<G, A, R>() {

        @Override
        public void activeMapViewChanged(@Nullable final MapView<G, A, R> mapView) {
            setCurrentMapView(mapView);
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
     * The {@link MapCursorListener} used to track cursor state changes in
     * {@link #currentMapView}.
     */
    @NotNull
    private final MapCursorListener<G, A, R> mapCursorListener = new MapCursorListener<G, A, R>() {

        @Override
        public void mapCursorChangedPos(@Nullable final Point location) {
            // ignore
        }

        @Override
        public void mapCursorChangedMode() {
            refreshActions();
        }

        @Override
        public void mapCursorChangedGameObject(@Nullable final MapSquare<G, A, R> mapSquare, @Nullable final G gameObject) {
            //ignore
        }

    };

    /**
     * Creates a new instance.
     * @param exitConnectorActions the exit connector actions to use
     * @param exitConnectorModel the model for this controller
     * @param mapViewManager the map view manager to track
     */
    public ExitConnectorController(@NotNull final ExitConnectorActions<G, A, R> exitConnectorActions, @NotNull final ExitConnectorModel exitConnectorModel, @NotNull final MapViewManager<G, A, R> mapViewManager) {
        this.exitConnectorActions = exitConnectorActions;
        mapViewManager.addMapViewManagerListener(mapViewManagerListener);
        exitConnectorModel.addExitConnectorModelListener(exitConnectorModelListener);
        setCurrentMapView(mapViewManager.getActiveMapView());
    }

    /**
     * Action method for "copy exit".
     */
    @ActionMethod
    public void exitCopy() {
        doExitCopy(true);
    }

    /**
     * Action method for "paste exit".
     */
    @ActionMethod
    public void exitPaste() {
        doExitPaste(true);
    }

    /**
     * Action method for "connect exit".
     */
    @ActionMethod
    public void exitConnect() {
        doExitConnect(true);
    }

    /**
     * Updates {@link #currentMapView}, registering or de-registering listeners
     * as needed.
     * @param currentMapView the new current map view
     */
    private void setCurrentMapView(@Nullable final MapView<G, A, R> currentMapView) {
        if (this.currentMapView != null) {
            this.currentMapView.getMapCursor().removeMapCursorListener(mapCursorListener);
        }
        this.currentMapView = currentMapView;
        if (this.currentMapView != null) {
            this.currentMapView.getMapCursor().addMapCursorListener(mapCursorListener);
        }
        refreshActions();
    }

    /**
     * Updates the enabled state of all actions.
     */
    private void refreshActions() {
        aExitCopy.setEnabled(doExitCopy(false));
        aExitPaste.setEnabled(doExitPaste(false));
        aExitConnect.setEnabled(doExitConnect(false));
    }

    /**
     * Performs or checks availability of the "exit copy" action.
     * @param performAction whether the action should be performed
     * @return whether the action was or can be performed
     */
    private boolean doExitCopy(final boolean performAction) {
        final MapView<G, A, R> mapView = currentMapView;
        if (mapView == null) {
            // no active map view ==> no location to remember
            return false;
        }

        final Point location = mapView.getMapCursor().getLocation();
        // no active cursor ==> no location to remember
        return location != null && exitConnectorActions.doExitCopy(performAction, mapView.getMapControl(), location);
    }

    /**
     * Performs or checks availability of the "exit paste" action.
     * @param performAction whether the action should be performed
     * @return whether the action was or can be performed
     */
    private boolean doExitPaste(final boolean performAction) {
        final MapView<G, A, R> mapView = currentMapView;
        if (mapView == null) {
            return false;
        }

        final Point targetLocation = mapView.getMapCursor().getLocation();
        return targetLocation != null && exitConnectorActions.doExitPaste(performAction, mapView.getMapControl(), targetLocation);
    }

    /**
     * Performs or checks availability of the "exit connect" action.
     * @param performAction whether the action should be performed
     * @return whether the action was or can be performed
     */
    private boolean doExitConnect(final boolean performAction) {
        final MapView<G, A, R> mapView = currentMapView;
        if (mapView == null) {
            return false;
        }

        final Point targetLocation = mapView.getMapCursor().getLocation();
        return targetLocation != null && exitConnectorActions.doExitConnect(performAction, mapView.getMapControl(), targetLocation);
    }

}
