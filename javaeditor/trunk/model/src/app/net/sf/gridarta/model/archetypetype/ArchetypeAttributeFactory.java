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
import org.jetbrains.annotations.Nullable;

/**
 * Interface for classes implementing {@link ArchetypeAttribute} factories.
 * @author Andreas Kirschbaum
 */
public interface ArchetypeAttributeFactory {

    /**
     * Creates a new "animname" archetype attribute.
     * @param archetypeAttributeName the archetype attribute name
     * @param attributeName the user interface attribute name
     * @param description the attribute's description
     * @param inputLength the input length in characters for text input fields
     * @param sectionName the section name for the new attribute
     * @return the archetype attribute
     */
    @NotNull
    ArchetypeAttribute newArchetypeAttributeAnimname(@NotNull String archetypeAttributeName, @NotNull String attributeName, @NotNull String description, int inputLength, @NotNull String sectionName);

    /**
     * Creates a new "bitmask" archetype attribute.
     * @param archetypeAttributeName the archetype attribute name
     * @param attributeName the user interface attribute name
     * @param description the attribute's description
     * @param inputLength the input length in characters for text input fields
     * @param sectionName the section name for the new attribute
     * @param bitmaskName the bitmask name
     * @return the bitmask attribute
     */
    @NotNull
    ArchetypeAttribute newArchetypeAttributeBitmask(@NotNull String archetypeAttributeName, @NotNull String attributeName, @NotNull String description, int inputLength, @NotNull String sectionName, @NotNull String bitmaskName);

    /**
     * Creates a new "bool" archetype attribute.
     * @param archetypeAttributeName the archetype attribute name
     * @param attributeName the user interface attribute name
     * @param description the attribute's description
     * @param inputLength the input length in characters for text input fields
     * @param sectionName the section name for the new attribute
     * @return the bitmask attribute
     */
    @NotNull
    ArchetypeAttribute newArchetypeAttributeBool(@NotNull String archetypeAttributeName, @NotNull String attributeName, @NotNull String description, int inputLength, @NotNull String sectionName);

    /**
     * Creates a new "bool spec" archetype attribute.
     * @param archetypeAttributeName the archetype attribute name
     * @param attributeName the user interface attribute name
     * @param description the attribute's description
     * @param inputLength the input length in characters for text input fields
     * @param sectionName the section name for the new attribute
     * @param trueValue the true value
     * @param falseValue the false value
     * @return the bitmask attribute
     */
    @NotNull
    ArchetypeAttribute newArchetypeAttributeBoolSpec(@NotNull String archetypeAttributeName, @NotNull String attributeName, @NotNull String description, int inputLength, @NotNull String sectionName, @NotNull String trueValue, @NotNull String falseValue);

    /**
     * Creates a new "face name" archetype attribute.
     * @param archetypeAttributeName the archetype attribute name
     * @param attributeName the user interface attribute name
     * @param description the attribute's description
     * @param inputLength the input length in characters for text input fields
     * @param sectionName the section name for the new attribute
     * @return the bitmask attribute
     */
    @NotNull
    ArchetypeAttribute newArchetypeAttributeFacename(@NotNull String archetypeAttributeName, @NotNull String attributeName, @NotNull String description, int inputLength, @NotNull String sectionName);

    /**
     * Creates a new "fixed" archetype attribute.
     * @param archetypeAttributeName the archetype attribute name
     * @param attributeName the user interface attribute name
     * @param description the attribute's description
     * @param inputLength the input length in characters for text input fields
     * @param sectionName the section name for the new attribute
     * @return the bitmask attribute
     */
    @NotNull
    ArchetypeAttribute newArchetypeAttributeFixed(@NotNull String archetypeAttributeName, @NotNull String attributeName, @NotNull String description, int inputLength, @NotNull String sectionName);

    /**
     * Creates a new "float" archetype attribute.
     * @param archetypeAttributeName the archetype attribute name
     * @param attributeName the user interface attribute name
     * @param description the attribute's description
     * @param inputLength the input length in characters for text input fields
     * @param sectionName the section name for the new attribute
     * @return the bitmask attribute
     */
    @NotNull
    ArchetypeAttribute newArchetypeAttributeFloat(@NotNull String archetypeAttributeName, @NotNull String attributeName, @NotNull String description, int inputLength, @NotNull String sectionName);

    /**
     * Creates a new "int" archetype attribute.
     * @param archetypeAttributeName the archetype attribute name
     * @param attributeName the user interface attribute name
     * @param description the attribute's description
     * @param inputLength the input length in characters for text input fields
     * @param sectionName the section name for the new attribute
     * @param minValue the absolute minimum allowed value
     * @param maxValue the absolute maximum allowed value
     * @param minCheckValue the logical minimum allowed value
     * @param maxCheckValue the logical maximum allowed value
     * @return the bitmask attribute
     */
    @NotNull
    ArchetypeAttribute newArchetypeAttributeInt(@NotNull String archetypeAttributeName, @NotNull String attributeName, @NotNull String description, int inputLength, @NotNull String sectionName, int minValue, int maxValue, int minCheckValue, int maxCheckValue);

    /**
     * Creates a new "inv spell" archetype attribute.
     * @param archetypeAttributeName the archetype attribute name
     * @param attributeName the user interface attribute name
     * @param description the attribute's description
     * @param inputLength the input length in characters for text input fields
     * @param sectionName the section name for the new attribute
     * @return the bitmask attribute
     */
    @NotNull
    ArchetypeAttribute newArchetypeAttributeInvSpell(@NotNull String archetypeAttributeName, @NotNull String attributeName, @NotNull String description, int inputLength, @NotNull String sectionName);

    /**
     * Creates a new "inv spell optional" archetype attribute.
     * @param archetypeAttributeName the archetype attribute name
     * @param attributeName the user interface attribute name
     * @param description the attribute's description
     * @param inputLength the input length in characters for text input fields
     * @param sectionName the section name for the new attribute
     * @return the bitmask attribute
     */
    @NotNull
    ArchetypeAttribute newArchetypeAttributeInvSpellOptional(@NotNull String archetypeAttributeName, @NotNull String attributeName, @NotNull String description, int inputLength, @NotNull String sectionName);

    /**
     * Creates a new "list" archetype attribute.
     * @param archetypeAttributeName the archetype attribute name
     * @param attributeName the user interface attribute name
     * @param description the attribute's description
     * @param inputLength the input length in characters for text input fields
     * @param sectionName the section name for the new attribute
     * @param listName the list name
     * @return the bitmask attribute
     */
    @NotNull
    ArchetypeAttribute newArchetypeAttributeList(@NotNull String archetypeAttributeName, @NotNull String attributeName, @NotNull String description, int inputLength, @NotNull String sectionName, @NotNull String listName);

    /**
     * Creates a new "long" archetype attribute.
     * @param archetypeAttributeName the archetype attribute name
     * @param attributeName the user interface attribute name
     * @param description the attribute's description
     * @param inputLength the input length in characters for text input fields
     * @param sectionName the section name for the new attribute
     * @return the bitmask attribute
     */
    @NotNull
    ArchetypeAttribute newArchetypeAttributeLong(@NotNull String archetypeAttributeName, @NotNull String attributeName, @NotNull String description, int inputLength, @NotNull String sectionName);

    /**
     * Creates a new "map path" archetype attribute.
     * @param archetypeAttributeName the archetype attribute name
     * @param attributeName the user interface attribute name
     * @param description the attribute's description
     * @param inputLength the input length in characters for text input fields
     * @param sectionName the section name for the new attribute
     * @return the bitmask attribute
     */
    @NotNull
    ArchetypeAttribute newArchetypeAttributeMapPath(@NotNull String archetypeAttributeName, @NotNull String attributeName, @NotNull String description, int inputLength, @NotNull String sectionName);

    /**
     * Creates a new "script file" archetype attribute.
     * @param archetypeAttributeName the archetype attribute name
     * @param attributeName the user interface attribute name
     * @param description the attribute's description
     * @param inputLength the input length in characters for text input fields
     * @param sectionName the section name for the new attribute
     * @return the bitmask attribute
     */
    @NotNull
    ArchetypeAttribute newArchetypeAttributeScriptFile(@NotNull String archetypeAttributeName, @NotNull String attributeName, @NotNull String description, int inputLength, @NotNull String sectionName);

    /**
     * Creates a new "spell" archetype attribute.
     * @param archetypeAttributeName the archetype attribute name
     * @param attributeName the user interface attribute name
     * @param description the attribute's description
     * @param inputLength the input length in characters for text input fields
     * @param sectionName the section name for the new attribute
     * @return the bitmask attribute
     */
    @NotNull
    ArchetypeAttribute newArchetypeAttributeSpell(@NotNull String archetypeAttributeName, @NotNull String attributeName, @NotNull String description, int inputLength, @NotNull String sectionName);

    /**
     * Creates a new "string" archetype attribute.
     * @param archetypeAttributeName the archetype attribute name
     * @param attributeName the user interface attribute name
     * @param description the attribute's description
     * @param inputLength the input length in characters for text input fields
     * @param sectionName the section name for the new attribute
     * @return the bitmask attribute
     */
    @NotNull
    ArchetypeAttribute newArchetypeAttributeString(@NotNull String archetypeAttributeName, @NotNull String attributeName, @NotNull String description, int inputLength, @NotNull String sectionName);

    /**
     * Creates a new "text" archetype attribute.
     * @param archetypeAttributeName the archetype attribute name
     * @param endingOld the terminating string
     * @param attributeName the user interface attribute name
     * @param description the attribute's description
     * @param inputLength the input length in characters for text input fields
     * @param fileExtension the file extension
     * @return the bitmask attribute
     */
    @NotNull
    ArchetypeAttribute newArchetypeAttributeText(@NotNull String archetypeAttributeName, @NotNull String endingOld, @NotNull String attributeName, @NotNull String description, int inputLength, @Nullable String fileExtension);

    /**
     * Creates a new "treasure" archetype attribute.
     * @param archetypeAttributeName the archetype attribute name
     * @param attributeName the user interface attribute name
     * @param description the attribute's description
     * @param inputLength the input length in characters for text input fields
     * @param sectionName the section name for the new attribute
     * @return the bitmask attribute
     */
    @NotNull
    ArchetypeAttribute newArchetypeAttributeTreasure(@NotNull String archetypeAttributeName, @NotNull String attributeName, @NotNull String description, int inputLength, @NotNull String sectionName);

    /**
     * Creates a new "z-spell" archetype attribute.
     * @param archetypeAttributeName the archetype attribute name
     * @param attributeName the user interface attribute name
     * @param description the attribute's description
     * @param inputLength the input length in characters for text input fields
     * @param sectionName the section name for the new attribute
     * @return the bitmask attribute
     */
    @NotNull
    ArchetypeAttribute newArchetypeAttributeZSpell(@NotNull String archetypeAttributeName, @NotNull String attributeName, @NotNull String description, int inputLength, @NotNull String sectionName);

}
