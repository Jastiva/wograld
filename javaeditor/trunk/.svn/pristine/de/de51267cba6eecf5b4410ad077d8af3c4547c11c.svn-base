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

import java.util.regex.Pattern;
import net.sf.gridarta.model.errorview.ErrorViewCategory;
import net.sf.gridarta.model.errorview.ErrorViewCollector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Utility class for parsing {@link ArchetypeAttribute ArchetypeAttributes}.
 * @author Andreas Kirschbaum
 */
public class ArchetypeAttributeParser {

    /**
     * Pattern to match line breaks in attribute text.
     */
    @NotNull
    private static final Pattern LINE_BREAK = Pattern.compile("\\s*\n\\s*");

    /**
     * The name of the "attribute" element.
     */
    @NotNull
    public static final String XML_ELEMENT_ATTRIBUTE = "attribute";

    /**
     * The name of the "arch" attribute within {@link #XML_ELEMENT_ATTRIBUTE}
     * elements.
     */
    @NotNull
    public static final String XML_ATTRIBUTE_ARCH = "arch";

    /**
     * The name of the "editor" attribute within {@link #XML_ELEMENT_ATTRIBUTE}
     * elements.
     */
    @NotNull
    public static final String XML_ATTRIBUTE_EDITOR = "editor";

    /**
     * The name of the "value" attribute within {@link #XML_ELEMENT_ATTRIBUTE}
     * elements.
     */
    @NotNull
    public static final String XML_ATTRIBUTE_VALUE = "value";

    /**
     * The name of the "min" attribute within {@link #XML_ELEMENT_ATTRIBUTE}
     * elements.
     */
    @NotNull
    public static final String XML_ATTRIBUTE_MIN = "min";

    /**
     * The name of the "max" attribute within {@link #XML_ELEMENT_ATTRIBUTE}
     * elements.
     */
    @NotNull
    public static final String XML_ATTRIBUTE_MAX = "max";

    /**
     * The name of the "check_min" attribute within {@link
     * #XML_ELEMENT_ATTRIBUTE} elements.
     */
    @NotNull
    public static final String XML_ATTRIBUTE_CHECK_MIN = "check_min";

    /**
     * The name of the "check_max" attribute within {@link
     * #XML_ELEMENT_ATTRIBUTE} elements.
     */
    @NotNull
    public static final String XML_ATTRIBUTE_CHECK_MAX = "check_max";

    /**
     * The name of the "type" attribute within {@link #XML_ELEMENT_ATTRIBUTE}
     * elements.
     */
    @NotNull
    public static final String XML_ATTRIBUTE_TYPE = "type";

    /**
     * The name of the "arch_begin" attribute within {@link
     * #XML_ELEMENT_ATTRIBUTE} elements.
     */
    @NotNull
    public static final String XML_ATTRIBUTE_ARCH_BEGIN = "arch_begin";

    /**
     * The name of the "arch_end" attribute within {@link
     * #XML_ELEMENT_ATTRIBUTE} elements.
     */
    @NotNull
    public static final String XML_ATTRIBUTE_ARCH_END = "arch_end";

    /**
     * The name of the "length" attribute within {@link #XML_ELEMENT_ATTRIBUTE}
     * elements.
     */
    @NotNull
    public static final String XML_ATTRIBUTE_LENGTH = "length";

    /**
     * The name of the "true" attribute within {@link #XML_ELEMENT_ATTRIBUTE}
     * elements.
     */
    @NotNull
    public static final String XML_ATTRIBUTE_TRUE = "true";

    /**
     * The name of the "false" attribute within {@link #XML_ELEMENT_ATTRIBUTE}
     * elements.
     */
    @NotNull
    public static final String XML_ATTRIBUTE_FALSE = "false";

    /**
     * The name of the "marker" attribute within {@link #XML_ELEMENT_ATTRIBUTE}
     * elements.
     */
    @NotNull
    public static final String XML_ATTRIBUTE_MARKER = "marker";

    /**
     * The {@link ArchetypeAttributeFactory} for creating {@link
     * ArchetypeAttribute ArchetypeAttributes}.
     */
    @NotNull
    private final ArchetypeAttributeFactory archetypeAttributeFactory;

    /**
     * Creates a new instance.
     * @param archetypeAttributeFactory the archetype attribute factory to use
     */
    public ArchetypeAttributeParser(@NotNull final ArchetypeAttributeFactory archetypeAttributeFactory) {
        this.archetypeAttributeFactory = archetypeAttributeFactory;
    }

    /**
     * Loading the data from an xml element into this object.
     * @param attributeElement the xml 'attribute' element
     * @param errorViewCollector the error view collector for reporting errors
     * @param archetypeTypeSet the archetype type list
     * @param typeName (descriptive) name of the type this attribute belongs to
     * (e.g. "Weapon")
     * @param sectionName the section name for the new attribute
     * @return the archetype attribute or <code>null</code> if an error occurs
     * @throws MissingAttributeException if the element cannot be parsed
     */
    @Nullable
    @SuppressWarnings("FeatureEnvy")
    public ArchetypeAttribute load(@NotNull final Element attributeElement, @NotNull final ErrorViewCollector errorViewCollector, @NotNull final ArchetypeTypeSet archetypeTypeSet, @NotNull final String typeName, @NotNull final String sectionName) throws MissingAttributeException {
        final String description = parseText(attributeElement);
        final int inputLength = getAttributeIntValue(attributeElement, XML_ATTRIBUTE_LENGTH, 0, typeName, errorViewCollector);
        final String attributeType = getAttributeValue(attributeElement, XML_ATTRIBUTE_TYPE);
        if (attributeType.equals("bool")) {
            return archetypeAttributeFactory.newArchetypeAttributeBool(getAttributeValue(attributeElement, XML_ATTRIBUTE_ARCH), getAttributeValue(attributeElement, XML_ATTRIBUTE_EDITOR), description, inputLength, sectionName);
        } else if (attributeType.equals("bool_special")) {
            return archetypeAttributeFactory.newArchetypeAttributeBoolSpec(getAttributeValue(attributeElement, XML_ATTRIBUTE_ARCH), getAttributeValue(attributeElement, XML_ATTRIBUTE_EDITOR), description, inputLength, sectionName, getAttributeValue(attributeElement, XML_ATTRIBUTE_TRUE), getAttributeValue(attributeElement, XML_ATTRIBUTE_FALSE));
        } else if (attributeType.equals("int")) {
            final int minValue = getAttributeIntValue(attributeElement, XML_ATTRIBUTE_MIN, Integer.MIN_VALUE, typeName, errorViewCollector);
            final int maxValue = getAttributeIntValue(attributeElement, XML_ATTRIBUTE_MAX, Integer.MAX_VALUE, typeName, errorViewCollector);
            final int minCheckValue = getAttributeIntValue(attributeElement, XML_ATTRIBUTE_CHECK_MIN, minValue, typeName, errorViewCollector);
            final int maxCheckValue = getAttributeIntValue(attributeElement, XML_ATTRIBUTE_CHECK_MAX, maxValue, typeName, errorViewCollector);

            if (minValue > maxValue) {
                errorViewCollector.addWarning(ErrorViewCategory.TYPES_ENTRY_INVALID, "type " + typeName + " has invalid " + XML_ATTRIBUTE_MIN + ".." + XML_ATTRIBUTE_MAX + " range: " + minValue + ".." + maxValue + ".");
                return null;
            }

            if (minCheckValue > maxCheckValue) {
                errorViewCollector.addWarning(ErrorViewCategory.TYPES_ENTRY_INVALID, "type " + typeName + " has invalid " + XML_ATTRIBUTE_CHECK_MIN + ".." + XML_ATTRIBUTE_CHECK_MAX + " range: " + minCheckValue + ".." + maxCheckValue + ".");
                return null;
            }

            if (minCheckValue < minValue) {
                errorViewCollector.addWarning(ErrorViewCategory.TYPES_ENTRY_INVALID, "type " + typeName + " has invalid " + XML_ATTRIBUTE_CHECK_MIN + " value: " + minCheckValue + " < " + XML_ATTRIBUTE_MIN + " = " + minValue + ".");
                return null;
            }

            if (maxCheckValue > maxValue) {
                errorViewCollector.addWarning(ErrorViewCategory.TYPES_ENTRY_INVALID, "type " + typeName + " has invalid " + XML_ATTRIBUTE_CHECK_MAX + " value: " + maxCheckValue + " > " + XML_ATTRIBUTE_MAX + " = " + maxValue + ".");
                return null;
            }

            return archetypeAttributeFactory.newArchetypeAttributeInt(getAttributeValue(attributeElement, XML_ATTRIBUTE_ARCH), getAttributeValue(attributeElement, XML_ATTRIBUTE_EDITOR), description, inputLength, sectionName, minValue, maxValue, minCheckValue, maxCheckValue);
        } else if (attributeType.equals("long")) {
            return archetypeAttributeFactory.newArchetypeAttributeLong(getAttributeValue(attributeElement, XML_ATTRIBUTE_ARCH), getAttributeValue(attributeElement, XML_ATTRIBUTE_EDITOR), description, inputLength, sectionName);
        } else if (attributeType.equals("float")) {
            return archetypeAttributeFactory.newArchetypeAttributeFloat(getAttributeValue(attributeElement, XML_ATTRIBUTE_ARCH), getAttributeValue(attributeElement, XML_ATTRIBUTE_EDITOR), description, inputLength, sectionName);
        } else if (attributeType.equals("string")) {
            return archetypeAttributeFactory.newArchetypeAttributeString(getAttributeValue(attributeElement, XML_ATTRIBUTE_ARCH), getAttributeValue(attributeElement, XML_ATTRIBUTE_EDITOR), description, inputLength, sectionName);
        } else if (attributeType.equals("map_path")) {
            return archetypeAttributeFactory.newArchetypeAttributeMapPath(getAttributeValue(attributeElement, XML_ATTRIBUTE_ARCH), getAttributeValue(attributeElement, XML_ATTRIBUTE_EDITOR), description, inputLength, sectionName);
        } else if (attributeType.equals("script_file")) {
            return archetypeAttributeFactory.newArchetypeAttributeScriptFile(getAttributeValue(attributeElement, XML_ATTRIBUTE_ARCH), getAttributeValue(attributeElement, XML_ATTRIBUTE_EDITOR), description, inputLength, sectionName);
        } else if (attributeType.equals("facename")) {
            return archetypeAttributeFactory.newArchetypeAttributeFacename(getAttributeValue(attributeElement, XML_ATTRIBUTE_ARCH), getAttributeValue(attributeElement, XML_ATTRIBUTE_EDITOR), description, inputLength, sectionName);
        } else if (attributeType.equals("animname")) {
            return archetypeAttributeFactory.newArchetypeAttributeAnimname(getAttributeValue(attributeElement, XML_ATTRIBUTE_ARCH), getAttributeValue(attributeElement, XML_ATTRIBUTE_EDITOR), description, inputLength, sectionName);
        } else if (attributeType.equals("text")) {
            return archetypeAttributeFactory.newArchetypeAttributeText(getAttributeValue(attributeElement, XML_ATTRIBUTE_ARCH_BEGIN), getAttributeValue(attributeElement, XML_ATTRIBUTE_ARCH_END), getAttributeValue(attributeElement, XML_ATTRIBUTE_EDITOR), description, inputLength, getAttributeValueOptional(attributeElement, XML_ATTRIBUTE_MARKER));
        } else if (attributeType.equals("fixed")) {
            return archetypeAttributeFactory.newArchetypeAttributeFixed(getAttributeValue(attributeElement, XML_ATTRIBUTE_ARCH), getAttributeValue(attributeElement, XML_ATTRIBUTE_VALUE), description, inputLength, sectionName);
        } else if (attributeType.equals("spell")) {
            return archetypeAttributeFactory.newArchetypeAttributeSpell(getAttributeValue(attributeElement, XML_ATTRIBUTE_ARCH), getAttributeValue(attributeElement, XML_ATTRIBUTE_EDITOR), description, inputLength, sectionName);
        } else if (attributeType.equals("nz_spell")) {
            return archetypeAttributeFactory.newArchetypeAttributeZSpell(getAttributeValue(attributeElement, XML_ATTRIBUTE_ARCH), getAttributeValue(attributeElement, XML_ATTRIBUTE_EDITOR), description, inputLength, sectionName);
        } else if (attributeType.equals("inv_spell")) {
            return archetypeAttributeFactory.newArchetypeAttributeInvSpell(getAttributeValue(attributeElement, XML_ATTRIBUTE_ARCH), getAttributeValue(attributeElement, XML_ATTRIBUTE_EDITOR), description, inputLength, sectionName);
        } else if (attributeType.equals("inv_spell_optional")) {
            return archetypeAttributeFactory.newArchetypeAttributeInvSpellOptional(getAttributeValue(attributeElement, XML_ATTRIBUTE_ARCH), getAttributeValue(attributeElement, XML_ATTRIBUTE_EDITOR), description, inputLength, sectionName);
        } else if (attributeType.startsWith("bitmask")) {
            // got a bitmask attribute
            final String bitmaskName = attributeType.substring(8).trim();
            if (archetypeTypeSet.getBitmask(bitmaskName) == null) {
                errorViewCollector.addWarning(ErrorViewCategory.TYPES_ENTRY_INVALID, "type " + typeName + ": Bitmask \"" + bitmaskName + "\" is undefined.");
                return null;
            }
            return archetypeAttributeFactory.newArchetypeAttributeBitmask(getAttributeValue(attributeElement, XML_ATTRIBUTE_ARCH), getAttributeValue(attributeElement, XML_ATTRIBUTE_EDITOR), description, inputLength, sectionName, bitmaskName);
        } else if (attributeType.startsWith("list_")) {
            // got a bitmask attribute
            final String listName = attributeType.substring(5).trim();
            if (archetypeTypeSet.getList(listName) == null) {
                errorViewCollector.addWarning(ErrorViewCategory.TYPES_ENTRY_INVALID, "type " + typeName + ": List \"" + listName + "\" is undefined.");
                return null;
            }
            return archetypeAttributeFactory.newArchetypeAttributeList(getAttributeValue(attributeElement, XML_ATTRIBUTE_ARCH), getAttributeValue(attributeElement, XML_ATTRIBUTE_EDITOR), description, inputLength, sectionName, listName);
        } else if (attributeType.equals("treasurelist")) {
            return archetypeAttributeFactory.newArchetypeAttributeTreasure(getAttributeValue(attributeElement, XML_ATTRIBUTE_ARCH), getAttributeValue(attributeElement, XML_ATTRIBUTE_EDITOR), description, inputLength, sectionName);
        } else {
            // unknown type
            errorViewCollector.addWarning(ErrorViewCategory.TYPES_ENTRY_INVALID, "type " + typeName + " has an attribute with unknown type: '" + attributeType + "'.");
            return null;
        }
    }

    /**
     * This method reads the text out of the element's "body" and cuts off white
     * spaces at front/end of lines. Probably a more correct approach would be
     * to display the info text in an html context.
     * @param attributeElement xml attribute element
     * @return the body's text
     */
    @NotNull
    private static String parseText(@NotNull final Node attributeElement) {
        final String tmp = attributeElement.getTextContent().trim();
        return LINE_BREAK.matcher(tmp).replaceAll(tmp.startsWith("<html>") ? " " : "\n");
    }

    /**
     * Returns the value of an attribute.
     * @param element the element to use
     * @param attributeKey the attribute key to look up
     * @return the attribute value
     * @throws MissingAttributeException if the attribute key does not exist
     */
    @NotNull
    private static String getAttributeValue(@NotNull final Element element, @NotNull final String attributeKey) throws MissingAttributeException {
        final Attr attr = element.getAttributeNode(attributeKey);
        if (attr == null) {
            throw new MissingAttributeException(element.getTagName(), attributeKey);
        }

        return attr.getValue().trim();
    }

    /**
     * Returns the value of an attribute.
     * @param element the element to use
     * @param attributeKey the attribute key to look up
     * @return the attribute value or <code>null</code> if the attribute key
     *         does not exist
     */
    @Nullable
    private static String getAttributeValueOptional(@NotNull final Element element, @NotNull final String attributeKey) {
        final Attr attr = element.getAttributeNode(attributeKey);
        return attr == null ? null : attr.getValue().trim();
    }

    /**
     * Returns the value of an attribute as an <code>int</code> value.
     * @param element the element to use
     * @param attributeKey the attribute key to look up
     * @param defaultValue the default value if the element does not contain the
     * key
     * @param typeName the type name for error messages
     * @param errorViewCollector the error view collector for error messages
     * @return the int value
     */
    private static int getAttributeIntValue(@NotNull final Element element, @NotNull final String attributeKey, final int defaultValue, @NotNull final String typeName, @NotNull final ErrorViewCollector errorViewCollector) {
        final String value = getAttributeValueOptional(element, attributeKey);
        if (value == null) {
            return defaultValue;
        }

        try {
            return Integer.parseInt(value);
        } catch (final NumberFormatException ignored) {
            errorViewCollector.addWarning(ErrorViewCategory.TYPES_ENTRY_INVALID, "type " + typeName + " has attribute '" + attributeKey + "' with invalid value '" + value + "' (must be a number).");
        }

        return defaultValue;
    }

}
