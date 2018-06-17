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

package net.sf.gridarta.model.archetypetype;

import net.sf.gridarta.model.baseobject.BaseObject;
import org.jetbrains.annotations.NotNull;

/**
 * Holds the key/value pair of an archetype attribute definition. Corresponds to
 * a &lt;attribute&gt; entry in types.xml
 * @author Andreas Kirschbaum
 */
public class ArchetypeAttributeDefinition {

    /**
     * The key.
     */
    @NotNull
    private final String key;

    /**
     * The value.
     */
    @NotNull
    private final String value;

    /**
     * Creates a new instance.
     * @param key the key
     * @param value the value
     */
    public ArchetypeAttributeDefinition(@NotNull final String key, @NotNull final String value) {
        this.key = key;
        this.value = value;
    }

    /**
     * Returns whether a base object matches this attribute definition.
     * @param baseObject the base object
     * @return whether the base object matches
     */
    public boolean matches(@NotNull final BaseObject<?, ?, ?, ?> baseObject) {
        return matches(baseObject.getAttributeString(key));
    }

    /**
     * Returns whether an attribute value matches this attribute definition.
     * @param attributeValue the attribute value
     * @return whether the attribute value matches
     */
    private boolean matches(@NotNull final CharSequence attributeValue) {
        return attributeValue.equals(value) || (value.equals("0") && attributeValue.length() == 0);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public String toString() {
        return key + "=" + value;
    }

}
