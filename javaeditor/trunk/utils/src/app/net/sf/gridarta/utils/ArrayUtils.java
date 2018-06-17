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
 * Utility class for array related functions.
 * @author Andreas Kirschbaum
 */
public class ArrayUtils {

    /**
     * Private constructor to prevent instantiation.
     */
    private ArrayUtils() {
    }

    /**
     * Helper method that checks whether a region in a byte array at a given
     * offset contains the same as another byte array. It's used by
     * loadAllDaimoninPNGFromCollect as a replacement for the C-function
     * strncmp() (but invocation args are a bit different).
     * @param src source byte array
     * @param offset offset to start comparison
     * @param search byte array containing the bytes to search
     * @return <code>true</code> if the bytes in <code><var>src</var></code>
     *         starting at <code><var>offset</var></code> are the same as those
     *         in <code><var>search</var></code>, otherwise <code>false</code>
     */
    public static boolean contains(final byte[] src, final int offset, final byte[] search) {
        //noinspection ProhibitedExceptionCaught
        try {
            for (int i = 0; i < search.length; i++) {
                if (src[offset + i] != search[i]) {
                    return false;
                }
            }
            return true;
        } catch (final ArrayIndexOutOfBoundsException ignored) {
            return false;
        }
    }
}
