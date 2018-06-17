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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/**
 * A set of {@link ArchetypeAttribute ArchetypeAttributes}.
 * @author Andreas Kirschbaum
 */
public class ArchetypeAttributeSection implements Iterable<ArchetypeAttribute> {

    /**
     * The {@link ArchetypeAttribute ArchetypeAttributes}.
     */
    @NotNull
    private final List<ArchetypeAttribute> archetypeAttributes = new ArrayList<ArchetypeAttribute>();

    /**
     * Adds an {@link ArchetypeAttribute}.
     * @param archetypeAttribute the archetype attribute
     */
    public void add(@NotNull final ArchetypeAttribute archetypeAttribute) {
        archetypeAttributes.add(archetypeAttribute);
    }

    /**
     * Adds all {@link ArchetypeAttribute ArchetypeAttributes} of another
     * archetype attributes to this archetype attributes.
     * @param archetypeAttributeSection the other archetype attributes
     */
    public void addAll(@NotNull final ArchetypeAttributeSection archetypeAttributeSection) {
        this.archetypeAttributes.addAll(archetypeAttributeSection.archetypeAttributes);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<ArchetypeAttribute> iterator() {
        return Collections.unmodifiableList(archetypeAttributes).iterator();
    }

    /**
     * Returns whether no {@link ArchetypeAttribute ArchetypeAttributes} exist.
     * @return whether no archetype attributes exist
     */
    public boolean isEmpty() {
        return archetypeAttributes.isEmpty();
    }

    /**
     * Updates the section names of all archetype attributes.
     * @param sectionNames the section names to use
     */
    public void setSectionNames(@NotNull final SectionNames sectionNames) {
        for (final ArchetypeAttribute archetypeAttribute : archetypeAttributes) {
            final String sectionName = archetypeAttribute.getSectionName();
            final int sectionId = sectionNames.defineSectionName(sectionName);
            archetypeAttribute.setSectionId(sectionId);
        }
    }

    /**
     * Looks up the section name from the ID.
     * @param sectionId the ID of the section
     * @return the name of that section
     */
    @NotNull
    public String getSectionName(final int sectionId) {
        for (final ArchetypeAttribute archetypeAttribute : archetypeAttributes) {
            if (archetypeAttribute.getSectionId() == sectionId) {
                final String sectionName = archetypeAttribute.getSectionName();
                if (sectionName.length() <= 1) {
                    return sectionName;
                }

                return sectionName.substring(0, 1).toUpperCase() + sectionName.substring(1);
            }
        }

        return "???";
    }

    /**
     * Returns whether an attribute key is defined.
     * @param key the attribute key
     * @return whether the attribute key is defined
     */
    public boolean hasAttributeKey(@NotNull final Comparable<String> key) {
        for (final ArchetypeAttribute attr : archetypeAttributes) {
            if (attr.getArchetypeAttributeName().equals(key)) {
                return true;
            }
        }

        return false;
    }

}
