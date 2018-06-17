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

package net.sf.gridarta.model.connectionview;

import java.util.regex.Pattern;
import net.sf.gridarta.model.baseobject.BaseObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Utility class to parse "connected" fields in game objects.
 * @author Andreas Kirschbaum
 */
public class Connections {

    /**
     * Pattern to match the arguments of "connected" fields.
     */
    private static final Pattern PATTERN_VALUES = Pattern.compile("\\d+(, *\\d+)*");

    /**
     * Pattern to split different values in "connected" field arguments.
     */
    private static final Pattern PATTERN_VALUE_SEPARATOR = Pattern.compile(",\\s*");

    /**
     * Private constructor to prevent instantiation.
     */
    private Connections() {
    }

    /**
     * Extract the "connected" value(s) from a given game object.
     * @param gameObject the game object to process
     * @return the connected values, or <code>null</code> if the game object
     *         does not contain a "connected" field
     */
    @Nullable
    public static int[] parseConnections(@NotNull final BaseObject<?, ?, ?, ?> gameObject) {
        final String connectionSpec = gameObject.getAttributeString("connected", false);
        if (!PATTERN_VALUES.matcher(connectionSpec).matches()) {
            return null;
        }

        final String[] values = PATTERN_VALUE_SEPARATOR.split(connectionSpec.trim(), 0);
        final int[] result = new int[values.length];
        for (int i = 0; i < values.length; i++) {
            result[i] = Integer.valueOf(values[i]);
        }
        return result;
    }

}
