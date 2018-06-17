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
 * An {@link ArchetypeAttributeFactory} suitable for unit tests.
 * @author Andreas Kirschbaum
 */
public class TestArchetypeAttributeFactory implements ArchetypeAttributeFactory {

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public ArchetypeAttribute newArchetypeAttributeAnimname(@NotNull final String archetypeAttributeName, @NotNull final String attributeName, @NotNull final String description, final int inputLength, @NotNull final String sectionName) {
        return new TestArchetypeAttribute("animname", archetypeAttributeName, attributeName, description, inputLength, sectionName);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public ArchetypeAttribute newArchetypeAttributeBitmask(@NotNull final String archetypeAttributeName, @NotNull final String attributeName, @NotNull final String description, final int inputLength, @NotNull final String sectionName, @NotNull final String bitmaskName) {
        return new TestArchetypeAttribute("bitmask", archetypeAttributeName, attributeName, description, inputLength, sectionName);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public ArchetypeAttribute newArchetypeAttributeBool(@NotNull final String archetypeAttributeName, @NotNull final String attributeName, @NotNull final String description, final int inputLength, @NotNull final String sectionName) {
        return new TestArchetypeAttribute("bool", archetypeAttributeName, attributeName, description, inputLength, sectionName);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public ArchetypeAttribute newArchetypeAttributeBoolSpec(@NotNull final String archetypeAttributeName, @NotNull final String attributeName, @NotNull final String description, final int inputLength, @NotNull final String sectionName, @NotNull final String trueValue, @NotNull final String falseValue) {
        return new TestArchetypeAttribute("bool_spec", archetypeAttributeName, attributeName, description, inputLength, sectionName);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public ArchetypeAttribute newArchetypeAttributeFacename(@NotNull final String archetypeAttributeName, @NotNull final String attributeName, @NotNull final String description, final int inputLength, @NotNull final String sectionName) {
        return new TestArchetypeAttribute("face_name", archetypeAttributeName, attributeName, description, inputLength, sectionName);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public ArchetypeAttribute newArchetypeAttributeFixed(@NotNull final String archetypeAttributeName, @NotNull final String attributeName, @NotNull final String description, final int inputLength, @NotNull final String sectionName) {
        return new TestArchetypeAttribute("fixed", archetypeAttributeName, attributeName, description, inputLength, sectionName);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public ArchetypeAttribute newArchetypeAttributeFloat(@NotNull final String archetypeAttributeName, @NotNull final String attributeName, @NotNull final String description, final int inputLength, @NotNull final String sectionName) {
        return new TestArchetypeAttribute("float", archetypeAttributeName, attributeName, description, inputLength, sectionName);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public ArchetypeAttribute newArchetypeAttributeInt(@NotNull final String archetypeAttributeName, @NotNull final String attributeName, @NotNull final String description, final int inputLength, @NotNull final String sectionName, final int minValue, final int maxValue, final int minCheckValue, final int maxCheckValue) {
        return new TestArchetypeAttribute("int", archetypeAttributeName, attributeName, description, inputLength, sectionName);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public ArchetypeAttribute newArchetypeAttributeInvSpell(@NotNull final String archetypeAttributeName, @NotNull final String attributeName, @NotNull final String description, final int inputLength, @NotNull final String sectionName) {
        return new TestArchetypeAttribute("inv_spell", archetypeAttributeName, attributeName, description, inputLength, sectionName);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public ArchetypeAttribute newArchetypeAttributeInvSpellOptional(@NotNull final String archetypeAttributeName, @NotNull final String attributeName, @NotNull final String description, final int inputLength, @NotNull final String sectionName) {
        return new TestArchetypeAttribute("inv_spell_optional", archetypeAttributeName, attributeName, description, inputLength, sectionName);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public ArchetypeAttribute newArchetypeAttributeList(@NotNull final String archetypeAttributeName, @NotNull final String attributeName, @NotNull final String description, final int inputLength, @NotNull final String sectionName, @NotNull final String listName) {
        return new TestArchetypeAttribute("list", archetypeAttributeName, attributeName, description, inputLength, sectionName);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public ArchetypeAttribute newArchetypeAttributeLong(@NotNull final String archetypeAttributeName, @NotNull final String attributeName, @NotNull final String description, final int inputLength, @NotNull final String sectionName) {
        return new TestArchetypeAttribute("long", archetypeAttributeName, attributeName, description, inputLength, sectionName);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public ArchetypeAttribute newArchetypeAttributeMapPath(@NotNull final String archetypeAttributeName, @NotNull final String attributeName, @NotNull final String description, final int inputLength, @NotNull final String sectionName) {
        return new TestArchetypeAttribute("map_path", archetypeAttributeName, attributeName, description, inputLength, sectionName);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public ArchetypeAttribute newArchetypeAttributeScriptFile(@NotNull final String archetypeAttributeName, @NotNull final String attributeName, @NotNull final String description, final int inputLength, @NotNull final String sectionName) {
        return new TestArchetypeAttribute("script_file", archetypeAttributeName, attributeName, description, inputLength, sectionName);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public ArchetypeAttribute newArchetypeAttributeSpell(@NotNull final String archetypeAttributeName, @NotNull final String attributeName, @NotNull final String description, final int inputLength, @NotNull final String sectionName) {
        return new TestArchetypeAttribute("spell", archetypeAttributeName, attributeName, description, inputLength, sectionName);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public ArchetypeAttribute newArchetypeAttributeString(@NotNull final String archetypeAttributeName, @NotNull final String attributeName, @NotNull final String description, final int inputLength, @NotNull final String sectionName) {
        return new TestArchetypeAttribute("string", archetypeAttributeName, attributeName, description, inputLength, sectionName);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public ArchetypeAttribute newArchetypeAttributeText(@NotNull final String archetypeAttributeName, @NotNull final String endingOld, @NotNull final String attributeName, @NotNull final String description, final int inputLength, @Nullable final String fileExtension) {
        return new TestArchetypeAttribute("text", archetypeAttributeName, attributeName, description, inputLength, attributeName);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public ArchetypeAttribute newArchetypeAttributeTreasure(@NotNull final String archetypeAttributeName, @NotNull final String attributeName, @NotNull final String description, final int inputLength, @NotNull final String sectionName) {
        return new TestArchetypeAttribute("treasure", archetypeAttributeName, attributeName, description, inputLength, sectionName);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public ArchetypeAttribute newArchetypeAttributeZSpell(@NotNull final String archetypeAttributeName, @NotNull final String attributeName, @NotNull final String description, final int inputLength, @NotNull final String sectionName) {
        return new TestArchetypeAttribute("nz_spell", archetypeAttributeName, attributeName, description, inputLength, sectionName);
    }

}
