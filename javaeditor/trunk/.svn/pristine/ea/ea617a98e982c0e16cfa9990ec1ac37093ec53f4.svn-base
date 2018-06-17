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
 * This Class contains the data of one archetype attribute.
 * @author <a href="mailto:andi.vogl@gmx.net">Andreas Vogl</a>
 */
public abstract class ArchetypeAttribute implements Cloneable {

    /**
     * The width (columns) for input fields like textfields or JChooseBoxes.
     */
    public static final int TEXTFIELD_COLUMNS = 18;

    /**
     * The archetype attribute name.
     */
    @NotNull
    private final String archetypeAttributeName;

    /**
     * The user interface attribute name.
     */
    @NotNull
    private final String attributeName;

    /**
     * The attribute's description.
     */
    @NotNull
    private final String description;

    /**
     * The input length in characters for text input fields.
     */
    private final int inputLength;

    /**
     * Identifier of the section this attribute is in.
     */
    private int sectionId = -1;

    /**
     * Name of the section this attribute is in.
     */
    @NotNull
    private final String sectionName;

    /**
     * Creates a new instance.
     * @param archetypeAttributeName the archetype attribute name
     * @param attributeName the user interface attribute name
     * @param description the attribute's description
     * @param inputLength the input length in characters for text input fields
     * @param sectionName the section name
     */
    protected ArchetypeAttribute(@NotNull final String archetypeAttributeName, @NotNull final String attributeName, @NotNull final String description, final int inputLength, @NotNull final String sectionName) {
        this.archetypeAttributeName = archetypeAttributeName;
        this.attributeName = attributeName;
        this.description = description;
        this.inputLength = inputLength;
        this.sectionName = sectionName;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public ArchetypeAttribute clone() {
        try {
            return (ArchetypeAttribute) super.clone();
        } catch (final CloneNotSupportedException ex) {
            throw new AssertionError(ex);
        }
    }

    /**
     * Returns the section ID.
     * @return the section ID
     */
    public int getSectionId() {
        return sectionId;
    }

    /**
     * Sets the section ID.
     * @param sectionId the section ID
     */
    public void setSectionId(final int sectionId) {
        this.sectionId = sectionId;
    }

    /**
     * Returns the section name.
     * @return the section name
     */
    @NotNull
    public String getSectionName() {
        return sectionName;
    }

    /**
     * Returns the archetype attribute name.
     * @return the archetype attribute name
     */
    @NotNull
    public String getArchetypeAttributeName() {
        return archetypeAttributeName;
    }

    /**
     * Returns the user interface attribute name.
     * @return the user interface attribute name
     */
    @NotNull
    public String getAttributeName() {
        return attributeName;
    }

    /**
     * Returns the attribute's description.
     * @return the attribute's description
     */
    @NotNull
    public String getDescription() {
        return description;
    }

    /**
     * Returns the input length in characters for text input fields.
     * @return the input length
     */
    public int getInputLength() {
        return inputLength;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public String toString() {
        return archetypeAttributeName + "[" + attributeName + "] section=" + sectionId + "/" + sectionName;
    }

    /**
     * Calls the <code>visit()</code> function appropriate for this instance.
     * @param visitor the visitor to call
     */
    public abstract void visit(@NotNull final ArchetypeAttributeVisitor visitor);

}
