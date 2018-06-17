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
import net.sf.gridarta.utils.Size2D;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test for {@link MapGrid}.
 * @author Andreas Kirschbaum
 */
public class MapGridTest {

    /**
     * Test case for selection border updates.
     */
    @Test
    public void testSelectionBorderUpdates() {
        final MapGrid mapGrid = new MapGrid(new Size2D(4, 3));
        checkSelectionBorder(mapGrid, "0000" + "0000" + "0000");

        mapGrid.select(new Point(1, 1), SelectionMode.ADD);
        checkSelectionBorder(mapGrid, "0000" + "0f00" + "0000");

        mapGrid.select(new Point(2, 1), SelectionMode.ADD);
        checkSelectionBorder(mapGrid, "0000" + "0d70" + "0000");

        mapGrid.select(new Point(3, 1), SelectionMode.ADD);
        checkSelectionBorder(mapGrid, "0000" + "0d57" + "0000");

        mapGrid.select(new Point(3, 1), SelectionMode.SUB);
        checkSelectionBorder(mapGrid, "0000" + "0d70" + "0000");

        mapGrid.selectArea(new Point(0, 0), new Point(3, 2), SelectionMode.FLIP);
        checkSelectionBorder(mapGrid, "9553" + "a00a" + "c556");

        mapGrid.select(new Point(1, 1), SelectionMode.FLIP);
        checkSelectionBorder(mapGrid, "9153" + "820a" + "c456");

        mapGrid.select(new Point(1, 1), SelectionMode.SUB);
        checkSelectionBorder(mapGrid, "9553" + "a00a" + "c556");

        mapGrid.selectArea(new Point(0, 0), new Point(3, 2), SelectionMode.ADD);
        checkSelectionBorder(mapGrid, "9113" + "8002" + "c446");

        mapGrid.resize(new Size2D(2, 1));
        checkSelectionBorder(mapGrid, "d7");

        mapGrid.resize(new Size2D(3, 4));
        checkSelectionBorder(mapGrid, "d700" + "0000" + "0000");

        mapGrid.selectArea(new Point(0, 0), new Point(2, 3), SelectionMode.ADD);
        checkSelectionBorder(mapGrid, "913" + "802" + "802" + "c46");

        mapGrid.resize(new Size2D(4, 3));
        checkSelectionBorder(mapGrid, "9130" + "8020" + "c460");
    }

    /**
     * Checks that the map border selection flags of a {@link MapGrid} instance
     * is as expected. The expected border selection is represented as
     * <code>String</code>. Each square is represented as one hex digit
     * character; 1=north, 2=east, 4=south, 8=east border is set.
     * @param mapGrid the map grid
     * @param expectedBorder the expected border selection
     */
    private static void checkSelectionBorder(final MapGrid mapGrid, final String expectedBorder) {
        final StringBuilder sb = new StringBuilder();
        final Size2D size = mapGrid.getSize();
        for (int y = 0; y < size.getHeight(); y++) {
            for (int x = 0; x < size.getWidth(); x++) {
                final int flags = mapGrid.getFlags(x, y);
                int value = 0;
                if ((flags & MapGrid.GRID_FLAG_SELECTION_NORTH) != 0) {
                    value |= 1;
                }
                if ((flags & MapGrid.GRID_FLAG_SELECTION_EAST) != 0) {
                    value |= 2;
                }
                if ((flags & MapGrid.GRID_FLAG_SELECTION_SOUTH) != 0) {
                    value |= 4;
                }
                if ((flags & MapGrid.GRID_FLAG_SELECTION_WEST) != 0) {
                    value |= 8;
                }
                sb.append(Integer.toHexString(value));
            }
        }
        Assert.assertEquals(expectedBorder, sb.toString());
    }

}
