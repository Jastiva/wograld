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

import org.jetbrains.annotations.NotNull;

/**
 * An {@link ArchetypeAttribute} for selecting a value from a list.
 * @author <a href="mailto:andi.vogl@gmx.net">Andreas Vogl</a>
 * @author Andreas Kirschbaum
 */
public class ArchetypeAttributeList extends AbstractArchetypeAttributeList {

    /**
     * The list name.
     */
    @NotNull
    private final String listName;

    /**
     * Creates a new instance.
     * @param archetypeAttributeName the archetype attribute name
     * @param attributeName the user interface attribute name
     * @param description the attribute's description
     * @param inputLength the input length in characters for text input fields
     * @param sectionName the section name for the new attribute
     * @param listName the list name
     */
    public ArchetypeAttributeList(@NotNull final String archetypeAttributeName, @NotNull final String attributeName, @NotNull final String description, final int inputLength, @NotNull final String sectionName, @NotNull final String listName) {
        super(archetypeAttributeName, attributeName, description, inputLength, sectionName);
        this.listName = listName;
    }

    /**
     * Returns the list name.
     * @return the list name
     */
    @NotNull
    public String getListName() {
        return listName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void visit(@NotNull final ArchetypeAttributeVisitor visitor) {
        visitor.visit(this);
    }

}
