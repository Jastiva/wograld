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
import net.sf.gridarta.model.baseobject.BaseObject;
import org.jetbrains.annotations.NotNull;

/**
 * A set of {@link ArchetypeAttributeDefinition ArchetypeAttributeDefinitions}.
 * Corresponds to a list of &lt;attribute&gt; definitions in types.xml.
 * @author Andreas Kirschbaum
 */
public class ArchetypeAttributesDefinition implements Iterable<ArchetypeAttributeDefinition> {

    /**
     * The {@link ArchetypeAttributeDefinition ArchetypeAttributeDefinitions}.
     */
    @NotNull
    private final List<ArchetypeAttributeDefinition> archetypeAttributes = new ArrayList<ArchetypeAttributeDefinition>();

    /**
     * Adds an {@link ArchetypeAttributeDefinition}.
     * @param archetypeAttribute the definition to add
     */
    public void add(@NotNull final ArchetypeAttributeDefinition archetypeAttribute) {
        archetypeAttributes.add(archetypeAttribute); // XXX: check for duplicates
    }

    /**
     * Checks whether a {@link BaseObject} matches all type attributes.
     * @param baseObject the base object to check
     * @return whether the base object matches
     */
    public boolean matches(@NotNull final BaseObject<?, ?, ?, ?> baseObject) {
        for (final ArchetypeAttributeDefinition typeAttribute : archetypeAttributes) {
            if (!typeAttribute.matches(baseObject)) {
                return false;
            }
        }

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<ArchetypeAttributeDefinition> iterator() {
        return Collections.unmodifiableList(archetypeAttributes).iterator();
    }

    /**
     * Returns whether no attribute definitions exist.
     * @return whether no attribute definitions exist
     */
    public boolean isEmpty() {
        return archetypeAttributes.isEmpty();
    }

}
