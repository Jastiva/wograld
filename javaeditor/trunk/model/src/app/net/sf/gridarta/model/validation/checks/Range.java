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

package net.sf.gridarta.model.validation.checks;

/**
 * Represents a range of attribute values.
 * @author Andreas Kirschbaum
 */
public class Range {

    /**
     * The minimum allowed value.
     */
    private final int min;

    /**
     * The maximum allowed value.
     */
    private final int max;

    /**
     * Create a new instance.
     * @param min the minimum allowed value
     * @param max the maximum allowed value
     */
    public Range(final int min, final int max) {
        if (min > max) {
            throw new IllegalArgumentException();
        }

        this.min = min;
        this.max = max;
    }

    /**
     * Return whether a given attribute value is within the valid attribute
     * values range.
     * @param value the value to check
     * @return whether the value is valid
     */
    public boolean isWithinRange(final int value) {
        return min <= value && value <= max;
    }

    /**
     * Returns the minimum allowed value.
     * @return the minimum allowed value
     */
    public int getMin() {
        return min;
    }

    /**
     * Returns the maximum allowed value.
     * @return the maximum allowed value
     */
    public int getMax() {
        return max;
    }

}
