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

package net.sf.gridarta.model.gameobject;

import java.awt.Dimension;
import org.jetbrains.annotations.NotNull;

/**
 * Encapsulated information about a multi-square image.
 * @author Andreas Kirschbaum
 */
public class MultiPositionEntry {

    /**
     * Number of columns in the array.
     */
    private static final int X_DIM = 32;

    /**
     * The total width of the multi-square image in pixels.
     */
    private final int width;

    /**
     * The total height of the multi-square image in pixels.
     */
    private final int height;

    /**
     * Array with position data.
     */
    @NotNull
    private final int[] data;

    /**
     * The vertical size of a square.
     */
    private final int yLen;

    /**
     * Creates a new instance.
     * @param isoMapSquareInfo the iso information to use
     * @param size the size of the entry in map squares
     */
    public MultiPositionEntry(@NotNull final IsoMapSquareInfo isoMapSquareInfo, @NotNull final Dimension size) {
        final int sum = size.width + size.height;
        width = sum * isoMapSquareInfo.getXLen2();
        height = sum * isoMapSquareInfo.getYLen2();
        data = new int[width * height];
        int index = 0;
        for (int y = 0; y < size.height; y++) {
            for (int x = 0; x < size.width; x++) {
                data[index++] = (x + size.height - y - 1) * isoMapSquareInfo.getXLen2();
                data[index++] = (x + y) * isoMapSquareInfo.getYLen2();
            }
        }
        yLen = isoMapSquareInfo.getYLen();
    }

    /**
     * Returns the x offset from the left-most pixel of the multi-square image
     * and the default x position. (The default position is where a
     * single-square image would be put.)
     * @param positionID the square number in the multi-square
     * @return the x offset
     */
    public int getXOffset(final int positionID) {
        return data[positionID * 2];
    }

    /**
     * Returns the y offset from the left-most pixel of the multi-square image
     * and the default y position. (The default position is where a
     * single-square image would be put.)
     * @param positionID the square number in the multi-square
     * @return the y offset
     */
    public int getYOffset(final int positionID) {
        return height - yLen - data[1 + positionID * 2];
    }

    /**
     * Returns the total width of a multi-square image.
     * @return the width in pixels
     */
    public int getWidth() {
        return width;
    }

    /**
     * Returns the total height of a multi-square image.
     * @return the height in pixels
     */
    public int getHeight() {
        return height;
    }

}
