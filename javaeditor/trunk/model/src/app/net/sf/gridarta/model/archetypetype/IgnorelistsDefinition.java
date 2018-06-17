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

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The contents of an &lt;ignorelists&gt; element of a types.xml file.
 * @author Andreas Kirschbaum
 */
public class IgnorelistsDefinition {

    /**
     * The ignore list entries. Maps ignore list name to ignored section names.
     */
    @NotNull
    private final Map<String, Set<String>> ignoreListTable = new HashMap<String, Set<String>>();

    /**
     * Returns whether an ignore list name exists.
     * @param name the ignore list name
     * @return whether the name exists
     */
    public boolean containsKey(@NotNull final String name) {
        return ignoreListTable.containsKey(name);
    }

    /**
     * Adds a new entry.
     * @param name the ignore list name
     * @param value the section name to be ignored
     * @throws IllegalArgumentException if the parameters are not valid
     */
    public void put(@NotNull final String name, @NotNull final String value) {
        if (name.isEmpty()) {
            throw new IllegalArgumentException("empty name");
        }

        if (value.isEmpty()) {
            throw new IllegalArgumentException("empty value for name '" + name + "'");
        }

        Set<String> values = ignoreListTable.get(name);
        if (values == null) {
            values = new HashSet<String>();
            ignoreListTable.put(name, values);
        }
        if (!values.add(value)) {
            throw new IllegalArgumentException("duplicate value '" + value + "' for name '" + name + "'");
        }
    }

    /**
     * Returns the ignored section names for a ignore list name.
     * @param name the ignore list name
     * @return the ignored section names
     */
    @Nullable
    public Iterable<String> get(@NotNull final String name) {
        final Set<String> values = ignoreListTable.get(name);
        return values == null ? null : Collections.unmodifiableCollection(values);
    }

}
