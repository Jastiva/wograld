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
import junit.framework.JUnit4TestAdapter;
import net.sf.gridarta.model.archetype.TestArchetype;
import net.sf.gridarta.model.direction.Direction;
import net.sf.gridarta.model.gameobject.TestGameObject;
import net.sf.gridarta.model.maparchobject.TestMapArchObject;
import net.sf.gridarta.model.mapgrid.MapGrid;
import net.sf.gridarta.model.mapgrid.SelectionMode;
import net.sf.gridarta.model.mapmodel.MapSquare;
import net.sf.gridarta.model.mapmodel.TestMapModelCreator;
import net.sf.gridarta.utils.Size2D;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link MapCursor}.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 * @author <a href="mailto:dlviegas@gmail.com">Daniel Viegas</a>
 * @author Andreas Kirschbaum
 */
public class MapCursorTest {

    /**
     * The size of the map grid for the tested cursor.
     */
    @NotNull
    private static final Size2D gridSize = new Size2D(6, 7);

    /**
     * A {@link MapCursorListener} that counts the number of callbacks.
     */
    @NotNull
    private static final TestMapCursorListener listener = new TestMapCursorListener();

    /**
     * Creates a new {@link MapCursor} instance. No more than one instance may
     * exist concurrently.
     * @param grid the underlying map grid
     * @return the new map cursor instance
     */
    @NotNull
    public static MapCursor<TestGameObject, TestMapArchObject, TestArchetype> createCursor(@NotNull final MapGrid grid) {
        final TestMapModelCreator mapModelCreator = new TestMapModelCreator(false);
        final Size2D gridSize = grid.getSize();
        final MapCursor<TestGameObject, TestMapArchObject, TestArchetype> cursor = new MapCursor<TestGameObject, TestMapArchObject, TestArchetype>(grid, mapModelCreator.newMapModel(gridSize.getWidth(), gridSize.getHeight()));
        cursor.addMapCursorListener(listener);
        Assert.assertFalse("MapCursor has to be disabled when created.", cursor.isActive());
        listener.changedPosCounter = 0;
        listener.changedModeCounter = 0;
        return cursor;
    }

    /**
     * Checks that settings the cursor outside of the grid behaves as expected.
     */
    @Test
    public void setOutside() {
        final MapGrid grid = new MapGrid(gridSize);
        final MapCursor<TestGameObject, TestMapArchObject, TestArchetype> cursor = createCursor(grid);
        final Point p = new Point();
        for (int i = -2; i < gridSize.getWidth() + 2; i++) {
            p.setLocation(i, -1);
            cursor.setLocation(p);
            testEvents(0, 0);
            Assert.assertFalse("MapCursor should be disabled after setLocation(" + p + ")", cursor.isActive());
            testEvents(0, 0);
            Assert.assertNull("getLocation() should return null after setLocation(" + p + ")", cursor.getLocation());
            testEvents(0, 0);
            p.setLocation(i, gridSize.getHeight());
            cursor.setLocation(p);
            testEvents(0, 0);
            Assert.assertFalse("MapCursor should be disabled after setLocation(" + p + ")", cursor.isActive());
            testEvents(0, 0);
            Assert.assertNull("getLocation() should return null after setLocation(" + p + ")", cursor.getLocation());
            testEvents(0, 0);
        }
        for (int i = -2; i < gridSize.getHeight() + 2; i++) {
            p.setLocation(-1, i);
            cursor.setLocation(p);
            testEvents(0, 0);
            Assert.assertFalse("MapCursor should be disabled after setLocation(" + p + ")", cursor.isActive());
            testEvents(0, 0);
            Assert.assertNull("getLocation() should return null after setLocation(" + p + ")", cursor.getLocation());
            testEvents(0, 0);
            p.setLocation(gridSize.getWidth(), i);
            cursor.setLocation(p);
            testEvents(0, 0);
            Assert.assertFalse("MapCursor should be disabled after setLocation(" + p + ")", cursor.isActive());
            testEvents(0, 0);
            Assert.assertNull("getLocation() should return null after setLocation(" + p + ")", cursor.getLocation());
            testEvents(0, 0);
        }
    }

    /**
     * Checks that settings the cursor within the map grid behaves as expected.
     */
    @Test
    public void setInside() {
        final MapGrid grid = new MapGrid(gridSize);
        final MapCursor<TestGameObject, TestMapArchObject, TestArchetype> cursor = createCursor(grid);
        boolean first = true;
        final Point p = new Point();
        for (int j = 0; j < gridSize.getHeight(); j++) {
            for (int i = 0; i < gridSize.getWidth(); i++) {
                p.setLocation(i, j);
                cursor.setLocation(p);
                if (first) {
                    // Only the first after activation should fire a change mode event
                    testEvents(1, 1);
                    first = false;
                } else {
                    testEvents(1, 0);
                }
                Assert.assertTrue("MapCursor should be active after setLocation(" + p + ")", cursor.isActive());
                testEvents(0, 0);
                final Point res = cursor.getLocation();
                testEvents(0, 0);
                Assert.assertEquals("getLocation()", p, res);
            }
        }
    }

    /**
     * Checks that settings the cursor to the same location does not generate
     * excess events.
     */
    @Test
    public void setSameLocation() {
        final MapGrid grid = new MapGrid(gridSize);
        final MapCursor<TestGameObject, TestMapArchObject, TestArchetype> cursor = createCursor(grid);
        final Point p = new Point(-1, -1);
        cursor.setLocation(null);
        testEvents(0, 0);
        Assert.assertFalse("MapCursor should be deactivated", cursor.isActive());
        testEvents(0, 0);
        cursor.setLocation(p);
        testEvents(0, 0);
        Assert.assertFalse("MapCursor should be deactivated", cursor.isActive());
        testEvents(0, 0);
        p.setLocation(3, 4);
        cursor.setLocation(p);
        testEvents(1, 1);
        Assert.assertTrue("MapCursor should be active", cursor.isActive());
        testEvents(0, 0);
        cursor.setLocation(p);
        testEvents(0, 0);
        Assert.assertTrue("MapCursor should be active", cursor.isActive());
        testEvents(0, 0);
        p.setLocation(-1, -1);
        cursor.setLocation(p);
        testEvents(1, 1);
        Assert.assertFalse("MapCursor should be deactivated", cursor.isActive());
        testEvents(0, 0);
    }

    /**
     * Checks {@link MapCursor#setLocationSafe(Point)}.
     */
    @Test
    public void setLocationSafe() {
        final MapGrid grid = new MapGrid(gridSize);
        final MapCursor<TestGameObject, TestMapArchObject, TestArchetype> cursor = createCursor(grid);
        final Point p = new Point(-1, -1);
        Assert.assertFalse("setLocationSafe(null) should return false", cursor.setLocationSafe(null));
        testEvents(0, 0);
        Assert.assertFalse("MapCursor should be deactivated", cursor.isActive());
        testEvents(0, 0);
        Assert.assertFalse("setLocationSafe(" + p + ") should return false", cursor.setLocationSafe(p));
        testEvents(0, 0);
        Assert.assertFalse("MapCursor should be deactivated", cursor.isActive());
        testEvents(0, 0);
        p.setLocation(3, 4);
        Assert.assertTrue("setLocationSafe(" + p + ") should return true", cursor.setLocationSafe(p));
        testEvents(1, 1);
        Assert.assertTrue("MapCursor should be active", cursor.isActive());
        testEvents(0, 0);
        Assert.assertFalse("setLocationSafe(" + p + ") should return false", cursor.setLocationSafe(p));
        testEvents(0, 0);
        Assert.assertTrue("MapCursor should be active", cursor.isActive());
        testEvents(0, 0);
        p.setLocation(-1, -1);
        Assert.assertFalse("setLocationSafe(" + p + ") should return false", cursor.setLocationSafe(p));
        testEvents(0, 0);
        Assert.assertTrue("MapCursor should be active", cursor.isActive());
        testEvents(0, 0);
        Assert.assertFalse("setLocationSafe(null) should return false", cursor.setLocationSafe(null));
        testEvents(0, 0);
        Assert.assertTrue("MapCursor should be active", cursor.isActive());
        testEvents(0, 0);
    }

    /**
     * Checks {@link MapCursor#isOnGrid(Point)}.
     */
    @Test
    public void testIsOnGrid() {
        final MapGrid grid = new MapGrid(gridSize);
        final MapCursor<TestGameObject, TestMapArchObject, TestArchetype> cursor = createCursor(grid);
        final Point p = new Point();
        for (int j = -2; j < gridSize.getHeight() + 2; j++) {
            for (int i = -2; i < gridSize.getWidth() + 2; i++) {
                p.setLocation(i, j);
                if (i >= 0 && i < gridSize.getWidth() && j >= 0 && j < gridSize.getHeight()) {
                    Assert.assertTrue(p + " should be on the grid.", cursor.isOnGrid(p));
                } else {
                    Assert.assertFalse(p + " should not be on the grid.", cursor.isOnGrid(p));
                }
            }
        }
        Assert.assertFalse("Null should not be on the grid.", cursor.isOnGrid(null));
    }

    /**
     * Checks {@link MapCursor#goTo(boolean, Direction)}.
     */
    @Test
    public void testGoTo() {
        final MapGrid grid = new MapGrid(gridSize);
        final MapCursor<TestGameObject, TestMapArchObject, TestArchetype> cursor = createCursor(grid);
        for (final Direction dir : Direction.values()) {
            Assert.assertFalse("go(" + dir + ") should return false.", cursor.goTo(true, dir));
            testEvents(0, 0);
        }
        final Point pStart = new Point(2, 3);
        final Point p = new Point(pStart);
        cursor.setLocation(p);
        testEvents(1, 1);
        for (final Direction dir : Direction.values()) {
            Assert.assertTrue("go(" + dir + ") should return true. (Maybe the grid was too small.)", cursor.goTo(true, dir));
            testEvents(1, 0);
            p.x += dir.getDx();
            p.y += dir.getDy();
            Assert.assertEquals("Moving cursor.", p, cursor.getLocation());
        }
        Assert.assertEquals("Moving in a circle.", pStart, cursor.getLocation());
    }

    /**
     * Checks the dragging related functions of {@link MapCursor}.
     */
    @Test
    public void dragging() {
        final MapGrid grid = new MapGrid(gridSize);
        final MapCursor<TestGameObject, TestMapArchObject, TestArchetype> cursor = createCursor(grid);
        Assert.assertFalse("MapCursor should not drag while deactivated.", cursor.isDragging());
        cursor.dragStart();
        testEvents(0, 0);
        Assert.assertFalse("MapCursor should not drag while deactivated.", cursor.isDragging());
        Assert.assertNull("Drag offset should be null", cursor.getDragOffset());
        final Point dragStart = new Point(3, 4);
        final Point p = new Point(dragStart);
        final Dimension offset = new Dimension(0, 0);
        cursor.setLocation(dragStart);
        testEvents(1, 1);
        cursor.dragStart();
        testEvents(0, 1);
        Assert.assertTrue("MapCursor should be in drag mode.", cursor.isDragging());
        Assert.assertEquals("Wrong offset", offset, cursor.getDragOffset());
        cursor.deactivate();
        testEvents(1, 1);
        Assert.assertFalse("MapCursor should not drag while deactivated.", cursor.isDragging());
        Assert.assertNull("Drag offset should be null", cursor.getDragOffset());
        cursor.setLocation(dragStart);
        testEvents(1, 1);
        cursor.dragStart();
        testEvents(0, 1);
        Assert.assertEquals("Wrong offset", offset, cursor.getDragOffset());
        dragTo(cursor, grid, Direction.WEST, dragStart, p, offset);
        dragTo(cursor, grid, Direction.EAST, dragStart, p, offset);
        dragTo(cursor, grid, Direction.EAST, dragStart, p, offset);
        dragTo(cursor, grid, Direction.NORTH_WEST, dragStart, p, offset);
        dragTo(cursor, grid, Direction.SOUTH, dragStart, p, offset);
        dragTo(cursor, grid, Direction.SOUTH, dragStart, p, offset);
        dragTo(cursor, grid, Direction.WEST, dragStart, p, offset);
        dragTo(cursor, grid, Direction.NORTH_EAST, dragStart, p, offset);
    }

    /**
     * Calls {@link MapCursor#dragTo(Point)} and checks for expected results.
     * @param cursor the map cursor to affect
     * @param grid the associated map grid
     * @param dir the direction to drag
     * @param start the starting location
     * @param p the destination location
     * @param offset the expected dragging offset
     */
    private static void dragTo(@NotNull final MapCursor<TestGameObject, TestMapArchObject, TestArchetype> cursor, @NotNull final MapGrid grid, @NotNull final Direction dir, @NotNull final Point start, @NotNull final Point p, @NotNull final Dimension offset) {
        final Point d = new Point(dir.getDx(), dir.getDy());
        p.x += d.x;
        p.y += d.y;
        Assert.assertTrue("dragTo(" + p + ")", cursor.dragTo(p));
        testEvents(1, 0);
        Assert.assertTrue("MapCursor should be in drag mode.", cursor.isDragging());
        Assert.assertEquals("Wrong position", p, cursor.getLocation());
        offset.width = p.x - start.x;
        offset.height = p.y - start.y;
        Assert.assertEquals("Wrong offset", offset, cursor.getDragOffset());
        assertPreSelection(grid, start, p);
    }

    /**
     * Checks that a {@link MapGrid} includes a rectangle of {@link
     * MapGrid#GRID_FLAG_SELECTING}.
     * @param grid the map grid to check
     * @param start one corner of the rectangle
     * @param end the diagonally opposite corner of the rectangle
     */
    private static void assertPreSelection(@NotNull final MapGrid grid, @NotNull final Point start, @NotNull final Point end) {
        final int minX = Math.min(start.x, end.x);
        final int maxX = Math.max(start.x, end.x);
        final int minY = Math.min(start.y, end.y);
        final int maxY = Math.max(start.y, end.y);
        final int height = gridSize.getHeight();
        final int width = gridSize.getWidth();
        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                if (i < minX || i > maxX || j < minY || j > maxY) {
                    //Not preselected
                    Assert.assertFalse("Pre-selection", (grid.getFlags(i, j) & MapGrid.GRID_FLAG_SELECTING) > 0);
                } else {
                    //Preselected
                    Assert.assertTrue("Pre-selection", (grid.getFlags(i, j) & MapGrid.GRID_FLAG_SELECTING) > 0);
                }
            }

        }
    }

    /**
     * Checks that a {@link MapGrid} includes a rectangle of {@link
     * MapGrid#GRID_FLAG_SELECTION}. Squares outside the rectangle are not
     * checked.
     * @param grid the map grid to check
     * @param start one corner of the rectangle
     * @param end the diagonally opposite corner of the rectangle
     * @param flag the expected selection state
     */
    private static void assertSelection(@NotNull final MapGrid grid, @NotNull final Point start, @NotNull final Point end, final boolean flag) {
        final int minX = Math.min(start.x, end.x);
        final int maxX = Math.max(start.x, end.x);
        final int minY = Math.min(start.y, end.y);
        final int maxY = Math.max(start.y, end.y);
        for (int j = minY; j <= maxY; j++) {
            for (int i = minX; i <= maxX; i++) {
                Assert.assertSame("Selection", flag, (grid.getFlags(i, j) & MapGrid.GRID_FLAG_SELECTION) > 0);
            }
        }
    }

    /**
     * Checks for correct behavior of {@link MapGrid#GRID_FLAG_SELECTING} flags
     * during selecting.
     */
    @Test
    public void selecting() {
        final MapGrid grid = new MapGrid(gridSize);
        final MapCursor<TestGameObject, TestMapArchObject, TestArchetype> cursor = createCursor(grid);
        final Point start = new Point(2, 3);
        final Point end = new Point(4, 5);
        final Point gridMaxIndex = new Point(gridSize.getWidth() - 1, gridSize.getHeight() - 1);
        cursor.dragRelease();
        testEvents(0, 0);
        cursor.setLocation(start);
        testEvents(1, 1);
        cursor.dragStart();
        testEvents(0, 1);
        cursor.dragTo(end);
        testEvents(1, 0);
        Assert.assertTrue("MapCursor should be in drag mode.", cursor.isDragging());
        cursor.dragRelease();
        testEvents(0, 1);
        Assert.assertFalse("MapCursor should not be in drag mode.", cursor.isDragging());
        cursor.setLocation(start);
        testEvents(1, 0);
        cursor.dragStart();
        testEvents(0, 1);
        cursor.dragTo(end);
        testEvents(1, 0);
        Assert.assertTrue("MapCursor should be in drag mode.", cursor.isDragging());
        cursor.setLocation(null);
        testEvents(1, 1);
        Assert.assertFalse("MapCursor should not be in drag mode.", cursor.isDragging());
        cursor.setLocation(start);
        testEvents(1, 1);
        cursor.dragStart();
        testEvents(0, 1);
        cursor.dragTo(end);
        testEvents(1, 0);
        Assert.assertTrue("MapCursor should be in drag mode.", cursor.isDragging());
        assertSelection(grid, new Point(0, 0), gridMaxIndex, false);
        cursor.dragSelect(SelectionMode.ADD);
        testEvents(0, 1);
        assertSelection(grid, start, end, true);
        //Check if nothing is preselected
        assertPreSelection(grid, new Point(-1, -1), new Point(-1, -1));
        cursor.setLocation(start);
        testEvents(1, 0);
        cursor.dragStart();
        testEvents(0, 1);
        cursor.dragTo(end);
        testEvents(1, 0);
        cursor.dragSelect(SelectionMode.SUB);
        testEvents(0, 1);
        assertSelection(grid, new Point(0, 0), gridMaxIndex, false);
        cursor.setLocation(start);
        testEvents(1, 0);
        cursor.dragStart();
        testEvents(0, 1);
        cursor.dragTo(end);
        testEvents(1, 0);
        cursor.dragSelect(SelectionMode.FLIP);
        testEvents(0, 1);
        assertSelection(grid, start, end, true);
        start.setLocation(3, 4);
        end.setLocation(5, 1);
        cursor.setLocation(start);
        testEvents(1, 0);
        cursor.dragStart();
        testEvents(0, 1);
        cursor.dragTo(end);
        testEvents(1, 0);
        cursor.dragSelect(SelectionMode.FLIP);
        testEvents(0, 1);
        assertSelection(grid, start, new Point(4, 4), false);
        assertSelection(grid, new Point(3, 2), end, true);
        assertSelection(grid, new Point(5, 3), new Point(5, 4), true);
        cursor.deactivate();
        testEvents(1, 1);
        assertSelection(grid, new Point(0, 0), gridMaxIndex, false);
    }

    /**
     * Checks if the number of events fired is correct.
     * @param nPos the expected number of position events
     * @param nMode the expected number of mode events
     */
    private static void testEvents(final int nPos, final int nMode) {
        Assert.assertEquals("Position change event", nPos, listener.changedPosCounter);
        listener.changedPosCounter = 0;
        Assert.assertEquals("Mode change event", nMode, listener.changedModeCounter);
        listener.changedModeCounter = 0;
    }

    /**
     * Returns a new test suite containing this test.
     * @return the new test suite
     */
    @NotNull
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(MapCursorTest.class);
    }

    /**
     * A {@link MapCursorListener} that counts the number of event callbacks.
     */
    private static class TestMapCursorListener implements MapCursorListener<TestGameObject, TestMapArchObject, TestArchetype> {

        /**
         * The number of calls to {@link #mapCursorChangedPos(Point)}.
         */
        private int changedPosCounter;

        /**
         * The number of calls to {@link #mapCursorChangedMode()}.
         */
        private int changedModeCounter;

        @Override
        public void mapCursorChangedPos(@Nullable final Point location) {
            changedPosCounter++;
        }

        @Override
        public void mapCursorChangedMode() {
            changedModeCounter++;
        }

        @Override
        public void mapCursorChangedGameObject(@Nullable final MapSquare<TestGameObject, TestMapArchObject, TestArchetype> mapSquare, @Nullable final TestGameObject gameObject) {
            // ignore
        }

    }

}
