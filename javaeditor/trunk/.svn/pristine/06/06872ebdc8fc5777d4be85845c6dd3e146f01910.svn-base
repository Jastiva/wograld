/*
 * Gridarta MMORPG map editor for Crossfire, Daimonin and similar games.
 * Copyright (C) 2000-2010 The Gridarta Developers.
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

package net.sf.gridarta.model.mapcursor;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.baseobject.BaseObject;
import net.sf.gridarta.model.direction.Direction;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapgrid.MapGrid;
import net.sf.gridarta.model.mapgrid.MapGridEvent;
import net.sf.gridarta.model.mapgrid.MapGridListener;
import net.sf.gridarta.model.mapgrid.SelectionMode;
import net.sf.gridarta.model.mapmodel.MapModel;
import net.sf.gridarta.model.mapmodel.MapSquare;
import net.sf.gridarta.utils.EventListenerList2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * MapCursor provides methods to move and drag on map. The MapCursor is bound to
 * a {@link MapGrid} which will change it flags depending on MapCursor
 * movement/selection. <p/> There are three different states for MapCursor: <ul>
 * <li>Deactivated</li> <li>On the map</li> <li>Dragging</li> </ul> When
 * coordinates or state of MapCursor changes an event is fired.
 * @author <a href="mailto:dlviegas@gmail.com">Daniel Viegas</a>
 * @author Andreas Kirschbaum
 */
public class MapCursor<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> {

    /**
     * Current cursor position. When cursor is active it is highlighted on the
     * MapGrid.
     */
    @NotNull
    private final Point pos = new Point();

    /**
     * Position where dragging has started.
     */
    @NotNull
    private final Point dragStart = new Point();

    /**
     * Offset of dragging. (0, 0) when drag is on the starting position.
     */
    @NotNull
    private final Dimension dragOffset = new Dimension();

    /**
     * Whether the cursor is currently on map.
     */
    private boolean onMap;

    /**
     * Gets set to <code>true</code> when in drag mode.
     */
    private boolean dragging;

    /**
     * Grid where cursor is bound to.
     */
    @NotNull
    private final MapGrid mapGrid;

    /**
     * The {@link MapModel} of this cursor.
     */
    @NotNull
    private final MapModel<G, A, R> mapModel;

    /**
     * Used to test if coordinates are on the map. Created by MapGrid.
     */
    @NotNull
    private final Rectangle mapRec;

    /**
     * Temporary point used in some methods. Placed as instance var to prevent
     * creation of temporary objects.
     */
    @NotNull
    private final Point tmpPoint = new Point();

    /**
     * The selected {@link MapSquare}.
     */
    @Nullable
    private MapSquare<G, A, R> mapSquare;

    /**
     * The selected {@link GameObject}.
     */
    @Nullable
    private G gameObject;

    /**
     * The nesting level of map cursor transactions. Change events are delivered
     * when all nested transactions are finished.
     */
    private int transactionDepth;

    /**
     * The value of {@link #pos} at the start of the outermost map cursor
     * transaction.
     */
    @NotNull
    private final Point transactionPos = new Point();

    /**
     * The value of {@link #onMap} at the start of the outermost map cursor
     * transaction.
     */
    private boolean transactionOnMap;

    /**
     * The value of {@link #dragging} at the start of the outermost map cursor
     * transaction.
     */
    private boolean transactionDragging;

    /**
     * The value of {@link #mapSquare} at the start of the outermost map cursor
     * transaction.
     */
    @Nullable
    private MapSquare<G, A, R> transactionMapSquare;

    /**
     * The value of {@link #gameObject} at the start of the outermost map cursor
     * transaction.
     */
    @Nullable
    private G transactionGameObject;

    /**
     * The MapCursorListeners to inform of changes.
     */
    @NotNull
    private final EventListenerList2<MapCursorListener<G, A, R>> listenerList = new EventListenerList2<MapCursorListener<G, A, R>>(MapCursorListener.class);

    /**
     * Construct a MapCursor. The cursor will be deactivated after
     * construction.
     * @param mapGrid Cursor is bound to this grid
     * @param mapModel the map model of this cursor
     */
    public MapCursor(@NotNull final MapGrid mapGrid, @NotNull final MapModel<G, A, R> mapModel) {
        this.mapGrid = mapGrid;
        this.mapModel = mapModel;
        mapRec = mapGrid.getMapRec();
        final MapGridListener mapGridListener = new MapGridListener() {

            @Override
            public void mapGridChanged(@NotNull final MapGridEvent e) {
                // We can ignore this event. Normally this MapCursor has caused it.
            }

            @Override
            public void mapGridResized(@NotNull final MapGridEvent e) {
                // Test if drag start point is outside map -> move inside
                if (dragging && !mapRec.contains(dragStart)) {
                    dragStart.x = Math.min(dragStart.x, mapRec.width - 1);
                    dragStart.y = Math.min(dragStart.y, mapRec.height - 1);
                }
                // Test if cursor position is outside map -> move inside
                if (onMap && !mapRec.contains(pos)) {
                    beginTransaction();
                    try {
                        pos.x = Math.min(pos.x, mapRec.width - 1);
                        pos.y = Math.min(pos.y, mapRec.height - 1);
                        selectMapSquare();
                    } finally {
                        endTransaction();
                    }
                }
            }

        };
        mapGrid.addMapGridListener(mapGridListener);
    }

    /**
     * Get position of cursor.
     * @return coordinates of cursor or <code>null</code> if cursor is not
     *         active
     */
    @Nullable
    public Point getLocation() {
        return onMap ? new Point(pos) : null;
    }

    /**
     * Move cursor to a new location. If new location is not on map, cursor gets
     * disabled.
     * @param p New location. If <code>p == null</code> cursor gets disabled
     */
    public void setLocation(@Nullable final Point p) {
        beginTransaction();
        try {
            if (p != null && mapRec.contains(p)) {
                if (onMap) {
                    if (!pos.equals(p)) {
                        pos.setLocation(p);
                        selectMapSquare();
                    }
                } else {
                    pos.setLocation(p);
                    selectMapSquare();
                    onMap = true;
                }
            } else {
                onMap = false;
            }
        } finally {
            endTransaction();
        }
    }

    /**
     * Move cursor to a new location. If new location is not on map, cursor does
     * not change.
     * @param p New location. If <code>p == null</code> nothing happens
     * @return <code>true</code> if cursor position changed
     */
    public boolean setLocationSafe(@Nullable final Point p) {
        beginTransaction();
        final boolean hasChanged;
        try {
            if (p != null && mapRec.contains(p)) {
                if (onMap) {
                    if (pos.equals(p)) {
                        hasChanged = false;
                    } else {
                        pos.setLocation(p);
                        selectMapSquare();
                        hasChanged = true;
                    }
                } else {
                    pos.setLocation(p);
                    selectMapSquare();
                    onMap = true;
                    hasChanged = true;
                }
            } else {
                hasChanged = false;
            }
        } finally {
            endTransaction();
        }
        return hasChanged;
    }

    /**
     * Set cursor to drag mode when it is active.
     */
    public void dragStart() {
        if (onMap && !dragging) {
            beginTransaction();
            try {
                dragStart.setLocation(pos);
                mapGrid.preSelect(dragStart, pos);
                dragging = true;
                dragOffset.setSize(0, 0);
            } finally {
                endTransaction();
            }
        }
    }

    /**
     * When in drag mode and the point is on the map cursor is moved to this
     * position. {@link MapGrid} changes pre-selection accordingly.
     * @param p new coordinates
     * @return <code>true</code> when dragging position changed successfully
     */
    public boolean dragTo(@Nullable final Point p) {
        if (p != null && mapRec.contains(p) && dragging && !pos.equals(p)) {
            beginTransaction();
            try {
                final Point oldPos = new Point(pos);
                pos.setLocation(p);
                selectMapSquare();
                mapGrid.updatePreSelect(dragStart, oldPos, pos);
                dragOffset.setSize(pos.x - dragStart.x, pos.y - dragStart.y);
            } finally {
                endTransaction();
            }
            return true;
        }
        return false;
    }

    /**
     * Leave drag mode and undo pre-selection.
     */
    public void dragRelease() {
        if (dragging) {
            beginTransaction();
            try {
                mapGrid.unPreSelect(dragStart, pos);
                dragging = false;
            } finally {
                endTransaction();
            }
        }
    }

    /**
     * Leave drag mode and select pre-selection using selectionMode.
     * @param selectionMode Mode how to change selection state
     * @see SelectionMode
     */
    public void dragSelect(@NotNull final SelectionMode selectionMode) {
        if (dragging) {
            beginTransaction();
            try {
                dragRelease();
                mapGrid.selectArea(dragStart, pos, selectionMode);
            } finally {
                endTransaction();
            }
        }
    }

    /**
     * Cursor gets deactivated. All selections get lost.
     */
    public final void deactivate() {
        beginTransaction();
        try {
            onMap = false;
            mapGrid.unSelect();
        } finally {
            endTransaction();
        }
    }

    /**
     * Get cursor state.
     * @return <code>true</code> if cursor is on the map
     */
    public boolean isActive() {
        return onMap;
    }

    /**
     * Get offset from start position of dragging.
     * @return offset or <code>null</code> when not in drag mode
     * @noinspection NullableProblems
     */
    @Nullable
    public Dimension getDragOffset() {
        return dragging ? dragOffset : null;
    }

    /**
     * Check if point is on grid.
     * @param p Coordinates of point
     * @return <code>true</code> if <code>p != null</code> and point is on grid
     */
    public boolean isOnGrid(@Nullable final Point p) {
        return p != null && mapRec.contains(p);
    }

    /**
     * Moves the cursor one square relative to current position.
     * @param performAction whether the action should be performed
     * @param dir the direction
     * @return <code>true</code> if cursor really moved
     */
    public boolean goTo(final boolean performAction, @NotNull final Direction dir) {
        if (onMap) {
            tmpPoint.setLocation(pos.x + dir.getDx(), pos.y + dir.getDy());
            if (!mapRec.contains(tmpPoint)) {
                return false;
            }
            if (performAction) {
                if (dragging) {
                    dragTo(tmpPoint);
                } else {
                    setLocationSafe(tmpPoint);
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Returns whether the cursor is currently being dragged.
     * @return <code>true</code> if dragging is active, otherwise
     *         <code>false</code>.
     */
    public boolean isDragging() {
        return dragging;
    }

    /**
     * Register a MapCursorListener.
     * @param listener MapCursorListener to register
     */
    public void addMapCursorListener(@NotNull final MapCursorListener<G, A, R> listener) {
        listenerList.add(listener);
    }

    /**
     * Remove a MapCursorListener.
     * @param listener MapCursorListener to remove
     */
    public void removeMapCursorListener(@NotNull final MapCursorListener<G, A, R> listener) {
        listenerList.remove(listener);
    }

    /**
     * Returns the selected {@link GameObject}.
     * @return the selected game object or <code>null</code> if the map cursor
     *         is not active or if the selected map square is empty
     */
    @Nullable
    public G getGameObject() {
        return gameObject;
    }

    /**
     * Sets the selected {@link GameObject}. If the game object is not on a map
     * or not on the map this cursor is attached to, the cursor is deactivated.
     * @param gameObject the selected game object or <code>null</code> to
     * deactivate the cursor
     */
    public void setGameObject(@Nullable final G gameObject) {
        beginTransaction();
        try {
            if (gameObject == null) {
                this.gameObject = null;
            } else {
                final MapSquare<G, A, R> mapSquare = gameObject.getMapSquare();
                if (mapSquare == null || mapSquare.getMapModel() != mapModel) {
                    onMap = false;
                } else {
                    onMap = true;
                    pos.setLocation(mapSquare.getMapX(), mapSquare.getMapY());
                    this.mapSquare = mapSquare;
                    this.gameObject = gameObject;
                }
            }
        } finally {
            endTransaction();
        }
    }

    /**
     * Sets the selected {@link MapSquare}. If the map square does not belong to
     * the map this cursor is attached to, the cursor is deactivated.
     * @param mapSquare the selected map square or <code>null</code> to
     * deactivate the cursor
     */
    public void setMapSquare(@Nullable final MapSquare<G, A, R> mapSquare) {
        beginTransaction();
        try {
            if (mapSquare == null || mapSquare.getMapModel() != mapModel) {
                onMap = false;
            } else {
                onMap = true;
                pos.setLocation(mapSquare.getMapX(), mapSquare.getMapY());
                this.mapSquare = mapSquare;
                selectTopmostGameObject();
            }
        } finally {
            endTransaction();
        }
    }

    /**
     * Start a new transaction. Transactions may be nested. Transactions serve
     * the purpose of firing events to the views when more changes are known to
     * come before the view is really required to update. Each invocation of
     * this function requires its own invocation of {@link #endTransaction()}.
     */
    public void beginTransaction() {
        if (transactionDepth == 0) {
            transactionPos.setLocation(pos);
            transactionOnMap = onMap;
            transactionDragging = dragging;
            transactionMapSquare = mapSquare;
            transactionGameObject = gameObject;
        }
        transactionDepth++;
        mapGrid.beginTransaction();
    }

    /**
     * End a transaction. Invoking this method will reduce the transaction depth
     * by 1. <p/> If the last transaction is ended, the changes are committed.
     */
    public void endTransaction() {
        assert transactionDepth > 0;
        transactionDepth--;

        if (!onMap) {
            dragging = false;
            mapSquare = null;
            gameObject = null;
        }

        if (transactionDepth == 0) {
            final boolean changedPos = !pos.equals(transactionPos) || mapSquare != transactionMapSquare;
            final boolean changedMode = onMap != transactionOnMap || dragging != transactionDragging;
            final boolean changedGameObject = gameObject != transactionGameObject;
            if (!onMap) {
                mapGrid.unSelect();
            }
            if (transactionOnMap && (!onMap || !pos.equals(transactionPos))) {
                mapGrid.unSetCursor(transactionPos);
            }
            if (onMap && (!transactionOnMap || !pos.equals(transactionPos))) {
                mapGrid.setCursor(pos);
            }
            if (changedMode) {
                for (final MapCursorListener<G, A, R> listener : listenerList.getListeners()) {
                    listener.mapCursorChangedMode();
                }
            }
            if (changedPos) {
                for (final MapCursorListener<G, A, R> listener : listenerList.getListeners()) {
                    listener.mapCursorChangedPos(getLocation());
                }
            }
            if (changedGameObject) {
                for (final MapCursorListener<G, A, R> listener : listenerList.getListeners()) {
                    listener.mapCursorChangedGameObject(mapSquare, gameObject);
                }
            }
        }

        mapGrid.endTransaction();
    }

    /**
     * Selects the map square on the current map location. Must be called from
     * within a map cursor transaction.
     */
    private void selectMapSquare() {
        assert transactionDepth > 0;
        try {
            mapSquare = mapModel.getMapSquare(pos);
        } catch (final IndexOutOfBoundsException ignored) {
            mapSquare = null;
        }
        selectTopmostGameObject();
    }

    /**
     * Selects the last (top-most) {@link GameObject} on the current map square.
     * Must be called from within a map cursor transaction.
     */
    private void selectTopmostGameObject() {
        assert transactionDepth > 0;
        gameObject = mapSquare == null ? null : mapSquare.getLast();
    }

    /**
     * Moves the selected {@link GameObject}. Does nothing if no game object is
     * selected.
     * @param performAction whether the action should be performed
     * @return whether the action was or can be performed
     */
    public boolean selectAbove(final boolean performAction) {
        if (mapSquare == null || gameObject == null) {
            return false;
        }

        final G newGameObject = mapSquare.getPrev(gameObject);
        if (newGameObject == null) {
            return false;
        }

        if (performAction) {
            beginTransaction();
            try {
                gameObject = newGameObject;
            } finally {
                endTransaction();
            }
        }
        return true;
    }

    /**
     * Moves the selected {@link GameObject}. Does nothing if no game object is
     * selected.
     * @param performAction whether the action should be performed
     * @return whether the action was or can be performed
     */
    public boolean selectBelow(final boolean performAction) {
        if (mapSquare == null || gameObject == null) {
            return false;
        }

        final G newGameObject = mapSquare.getNext(gameObject);
        if (newGameObject == null) {
            return false;
        }

        if (performAction) {
            beginTransaction();
            try {
                gameObject = newGameObject;
            } finally {
                endTransaction();
            }
        }
        return true;
    }

    /**
     * Inserts a {@link GameObject} before the selected game object. Does
     * nothing if no game object is selected.
     * @param performAction whether the action should be performed
     * @param gameObject the game object to insert
     * @param insertAtEnd whether to ignore the selected game object and insert
     * at the end
     * @param join if set, auto-joining is supported
     * @return whether the action was or can be performed
     */
    public boolean insertGameObject(final boolean performAction, @NotNull final BaseObject<G, A, R, ?> gameObject, final boolean insertAtEnd, final boolean join) {
        if (!onMap) {
            return false;
        }

        if (performAction) {
            mapModel.beginTransaction("Insert"); // TODO; I18N/L10N
            try {
                final G insertedGameObject = mapModel.insertArchToMap(gameObject, insertAtEnd ? null : this.gameObject, pos, join);
                if (insertedGameObject != null) {
                    beginTransaction();
                    try {
                        this.gameObject = insertedGameObject;
                    } finally {
                        endTransaction();
                    }
                }
            } finally {
                mapModel.endTransaction();
            }
        }
        return true;
    }

    /**
     * Deletes the selected game object. Does nothing if no game object is
     * selected.
     * @param performAction whether the action should be performed
     * @return whether the action was or can be performed
     */
    public boolean deleteSelectedGameObject(final boolean performAction) {
        if (!onMap) {
            return false;
        }

        final G gameObject = this.gameObject;
        if (gameObject == null) {
            return false;
        }

        final MapSquare<G, A, R> mapSquare = this.mapSquare;
        if (mapSquare == null) {
            return false;
        }

        if (performAction) {
            mapModel.beginTransaction("Delete"); // TODO; I18N/L10N
            try {
                G nextGameObject = mapSquare.getNext(gameObject);
                mapModel.removeGameObject(gameObject, true);
                if (nextGameObject == null) {
                    nextGameObject = mapSquare.getFirst();
                }
                if (nextGameObject != null) {
                    while (true) {
                        final G invGameObject = nextGameObject.getFirst();
                        if (invGameObject == null) {
                            break;
                        }
                        nextGameObject = invGameObject;
                    }
                }
                beginTransaction();
                try {
                    this.gameObject = nextGameObject;
                } finally {
                    endTransaction();
                }
            } finally {
                mapModel.endTransaction();
            }
        }

        return true;
    }

}
