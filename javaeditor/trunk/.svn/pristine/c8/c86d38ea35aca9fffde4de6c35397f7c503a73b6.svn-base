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

package net.sf.gridarta.gui.undo;

import java.util.IdentityHashMap;
import java.util.Map;
import javax.swing.Action;
import net.sf.gridarta.actions.UndoActions;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.gameobject.GameObjectFactory;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapcontrol.MapControl;
import net.sf.gridarta.model.mapmanager.MapManager;
import net.sf.gridarta.model.mapmanager.MapManagerListener;
import net.sf.gridarta.model.mapmodel.MapModel;
import net.sf.gridarta.model.mapmodel.MapTransactionListener;
import net.sf.gridarta.model.mapmodel.SavedSquares;
import net.sf.gridarta.model.match.GameObjectMatchers;
import net.sf.gridarta.model.undo.UndoModel;
import net.sf.gridarta.model.undo.UndoState;
import net.sf.gridarta.utils.ActionBuilderUtils;
import net.sf.gridarta.utils.ActionUtils;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import net.sf.japi.swing.action.ActionMethod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The class <code>UndoControl</code> implements the controller for undo/redo
 * actions.
 * @author Andreas Kirschbaum
 */
public class UndoControl<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> {

    /**
     * Action Builder to create Actions.
     */
    @NotNull
    private static final ActionBuilder ACTION_BUILDER = ActionBuilderFactory.getInstance().getActionBuilder("net.sf.gridarta");

    /**
     * Contains an {@link UndoModel} for each known {@link MapModel}.
     */
    @NotNull
    private final Map<MapModel<G, A, R>, UndoModel<G, A, R>> undoModels = new IdentityHashMap<MapModel<G, A, R>, UndoModel<G, A, R>>();

    /**
     * Map model for the current map. Set to <code>null</code> if no map is
     * active.
     */
    @Nullable
    private MapModel<G, A, R> mapModel;

    /**
     * The maximum number of undo states saved for each map.
     */
    private int maxUndoStates = 100;

    /**
     * Listener to be notified for map transactions. The same listener is
     * registered for all known maps.
     */
    @NotNull
    private final MapTransactionListener<G, A, R> mapTransactionListener = new MapTransactionListener<G, A, R>() {

        /**
         * Records the map state at transaction begin time. It is used to
         * detect (and drop) edit operations that do not change the map
         * state.
         *
         * XXX: since only one listener instance is used for all maps, it
         * will not work if concurrent transactions occur
         */
        @Nullable
        private UndoState<G, A, R> undoState;

        /**
         * Records the undo model at transaction begin time.
         */
        @Nullable
        private UndoModel<G, A, R> undoModel;

        @Override
        public void preBeginTransaction() {
            // ignore
        }

        @Override
        public void beginTransaction(@NotNull final MapModel<G, A, R> mapModel, @NotNull final String name) {
            undoModel = undoModels.get(mapModel);
            undoState = new UndoState<G, A, R>(name, mapModel.getMapArchObject().clone());
        }

        @Override
        public void endTransaction(@NotNull final MapModel<G, A, R> mapModel, @NotNull final SavedSquares<G, A, R> savedSquares) {
            final UndoState<G, A, R> savedUndoState = undoState;
            assert savedUndoState != null;
            final UndoModel<G, A, R> savedUndoModel = undoModel;
            assert savedUndoModel != null;
            if (savedSquares.isEmpty() && savedUndoState.getMapArchObject().equals(mapModel.getMapArchObject())) {
                return;
            }

            final SavedSquares<G, A, R> clonedSavedSquares = savedSquares.cloneAndClear();
            clonedSavedSquares.removeEmptySquares(savedUndoState.getMapArchObject().getMapSize());
            savedUndoState.setSavedSquares(clonedSavedSquares);
            savedUndoModel.finish(savedUndoState);
            if (maxUndoStates > 0) {
                savedUndoModel.trimToSize(maxUndoStates);
            }

            refreshMenus();
        }

        @Override
        public void postEndTransaction() {
            // ignore
        }

    };

    /**
     * Action for "undo" function.
     */
    @NotNull
    private final Action aUndo = ActionUtils.newAction(ACTION_BUILDER, "Undo", this, "undo");

    /**
     * Action for "redo" function.
     */
    @NotNull
    private final Action aRedo = ActionUtils.newAction(ACTION_BUILDER, "Undo", this, "redo");

    /**
     * Create a new instance.
     * @param mapManager the map manager
     * @param gameObjectFactory the game object factory to use
     * @param gameObjectMatchers the game object matchers to use
     */
    public UndoControl(@NotNull final MapManager<G, A, R> mapManager, @NotNull final GameObjectFactory<G, A, R> gameObjectFactory, @NotNull final GameObjectMatchers gameObjectMatchers) {
        final MapManagerListener<G, A, R> mapManagerListener = new MapManagerListener<G, A, R>() {

            @Override
            public void currentMapChanged(@Nullable final MapControl<G, A, R> mapControl) {
                mapModel = mapControl == null ? null : mapControl.getMapModel();
                refreshMenus();
            }

            @Override
            public void mapCreated(@NotNull final MapControl<G, A, R> mapControl, final boolean interactive) {
                undoModels.put(mapControl.getMapModel(), new UndoModel<G, A, R>(gameObjectFactory, gameObjectMatchers));
                mapControl.getMapModel().addMapTransactionListener(mapTransactionListener);
            }

            @Override
            public void mapClosing(@NotNull final MapControl<G, A, R> mapControl) {
                // ignore
            }

            @Override
            public void mapClosed(@NotNull final MapControl<G, A, R> mapControl) {
                undoModels.remove(mapControl.getMapModel());
                mapControl.getMapModel().removeMapTransactionListener(mapTransactionListener);
            }

        };
        mapManager.addMapManagerListener(mapManagerListener);

        refreshMenus();
    }

    /**
     * Enable/disable menu entries based on the current state.
     */
    private void refreshMenus() {
        final boolean undoEnabled = doUndo(false);
        aUndo.setEnabled(undoEnabled);
        if (undoEnabled) {
            final UndoModel<G, A, R> undoModel = undoModels.get(mapModel);
            assert undoModel != null;
            aUndo.putValue(Action.NAME, ACTION_BUILDER.format("undo.name", undoModel.undoName()));
        } else {
            aUndo.putValue(Action.NAME, ActionBuilderUtils.getString(ACTION_BUILDER, "undo.text"));
        }

        final boolean redoEnabled = doRedo(false);
        aRedo.setEnabled(redoEnabled);
        if (redoEnabled) {
            final UndoModel<G, A, R> redoModel = undoModels.get(mapModel);
            assert redoModel != null;
            aRedo.putValue(Action.NAME, ACTION_BUILDER.format("redo.name", redoModel.redoName()));
        } else {
            aRedo.putValue(Action.NAME, ActionBuilderUtils.getString(ACTION_BUILDER, "redo.text"));
        }
    }

    /**
     * "Undo" was selected.
     */
    @ActionMethod
    public void undo() {
        doUndo(true);
    }

    /**
     * "Redo" was selected.
     */
    @ActionMethod
    public void redo() {
        doRedo(true);
    }

    /**
     * Returns the maximum number of undo states saved for each map.
     * @return the maximum number of undo states saved for each map;
     *         <code>0</code>=unlimited
     */
    public int getMaxUndoStates() {
        return maxUndoStates;
    }

    /**
     * Sets the maximum number of undo states saved for each map.
     * @param maxUndoStates the maximum number of undo states saved for each
     * map; <code>0</code>=unlimited
     */
    public void setMaxUndoStates(final int maxUndoStates) {
        this.maxUndoStates = maxUndoStates;

        if (maxUndoStates > 0) {
            for (final UndoModel<G, A, R> undoModel : undoModels.values()) {
                undoModel.trimToSize(maxUndoStates);
            }
        }
    }

    /**
     * Performs the "undo" action.
     * @param performAction whether to perform or check the action
     * @return whether the action was or can be performed
     */
    private boolean doUndo(final boolean performAction) {
        final MapModel<G, A, R> tmpMapModel = mapModel;
        if (tmpMapModel == null) {
            return false;
        }

        final UndoModel<G, A, R> undoModel = undoModels.get(tmpMapModel);
        if (undoModel == null) {
            // XXX: should be "assert undoModel != null"; this does not work
            // because MapViewManager.activateMapView() calls
            // mainControl.setCurrentLevel() which then generates a
            // currentMapChanged() event before a mapCreated() event
            return false;
        }

        if (!undoModel.canUndo()) {
            return false;
        }

        if (performAction) {
            UndoActions.undo(undoModel, tmpMapModel);
            refreshMenus();
        }

        return true;
    }

    /**
     * Performs the "redo" action.
     * @param performAction whether to perform or check the action
     * @return whether the action was or can be performed
     */
    private boolean doRedo(final boolean performAction) {
        final MapModel<G, A, R> tmpMapModel = mapModel;
        if (tmpMapModel == null) {
            return false;
        }

        final UndoModel<G, A, R> undoModel = undoModels.get(tmpMapModel);
        if (undoModel == null) {
            // XXX: should be "assert undoModel != null"; this does not work
            // because MapViewManager.activateMapView() calls
            // mainControl.setCurrentLevel() which then generates a
            // currentMapChanged() event before a mapCreated() event
            return false;
        }

        if (!undoModel.canRedo()) {
            return false;
        }

        if (performAction) {
            UndoActions.redo(undoModel, tmpMapModel);
            refreshMenus();
        }

        return true;
    }

} // UndoControl
