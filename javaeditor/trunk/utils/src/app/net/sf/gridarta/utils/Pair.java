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

package net.sf.gridarta.utils;

/**
 * Stores a pair of values.
 * @author Andreas Kirschbaum
 */
public class Pair<T, U> {

    /**
     * The first value.
     */
    private final T first;

    /**
     * The second value.
     */
    private final U second;

    /**
     * Creates a new instance.
     * @param first the first value
     * @param second the second value
     */
    public Pair(final T first, final U second) {
        this.first = first;
        this.second = second;
    }

    /**
     * Returns the first value.
     * @return the first value
     */
    public T getFirst() {
        return first;
    }

    /**
     * Returns the second value.
     * @return the second value
     */
    public U getSecond() {
        return second;
    }

}

