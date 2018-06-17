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
import javax.swing.JOptionPane;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.text.Style;
import net.sf.gridarta.gui.treasurelist.CFTreasureListTree;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.archetypetype.ArchetypeAttributeTreasure;
import net.sf.gridarta.model.archetypetype.ArchetypeType;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.treasurelist.TreasureTree;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

// a treasurelist attribute

public class DialogAttributeTreasure<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends DialogAttribute<G, A, R, ArchetypeAttributeTreasure> {

    /**
     * The input ui component for editing the value.
     */
    @NotNull
    private final JTextComponent input;

    /**
     * The {@link TreasureTree} to use.
     */
    @NotNull
    private final TreasureTree treasureTree;

    /**
     * Creates a new instance.
     * @param ref reference to the attribute data
     * @param input the input ui component for editing the value
     * @param treasureTree the treasure tree to use
     */
    public DialogAttributeTreasure(@NotNull final ArchetypeAttributeTreasure ref, @NotNull final JTextComponent input, @NotNull final TreasureTree treasureTree) {
        super(ref);
        this.input = input;
        this.treasureTree = treasureTree;
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public String getText2(@NotNull final G gameObject, @NotNull final Archetype<G, A, R> archetype, final String[] newMsg, final ArchetypeType archetypeType, final Component parent) {
        final String inline = input.getText().trim(); // input string
        final boolean isNone = inline.equals(CFTreasureListTree.NONE_SYM) || inline.length() == 0;
        final String archetypeAttributeName = getRef().getArchetypeAttributeName();
        if (!isNone && treasureTree.get(inline) == null && !inline.equalsIgnoreCase(archetype.getAttributeString(archetypeAttributeName))) {
            // The user has specified a WRONG treasurelist name, and it does not come
            // from the default gameObject. -> Error and out.
            JOptionPane.showMessageDialog(parent, "In attribute '" + getRef().getAttributeName() + "':\n" + "'" + inline + "' is not a known treasurelist name!", "Input Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        if (!inline.equalsIgnoreCase(archetype.getAttributeString(archetypeAttributeName)) && !(isNone && !archetype.hasAttribute(archetypeAttributeName))) {
            if (isNone) {
                return archetypeAttributeName + " none";
            } else {
                return archetypeAttributeName + " " + inline;
            }
        }

        return "";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void appendSummary(@NotNull final Document doc, @NotNull final Style style) {
        final String value = input.getText().trim();
        if (value.length() > 0 && !value.equals(CFTreasureListTree.NONE_SYM)) {
            addLine(doc, style, getRef().getAttributeName() + " = " + value);
        }
    }

}
