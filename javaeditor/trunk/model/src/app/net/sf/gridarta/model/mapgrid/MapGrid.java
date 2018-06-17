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

package net.sf.gridarta.model.mapgrid;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import net.sf.gridarta.utils.EventListenerList2;
import net.sf.gridarta.utils.Size2D;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * 2D-Grid containing flags for selection, pre-selection, cursor, warnings and
 * errors. This class provides methods to modify these flags. Normally a map
 * cursor is used to access them. Selection flags are not changed directly.
 * First pre-selection flags have to be set with {@link #preSelect(Point,
 * Point)}, then {@link #selectArea(Point, Point, SelectionMode)} will change
 * the selection flags according to {@link SelectionMode}. This allows
 * visualisation of the dragging process when selecting an area. <p/> Every
 * change of the grid size or flags fires a {@link MapGridEvent} and informs all
 * registered {@link MapGridListener MapGridListeners}.
 * @author <a href="mailto:dlviegas@gmail.com">Daniel Viegas</a>
 * @author Andreas Kirschbaum
 */
public class MapGrid {

    /**
     * 2D-array to store grid flags.
     */
    @NotNull
    private int[][] gridFlags;

    /**
     * Left upper coordinates of rectangle that is being processed.
     */
    @NotNull
    private final Point cornerMin = new Point();

    /**
     * Right lower coordinates of rectangle that is being processed.
     */
    @NotNull
    private final Point cornerMax = new Point();

    /**
     * Size of <code>gridFlags[][]</code>.
     */
    @NotNull
    private Size2D gridSize;

    /**
     * Rectangle to store location of last grid change.
     */
    @NotNull
    private final Rectangle recChange = new Rectangle();

    /**
     * If set, {@link #cachedSelectedRec} is up-to-date.
     */
    private boolean cachedSelectedRecValid;

    /**
     * The return value for {@link #getSelectedRec()}. Only valid if {@link
     * #cachedSelectedRecValid} is set.
     */
    @Nullable
    private Rectangle cachedSelectedRec;

    /**
     * Selection - marks all selected squares.
     */
    public static final int GRID_FLAG_SELECTION = 1;

    /**
     * Pre-selection - used to preselect squares.
     */
    public static final int GRID_FLAG_SELECTING = 1 << 1;

    /**
     * Flag to highlight as information. RFU.
     */
    public static final int GRID_FLAG_INFORMATION = 1 << 2;

    /**
     * Flag to highlight as warning. RFU.
     */
    public static final int GRID_FLAG_WARNING = 1 << 3;

    /**
     * Flag to highlight as error.
     */
    public static final int GRID_FLAG_ERROR = 1 << 4;

    /**
     * Flag to highlight as fatal. RFU.
     */
    public static final int GRID_FLAG_FATAL = 1 << 5;

    /**
     * Flag to highlight as part of a connection group. RFU.
     */
    public static final int GRID_FLAG_CONNECTION = 1 << 6;

    /**
     * Flag to highlight cursor position. Normally there will only be up to one
     * square that has this flag set.
     */
    public static final int GRID_FLAG_CURSOR = 1 << 7;

    /**
     * Selection - is set for squares at the north edge of the selected area.
     */
    public static final int GRID_FLAG_SELECTION_NORTH = 1 << 8;

    /**
     * Selection - is set for squares at the east edge of the selected area.
     */
    public static final int GRID_FLAG_SELECTION_EAST = 1 << 9;

    /**
     * Selection - is set for squares at the south edge of the selected area.
     */
    public static final int GRID_FLAG_SELECTION_SOUTH = 1 << 10;

    /**
     * Selection - is set for squares at the west edge of the selected area.
     */
    public static final int GRID_FLAG_SELECTION_WEST = 1 << 11;

    /**
     * The MapGridListeners to inform of changes.
     */
    @NotNull
    private final EventListenerList2<MapGridListener> listenerList = new EventListenerList2<MapGridListener>(MapGridListener.class);

    /**
     * Set to size of grid size. Used to test if coordinates are on the map.
     */
    @NotNull
    private final Rectangle mapRec = new Rectangle();

    /**
     * The transaction depth. A value of 0 means there's no transaction going
     * on. A value > 0 means there's a transaction going on and denotes the
     * nesting level.
     * @invariant transactionDepth >= 0
     */
    private int transactionDepth;

    /**
     * The thread that performs the current transaction.
     * @invariant transactionDepth > 0 || transactionThread == null
     */
    @Nullable
    private Thread transactionThread;

    /**
     * Creates a new instance.
     * @param gridSize the size of the grid
     */
    public MapGrid(@NotNull final Size2D gridSize) {
        mapRec.setBounds(0, 0, gridSize.getWidth(), gridSize.getHeight());
        this.gridSize = gridSize;
        gridFlags = new int[gridSize.getWidth()][gridSize.getHeight()];
    }

    /**
     * Registers a MapGridListener.
     * @param listener the listener to register
     */
    public void addMapGridListener(@NotNull final MapGridListener listener) {
        listenerList.add(listener);
    }

    /**
     * Removes a MapGridListener.
     * @param listener the listener to remove
     */
    public void removeMapGridListener(@NotNull final MapGridListener listener) {
        listenerList.remove(listener);
    }

    /**
     * Returns a {@link Rectangle} with position and dimension of this grid.
     * @return a rectangle with position and dimension of this grid
     */
    @NotNull
    public Rectangle getMapRec() {
        return mapRec;
    }

    /**
     * Resizes the MapGrid. Old flags will be copied to the new grid.
     * @param newSize the new grid size
     */
    public void resize(@NotNull final Size2D newSize) {
        if (gridSize.equals(newSize)) {
            // Nothing to do
            return;
        }
        final int[][] newGridFlags = new int[newSize.getWidth()][newSize.getHeight()];
        final Point pos = new Point();
        final Size2D minSize = new Size2D(Math.min(newSize.getWidth(), gridSize.getWidth()), Math.min(newSize.getHeight(), gridSize.getHeight()));
        if (newSize.getWidth() < gridSize.getWidth()) {
            unsetFlags(newSize.getWidth(), 0, newSize.getWidth(), minSize.getHeight() - 1, GRID_FLAG_SELECTION);
        }
        if (newSize.getHeight() < gridSize.getHeight()) {
            unsetFlags(0, newSize.getHeight(), minSize.getWidth() - 1, newSize.getHeight(), GRID_FLAG_SELECTION);
        }
        for (pos.x = 0; pos.x < minSize.getWidth(); pos.x++) {
            for (pos.y = 0; pos.y < minSize.getHeight(); pos.y++) {
                newGridFlags[pos.x][pos.y] = gridFlags[pos.x][pos.y];
            }
        }
        gridFlags = newGridFlags;
        gridSize = newSize;
        cachedSelectedRecValid = false;
        mapRec.setSize(newSize.getWidth(), newSize.getHeight());
        fireMapGridResizeEvent();
    }

    /**
     * Inform all registered listeners that the flags on MapGrid have changed.
     */
    private void fireMapGridChangedEvent() {
        final MapGridEvent e = new MapGridEvent(this);
        for (final MapGridListener listener : listenerList.getListeners()) {
            listener.mapGridChanged(e);
        }
    }

    /**
     * Informs all registered listeners that the size of MapGrid has changed.
     */
    private void fireMapGridResizeEvent() {
        final MapGridEvent e = new MapGridEvent(this);
        for (final MapGridListener listener : listenerList.getListeners()) {
            listener.mapGridResized(e);
        }
    }

    /**
     * Clears all selection and pre-selection flags from the grid.
     */
    public void unSelect() {
        beginTransaction();
        try {
            unsetFlags(0, 0, gridSize.getWidth() - 1, gridSize.getHeight() - 1, GRID_FLAG_SELECTION | GRID_FLAG_SELECTING);
        } finally {
            endTransaction();
        }
    }

    /**
     * Un-highlights the given cursor position.
     * @param pos the map coordinates of square to un-highlight
     */
    public void unSetCursor(@NotNull final Point pos) {
        beginTransaction();
        try {
            try {
                
               unsetFlags(pos.x, pos.y, pos.x, pos.y, GRID_FLAG_CURSOR);
          //    unsetFlags(pos.x+1, pos.y, pos.x+1, pos.y, GRID_FLAG_CURSOR);
            } catch (final ArrayIndexOutOfBoundsException ignored) {
                // happens after map resizes if the map cursor was within the cut off area
            }
        } finally {
            endTransaction();
        }
    }

    /**
     * Highlights the given cursor position.
     * @param pos the map coordinates of square to highlight
     */
    public void setCursor(@NotNull final Point pos) {
        beginTransaction();
        try {
            
           setFlags(pos.x, pos.y, pos.x, pos.y, GRID_FLAG_CURSOR);
        //   setFlags(pos.x+1, pos.y, pos.x+1, pos.y, GRID_FLAG_CURSOR);
        } finally {
            endTransaction();
        }
    }

    /**
     * Rectangle defined by two points gets preselected. The points can be on
     * any corner as long as they are opposite to each other.
     * @param start any point on a corner of the rectangle
     * @param end the point on the opposite corner
     * @see MapGrid#unPreSelect(Point, Point)
     */
    public void preSelect(@NotNull final Point start, @NotNull final Point end) {
        beginTransaction();
        try {
            calculateRec(start, end);
            
            setFlags(cornerMin.x, cornerMin.y, cornerMax.x, cornerMax.y, GRID_FLAG_SELECTING);
         //    setFlags(cornerMin.x+1, cornerMin.y, cornerMax.x+1, cornerMax.y, GRID_FLAG_SELECTING);
        } finally {
            endTransaction();
        }
    }

    /**
     * Update the pre-selection rectangle. The effect is identical to calling
     * <code>unPreSelect(start, oldEnd); preSelect(start, newEnd);</code> except
     * that smaller change events are generated.
     * @param start the coordinate of the common first point
     * @param oldEnd the coordinate of the end point for selection deletion
     * @param newEnd the coordinate of the end point for selection addition
     * @note This function assumes that all of (start-oldEnd) has set
     * GRID_FLAG_SELECTING but no other square has set GRID_FLAG_SELECTING; if
     * this precondition does not hold, it does not correctly update the flags
     */
    public void updatePreSelect(@NotNull final Point start, @NotNull final Point oldEnd, @NotNull final Point newEnd) {
        final Point old1 = new Point(Math.min(start.x, oldEnd.x), Math.min(start.y, oldEnd.y));
        final Point old2 = new Point(Math.max(start.x, oldEnd.x), Math.max(start.y, oldEnd.y));
        final Point new1 = new Point(Math.min(start.x, newEnd.x), Math.min(start.y, newEnd.y));
        final Point new2 = new Point(Math.max(start.x, newEnd.x), Math.max(start.y, newEnd.y));
        beginTransaction();
        try {
            // delete old selection
            if (old1.x < new1.x) {
               unsetFlags(old1.x, old1.y, new1.x - 1, old2.y, GRID_FLAG_SELECTING);
             //   unsetFlags(old1.x+1, old1.y, new1.x, old2.y, GRID_FLAG_SELECTING);
                old1.x = new1.x;
            }
            if (new2.x < old2.x) {
                unsetFlags(new2.x + 1, old1.y, old2.x, old2.y, GRID_FLAG_SELECTING);
              //  unsetFlags(new2.x + 2, old1.y, old2.x+1, old2.y, GRID_FLAG_SELECTING);
                old2.x = new2.x;
            }
            if (old1.y < new1.y) {
                unsetFlags(old1.x, old1.y, old2.x, new1.y - 1, GRID_FLAG_SELECTING);
              //   unsetFlags(old1.x+1, old1.y, old2.x+1, new1.y - 1, GRID_FLAG_SELECTING);
                old1.y = new1.y;
            }
            if (new2.y < old2.y) {
                unsetFlags(old1.x, new2.y + 1, old2.x, old2.y, GRID_FLAG_SELECTING);
              //  unsetFlags(old1.x+1, new2.y + 1, old2.x+1, old2.y, GRID_FLAG_SELECTING);
                old2.y = new2.y;
            }

            // add new selection
            if (new1.x < old1.x) {
                setFlags(new1.x, new1.y, old1.x - 1, new2.y, GRID_FLAG_SELECTING);
               // setFlags(new1.x+1, new1.y, old1.x , new2.y, GRID_FLAG_SELECTING);
                old1.x = new1.x;
            }
            if (old2.x < new2.x) {
               setFlags(old2.x + 1, new1.y, new2.x, new2.y, GRID_FLAG_SELECTING);
             //   setFlags(old2.x + 2, new1.y, new2.x+1, new2.y, GRID_FLAG_SELECTING);
                old2.x = new2.x;
            }
            if (new1.y < old1.y) {
                setFlags(new1.x, new1.y, new2.x, old1.y - 1, GRID_FLAG_SELECTING);
              //  setFlags(new1.x+1, new1.y, new2.x+1, old1.y - 1, GRID_FLAG_SELECTING);
                
                old1.y = new1.y;
            }
            if (old2.y < new2.y) {
                setFlags(new1.x, old2.y + 1, new2.x, new2.y, GRID_FLAG_SELECTING);
             //   setFlags(new1.x+1, old2.y + 1, new2.x+1, new2.y, GRID_FLAG_SELECTING);
                old2.y = new2.y;
            }

            // sanity check
            assert old1.x == new1.x;
            assert old2.x == new2.x;
            assert old1.y == new1.y;
            assert old2.y == new2.y;
        } finally {
            endTransaction();
        }
    }

    /**
     * Pre-selection of rectangle defined by points gets deleted.
     * @param start the coordinates of the first corner
     * @param end the coordinates of the opposite corner
     * @see MapGrid#preSelect(Point, Point)
     */
    public void unPreSelect(@NotNull final Point start, @NotNull final Point end) {
        beginTransaction();
        try {
            calculateRec(start, end);
            unsetFlags(cornerMin.x, cornerMin.y, cornerMax.x, cornerMax.y, GRID_FLAG_SELECTING);
         //    unsetFlags(cornerMin.x+1, cornerMin.y, cornerMax.x+1, cornerMax.y, GRID_FLAG_SELECTING);
        } finally {
            endTransaction();
        }
    }

    /**
     * Selects or deselects a single square.
     * @param pos the square
     * @param selectionMode the selection mode
     */
    public void select(@NotNull final Point pos, @NotNull final SelectionMode selectionMode) {
        selectArea(pos, pos, selectionMode);
    }

    /**
     * Selects or deselects all squares in an area.
     * @param pos1 the first corner of rectangle
     * @param pos2 the opposite corner of rectangle
     * @param selectionMode the selection mode
     */
    public void selectArea(@NotNull final Point pos1, @NotNull final Point pos2, @NotNull final SelectionMode selectionMode) {
        beginTransaction();
        try {
            calculateRec(pos1, pos2);
            switch (selectionMode) {
            case ADD:
                setFlags(cornerMin.x, cornerMin.y, cornerMax.x, cornerMax.y, GRID_FLAG_SELECTION);
            //    setFlags(cornerMin.x+1, cornerMin.y, cornerMax.x+1, cornerMax.y, GRID_FLAG_SELECTION);
                break;
            case SUB:
               unsetFlags(cornerMin.x, cornerMin.y, cornerMax.x, cornerMax.y, GRID_FLAG_SELECTION);
             //    unsetFlags(cornerMin.x+1, cornerMin.y, cornerMax.x+1, cornerMax.y, GRID_FLAG_SELECTION);
                break;
            case FLIP:
                toggleFlags(cornerMin.x, cornerMin.y, cornerMax.x, cornerMax.y, GRID_FLAG_SELECTION);
           //     toggleFlags(cornerMin.x+1, cornerMin.y, cornerMax.x+1, cornerMax.y, GRID_FLAG_SELECTION);
                break;
            }
        } finally {
            endTransaction();
        }
    }

    /**
     * <code>minX, maxX, minY, maxY</code> are set according to the the points
     * <code>p1</code> and <code>p2</code>.
     * @param p1 the coordinates of one corner of a rectangle
     * @param p2 the coordinates of the opposite corner
     */
    private void calculateRec(@NotNull final Point p1, @NotNull final Point p2) {
        if (p1.x > p2.x) {
            cornerMin.x = p2.x;
            cornerMax.x = p1.x;
        } else {
            cornerMin.x = p1.x;
            cornerMax.x = p2.x;
        }
        if (p1.y > p2.y) {
            cornerMin.y = p2.y;
            cornerMax.y = p1.y;
        } else {
            cornerMin.y = p1.y;
            cornerMax.y = p2.y;
        }
    }

    /**
     * Checks if a square has the error flag set.
     * @param p the point to check
     * @return <code>true</code> if {@link #GRID_FLAG_ERROR} is set
     */
    public boolean hasError(@NotNull final Point p) {
        return (gridFlags[p.x][p.y] & GRID_FLAG_ERROR) != 0;
    }

    /**
     * Returns the flags of a square.
     * @param x the x-coordinate of the square
     * @param y the y-coordinate of the square
     * @return the grid flags
     */
    public int getFlags(final int x, final int y) {
        return gridFlags[x][y];
    }

    /**
     * Returns the flags of a square.
     * @param p the coordinates of the square
     * @return the grid flags
     */
    public int getFlags(@NotNull final Point p) {
        return gridFlags[p.x][p.y];
    }

    /**
     * Returns a rectangle where the grid was changed. Width and height is 0 for
     * single squares.
     * @return the changed rectangle
     */
    @NotNull
    public Rectangle getRecChange() {
        return new Rectangle(recChange);
    }

    /**
     * Returns size of grid.
     * @return the size of gri
     */
    @NotNull
    public Size2D getSize() {
        return gridSize;
    }

    /**
     * Returns the smallest rectangle containing selection.
     * @return the rectangle containing selection or <code>null</code> if
     *         nothing is selected
     */
    @Nullable
    public Rectangle getSelectedRec() {
        calculateCachedSelectedRec();
        return cachedSelectedRec == null ? null : new Rectangle(cachedSelectedRec);
    }

    /**
     * Makes sure the value of {@link #cachedSelectedRec} if up-to-date.
     */
    private void calculateCachedSelectedRec() {
        if (cachedSelectedRecValid) {
            return;
        }
        cachedSelectedRecValid = true;

        int x1 = -1;
        for (int x = 0; x < gridSize.getWidth(); x++) {
            for (int y = 0; y < gridSize.getHeight(); y++) {
                if ((gridFlags[x][y] & GRID_FLAG_SELECTION) > 0) {
                    x1 = x;
                    break;
                }
            }
            if (x1 >= 0) {
                break;
            }
        }
        if (x1 < 0) {
            cachedSelectedRec = null;
            return;
        }

        int x2 = -1;
        for (int x = gridSize.getWidth() - 1; x >= x1; x--) {
            for (int y = 0; y < gridSize.getHeight(); y++) {
                if ((gridFlags[x][y] & GRID_FLAG_SELECTION) > 0) {
                    x2 = x;
                    break;
                }
            }
            if (x2 >= 0) {
                break;
            }
        }
        int y1 = -1;
        for (int y = 0; y < gridSize.getHeight(); y++) {
            for (int x = 0; x < gridSize.getWidth(); x++) {
                if ((gridFlags[x][y] & GRID_FLAG_SELECTION) > 0) {
                    y1 = y;
                    break;
                }
            }
            if (y1 >= 0) {
                break;
            }
        }
        int y2 = -1;
        for (int y = gridSize.getHeight() - 1; y >= y1; y--) {
            for (int x = 0; x < gridSize.getWidth(); x++) {
                if ((gridFlags[x][y] & GRID_FLAG_SELECTION) > 0) {
                    y2 = y;
                    break;
                }
            }
            if (y2 >= 0) {
                break;
            }
        }
        cachedSelectedRec = new Rectangle(x1, y1, x2 - x1 + 1, y2 - y1 + 1);
    }

    /**
     * Marks all squares as selected.
     */
    public void selectAll() {
        beginTransaction();
        try {
            setFlags(0, 0, gridSize.getWidth() - 1, gridSize.getHeight() - 1, GRID_FLAG_SELECTION);
        } finally {
            endTransaction();
        }
    }

    /**
     * Inverts all selected squares.
     */
    public void invertSelection() {
        beginTransaction();
        try {
            toggleFlags(0, 0, gridSize.getWidth() - 1, gridSize.getHeight() - 1, GRID_FLAG_SELECTION);
        } finally {
            endTransaction();
        }
    }

    /**
     * Sets the error flag at given coordinates.
     * @param x the x coordinate
     * @param y the y coordinate
     */
    public void setError(final int x, final int y) {
        beginTransaction();
        try {
            setFlags(x, y, x, y, GRID_FLAG_ERROR);
        } finally {
            endTransaction();
        }
    }

    /**
     * Clears all error flags.
     */
    public void clearErrors() {
        beginTransaction();
        try {
            unsetFlags(0, 0, gridSize.getWidth() - 1, gridSize.getHeight() - 1, GRID_FLAG_ERROR);
        } finally {
            endTransaction();
        }
    }

    /**
     * Begins a set of changes.
     * @note Until {@link #endRecChange()} is called, {@link #recChange} does
     * not contain a valid rectangle: <code>width</code> and <code>height</code>
     * is used as coordinates.
     */
    private void beginRecChange() {
        recChange.x = gridSize.getWidth();
        recChange.y = gridSize.getHeight();
        recChange.width = 0;
        recChange.height = 0;
    }

    /**
     * Adds a point to the set of changes.
     * @param x the x-coordinate
     * @param y the y-coordinate
     */
    private void updateRecChange(final int x, final int y) {
        if (recChange.x > x) {
            recChange.x = x;
        }
        if (recChange.y > y) {
            recChange.y = y;
        }
        if (recChange.width < x) {
            recChange.width = x;
        }
        if (recChange.height < y) {
            recChange.height = y;
        }
    }

    /**
     * Ends the set of changes and store the bounding box for all recorded
     * changes in {@link #recChange}.
     * @return <code>true</code> if the set of changes is not empty
     */
    private boolean endRecChange() {
        recChange.width = recChange.width - recChange.x + 1;
        recChange.height = recChange.height - recChange.y + 1;
        return recChange.width > 0 && recChange.height > 0;
    }

    /**
     * Sets flags in a rectangle and generate a grid change event.
     * @param minX the left x-coordinate
     * @param minY the left y-coordinate
     * @param maxX the right x-coordinate
     * @param maxY the right y-coordinate
     * @param flags the flags to set
     */
    private void setFlags(final int minX, final int minY, final int maxX, final int maxY, final int flags) {
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                if ((gridFlags[x][y] & flags) != flags) {
                    if ((flags & GRID_FLAG_SELECTION) != 0 && (gridFlags[x][y] & GRID_FLAG_SELECTION) == 0) {
                        updateSelectionFlag(x, y, true);
                    }
                    gridFlags[x][y] |= flags;
                    updateRecChange(x, y);
                    cachedSelectedRecValid = false;
                }
            }
        }
    }

    /**
     * Resets flags in a rectangle and generate a grid change event.
     * @param minX the left x-coordinate
     * @param minY the left y-coordinate
     * @param maxX the right x-coordinate
     * @param maxY the right y-coordinate
     * @param flags the flags to reset
     */
    private void unsetFlags(final int minX, final int minY, final int maxX, final int maxY, final int flags) {
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                if ((gridFlags[x][y] & flags) != 0) {
                    if ((flags & GRID_FLAG_SELECTION) != 0 && (gridFlags[x][y] & GRID_FLAG_SELECTION) != 0) {
                        updateSelectionFlag(x, y, false);
                    }
                    gridFlags[x][y] &= ~flags;
                    updateRecChange(x, y);
                    cachedSelectedRecValid = false;
                }
            }
        }
    }

    /**
     * Flips flags in a rectangle and generate a grid change event.
     * @param minX the left x-coordinate
     * @param minY the left y-coordinate
     * @param maxX the right x-coordinate
     * @param maxY the right y-coordinate
     * @param flags the flags to reset
     */
    private void toggleFlags(final int minX, final int minY, final int maxX, final int maxY, final int flags) {
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                updateSelectionFlag(x, y, (gridFlags[x][y] & flags) == 0);
                gridFlags[x][y] ^= flags;
            }
        }
        cachedSelectedRecValid = false;
        updateRecChange(minX, minY);
        updateRecChange(maxX, maxY);
    }

    /**
     * Starts a new transaction. Transactions may be nested. Transactions serve
     * the purpose of firing events to the views when more changes are known to
     * come before the view is really required to update. Each invocation of
     * this function requires its own invocation of {@link #endTransaction()}.
     */
    public void beginTransaction() {
        if (transactionDepth == 0) {
            transactionThread = Thread.currentThread();
            beginRecChange();
        } else {
            // == is okay for threads.
            //noinspection ObjectEquality
            if (transactionThread != Thread.currentThread()) {
                throw new IllegalStateException("A transaction must only be used by one thread.");
            }
        }
        transactionDepth++;
    }

    /**
     * Ends a transaction. Invoking this method will reduce the transaction
     * depth by 1. <p/> If the last transaction is ended, the changes are
     * committed.
     */
    public void endTransaction() {
        if (transactionDepth <= 0) {
            throw new IllegalStateException("Tried to end a transaction but no transaction was open.");
        }
        transactionDepth--;
        assert transactionDepth >= 0;
        if (transactionDepth == 0) {
            transactionDepth = 0;
            transactionThread = null;
            if (endRecChange()) {
                fireMapGridChangedEvent();
            }
        }
        // the redraw responses to insert or delete after a select,
        // did not appear to activate this code, instead redrawing a small box
        // so we moved this here
        // however, it is slow
        // and, the smaller box may result from a different chain reaction passing
        // through the model?
   //     fireMapGridChangedEvent();
    }

    /**
     * Updates the border selection flags of a square and its adjacent squares.
     * This function assumes the the square's selection state has changed.
     * @param x the x-coordinate of the square
     * @param y the y-coordinate of the square
     * @param newState the new square's selection state
     */
    private void updateSelectionFlag(final int x, final int y, final boolean newState) {
        updateSelectionFlag(x, y, newState, x, y - 1, GRID_FLAG_SELECTION_NORTH, GRID_FLAG_SELECTION_SOUTH);
        updateSelectionFlag(x, y, newState, x, y + 1, GRID_FLAG_SELECTION_SOUTH, GRID_FLAG_SELECTION_NORTH);
        updateSelectionFlag(x, y, newState, x - 1, y, GRID_FLAG_SELECTION_WEST, GRID_FLAG_SELECTION_EAST);
        updateSelectionFlag(x, y, newState, x + 1, y, GRID_FLAG_SELECTION_EAST, GRID_FLAG_SELECTION_WEST);
    }

    /**
     * Updates the border selection flags of a square and one adjacent square.
     * This function assumes the the square's selection state has changed.
     * @param x the x-coordinate of the square
     * @param y the y-coordinate of the square
     * @param newState the new square's selection state
     * @param dx the x-coordinate of the adjacent square
     * @param dy the y-coordinate of the adjacent square
     * @param flag the border selection flag for the square
     * @param dFlag the border selection flag for the adjacent square
     */
    private void updateSelectionFlag(final int x, final int y, final boolean newState, final int dx, final int dy, final int flag, final int dFlag) {
        final boolean dState = 0 <= dx && dx < gridSize.getWidth() && 0 <= dy && dy < gridSize.getHeight() && (gridFlags[dx][dy] & GRID_FLAG_SELECTION) != 0;
        if (newState) {
            if (dState) {
                gridFlags[dx][dy] &= ~dFlag;
                updateRecChange(dx, dy);
            } else {
                gridFlags[x][y] |= flag;
                updateRecChange(x, y);
            }
        } else {
            if (dState) {
                gridFlags[dx][dy] |= dFlag;
                updateRecChange(dx, dy);
            } else {
                gridFlags[x][y] &= ~flag;
                updateRecChange(x, y);
            }
        }
    }

    /**
     * Returns the selection.
     * @return the selection
     */
    @NotNull
    public Point[] getSelection() {
        final List<Point> selection = new ArrayList<Point>();
        calculateCachedSelectedRec();
        final Rectangle selectedRec = cachedSelectedRec;
        if (selectedRec != null) {
            for (int x = selectedRec.x; x < selectedRec.x + selectedRec.width; x++) {
                for (int y = selectedRec.y; y < selectedRec.y + selectedRec.height; y++) {
                    if ((gridFlags[x][y] & GRID_FLAG_SELECTION) > 0) {
                        selection.add(new Point(x, y));
                    }
                }
            }
        }
        return selection.toArray(new Point[selection.size()]);
    }

}
