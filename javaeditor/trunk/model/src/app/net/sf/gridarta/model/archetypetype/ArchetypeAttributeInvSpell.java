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
 * An {@link ArchetypeAttribute} for selecting a spell encoded as an inventory
 * game object.
 * @author <a href="mailto:andi.vogl@gmx.net">Andreas Vogl</a>
 * @author Andreas Kirschbaum
 */
public class ArchetypeAttributeInvSpell extends AbstractArchetypeAttributeInvSpell {

    /**
     * Whether the spell game object is optional.
     */
    private final boolean isOptionalSpell;

    /**
     * Creates a new instance.
     * @param isOptionalSpell whether the spell game object is optional
     * @param archetypeAttributeName the archetype attribute name
     * @param attributeName the user interface attribute name
     * @param description the attribute's description
     * @param inputLength the input length in characters for text input fields
     * @param sectionName the section name for the new attribute
     */
    public ArchetypeAttributeInvSpell(final boolean isOptionalSpell, @NotNull final String archetypeAttributeName, @NotNull final String attributeName, @NotNull final String description, final int inputLength, @NotNull final String sectionName) {
        super(archetypeAttributeName, attributeName, description, inputLength, sectionName);
        this.isOptionalSpell = isOptionalSpell;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void visit(@NotNull final ArchetypeAttributeVisitor visitor) {
        visitor.visit(this);
    }

    /**
     * Returns whether the spell game object is optional.
     * @return whether the spell game object is optional
     */
    public boolean isOptionalSpell() {
        return isOptionalSpell;
    }

}
