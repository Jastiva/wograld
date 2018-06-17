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

package net.sf.gridarta.model.direction;

import org.jetbrains.annotations.NotNull;

/**
 * A direction.
 * @author Andreas Kirschbaum
 */
public enum Direction {

    /**
     * North.
     */
    NORTH(0, -1, "North"),

    /**
     * East.
     */
    EAST(1, 0, "East"),

    /**
     * South.
     */
    SOUTH(0, 1, "South"),

    /**
     * West.
     */
    WEST(-1, 0, "West"),

    /**
     * North east.
     */
    NORTH_EAST(1, -1, "NorthEast"),

    /**
     * South east.
     */
    SOUTH_EAST(1, 1, "SouthEast"),

    /**
     * South west.
     */
    SOUTH_WEST(-1, 1, "SouthWest"),

    /**
     * North west.
     */
    NORTH_WEST(-1, -1, "NorthWest");

    /**
     * The relative x direction.
     */
    private final int dx;

    /**
     * The relative y direction.
     */
    private final int dy;

    /**
     * The identification string.
     */
    @NotNull
    private final String id;

    /**
     * Creates a new instance.
     * @param dx the relative x direction
     * @param dy the relative y direction
     * @param id the identification string
     */
    Direction(final int dx, final int dy, @NotNull final String id) {
        this.dx = dx;
        this.dy = dy;
        this.id = id;
    }

    /**
     * Returns the relative x direction.
     * @return the relative x direction
     */
    public int getDx() {
        return dx;
    }

    /**
     * Returns the relative y direction.
     * @return the relative y direction
     */
    public int getDy() {
        return dy;
    }

    /**
     * Returns the identification string.
     * @return the identification string
     */
    @NotNull
    public String getId() {
        return id;
    }

}
