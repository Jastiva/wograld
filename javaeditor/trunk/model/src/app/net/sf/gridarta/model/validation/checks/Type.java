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

package net.sf.gridarta.model.validation.checks;

import java.util.HashMap;
import java.util.Map;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.validation.ErrorCollector;
import org.jetbrains.annotations.NotNull;

/**
 * Manages all checks for one game object type.
 * @author Andreas Kirschbaum
 */
public class Type<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> {

    /**
     * Maps attribute name to corresponding {@link RangeEntry} instance.
     */
    @NotNull
    private final Map<String, RangeEntry<G, A, R>> entries = new HashMap<String, RangeEntry<G, A, R>>();

    /**
     * Add an attribute to check.
     * @param name The attribute name to check.
     * @param displayName The attribute name to display in error messages.
     * @param range The valid attribute values range.
     * @throws InvalidCheckException if the check is invalid
     */
    public void add(@NotNull final String name, @NotNull final String displayName, @NotNull final Range range) throws InvalidCheckException {
        if (entries.containsKey(name)) {
            throw new InvalidCheckException("multiple checks for attribute '" + name + "'");
        }

        final RangeEntry<G, A, R> entry = new RangeEntry<G, A, R>(displayName, range);
        entries.put(name, entry);
    }

    /**
     * Validate a game object.
     * @param gameObject The game object to check.
     * @param errorCollector The error collector to receive the errors.
     */
    public void validate(@NotNull final G gameObject, @NotNull final ErrorCollector<G, A, R> errorCollector) {
        for (final Map.Entry<String, RangeEntry<G, A, R>> e : entries.entrySet()) {
            final String name = e.getKey();
            final RangeEntry<G, A, R> entry = e.getValue();
            entry.validate(gameObject.getHead().getAttributeInt(name), gameObject, errorCollector);
        }
    }

}
