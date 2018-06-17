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

import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.validation.ErrorCollector;
import net.sf.gridarta.model.validation.errors.AttributeRangeError;
import org.jetbrains.annotations.NotNull;

/**
 * An attribute to check.
 * @author Andreas Kirschbaum
 */
public class RangeEntry<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> {

    /**
     * The attribute name to display in error messages.
     */
    @NotNull
    private final String displayName;

    /**
     * The attribute values range to check for.
     */
    @NotNull
    private final Range range;

    /**
     * Create a new instance.
     * @param displayName The attribute name to display in error messages.
     * @param range The attribute values range to check for.
     */
    public RangeEntry(@NotNull final String displayName, @NotNull final Range range) {
        this.displayName = displayName;
        this.range = range;
    }

    /**
     * Validate an attribute value.
     * @param value The attribute value to check.
     * @param gameObject The game object containing the attribute value.
     * @param errorCollector The error collector to receive the errors.
     */
    public void validate(final int value, @NotNull final G gameObject, @NotNull final ErrorCollector<G, A, R> errorCollector) {
        if (!range.isWithinRange(value)) {
            errorCollector.collect(new AttributeRangeError<G, A, R>(gameObject, displayName, Integer.toString(value), Integer.toString(range.getMin()), Integer.toString(range.getMax())));
        }
    }

}
