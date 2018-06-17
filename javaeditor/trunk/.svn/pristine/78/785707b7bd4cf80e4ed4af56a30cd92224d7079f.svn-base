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

package net.sf.gridarta.model.validation.errors;

import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Indicate an attribute value that is out of range.
 * @author Andreas Kirschbaum
 */
public class AttributeRangeError<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends GameObjectValidationError<G, A, R> {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The suspicious attribute name.
     * @serial
     */
    @NotNull
    private final String attributeName;

    /**
     * The current (incorrect) attribute value.
     * @serial
     */
    @NotNull
    private final String curValue;

    /**
     * The minimal allowed attribute value.
     * @serial
     */
    @NotNull
    private final String minValue;

    /**
     * The maximal allowed attribute value.
     * @serial
     */
    @NotNull
    private final String maxValue;

    /**
     * Create a new instance.
     * @param gameObject The game object having an out of range value.
     * @param attributeName The suspicious attribute name.
     * @param curValue the current (incorrect) attribute value
     * @param minValue the minimal allowed attribute value
     * @param maxValue the maximal allowed attribute value
     */
    public AttributeRangeError(@NotNull final G gameObject, @NotNull final String attributeName, @NotNull final String curValue, @NotNull final String minValue, @NotNull final String maxValue) {
        super(gameObject);
        this.attributeName = attributeName;
        this.curValue = curValue;
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public String getParameter(final int id) {
        switch (id) {
        case 0:
            return attributeName;

        case 1:
            return curValue;

        case 2:
            return minValue;

        case 3:
            return maxValue;

        default:
            return null;
        }
    }

}
