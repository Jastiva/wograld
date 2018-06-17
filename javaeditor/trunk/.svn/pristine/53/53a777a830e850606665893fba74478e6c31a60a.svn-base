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
 * An {@link ArchetypeAttribute} for selecting boolean values having specific
 * "true" and "false" values.
 * @author <a href="mailto:andi.vogl@gmx.net">Andreas Vogl</a>
 * @author Andreas Kirschbaum
 */
public class ArchetypeAttributeBoolSpec extends ArchetypeAttribute {

    /**
     * The true value.
     */
    @NotNull
    private final String trueValue;

    /**
     * The false value.
     */
    @NotNull
    private final String falseValue;

    /**
     * Creates a new instance.
     * @param archetypeAttributeName the archetype attribute name
     * @param attributeName the user interface attribute name
     * @param description the attribute's description
     * @param inputLength the input length in characters for text input fields
     * @param sectionName the section name for the new attribute
     * @param trueValue the true value
     * @param falseValue the false value
     */
    public ArchetypeAttributeBoolSpec(@NotNull final String archetypeAttributeName, @NotNull final String attributeName, @NotNull final String description, final int inputLength, @NotNull final String sectionName, @NotNull final String trueValue, @NotNull final String falseValue) {
        super(archetypeAttributeName, attributeName, description, inputLength, sectionName);
        this.trueValue = trueValue;
        this.falseValue = falseValue;
    }

    /**
     * Returns the true value.
     * @return the true value
     */
    @NotNull
    public String getTrueValue() {
        return trueValue;
    }

    /**
     * Returns the false value.
     * @return the false value
     */
    @NotNull
    public String getFalseValue() {
        return falseValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void visit(@NotNull final ArchetypeAttributeVisitor visitor) {
        visitor.visit(this);
    }

}
