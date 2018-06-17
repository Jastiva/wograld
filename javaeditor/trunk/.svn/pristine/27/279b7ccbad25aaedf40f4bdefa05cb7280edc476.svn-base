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
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.text.Style;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.archetypetype.ArchetypeAttributeBitmask;
import net.sf.gridarta.model.archetypetype.ArchetypeType;
import net.sf.gridarta.model.archetypetype.AttributeBitmask;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * DialogAttribute for types with bitmasks to choose from.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 */
public class DialogAttributeBitmask<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends DialogAttribute<G, A, R, ArchetypeAttributeBitmask> {

    /**
     * The Logger for printing log messages.
     */
    private static final Category log = Logger.getLogger(DialogAttributeBitmask.class);

    /**
     * The input ui component for editing the value.
     */
    @NotNull
    private final JTextComponent input;

    /**
     * Active bitmask value.
     */
    private int value;

    /**
     * Reference to the bitmask data.
     */
    private AttributeBitmask bitmask;

    /**
     * Creates a new instance.
     * @param ref reference to the attribute data
     * @param input the input ui component for editing the value
     */
    public DialogAttributeBitmask(@NotNull final ArchetypeAttributeBitmask ref, @NotNull final JTextComponent input) {
        super(ref);
        this.input = input;
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public String getText2(@NotNull final G gameObject, @NotNull final Archetype<G, A, R> archetype, final String[] newMsg, final ArchetypeType archetypeType, final Component parent) {
        final String encodedValue = getEncodedValue(); // get bitmask value
        final String archetypeAttributeName = getRef().getArchetypeAttributeName();
        CharSequence oldValue = archetype.getAttributeString(archetypeAttributeName);
        if (oldValue.length() == 0) {
            oldValue = "0";
        }

        if (!oldValue.equals(encodedValue)) {
            return archetypeAttributeName + " " + encodedValue;
        }

        return "";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void appendSummary(@NotNull final Document doc, @NotNull final Style style) {
        final String tmp = input.getText().trim();
        if (tmp.length() > 0 && !tmp.startsWith("<")) {
            addLine(doc, style, getRef().getAttributeName() + " = " + tmp);
        }
    }

    /**
     * Get the active bitmask value.
     * @return active bitmask value
     */
    public int getValue() {
        return value;
    }

    /**
     * Get the active bitmask value in external file representation.
     * @return active bitmask value in external file representation
     */
    private String getEncodedValue() {
        return bitmask.encodeValue(value);
    }

    /**
     * Set the active bitmask value.
     * @param value new active bitmask value
     */
    public void setValue(final int value) {
        this.value = value;
        if (bitmask != null) {
            input.setText(bitmask.getText(value));
        } else {
            log.warn("null bitmask");
        }
    }

    /**
     * Set the active bitmask value in external file representation.
     * @param encodedValue new active bitmask value in external file
     * representation
     */
    public void setEncodedValue(final String encodedValue) {
        assert bitmask != null;
        setValue(bitmask.decodeValue(encodedValue));
    }

    public AttributeBitmask getBitmask() {
        return bitmask;
    }

    public void setBitmask(final AttributeBitmask bitmask) {
        this.bitmask = bitmask;
    }

    /**
     * Returns the input ui component for editing the value.
     * @return the component
     */
    @NotNull
    public Component getInput() {
        return input;
    }

}
