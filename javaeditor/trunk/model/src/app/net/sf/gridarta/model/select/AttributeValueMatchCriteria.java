/*
 * Gridarta MMORPG map editor for Crossfire, Daimonin and similar games.
 * Copyright (C) 2000-2011 The Gridarta Developers.
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

package net.sf.gridarta.model.select;

import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link MatchCriteria} that matches an attribute value.
 * @author Andreas Kirschbaum
 */
public class AttributeValueMatchCriteria<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> implements MatchCriteria<G, A, R> {

    /**
     * The attribute name to check.
     */
    @NotNull
    private final String attributeName;

    /**
     * The attribute value to check.
     */
    @NotNull
    private final String attributeValue;

    /**
     * Creates a new instance.
     * @param attributeName the attribute name to check
     * @param attributeValue the attribute value to check
     */
    public AttributeValueMatchCriteria(@NotNull final String attributeName, @NotNull final String attributeValue) {
        this.attributeName = attributeName;
        this.attributeValue = attributeValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean matches(@NotNull final G gameObject) {
        return gameObject.getAttributeString(attributeName).contains(attributeValue);
    }

}
