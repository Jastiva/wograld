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
 * Default implementation of {@link ArchetypeAttributeFactory}. This instance
 * creates {@link ArchetypeAttribute} instances suitable for the Gridarta core.
 * @author Andreas Kirschbaum
 */
public class DefaultArchetypeAttributeFactory implements ArchetypeAttributeFactory {

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public ArchetypeAttribute newArchetypeAttributeAnimname(@NotNull final String archetypeAttributeName, @NotNull final String attributeName, @NotNull final String description, final int inputLength, @NotNull final String sectionName) {
        return new ArchetypeAttributeAnimationName(archetypeAttributeName, attributeName, description, inputLength, sectionName);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public ArchetypeAttribute newArchetypeAttributeBitmask(@NotNull final String archetypeAttributeName, @NotNull final String attributeName, @NotNull final String description, final int inputLength, @NotNull final String sectionName, @NotNull final String bitmaskName) {
        return new ArchetypeAttributeBitmask(archetypeAttributeName, attributeName, description, inputLength, sectionName, bitmaskName);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public ArchetypeAttribute newArchetypeAttributeBool(@NotNull final String archetypeAttributeName, @NotNull final String attributeName, @NotNull final String description, final int inputLength, @NotNull final String sectionName) {
        return new ArchetypeAttributeBool(archetypeAttributeName, attributeName, description, inputLength, sectionName);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public ArchetypeAttribute newArchetypeAttributeBoolSpec(@NotNull final String archetypeAttributeName, @NotNull final String attributeName, @NotNull final String description, final int inputLength, @NotNull final String sectionName, @NotNull final String trueValue, @NotNull final String falseValue) {
        return new ArchetypeAttributeBoolSpec(archetypeAttributeName, attributeName, description, inputLength, sectionName, trueValue, falseValue);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public ArchetypeAttribute newArchetypeAttributeFacename(@NotNull final String archetypeAttributeName, @NotNull final String attributeName, @NotNull final String description, final int inputLength, @NotNull final String sectionName) {
        return new ArchetypeAttributeFaceName(archetypeAttributeName, attributeName, description, inputLength, sectionName);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public ArchetypeAttribute newArchetypeAttributeFixed(@NotNull final String archetypeAttributeName, @NotNull final String attributeName, @NotNull final String description, final int inputLength, @NotNull final String sectionName) {
        return new ArchetypeAttributeFixed(archetypeAttributeName, attributeName, description, inputLength, sectionName);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public ArchetypeAttribute newArchetypeAttributeFloat(@NotNull final String archetypeAttributeName, @NotNull final String attributeName, @NotNull final String description, final int inputLength, @NotNull final String sectionName) {
        return new ArchetypeAttributeFloat(archetypeAttributeName, attributeName, description, inputLength, sectionName);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public ArchetypeAttribute newArchetypeAttributeInt(@NotNull final String archetypeAttributeName, @NotNull final String attributeName, @NotNull final String description, final int inputLength, @NotNull final String sectionName, final int minValue, final int maxValue, final int minCheckValue, final int maxCheckValue) {
        return new ArchetypeAttributeInt(archetypeAttributeName, attributeName, description, inputLength, sectionName, minValue, maxValue, minCheckValue, maxCheckValue);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public ArchetypeAttribute newArchetypeAttributeInvSpell(@NotNull final String archetypeAttributeName, @NotNull final String attributeName, @NotNull final String description, final int inputLength, @NotNull final String sectionName) {
        return new ArchetypeAttributeInvSpell(false, archetypeAttributeName, attributeName, description, inputLength, sectionName);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public ArchetypeAttribute newArchetypeAttributeInvSpellOptional(@NotNull final String archetypeAttributeName, @NotNull final String attributeName, @NotNull final String description, final int inputLength, @NotNull final String sectionName) {
        return new ArchetypeAttributeInvSpell(true, archetypeAttributeName, attributeName, description, inputLength, sectionName);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public ArchetypeAttribute newArchetypeAttributeList(@NotNull final String archetypeAttributeName, @NotNull final String attributeName, @NotNull final String description, final int inputLength, @NotNull final String sectionName, @NotNull final String listName) {
        return new ArchetypeAttributeList(archetypeAttributeName, attributeName, description, inputLength, sectionName, listName);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public ArchetypeAttribute newArchetypeAttributeLong(@NotNull final String archetypeAttributeName, @NotNull final String attributeName, @NotNull final String description, final int inputLength, @NotNull final String sectionName) {
        return new ArchetypeAttributeLong(archetypeAttributeName, attributeName, description, inputLength, sectionName);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public ArchetypeAttribute newArchetypeAttributeMapPath(@NotNull final String archetypeAttributeName, @NotNull final String attributeName, @NotNull final String description, final int inputLength, @NotNull final String sectionName) {
        return new ArchetypeAttributeMapPath(archetypeAttributeName, attributeName, description, inputLength, sectionName);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public ArchetypeAttribute newArchetypeAttributeScriptFile(@NotNull final String archetypeAttributeName, @NotNull final String attributeName, @NotNull final String description, final int inputLength, @NotNull final String sectionName) {
        return new ArchetypeAttributeScriptFile(archetypeAttributeName, attributeName, description, inputLength, sectionName);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public ArchetypeAttribute newArchetypeAttributeSpell(@NotNull final String archetypeAttributeName, @NotNull final String attributeName, @NotNull final String description, final int inputLength, @NotNull final String sectionName) {
        return new ArchetypeAttributeSpell(archetypeAttributeName, attributeName, description, inputLength, sectionName);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public ArchetypeAttribute newArchetypeAttributeString(@NotNull final String archetypeAttributeName, @NotNull final String attributeName, @NotNull final String description, final int inputLength, @NotNull final String sectionName) {
        return new ArchetypeAttributeString(archetypeAttributeName, attributeName, description, inputLength, sectionName);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public ArchetypeAttribute newArchetypeAttributeText(@NotNull final String archetypeAttributeName, @NotNull final String endingOld, @NotNull final String attributeName, @NotNull final String description, final int inputLength, @Nullable final String fileExtension) {
        return new ArchetypeAttributeText(archetypeAttributeName, endingOld, attributeName, description, inputLength, fileExtension);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public ArchetypeAttribute newArchetypeAttributeTreasure(@NotNull final String archetypeAttributeName, @NotNull final String attributeName, @NotNull final String description, final int inputLength, @NotNull final String sectionName) {
        return new ArchetypeAttributeTreasure(archetypeAttributeName, attributeName, description, inputLength, sectionName);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public ArchetypeAttribute newArchetypeAttributeZSpell(@NotNull final String archetypeAttributeName, @NotNull final String attributeName, @NotNull final String description, final int inputLength, @NotNull final String sectionName) {
        return new ArchetypeAttributeZSpell(archetypeAttributeName, attributeName, description, inputLength, sectionName);
    }

}
