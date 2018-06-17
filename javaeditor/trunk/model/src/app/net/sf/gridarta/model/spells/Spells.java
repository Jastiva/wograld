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

package net.sf.gridarta.model.spells;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/**
 * Common base class for spells and spell lists.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 */
public class Spells<S extends Spell> implements Iterable<S> {

    /**
     * All defined spells.
     */
    @NotNull
    private final List<S> spells = new ArrayList<S>();

    /**
     * A comparator to sort spells by name.
     */
    private static final Comparator<Spell> spellComparator = new Comparator<Spell>() {

        @Override
        public int compare(final Spell o1, final Spell o2) {
            return String.CASE_INSENSITIVE_ORDER.compare(o1.getName(), o2.getName());
        }

    };

    /**
     * Sort the added spells after loading is finished.
     */
    public void sort() {
        Collections.sort(spells, spellComparator);
    }

    /**
     * Return the number of existing spell objects.
     * @return the number of existing spell objects
     */
    public int size() {
        return spells.size();
    }

    /**
     * Return all known spell objects.
     * @return the spell objects
     */
    @NotNull
    @Override
    public Iterator<S> iterator() {
        return Collections.unmodifiableList(spells).iterator();
    }

    /**
     * Add a spell.
     * @param spell the spell to add
     */
    public void add(@NotNull final S spell) {
        spells.add(spell);
    }

    /**
     * Return one spell object by index.
     * @param index the spell index
     * @return the spell object
     */
    public S getSpell(final int index) {
        try {
            return spells.get(index);
        } catch (final IndexOutOfBoundsException ignored) {
            return spells.get(0);
        }
    }

}
