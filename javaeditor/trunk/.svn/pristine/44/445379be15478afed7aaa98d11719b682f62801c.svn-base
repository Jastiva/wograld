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

import org.jetbrains.annotations.NotNull;

/**
 * Utility class for parsing strings into numbers.
 * @author Andreas Kirschbaum
 */
public class NumberUtils {

    /**
     * Private constructor to prevent instantiation.
     */
    private NumberUtils() {
    }

    /**
     * Parses an integer string.
     * @param s the string to parse
     * @return the integer value or zero if value not readable
     */
    public static int parseInt(@NotNull final String s) {
        return parseInt(s, 0);
    }

    /**
     * Parses an integer string.
     * @param s the string to parse
     * @param defaultValue the default value
     * @return the integer value or the default value if value not readable
     */
    public static int parseInt(@NotNull final String s, final int defaultValue) {
        if (s.isEmpty()) {
            return defaultValue;
        }

        try {
            return Integer.parseInt(s);
        } catch (final NumberFormatException ignored) {
            return defaultValue;
        }
    }

    /**
     * Parses a long string.
     * @param s the string to parse
     * @return the long value or zero if value not readable
     */
    public static long parseLong(@NotNull final String s) {
        if (s.isEmpty()) {
            return 0L;
        }

        try {
            return Long.parseLong(s);
        } catch (final NumberFormatException ignored) {
            return 0L;
        }
    }

    /**
     * Parses a double string.
     * @param s the string to parse
     * @return the double value or zero if value not readable
     */
    public static double parseDouble(@NotNull final String s) {
        if (s.isEmpty()) {
            return 0.0;
        }

        try {
            return Double.parseDouble(s);
        } catch (final NumberFormatException ignored) {
            return 0.0;
        }
    }

}
