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
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.text.Document;
import javax.swing.text.Style;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.archetypetype.ArchetypeAttributeInt;
import net.sf.gridarta.model.archetypetype.ArchetypeType;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DialogAttributeInt<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends DialogAttribute<G, A, R, ArchetypeAttributeInt> {

    /**
     * Reference to the attribute data.
     */
    @NotNull
    private final ArchetypeAttributeInt ref;

    /**
     * The input ui component for editing the value.
     */
    @NotNull
    private final JFormattedTextField input;

    /**
     * Creates a new instance.
     * @param ref reference to the attribute data
     * @param input the input ui component for editing the value
     */
    public DialogAttributeInt(@NotNull final ArchetypeAttributeInt ref, @NotNull final JFormattedTextField input) {
        super(ref);
        this.ref = ref;
        this.input = input;
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public String getText2(@NotNull final G gameObject, @NotNull final Archetype<G, A, R> archetype, final String[] newMsg, final ArchetypeType archetypeType, final Component parent) {
        final String archetypeAttributeName = ref.getArchetypeAttributeName();
        final int value;
        if (input.getText().trim().length() == 0) {
            value = 0;
        } else {
            try {
                value = ((Number) input.getValue()).intValue();
            } catch (final NumberFormatException ignored) {
                JOptionPane.showMessageDialog(parent, "Attribute '" + ref.getAttributeName() + "' must be a number!", "Input Error", JOptionPane.ERROR_MESSAGE);
                return null;
            }
        }

        if (value < ref.getMinValue() || value > ref.getMaxValue()) {
            JOptionPane.showMessageDialog(parent, "Attribute '" + ref.getAttributeName() + "' must be " + ref.getMinValue() + ".." + ref.getMaxValue() + "!", "Input Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        if (archetype.getAttributeInt(archetypeAttributeName) != value) {
            return archetypeAttributeName + " " + value;
        }

        return "";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void appendSummary(@NotNull final Document doc, @NotNull final Style style) {
        final String value = input.getText();
        if (value != null && value.length() > 0 && !value.equals("0")) {
            addLine(doc, style, ref.getAttributeName() + " = " + value);
        }
    }

}
