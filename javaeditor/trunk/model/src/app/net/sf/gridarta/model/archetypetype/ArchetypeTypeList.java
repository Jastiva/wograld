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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import net.sf.gridarta.utils.Pair;
import org.jetbrains.annotations.NotNull;

/**
 * A list definition.
 * @author Andreas Kirschbaum
 */
public class ArchetypeTypeList implements Iterable<Pair<Integer, String>> {

    /**
     * The list entries.
     */
    @NotNull
    private final List<Pair<Integer, String>> list = new ArrayList<Pair<Integer, String>>();

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public Iterator<Pair<Integer, String>> iterator() {
        return Collections.unmodifiableList(list).iterator();
    }

    /**
     * Returns the number of entries.
     * @return the number of entries
     */
    public int size() {
        return list.size();
    }

    /**
     * Adds an entry.
     * @param value the entry's value
     * @param name the entry's name
     * @throws IllegalArgumentException if the values are not acceptable
     */
    public void add(final int value, @NotNull final String name) {
        if (name.isEmpty()) {
            throw new IllegalArgumentException("empty name");
        }
        for (final Pair<Integer, String> pair : list) {
            if (pair.getFirst() == value) {
                throw new IllegalArgumentException("duplicate value '" + value + "' for '" + pair.getSecond() + "' and '" + name + "'");
            }
        }
        list.add(new Pair<Integer, String>(value, name));
    }

    /**
     * Returns an entry by index.
     * @param index the index
     * @return the entry
     */
    @NotNull
    public Pair<Integer, String> get(final int index) {
        return list.get(index);
    }

}
