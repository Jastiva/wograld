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
import net.sf.gridarta.model.archetypetype.ArchetypeAttributeInvSpell;
import net.sf.gridarta.model.archetypetype.ArchetypeType;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.spells.GameObjectSpell;
import net.sf.gridarta.model.spells.Spells;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DialogAttributeInvSpell<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends DialogAttribute<G, A, R, ArchetypeAttributeInvSpell> {

    /**
     * Whether the spell game object is optional.
     */
    private final boolean isOptionalSpell;

    /**
     * The input ui component for editing the value.
     */
    @NotNull
    private final JComboBox input;

    /**
     * The game object spells to display.
     */
    @NotNull
    private final Spells<GameObjectSpell<G, A, R>> gameObjectSpells;

    /**
     * Creates a new instance.
     * @param isOptionalSpell whether the spell game object is optional
     * @param ref reference to the attribute data
     * @param input the input ui component for editing the value
     * @param gameObjectSpells the game object spells to display
     */
    public DialogAttributeInvSpell(final boolean isOptionalSpell, @NotNull final ArchetypeAttributeInvSpell ref, @NotNull final JComboBox input, @NotNull final Spells<GameObjectSpell<G, A, R>> gameObjectSpells) {
        super(ref);
        this.isOptionalSpell = isOptionalSpell;
        this.input = input;
        this.gameObjectSpells = gameObjectSpells;
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public String getText2(@NotNull final G gameObject, @NotNull final Archetype<G, A, R> archetype, final String[] newMsg, final ArchetypeType archetypeType, final Component parent) {
        final int index = input.getSelectedIndex();
        if (index < gameObjectSpells.size() + (isOptionalSpell ? 1 : 0)) {
            final boolean isModified;
            switch (gameObject.countInvObjects()) {
            case 0:
                // game object has no inventory ==> isModified if
                // anything other than <none> is selected
                isModified = index != gameObjectSpells.size();
                break;

            default:
                // game object has multiple inventories ==> always isModified
                isModified = true;
                break;

            case 1:
                if (index >= gameObjectSpells.size()) {
                    // game object has one inventory, <none> is
                    // selected ==> isModified
                    isModified = true;
                } else {
                    // game object has one inventory, a spell is
                    // selected ==> isModified if a different spell is
                    // selected
                    final GameObject<G, A, R> invObject = gameObject.iterator().next();
                    if (invObject.isDefaultGameObject()) {
                        final String invObjectArchetypeName = invObject.getArchetype().getArchetypeName();
                        final GameObjectSpell<G, A, R> spellObject = gameObjectSpells.getSpell(index);
                        isModified = !invObjectArchetypeName.equals(spellObject.getArchetypeName());
                    } else {
                        isModified = true;
                    }
                }
                break;
            }
            if (isModified) {
                gameObject.removeAll();
                if (index < gameObjectSpells.size()) {
                    final G spellObject = gameObjectSpells.getSpell(index).createGameObject();
                    gameObject.addLast(spellObject);
                }

                // remove the entry for a customized spell
                final int modelSize = input.getModel().getSize();
                if (modelSize > gameObjectSpells.size() + (isOptionalSpell ? 1 : 0)) {
                    input.removeItemAt(modelSize - 1);
                }
            }
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
