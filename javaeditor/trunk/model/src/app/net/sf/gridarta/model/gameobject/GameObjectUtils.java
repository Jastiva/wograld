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

import net.sf.gridarta.model.archetypetype.ArchetypeType;
import net.sf.gridarta.model.baseobject.BaseObject;
import net.sf.gridarta.utils.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Utility class for {@link GameObject} related functions.
 * @author Andreas Kirschbaum
 */
public class GameObjectUtils {

    /**
     * Private constructor to prevent instantiation.
     */
    private GameObjectUtils() {
    }

    /**
     * This method checks the objectText for syntax errors. More precisely: It
     * reads every line in the objectText and looks if it matches the
     * type-definitions (-&gt; see {@link net.sf.gridarta.model.archetypetype.ArchetypeTypeSet})
     * for this base object. If there is no match, the line is considered wrong.
     * Of course the type-definitions will never be perfect, this should be kept
     * in mind. <p/> Note that the archetype is ignored in the check. The
     * default arches should be correct, and even if not - it isn't the map
     * maker to blame.
     * @param gameObject the game object to check
     * @param type the type structure belonging to this base object
     * @return a String with all lines which don't match the
     *         type-definitions.<br> If no such "errors" encountered, null is
     *         returned
     */
    @Nullable
    public static String getSyntaxErrors(@NotNull final BaseObject<?, ?, ?, ?> gameObject, @NotNull final ArchetypeType type) {
        final StringBuilder errors = new StringBuilder();  // return value: all error lines
        for (final String line : StringUtils.PATTERN_END_OF_LINE.split(gameObject.getObjectText())) {
            // get only the key-part of the attribute.
            final int spaceIndex = line.indexOf(' ');
            final Comparable<String> attrKey = spaceIndex == -1 ? line : line.substring(0, spaceIndex);

            // now check if there's a match in the definitions
            /* we exclude "direction", "type", and "name" on the hard way */
            if (!attrKey.equals(BaseObject.DIRECTION) && !attrKey.equals(BaseObject.TYPE) && !attrKey.equals(BaseObject.NAME) && !type.hasAttributeKey(attrKey)) {
                errors.append(line.trim()).append('\n'); // append line to the errors
                /*
                // the attribute doesn't match the definitions,
                // now check if it is a negation of an entry in the archetype
                if (line.contains(" ")) {
                    String attr_val = line.substring(line.indexOf(" ")).trim();
                    if (!(archetype.getAttributeString(attrKey, null).length() > 0 && (attr_val.equals("0") || attr_val.equals("null") || attr_val.equals("none")))) {
                        errors.append(line.trim()).append('\n');    // append line to the errors
                    }
                } else {
                    errors.append(line.trim()).append('\n'); // append line to the errors
                }
                */
            }
        }

        // return errors, or null if empty
        final String retErrors = errors.toString();
        if (retErrors.trim().length() == 0) {
            return null;
        }
        return retErrors;
    }

}
