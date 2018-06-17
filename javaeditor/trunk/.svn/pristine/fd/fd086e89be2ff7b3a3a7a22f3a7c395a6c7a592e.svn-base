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

import net.sf.gridarta.model.spells.NumberSpell;
import net.sf.gridarta.model.spells.Spells;
import org.jetbrains.annotations.NotNull;

/**
 * Abstract base class for {@link ArchetypeAttribute ArchetypeAttributes} for
 * spells.
 * @author Andreas Kirschbaum
 */
public abstract class AbstractArchetypeAttributeSpell extends ArchetypeAttribute {

    /**
     * Creates a new instance.
     * @param archetypeAttributeName the archetype attribute name
     * @param attributeName the user interface attribute name
     * @param description the attribute's description
     * @param inputLength the input length in characters for text input fields
     * @param sectionName the section name for the new attribute
     */
    protected AbstractArchetypeAttributeSpell(@NotNull final String archetypeAttributeName, @NotNull final String attributeName, @NotNull final String description, final int inputLength, @NotNull final String sectionName) {
        super(archetypeAttributeName, attributeName, description, inputLength, sectionName);
    }

    /**
     * Returns the spell index for a spell number.
     * @param numberSpells the number spells to use
     * @param number the spell number
     * @return the spell index or <code>-1</code> if not found
     */
    public static int findSpellIndex(@NotNull final Spells<NumberSpell> numberSpells, final int number) {
        for (int i = 0; i < numberSpells.size(); i++) {
            if (numberSpells.getSpell(i).getNumber() == number) {
                return i;
            }
        }
        return -1;
    }

}
