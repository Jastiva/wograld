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

package net.sf.gridarta.model.archetype;

import net.sf.gridarta.model.baseobject.BaseObject;
import net.sf.gridarta.utils.StringUtils;
import org.jetbrains.annotations.NotNull;

/**
 * Utility class for archetype attribute related functions.
 * @author Andreas Kirschbaum
 */
public class AttributeListUtils {

    /**
     * Private constructor to prevent instantiation.
     */
    private AttributeListUtils() {
    }

    /**
     * Removes an attribute from an attribute list.
     * @param attributeList the attribute list to modify
     * @param key the attribute key to remove
     * @return the attribute list with the given key removed
     */
    public static String removeAttribute(@NotNull final String attributeList, @NotNull final String key) {
        if (attributeList.length() <= 0) {
            return attributeList;
        }

        final String prefix = key + " ";

        final String[] lines = StringUtils.PATTERN_END_OF_LINE.split(attributeList, -1);

        final StringBuilder sb = new StringBuilder();
        for (final String line : lines) {
            if (line.length() > 0 && !line.startsWith(prefix)) {
                sb.append(line);
                sb.append('\n');
            }
        }
        return sb.toString();
    }

    /**
     * Returns all attributes from the given game object that don't exist in an
     * archetype. Ignores the values; compares only keys.
     * @param gameObject the game object
     * @param archetype the archetype
     * @return all attributes from the archetype that don't occur in the game
     *         object
     */
    @NotNull
    public static String diffArchTextKeys(@NotNull final BaseObject<?, ?, ?, ?> gameObject, @NotNull final BaseObject<?, ?, ?, ?> archetype) {
        final CharSequence oldObjectText = gameObject.getObjectText();
        final StringBuilder result = new StringBuilder();
        for (final String line : StringUtils.PATTERN_END_OF_LINE.split(archetype.getObjectText(), 0)) {
            final int spaceIndex = line.indexOf(' ');
            if (line.length() > 0 && spaceIndex > 0 && StringUtils.diffTextString(oldObjectText, line.substring(0, spaceIndex + 1), true) == null) {
                result.append(line).append('\n');
            }
        }
        return result.toString();
    }

    /**
     * Returns all entries from the given attributes that don't exist in an
     * archetype.
     * @param attributes the attributes
     * @param archetype the game object
     * @return all lines from <code>attributes</code> that don't occur in this
     *         game object
     */
    @NotNull
    public static String diffArchTextValues(@NotNull final BaseObject<?, ?, ?, ?> archetype, @NotNull final CharSequence attributes) {
        final CharSequence oldObjectText = archetype.getObjectText();
        final StringBuilder result = new StringBuilder();
        for (final String line : StringUtils.PATTERN_END_OF_LINE.split(attributes, 0)) {
            try {
                final CharSequence test = StringUtils.diffTextString(oldObjectText, line, false);
                char c = '\n';
                if (test != null) {
                    c = test.charAt(0);
                }
                if (line.length() > 0 && (test == null || c == '\n')) {
                    result.append(line).append('\n');
                }
            } catch (final StringIndexOutOfBoundsException ignored) {
                // ignore
            }
        }
        return result.toString();
    }

}
