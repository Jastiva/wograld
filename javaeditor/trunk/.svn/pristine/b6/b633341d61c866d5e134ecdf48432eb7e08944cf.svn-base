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

package net.sf.gridarta.model.autojoin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;

/**
 * The <code>AutojoinList</code> class contains a list of (typically
 * wall-)arches which do autojoining.
 * @author <a href="mailto:andi.vogl@gmx.net">Andreas Vogl</a>
 */
public class AutojoinList<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> implements Serializable {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The Logger for printing log messages.
     */
    @NotNull
    private static final Category log = Logger.getLogger(AutojoinList.class);

    /**
     * The number of archetypes in an autojoin list.
     */
    public static final int SIZE = 16;

    // bitmask constants for {@link #archetypes} index

    public static final int NORTH = 1;

    public static final int EAST = 2;

    public static final int SOUTH = 4;

    public static final int WEST = 8;

    /**
     * Maps index to list of archetypes representing autojoin-able archetypes
     * for this direction. The first archetype is the main archetype that can be
     * converted into other archetypes. Following archetypes are alternative
     * variants that are recognized but never converted. Examples for
     * alternative archetypes are walls having windows.
     * <p/>
     * The indices are interpreted in following order (0-15): <ol> <li>index: 0,
     * 1, 2,  3, 4,  5,  6,   7, 8,  9, 10,  11, 12,  13,  14,   15</li>
     * <li>means: 0, N, E, NE, S, NS, ES, NES, W, WN, WE, WNE, SW, SWN, ESW,
     * NESW</li> <li>(0 = no connection, N = north, E = east, S = south, W =
     * west)</li> </ol>
     */
    @NotNull
    private final List<List<R>> archetypes;

    /**
     * Create an AutojoinList.
     * @param archetypes the archetypes that form a set of join-able archetypes
     * @throws IllegalAutojoinListException if the autojoin list cannot be
     * created
     */
    public AutojoinList(@NotNull final Iterable<List<R>> archetypes) throws IllegalAutojoinListException {
        final ArrayList<List<R>> newArchetypes = new ArrayList<List<R>>();

        for (final List<R> archetypeList : archetypes) {
            newArchetypes.add(validate(archetypeList));
        }

        if (newArchetypes.size() > SIZE) {
            throw new IllegalAutojoinListException("autojoin list with more than " + SIZE + " valid entries");
        } else if (newArchetypes.size() < SIZE) {
            throw new IllegalAutojoinListException("autojoin list with less than " + SIZE + " valid entries");
        }

        newArchetypes.trimToSize();
        this.archetypes = newArchetypes;
    }

    /**
     * Validates a list of archetypes: no entry must be <null>, no duplicate
     * entry must exist, at least one entry must exist.
     * @param archetypeList the archetype list to validate
     * @return a copy of the validated archetype list
     * @throws IllegalAutojoinListException if a validation error was detected
     */
    @NotNull
    private static <R extends Archetype<?, ?, R>> List<R> validate(@NotNull final Iterable<R> archetypeList) throws IllegalAutojoinListException {
        final ArrayList<R> result = new ArrayList<R>();
        final Map<R, R> tmp = new IdentityHashMap<R, R>();
        for (final R archetype : archetypeList) {
            if (archetype == null) {
                throw new IllegalArgumentException();
            }

            if (tmp.put(archetype, archetype) != null) {
                throw new IllegalAutojoinListException("autojoin list contains duplicate archetype '" + archetype.getArchetypeName() + "'");
            }

            result.add(archetype);
        }
        if (result.isEmpty()) {
            throw new IllegalAutojoinListException("autojoin list is empty");
        }
        result.trimToSize();
        return result;
    }

    /**
     * Looks up the given node in the archetype array of this class.
     * @param archetype the node to lookup
     * @return the index of the node in the array
     */
    public int getIndex(@NotNull final R archetype) {
        for (int i = 0; i < SIZE; i++) {
            if (archetypes.get(i).contains(archetype)) {
                return i;
            }
        }

        log.warn("Error in AutojoinList.get_index: index not found");
        return 0;
    }

    /**
     * Returns the index of an {@link Archetype} if it is a main archetype for
     * any direction.
     * @param archetype the archetype to check
     * @return whether the archetype is a main archetype
     */
    public boolean isMainIndex(@NotNull final R archetype) {
        for (int i = 0; i < SIZE; i++) {
            if (archetypes.get(i).get(0) == archetype) {
                return true;
            }
        }

        return false;
    }

    /**
     * Returns the index of an {@link Archetype} if it is an alternative
     * archetype for any direction.
     * @param archetype the archetype to check
     * @return the direction or <code>-1</code>
     */
    public int getAlternativeIndex(@NotNull final R archetype) {
        for (int i = 0; i < SIZE; i++) {
            final List<R> tmp = archetypes.get(i);
            if (tmp.get(0) != archetype && tmp.contains(archetype)) {
                return i;
            }
        }

        return -1;
    }

    /**
     * @noinspection TypeMayBeWeakened
     */
    @NotNull
    public R getArchetype(final int index) {
        return archetypes.get(index).get(0);
    }

    /**
     * Returns all archetypes for an index.
     * @param index the index
     * @return the archetypes; the result contains at least one entry
     */
    @NotNull
    public Iterable<R> getArchetypes(final int index) {
        return Collections.unmodifiableCollection(archetypes.get(index));
    }

}
