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

package net.sf.gridarta.gui.dialog.gameobjectattributes;

import java.awt.Component;
import javax.swing.JComboBox;
import javax.swing.text.Document;
import javax.swing.text.Style;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.archetypetype.ArchetypeAttributeZSpell;
import net.sf.gridarta.model.archetypetype.ArchetypeType;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.spells.NumberSpell;
import net.sf.gridarta.model.spells.Spells;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DialogAttributeZSpell<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends DialogAttribute<G, A, R, ArchetypeAttributeZSpell> {

    /**
     * The input ui component for editing the value.
     */
    @NotNull
    private final JComboBox input;

    /**
     * The number spells to display.
     */
    private final Spells<NumberSpell> numberSpells;

    /**
     * The index used for "no" spell.
     */
    private final int undefinedSpellIndex;

    /**
     * Creates a new instance.
     * @param ref reference to the attribute data
     * @param input the input ui component for editing the value
     * @param numberSpells the number spells to display
     * @param undefinedSpellIndex the index used for "no" spell
     */
    public DialogAttributeZSpell(@NotNull final ArchetypeAttributeZSpell ref, @NotNull final JComboBox input, @NotNull final Spells<NumberSpell> numberSpells, final int undefinedSpellIndex) {
        super(ref);
        this.input = input;
        this.numberSpells = numberSpells;
        this.undefinedSpellIndex = undefinedSpellIndex;
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public String getText2(@NotNull final G gameObject, @NotNull final Archetype<G, A, R> archetype, final String[] newMsg, final ArchetypeType archetypeType, final Component parent) {
        final int index = input.getSelectedIndex();
        final int attributeValue = index == 0 ? undefinedSpellIndex : numberSpells.getSpell(index - 1).getNumber(); // attribute value
        final String archetypeAttributeName = getRef().getArchetypeAttributeName();
        if (archetype.getAttributeInt(archetypeAttributeName) != attributeValue) {
            return archetypeAttributeName + " " + attributeValue;
        }

        return "";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void appendSummary(@NotNull final Document doc, @NotNull final Style style) {
        final String value = input.getSelectedItem().toString().trim();
        if (value.length() > 0 && !value.startsWith("<")) {
            addLine(doc, style, getRef().getAttributeName() + " = " + value);
        }
    }

}
