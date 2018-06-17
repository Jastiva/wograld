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
import net.sf.gridarta.model.archetypetype.ArchetypeAttributeList;
import net.sf.gridarta.model.archetypetype.ArchetypeType;
import net.sf.gridarta.model.archetypetype.ArchetypeTypeSet;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DialogAttributeList<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends DialogAttribute<G, A, R, ArchetypeAttributeList> {

    /**
     * The input ui component for editing the value.
     */
    @NotNull
    private final JComboBox input;

    /**
     * The archetype type set.
     */
    @NotNull
    private final ArchetypeTypeSet archetypeTypeSet;

    /**
     * Creates a new instance.
     * @param ref reference to the attribute data
     * @param input the input ui component for editing the value
     * @param archetypeTypeSet the archetype type set
     */
    public DialogAttributeList(@NotNull final ArchetypeAttributeList ref, @NotNull final JComboBox input, @NotNull final ArchetypeTypeSet archetypeTypeSet) {
        super(ref);
        this.input = input;
        this.archetypeTypeSet = archetypeTypeSet;
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public String getText2(@NotNull final G gameObject, @NotNull final Archetype<G, A, R> archetype, final String[] newMsg, final ArchetypeType archetypeType, final Component parent) {
        final int attributeValueTmp = input.getSelectedIndex();
        final int attributeValue = archetypeTypeSet.getList(getRef().getListName()).get(attributeValueTmp).getFirst(); // attribute value

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
