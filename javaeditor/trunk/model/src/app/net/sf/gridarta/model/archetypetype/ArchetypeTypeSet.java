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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import net.sf.gridarta.model.baseobject.BaseObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Manages {@link ArchetypeType} instances, list, and bitmask definitions.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 * @author Andreas Kirschbaum
 */
public class ArchetypeTypeSet implements Iterable<ArchetypeType> {

    /**
     * The default (fallback) {@link ArchetypeType} used for game objects not
     * matching any defined type.
     */
    @NotNull
    private ArchetypeType fallbackArchetypeType = new ArchetypeType("", 0, "", true, null, false, null, null, 2, new ArchetypeAttributeSection(), new ArchetypeAttributesDefinition());

    /**
     * Lists with all defined {@link ArchetypeType ArchetypeTypes}. The ordering
     * is the same as in the "types.xml" file.
     */
    @NotNull
    private final List<ArchetypeType> archetypeTypeList = new ArrayList<ArchetypeType>();

    /**
     * Table with type archetype type name as keys ({@link String}), and {@link
     * ArchetypeType} object as values (<code>ArchetypeType</code>).
     */
    @NotNull
    private final Map<String, ArchetypeType> archetypeTypeNames = new HashMap<String, ArchetypeType>();

    /**
     * Table with {@link List} objects for lists (value) accessible by name
     * (key).
     */
    @NotNull
    private final Map<String, ArchetypeTypeList> listTable = new HashMap<String, ArchetypeTypeList>();

    /**
     * Table with {@link AttributeBitmask} objects (value) accessible by name
     * (key).
     */
    @NotNull
    private final Map<String, AttributeBitmask> bitmaskTable = new HashMap<String, AttributeBitmask>();

    /**
     * Adds an {@link ArchetypeType} instance.
     * @param archetypeType the archetype type instance
     */
    public void addArchetypeType(@NotNull final ArchetypeType archetypeType) {
        archetypeTypeList.add(archetypeType);
        final String typeName = archetypeType.getTypeName();
        archetypeTypeNames.put(typeName, archetypeType);
        if (typeName.equals("Misc")) {
            fallbackArchetypeType = archetypeType;
        }
    }

    /**
     * Returns an {@link ArchetypeType} by type name.
     * @param typeName the type name to look up
     * @return the archetype type instance or <code>null</code> if the type name
     *         does not exist
     */
    @Nullable
    public Iterable<ArchetypeAttribute> getArchetypeType(@NotNull final String typeName) {
        return archetypeTypeNames.get(typeName);
    }

    /**
     * Finds and returns the type-structure (<code>ArchetypeType</code>) that
     * matches the given 'typeName'. These type-names are "artificial" names,
     * defined in "types.xml". They appear in the type selection box in the
     * attribute-dialog.
     * @param typeName the name of the type to get
     * @return the <code>ArchetypeType</code> that matches, or the default
     *         ("Misc") type if no match is found
     */
    @NotNull
    public ArchetypeType getArchetypeTypeByName(@NotNull final String typeName) {
        final ArchetypeType type = archetypeTypeNames.get(typeName.trim());
        if (type != null) {
            return type;
        }

        return fallbackArchetypeType;
    }

    /**
     * Returns the {@link ArchetypeType} for the given {@link BaseObject}.
     * @param baseObject the base object to find the archetype type for
     * @return the archetype type for the given base object or the default type
     *         if no match was found
     */
    @NotNull
    public ArchetypeType getArchetypeTypeByBaseObject(@NotNull final BaseObject<?, ?, ?, ?> baseObject) {
        for (final ArchetypeType archetypeType : archetypeTypeList) {
            if (archetypeType.matches(baseObject)) {
                return archetypeType;
            }
        }

        return fallbackArchetypeType;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public Iterator<ArchetypeType> iterator() {
        return Collections.unmodifiableList(archetypeTypeList).iterator();
    }

    /**
     * Returns the index of an {@link ArchetypeType} instance.
     * @param archetypeType the archetype type instance
     * @return the index
     */
    public int getArchetypeTypeIndex(@NotNull final ArchetypeType archetypeType) {
        return archetypeTypeList.indexOf(archetypeType);
    }

    /**
     * Returns the number of ArchetypeTypes in the list. The default type is not
     * counted.
     * @return the number of archetype types in the list
     */
    public int getArchetypeTypeCount() {
        return archetypeTypeList.size();
    }

    /**
     * Returns whether a given {@link ArchetypeType} is the fallback archetype
     * type used for game objects not matching any defined archetype type.
     * @param archetypeType the archetype type to check
     * @return whether the given archetype type is the fallback archetype type
     */
    public boolean isFallbackArchetypeType(@NotNull final ArchetypeType archetypeType) {
        return archetypeType == fallbackArchetypeType;
    }

    /**
     * Adds a list definition.
     * @param attribute the attribute name
     * @param list the list definition
     */
    public void addList(@NotNull final String attribute, @NotNull final ArchetypeTypeList list) {
        listTable.put(attribute, list);
    }

    /**
     * Returns a list type definition.
     * @param listName the list name
     * @return the list type definition or <code>null</code> if the list does
     *         not exist
     */
    @Nullable
    public ArchetypeTypeList getList(@NotNull final String listName) {
        return listTable.get(listName);
    }

    /**
     * Adds a bitmask definition.
     * @param attribute the attribute name
     * @param attributeBitmask the list definition
     */
    public void addBitmask(@NotNull final String attribute, @NotNull final AttributeBitmask attributeBitmask) {
        bitmaskTable.put(attribute, attributeBitmask);
    }

    /**
     * Returns a bitmask type by name.
     * @param bitmaskName the bitmask name to look up
     * @return the bitmask type or <code>null</code> if the name does not exist
     */
    @Nullable
    public AttributeBitmask getBitmask(@NotNull final String bitmaskName) {
        return bitmaskTable.get(bitmaskName);
    }

    /**
     * Returns a description of this type.
     * @param baseObject the base object to describe
     * @return the description
     */
    @NotNull
    public String getDisplayName(@NotNull final BaseObject<?, ?, ?, ?> baseObject) {
        return getArchetypeTypeByBaseObject(baseObject).getDisplayName(baseObject);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        final Map<String, ArchetypeType> sortedArchetypeTypes = new TreeMap<String, ArchetypeType>(archetypeTypeNames);
        for (final ArchetypeType archetypeType : sortedArchetypeTypes.values()) {
            format(sb, archetypeType, "type");
        }
        return sb.toString();
    }

    /**
     * Appends the string representation of an {@link ArchetypeType} instance to
     * a {@link StringBuilder}.
     * @param sb the string builder
     * @param archetypeType the archetype type
     * @param title the title to prepend the entry
     */
    private static void format(@NotNull final StringBuilder sb, @NotNull final ArchetypeType archetypeType, @NotNull final String title) {
        sb.append(title);
        sb.append(':');
        archetypeType.toString(sb);
        sb.append('\n');
    }

}
