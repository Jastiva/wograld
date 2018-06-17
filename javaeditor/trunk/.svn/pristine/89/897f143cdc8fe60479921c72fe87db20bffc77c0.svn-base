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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.utils.StringUtils;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link MatchCriteria} that matches attribute values of all but a set of
 * attributes.
 * @author Andreas Kirschbaum
 */
public class AttributeOtherValueMatchCriteria<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> implements MatchCriteria<G, A, R> {

    /**
     * The attribute names <em>not</em> to check.
     */
    @NotNull
    private final Set<String> attributeNames;

    /**
     * The attribute value to check.
     */
    @NotNull
    private final String attributeValue;

    /**
     * Creates a new instance.
     * @param attributeValue the attribute value to check
     * @param attributeNames the attribute names <em>not</em> to check
     */
    public AttributeOtherValueMatchCriteria(@NotNull final String attributeValue, @NotNull final String... attributeNames) {
        this.attributeValue = attributeValue;
        this.attributeNames = new HashSet<String>(Arrays.asList(attributeNames));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean matches(@NotNull final G gameObject) {
        for (final String attribute : StringUtils.PATTERN_END_OF_LINE.split(gameObject.getObjectText())) {
            final String[] tmp = StringUtils.PATTERN_SPACES.split(attribute, 2);
            if (tmp.length == 2 && !attributeNames.contains(tmp[0])) {
                if (tmp[1].contains(attributeValue)) {
                    return true;
                }
            }
        }

        return false;
    }

}
