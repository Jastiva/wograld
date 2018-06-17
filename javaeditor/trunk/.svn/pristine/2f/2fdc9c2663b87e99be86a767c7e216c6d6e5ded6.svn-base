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

package net.sf.gridarta.gui.mapcursor;

import java.awt.Point;
import javax.swing.Action;
import net.sf.gridarta.gui.dialog.gameobjectattributes.GameObjectAttributesDialogFactory;
import net.sf.gridarta.gui.dialog.golocation.GoLocationDialog;
import net.sf.gridarta.gui.dialog.golocation.GoLocationDialogManager;
import net.sf.gridarta.gui.map.AbstractPerMapDialogManager;
import net.sf.gridarta.gui.map.mapview.MapView;
import net.sf.gridarta.gui.map.mapview.MapViewManager;
import net.sf.gridarta.gui.map.mapview.MapViewManagerListener;
import net.sf.gridarta.gui.map.renderer.MapRenderer;
import net.sf.gridarta.gui.panel.objectchooser.ObjectChooser;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.baseobject.BaseObject;
import net.sf.gridarta.model.direction.Direction;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapcursor.MapCursor;
import net.sf.gridarta.model.mapcursor.MapCursorListener;
import net.sf.gridarta.model.mapgrid.SelectionMode;
import net.sf.gridarta.model.mapmodel.MapSquare;
import net.sf.gridarta.utils.ActionUtils;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import net.sf.japi.swing.action.ActionMethod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Cursor related actions.
 * @author Andreas Kirschbaum
 */
public class MapCursorActions<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> {

    /**
     * The visible border around the cursor. Whenever the cursor moves, the map
     * view is scrolled to make this area visible.
     */
    private static final int BORDER = 3;

    /**
     * The object chooser.
     */
    @NotNull
    private final ObjectChooser<G, A, R> objectChooser;

    /**
     * All {@link Direction Directions}.
     */
    @NotNull
    private static final Direction[] directions = Direction.values();

    /**
     * Actions for "move cursor".
     */
    @NotNull
    private final Action[] aMoveCursor;

    /**
     * Action for "go location".
     */
    @NotNull
    private final Action aGoLocation;

    /**
     * Action for "select square".
     */
    @NotNull
    private final Action aSelectSquare;

    /**
     * Action for "add to selection".
     */
    @NotNull
    private final Action aAddToSelection;

    /**
     * Action for "remove from selection".
     */
    @NotNull
    private final Action aSubFromSelection;

    /**
     * Action for "stop stop drag".
     */
    @NotNull
    private final Action aStartStopDrag;

    /**
     * Action for "release drag".
     */
    @NotNull
    private final Action aReleaseDrag;

    /**
     * Action for "insert arch".
     */
    @NotNull
    private final Action aInsertArch;

    /**
     * Action for "delete arch".
     */
    @NotNull
    private final Action aDeleteArch;

    /**
     * Action for "select arch above".
     */
    @NotNull
    private final Action aSelectArchAbove;

    /**
     * Action for "select arch below".
     */
    @NotNull
    private final Action aSelectArchBelow;

    /**
     * Action for "arch attributes".
     */
    @NotNull
    private final Action aArchAttributes;

    /**
     * The factory for creating game object attributes dialog instances.
     */
    @NotNull

    private final GameObjectAttributesDialogFactory<G, A, R> gameObjectAttributesDialogFactory;

    /**
     * The {@link GoLocationDialogManager} to track go location dialog
     * instances.
     */
    @NotNull
    private final AbstractPerMapDialogManager<G, A, R, GoLocationDialog<G, A, R>> goLocationDialogManager;

    /**
     * The active map view, or <code>null</code> if no map view exists.
     */
    @Nullable
    private MapView<G, A, R> currentMapView;

    /**
     * The map view manager listener used to detect changed current maps.
     */
    @NotNull
    private final MapViewManagerListener<G, A, R> mapViewManagerListener = new MapViewManagerListener<G, A, R>() {

        @Override
        public void activeMapViewChanged(@Nullable final MapView<G, A, R> mapView) {
            currentMapView = mapView;
            refreshActions();
        }

        @Override
        public void mapViewCreated(@NotNull final MapView<G, A, R> mapView) {
            mapView.getMapCursor().addMapCursorListener(mapCursorListener);
        }

        @Override
        public void mapViewClosing(@NotNull final MapView<G, A, R> mapView) {
            mapView.getMapCursor().removeMapCursorListener(mapCursorListener);
        }

    };

    /**
     * The map cursor listener used to detect cursor state changes in {@link
     * #currentMapView}.
     */
    @NotNull
    private final MapCursorListener<G, A, R> mapCursorListener = new MapCursorListener<G, A, R>() {

        @Override
        public void mapCursorChangedPos(@Nullable final Point location) {
            refreshActions();
        }

        @Override
        public void mapCursorChangedMode() {
            refreshActions();
        }

        @Override
        public void mapCursorChangedGameObject(@Nullable final MapSquare<G, A, R> mapSquare, @Nullable final G gameObject) {
            // ignore
        }

    };

    /**
     * Create a new instance.
     * @param objectChooser the object chooser
     * @param gameObjectAttributesDialogFactory the factory for creating game
     * object attributes dialog instances
     * @param mapViewManager the map view manager
     */
    public MapCursorActions(@NotNull final ObjectChooser<G, A, R> objectChooser, @NotNull final GameObjectAttributesDialogFactory<G, A, R> gameObjectAttributesDialogFactory, @NotNull final MapViewManager<G, A, R> mapViewManager) {
        this.objectChooser = objectChooser;
        this.gameObjectAttributesDialogFactory = gameObjectAttributesDialogFactory;
        goLocationDialogManager = new GoLocationDialogManager<G, A, R>(mapViewManager);
        aMoveCursor = new Action[directions.length];
        final ActionBuilder actionBuilder = ActionBuilderFactory.getInstance().getActionBuilder("net.sf.gridarta");
        for (final Direction direction : directions) {
            aMoveCursor[direction.ordinal()] = ActionUtils.newAction(actionBuilder, "Map Cursor,Map/Selection", this, "moveCursor" + direction.getId());
        }
        aGoLocation = ActionUtils.newAction(actionBuilder, "Map Cursor", this, "goLocation");
        aSelectSquare = ActionUtils.newAction(actionBuilder, "Map Cursor,Map/Selection", this, "selectSquare");
        aAddToSelection = ActionUtils.newAction(actionBuilder, "Map Cursor,Map/Selection", this, "addToSelection");
        aSubFromSelection = ActionUtils.newAction(actionBuilder, "Map Cursor,Map/Selection", this, "subFromSelection");
        aStartStopDrag = ActionUtils.newAction(actionBuilder, "Map Cursor,Map/Selection", this, "startStopDrag");
        aReleaseDrag = ActionUtils.newAction(actionBuilder, "Map Cursor,Map/Selection", this, "releaseDrag");
        aInsertArch = ActionUtils.newAction(actionBuilder, "Map Cursor,Map", this, "insertArch");
        aDeleteArch = ActionUtils.newAction(actionBuilder, "Map Cursor,Map", this, "deleteArch");
        aSelectArchAbove = ActionUtils.newAction(actionBuilder, "Map Cursor,Selected Square View", this, "selectArchAbove");
        aSelectArchBelow = ActionUtils.newAction(actionBuilder, "Map Cursor,Selected Square View", this, "selectArchBelow");
        aArchAttributes = ActionUtils.newAction(actionBuilder, "Map", this, "archAttributes");
        mapViewManager.addMapViewManagerListener(mapViewManagerListener);
        currentMapView = mapViewManager.getActiveMapView();
        refreshActions();
    }

    /**
     * Action method for "move cursor north".
     */
    @ActionMethod
    public void moveCursorNorth() {
        doMoveCursor(true, Direction.NORTH);
    }

    /**
     * Action method for "move cursor south".
     */
    @ActionMethod
    public void moveCursorSouth() {
        doMoveCursor(true, Direction.SOUTH);
    }

    /**
     * Action method for "move cursor east".
     */
    @ActionMethod
    public void moveCursorEast() {
        doMoveCursor(true, Direction.EAST);
    }

    /**
     * Action method for "move cursor west".
     */
    @ActionMethod
    public void moveCursorWest() {
        doMoveCursor(true, Direction.WEST);
    }

    /**
     * Action method for "move cursor north east".
     */
    @ActionMethod
    public void moveCursorNorthEast() {
        doMoveCursor(true, Direction.NORTH_EAST);
    }

    /**
     * Action method for "move cursor north west".
     */
    @ActionMethod
    public void moveCursorNorthWest() {
        doMoveCursor(true, Direction.NORTH_WEST);
    }

    /**
     * Action method for "move cursor south east".
     */
    @ActionMethod
    public void moveCursorSouthEast() {
        doMoveCursor(true, Direction.SOUTH_EAST);
    }

    /**
     * Action method for "move cursor south west".
     */
    @ActionMethod
    public void moveCursorSouthWest() {
        doMoveCursor(true, Direction.SOUTH_WEST);
    }

    /**
     * Action method for "go location".
     */
    @ActionMethod
    public void goLocation() {
        doGoLocation(true);
    }

    /**
     * Action method for "select square".
     */
    @ActionMethod
    public void selectSquare() {
        doSelectSquare(true);
    }

    /**
     * Action method for "add to selection".
     */
    @ActionMethod
    public void addToSelection() {
        doAddToSelection(true);
    }

    /**
     * Action method for "remove from selection".
     */
    @ActionMethod
    public void subFromSelection() {
        doSubFromSelection(true);
    }

    /**
     * Action method for "start stop drag".
     */
    @ActionMethod
    public void startStopDrag() {
        doStartStopDrag(true);
    }

    /**
     * Action method for "release drag".
     */
    @ActionMethod
    public void releaseDrag() {
        doReleaseDrag(true);
    }

    /**
     * Action method for "insert arch".
     */
    @ActionMethod
    public void insertArch() {
        doInsertArch(true);
    }

    /**
     * Action method for "delete arch".
     */
    @ActionMethod
    public void deleteArch() {
        doDeleteArch(true);
    }

    /**
     * Action method for "select arch above".
     */
    @ActionMethod
    public void selectArchAbove() {
        doSelectArchAbove(true);
    }

    /**
     * Action method for "select arch below".
     */
    @ActionMethod
    public void selectArchBelow() {
        doSelectArchBelow(true);
    }

    /**
     * Action method for "arch attributes".
     */
    @ActionMethod
    public void archAttributes() {
        doArchAttributes(true);
    }

    /**
     * Return the map cursor of the current map view if it is active.
     * @return the map cursor, or <code>null</code> if the cursor is not active,
     *         or if no map view exists
     */
    @Nullable
    private MapCursor<G, A, R> getActiveMapCursor() {
        final MapView<G, A, R> mapView = currentMapView;
        return mapView == null ? null : getActiveMapCursor(mapView);
    }

    /**
     * Return the map cursor of a map view if it is active.
     * @param mapView the map view
     * @return the map cursor, or <code>null</code> if the cursor is not active,
     *         or if no map view exists
     */
    @Nullable
    private MapCursor<G, A, R> getActiveMapCursor(@NotNull final MapView<G, A, R> mapView) {
        final MapCursor<G, A, R> mapCursor = mapView.getMapCursor();
        return mapCursor.isActive() ? mapCursor : null;
    }

    /**
     * Enable/disable menu entries based on the current cursor state.
     */
    private void refreshActions() {
        for (final Direction direction : directions) {
            aMoveCursor[direction.ordinal()].setEnabled(doMoveCursor(false, direction));
        }
        aGoLocation.setEnabled(doGoLocation(false));
        aSelectSquare.setEnabled(doSelectSquare(false));
        aAddToSelection.setEnabled(doAddToSelection(false));
        aSubFromSelection.setEnabled(doSubFromSelection(false));
        aStartStopDrag.setEnabled(doStartStopDrag(false));
        aReleaseDrag.setEnabled(doReleaseDrag(false));
        aInsertArch.setEnabled(doInsertArch(false));
        aDeleteArch.setEnabled(doDeleteArch(false));
        aSelectArchAbove.setEnabled(doSelectArchAbove(false));
        aSelectArchBelow.setEnabled(doSelectArchBelow(false));
        aArchAttributes.setEnabled(doArchAttributes(false));
    }

    private void selectSquare(@NotNull final MapCursor<G, A, R> mapCursor, final SelectionMode mode) {
        mapCursor.dragStart();
        mapCursor.dragSelect(mode);
    }

    /**
     * Executes the "move cursor" action.
     * @param performAction whether the action should be performed
     * @param direction the direction to move the cursor
     * @return whether the action was or can be performed
     */
    private boolean doMoveCursor(final boolean performAction, @NotNull final Direction direction) {
        final MapView<G, A, R> mapView = currentMapView;
        if (mapView == null) {
            return false;
        }

        final MapCursor<G, A, R> mapCursor = getActiveMapCursor(mapView);
        if (mapCursor == null) {
            return false;
        }

        if (!mapCursor.goTo(performAction, direction)) {
            return false;
        }

        if (performAction) {
            final MapRenderer renderer = mapView.getRenderer();
            final Point location = mapCursor.getLocation();
            if (location != null) {
                location.translate(BORDER * direction.getDx(), BORDER * direction.getDy());
                renderer.scrollRectToVisible(renderer.getSquareBounds(location));
            }
        }

        return true;
    }

    /**
     * Executes the "go location" action.
     * @param performAction whether the action should be performed
     * @return whether the action was or can be performed
     */
    private boolean doGoLocation(final boolean performAction) {
        final MapView<G, A, R> mapView = currentMapView;
        if (mapView == null) {
            return false;
        }

        if (performAction) {
            goLocationDialogManager.showDialog(mapView);
        }

        return true;
    }

    /**
     * Executes the "select square" action.
     * @param performAction whether the action should be performed
     * @return whether the action was or can be performed
     */
    private boolean doSelectSquare(final boolean performAction) {
        return doSelection(performAction, SelectionMode.FLIP);
    }

    /**
     * Executes the "add to selection" action.
     * @param performAction whether the action should be performed
     * @return whether the action was or can be performed
     */
    private boolean doAddToSelection(final boolean performAction) {
        return doSelection(performAction, SelectionMode.ADD);
    }

    /**
     * Executes the "sub from selection" action.
     * @param performAction whether the action should be performed
     * @return whether the action was or can be performed
     */
    private boolean doSubFromSelection(final boolean performAction) {
        return doSelection(performAction, SelectionMode.SUB);
    }

    /**
     * Executes an action for changing the selection.
     * @param performAction whether the action should be performed
     * @param mode the type of action to perform
     * @return whether the action was or can be performed
     */
    private boolean doSelection(final boolean performAction, @NotNull final SelectionMode mode) {
        final MapCursor<G, A, R> mapCursor = getActiveMapCursor();
        if (mapCursor == null) {
            return false;
        }

        if (performAction) {
            selectSquare(mapCursor, mode);
        }

        return true;
    }

    /**
     * Executes the "start stop drag" action.
     * @param performAction whether the action should be performed
     * @return whether the action was or can be performed
     */
    private boolean doStartStopDrag(final boolean performAction) {
        final MapCursor<G, A, R> mapCursor = getActiveMapCursor();
        if (mapCursor == null) {
            return false;
        }

        if (performAction) {
            if (mapCursor.isDragging()) {
                mapCursor.dragSelect(SelectionMode.FLIP);
            } else {
                mapCursor.dragStart();
            }
        }

        return true;
    }

    /**
     * Executes the "release drag" action.
     * @param performAction whether the action should be performed
     * @return whether the action was or can be performed
     */
    private boolean doReleaseDrag(final boolean performAction) {
        final MapCursor<G, A, R> mapCursor = getActiveMapCursor();
        if (mapCursor == null) {
            return false;
        }

        if (performAction) {
            if (mapCursor.isDragging()) {
                mapCursor.dragRelease();
            } else {
                final Point location = mapCursor.getLocation();
                mapCursor.deactivate();
                mapCursor.setLocation(location);
            }
        }

        return true;
    }

    /**
     * Executes the "insert arch" action.
     * @param performAction whether the action should be performed
     * @return whether the action was or can be performed
     */
    private boolean doInsertArch(final boolean performAction) {
        final MapCursor<G, A, R> mapCursor = getActiveMapCursor();
        final BaseObject<G, A, R, ?> gameObject = objectChooser.getSelection();
        return mapCursor != null && gameObject != null && mapCursor.insertGameObject(performAction, gameObject, false, true);
    }

    /**
     * Executes the "delete arch" action.
     * @param performAction whether the action should be performed
     * @return whether the action was or can be performed
     */
    private boolean doDeleteArch(final boolean performAction) {
        final MapCursor<G, A, R> mapCursor = getActiveMapCursor();
        return mapCursor != null && mapCursor.deleteSelectedGameObject(performAction);
    }

    /**
     * Executes the "select arch above" action.
     * @param performAction whether the action should be performed
     * @return whether the action was or can be performed
     */
    private boolean doSelectArchAbove(final boolean performAction) {
        final MapCursor<G, A, R> mapCursor = getActiveMapCursor();
        return mapCursor != null && mapCursor.selectAbove(performAction);
    }

    /**
     * Executes the "select arch below" action.
     * @param performAction whether the action should be performed
     * @return whether the action was or can be performed
     */
    private boolean doSelectArchBelow(final boolean performAction) {
        final MapCursor<G, A, R> mapCursor = getActiveMapCursor();
        return mapCursor != null && mapCursor.selectBelow(performAction);
    }

    /**
     * Executes the "arch attributes" action.
     * @param performAction whether the action should be performed
     * @return whether the action was or can be performed
     */
    private boolean doArchAttributes(final boolean performAction) {
        final MapCursor<G, A, R> mapCursor = getActiveMapCursor();
        if (mapCursor == null) {
            return false;
        }

        final G gameObject = mapCursor.getGameObject();
        if (gameObject == null) {
            return false;
        }

        if (performAction) {
            gameObjectAttributesDialogFactory.showAttributeDialog(gameObject);
        }

        return true;
    }

}
