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

import java.util.Iterator;
import net.sf.gridarta.model.baseobject.BaseObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Contains the data of one Gridarta Object-Type. The data is read from a
 * definitions file called 'types.xml'. It is mainly used as info-base for the
 * arch-attribute GUI.
 * @author <a href="mailto:andi.vogl@gmx.net">Andreas Vogl</a>
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 */
public class ArchetypeType implements Iterable<ArchetypeAttribute> {

    /**
     * The type name (artificial).
     */
    @NotNull
    private final String typeName;

    /**
     * The type number.
     */
    private final int typeNo;

    /**
     * Additional description text.
     */
    @NotNull
    private final String display;

    /**
     * Whether this archetype type is allowed on maps.
     */
    private final boolean map;

    /**
     * Which archetype types allow this archetype type in their inventories or
     * <code>null</code> for no restrictions.
     */
    @Nullable
    private final int[] inv;

    /**
     * Whether this archetype type allows any inventory game objects, whether
     * these types have "inv" specifications.
     */
    private final boolean allowsAllInv;

    /**
     * The description.
     */
    @Nullable
    private final String description;

    /**
     * The usage notes.
     */
    @Nullable
    private final String use;

    /**
     * The number of attribute sections. The number of attribute sections
     * determine the number of tabs to be used.
     * <p/>
     * There's always the "general" and "special" sections (even if empty).
     */
    private final int sectionNum;

    /**
     * The list of {@link ArchetypeAttribute ArchetypeAttributes}.
     */
    @NotNull
    private final ArchetypeAttributeSection archetypeAttributeSection;

    /**
     * The list of additional attributes that an object must have in order to be
     * of this type.
     */
    @NotNull
    private final ArchetypeAttributesDefinition typeAttributes;

    /**
     * Creates a new instance.
     * @param typeName the type name (artificial)
     * @param typeNo the type number
     * @param display additional description text
     * @param map whether this archetype type is allowed on maps
     * @param inv which archetype types allow this archetype type in their
     * inventories or <code>null</code> for no restrictions
     * @param allowsAllInv whether this archetype type allows any inventory game
     * objects, whether these types have "inv" specifications
     * @param description the description
     * @param use the usage notes
     * @param sectionNum the number of attribute sections
     * @param archetypeAttributeSection the list of archetype attributes
     * @param typeAttributes the list of additional attributes
     */
    public ArchetypeType(@NotNull final String typeName, final int typeNo, @NotNull final String display, final boolean map, @Nullable final int[] inv, final boolean allowsAllInv, @Nullable final String description, @Nullable final String use, final int sectionNum, @NotNull final ArchetypeAttributeSection archetypeAttributeSection, @NotNull final ArchetypeAttributesDefinition typeAttributes) {
        this.typeName = typeName;
        this.typeNo = typeNo;
        this.display = display;
        this.map = map;
        this.inv = inv == null ? null : inv.clone();
        this.allowsAllInv = allowsAllInv;
        this.description = description;
        this.use = use;
        this.sectionNum = sectionNum;
        this.archetypeAttributeSection = archetypeAttributeSection;
        this.typeAttributes = typeAttributes;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public Iterator<ArchetypeAttribute> iterator() {
        return archetypeAttributeSection.iterator();
    }

    /**
     * Returns whether this archetype type defines at least one archetype
     * attribute.
     * @return whether at least one archetype attribute exists
     */
    public boolean hasAttribute() {
        return !archetypeAttributeSection.isEmpty();
    }

    /**
     * Returns number of attribute sections. The number of attribute sections
     * determine the number of tabs to be used.
     * @return the number of attribute sections
     */
    public int getSectionNum() {
        return sectionNum;
    }

    /**
     * Returns the type number.
     * @return the type number
     */
    public int getTypeNo() {
        return typeNo;
    }

    /**
     * Returns the type name (artificial).
     * @return the type name
     */
    @NotNull
    public String getTypeName() {
        return typeName;
    }

    /**
     * Returns whether this archetype is allowed on maps.
     * @return whether this archetype is allowed on maps
     */
    public boolean isMap() {
        return map;
    }

    /**
     * Returns which archetype types allow this archetype type in their
     * inventories.
     * @return the allowed environment types  or <code>null</code> for no
     *         restrictions
     */
    @Nullable
    public int[] getInv() {
        return inv == null ? null : inv.clone();
    }

    /**
     * Returns whether this archetype type allows any inventory game objects,
     * regardless whether these types have "inv" specifications.
     * @return whether this archetype type allows any inventory game objects
     */
    public boolean isAllowsAllInv() {
        return allowsAllInv;
    }

    /**
     * Returns the description.
     * @return the description
     */
    @NotNull
    @SuppressWarnings("NullableProblems")
    public String getDescription() {
        final String result = description;
        return result == null ? "" : result.trim();
    }

    /**
     * Returns the usage notes.
     * @return the usage notes
     */
    @NotNull
    @SuppressWarnings("NullableProblems")
    public String getUse() {
        final String result = use;
        return result == null ? "" : result.trim();
    }

    /**
     * Checks whether a {@link BaseObject} matches all type attributes.
     * @param baseObject the base object to check
     * @return whether the base object matches
     */
    public boolean matches(@NotNull final BaseObject<?, ?, ?, ?> baseObject) {
        return typeNo == baseObject.getTypeNo() && typeAttributes.matches(baseObject);
    }

    /**
     * Returns whether any type attributes are defined.
     * @return whether any type attributes are defined
     */
    public boolean hasTypeAttributes() {
        return !typeAttributes.isEmpty();
    }

    /**
     * Looks up the section name from the ID.
     * @param sectionId the ID of the section
     * @return the name of that section
     */
    @NotNull
    public String getSectionName(final int sectionId) {
        return archetypeAttributeSection.getSectionName(sectionId);
    }

    /**
     * Returns whether an attribute key is defined.
     * @param key the attribute key
     * @return whether the attribute key is defined
     */
    public boolean hasAttributeKey(@NotNull final Comparable<String> key) {
        return archetypeAttributeSection.hasAttributeKey(key);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        toString(sb);
        return sb.toString();
    }

    /**
     * Appends a string representation of this instance to a {@link
     * StringBuilder}.
     * @param sb the string builder
     */
    public void toString(@NotNull final StringBuilder sb) {
        sb.append(typeNo);
        sb.append(',');
        sb.append(typeName);
        sb.append("\n:\n");
        for (final ArchetypeAttributeDefinition typeAttribute : typeAttributes) {
            sb.append(typeAttribute);
            sb.append('\n');
        }
        sb.append(":\n");
        for (final ArchetypeAttribute archetypeAttribute : archetypeAttributeSection) {
            sb.append(archetypeAttribute);
            sb.append('\n');
        }
    }

    /**
     * Returns a description of this type.
     * @param baseObject the base object to describe
     * @return the description
     */
    @NotNull
    public String getDisplayName(@NotNull final BaseObject<?, ?, ?, ?> baseObject) {
        final StringBuilder sb = new StringBuilder();
        sb.append(typeName);
        sb.append(" (");
        sb.append(typeNo);
        sb.append(')');

        if (display.length() > 0) {
            sb.append(" [");
            int start = 0;
            while (start < display.length()) {
                final int end = display.indexOf("${", start);
                if (end == -1) {
                    break;
                }

                final int end2 = display.indexOf('}', end + 2);
                if (end2 == -1) {
                    // ignore syntax error
                    break;
                }

                sb.append(display.substring(start, end));
                final String spec = display.substring(end + 2, end2);
                final int question = spec.indexOf('?');
                final String value;
                if (question == -1) {
                    value = baseObject.getAttributeString(spec);
                } else {
                    final int colon = spec.indexOf(':', question + 1);
                    final String attributeValue = baseObject.getAttributeString(spec.substring(0, question));
                    final boolean attributeExists = attributeValue.length() != 0 && !attributeValue.equals("0");
                    if (attributeExists) {
                        value = spec.substring(question + 1, colon == -1 ? spec.length() : colon);
                    } else {
                        value = colon == -1 ? "" : spec.substring(colon + 1);
                    }
                }
                sb.append(value);
                start = end2 + 1;
            }
            sb.append(display.substring(start));
            sb.append(']');
        }

        return sb.toString();
    }

}
