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
import java.util.List;
import org.jetbrains.annotations.NotNull;

/**
 * Maintains a list of section names while parsing {@link ArchetypeAttribute
 * ArchetypeAttributes}.
 * @author Andreas Kirschbaum
 */
public class SectionNames {

    /**
     * The name of the "General" section.
     */
    public static final String GENERAL_SECTION = "General";

    /**
     * The name of the "Special" section.
     */
    public static final String SPECIAL_SECTION = "Special";

    /**
     * The section names in display order.
     */
    @NotNull
    private final List<String> sectionNames = new ArrayList<String>();

    /**
     * Creates a new instance.
     */
    public SectionNames() {
        sectionNames.add(GENERAL_SECTION);
        sectionNames.add(SPECIAL_SECTION);
    }

    /**
     * Defines a section name and returns the section ID.
     * @param sectionName the section name to define
     * @return the section ID
     */
    public int defineSectionName(final String sectionName) {
        final int sectionId = sectionNames.indexOf(sectionName);
        if (sectionId != -1) {
            return sectionId;
        }

        sectionNames.add(sectionName);
        return sectionNames.size() - 1;
    }

    /**
     * Returns the number of defined section names.
     * @return the number of defined section names
     */
    public int getSectionNames() {
        return sectionNames.size();
    }

}
