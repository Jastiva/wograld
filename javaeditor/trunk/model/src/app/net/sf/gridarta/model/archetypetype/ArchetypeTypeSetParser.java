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

import java.io.IOException;
import java.util.Iterator;
import javax.xml.parsers.DocumentBuilder;
import net.sf.gridarta.model.errorview.ErrorViewCategory;
import net.sf.gridarta.model.errorview.ErrorViewCollector;
import net.sf.gridarta.utils.SyntaxErrorException;
import net.sf.japi.xml.NodeListIterator;
import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Parser for {@link ArchetypeTypeSet ArchetypeTypeSets} ("types.xml" files).
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 * @author Andreas Kirschbaum
 */
public class ArchetypeTypeSetParser {

    /**
     * The name of the "default_type" element.
     */
    @NotNull
    private static final String XML_ELEMENT_DEFAULT_TYPE = "default_type";

    /**
     * The name of the "bitmasks" element.
     */
    @NotNull
    private static final String XML_ELEMENT_BITMASKS = "bitmasks";

    /**
     * The name of the "bitmask" element.
     */
    @NotNull
    private static final String XML_ELEMENT_BITMASK = "bitmask";

    /**
     * The name of the "name" attribute within {@link #XML_ELEMENT_BITMASK}
     * elements.
     */
    @NotNull
    private static final String XML_BITMASK_NAME = "name";

    /**
     * The name of the "is_named" attribute within {@link #XML_ELEMENT_BITMASK}
     * elements.
     */
    @NotNull
    private static final String XML_BITMASK_IS_NAMED = "is_named";

    /**
     * The name of the "bmentry" element.
     */
    @NotNull
    private static final String XML_ELEMENT_BMENTRY = "bmentry";

    /**
     * The name of the "bit" attribute within {@link #XML_ELEMENT_BMENTRY}
     * elements.
     */
    @NotNull
    private static final String XML_BMENTRY_BIT = "bit";

    /**
     * The name of the "name" attribute within {@link #XML_ELEMENT_BMENTRY}
     * elements.
     */
    @NotNull
    private static final String XML_BMENTRY_NAME = "name";

    /**
     * The name of the "value" attribute within {@link #XML_ELEMENT_BMENTRY}
     * elements.
     */
    @NotNull
    private static final String XML_BMENTRY_VALUE = "value";

    /**
     * The name of the "encoding" attribute within {@link #XML_ELEMENT_BMENTRY}
     * elements.
     */
    @NotNull
    private static final String XML_BMENTRY_ENCODING = "encoding";

    /**
     * The name of the "lists" element.
     */
    @NotNull
    private static final String XML_ELEMENT_LISTS = "lists";

    /**
     * The name of the "list" element.
     */
    @NotNull
    private static final String XML_ELEMENT_LIST = "list";

    /**
     * The name of the "name" attribute within {@link #XML_ELEMENT_LIST}
     * elements.
     */
    @NotNull
    private static final String XML_LIST_NAME = "name";

    /**
     * The name of the "listentry" element.
     */
    @NotNull
    private static final String XML_ELEMENT_LISTENTRY = "listentry";

    /**
     * The name of the "name" attribute within {@link #XML_ELEMENT_LISTENTRY}
     * elements.
     */
    @NotNull
    private static final String XML_LISTENTRY_NAME = "name";

    /**
     * The name of the "value" attribute within {@link #XML_ELEMENT_LISTENTRY}
     * elements.
     */
    @NotNull
    private static final String XML_LISTENTRY_VALUE = "value";

    /**
     * The name of the "type" element.
     */
    @NotNull
    private static final String XML_ELEMENT_TYPE = "type";

    /**
     * The name of the "available" attribute within {@link #XML_ELEMENT_TYPE}
     * elements.
     */
    @NotNull
    private static final String XML_TYPE_AVAILABLE = "available";

    /**
     * The name of the "name" attribute within {@link #XML_ELEMENT_TYPE} or
     * {@link #XML_ELEMENT_DEFAULT_TYPE} elements.
     */
    @NotNull
    public static final String XML_TYPE_NAME = "name";

    /**
     * The name of the "number" attribute within {@link #XML_ELEMENT_TYPE}
     * elements.
     */
    @NotNull
    public static final String XML_TYPE_NUMBER = "number";

    /**
     * The name of the "map" attribute within {@link #XML_ELEMENT_TYPE}
     * elements.
     */
    @NotNull
    public static final String XML_TYPE_MAP = "map";

    /**
     * The name of the "inv" attribute within {@link #XML_ELEMENT_TYPE}
     * elements.
     */
    @NotNull
    public static final String XML_TYPE_INV = "inv";

    /**
     * The name of the "allows_all_inv" attribute within {@link
     * #XML_ELEMENT_TYPE} elements.
     */
    @NotNull
    public static final String XML_TYPE_ALLOWS_ALL_INV = "allows_all_inv";

    /**
     * The name of the "display" attribute within {@link #XML_ELEMENT_TYPE}
     * elements.
     */
    @NotNull
    public static final String XML_TYPE_DISPLAY = "display";

    /**
     * The name of the "ignorelists" element.
     */
    @NotNull
    private static final String XML_ELEMENT_IGNORELISTS = "ignorelists";

    /**
     * The name of the "ignore_list" element.
     */
    @NotNull
    private static final String XML_ELEMENT_IGNORE_LIST = "ignore_list";

    /**
     * The name of the "name" attribute within {@link #XML_ELEMENT_IGNORE_LIST}
     * elements.
     */
    @NotNull
    private static final String XML_IGNORE_LIST_NAME = "name";

    /**
     * The logger for printing log messages.
     */
    @NotNull
    private static final Category log = Logger.getLogger(ArchetypeTypeSetParser.class);

    /**
     * The {@link DocumentBuilder} to use.
     */
    @NotNull
    private final DocumentBuilder documentBuilder;

    /**
     * The {@link ArchetypeTypeSet} to update.
     */
    @NotNull
    private final ArchetypeTypeSet archetypeTypeSet;

    /**
     * The {@link ArchetypeTypeParser} }to use.
     */
    @NotNull
    private final ArchetypeTypeParser archetypeTypeParser;

    /**
     * The default {@link ArchetypeType}.
     */
    @Nullable
    private ArchetypeType defaultArchetypeType;

    /**
     * Creates a new instance.
     * @param documentBuilder the document builder to use
     * @param archetypeTypeSet the archetype type set to update
     * @param archetypeTypeParser the archetype type parser to use
     */
    public ArchetypeTypeSetParser(@NotNull final DocumentBuilder documentBuilder, @NotNull final ArchetypeTypeSet archetypeTypeSet, @NotNull final ArchetypeTypeParser archetypeTypeParser) {
        this.documentBuilder = documentBuilder;
        this.archetypeTypeSet = archetypeTypeSet;
        this.archetypeTypeParser = archetypeTypeParser;
    }

    /**
     * Loads a types.xml file.
     * @param errorViewCollector the error view collector for reporting errors
     * @param inputSource the input source providing the contents of the
     * types.xml file
     */
    public void loadTypesFromXML(@NotNull final ErrorViewCollector errorViewCollector, @NotNull final InputSource inputSource) {
        final Document doc;
        try {
            doc = documentBuilder.parse(inputSource);
        } catch (final SAXException e) {
            errorViewCollector.addWarning(ErrorViewCategory.TYPES_ENTRY_INVALID, "parsing error: " + e.getMessage());
            return;
        } catch (final IOException e) {
            errorViewCollector.addWarning(ErrorViewCategory.TYPES_FILE_INVALID, e.getMessage());
            return;
        }
        loadTypesFromXML(errorViewCollector, inputSource.getSystemId(), doc);
    }

    /**
     * Loads a types.xml file.
     * @param errorViewCollector the error view collector for reporting errors
     * @param filename the filename of the types.xml file for error messages
     * @param document the document containing the contents of the types.xml
     * file
     */
    public void loadTypesFromXML(@NotNull final ErrorViewCollector errorViewCollector, @NotNull final String filename, @NotNull final Document document) {
        final Element rootElement = document.getDocumentElement();
        parseBitmasks(getChild(rootElement, XML_ELEMENT_BITMASKS), errorViewCollector);
        parseLists(getChild(rootElement, XML_ELEMENT_LISTS), errorViewCollector);
        final IgnorelistsDefinition ignorelistsDefinition = parseIgnoreLists(getChild(rootElement, XML_ELEMENT_IGNORELISTS), errorViewCollector);
        parseDefaultType(getChild(rootElement, XML_ELEMENT_DEFAULT_TYPE), errorViewCollector, ignorelistsDefinition);

        final Iterator<Element> typeElements = new NodeListIterator<Element>(rootElement, XML_ELEMENT_TYPE);
        while (typeElements.hasNext()) {
            parseTypes(typeElements.next(), errorViewCollector, ignorelistsDefinition);
        }

        if (log.isInfoEnabled()) {
            log.info("Loaded " + archetypeTypeSet.getArchetypeTypeCount() + " types from '" + filename + "\'");
        }
    }

    /**
     * Returns a child {@link Element} of a parent element. The child element
     * must exist.
     * @param parentElement the parent element
     * @param childName the name of the child element
     * @return the child element
     */
    @NotNull
    private static Element getChild(@NotNull final Element parentElement, @NotNull final String childName) {
        final Element element = NodeListIterator.getFirstChild(parentElement, childName);
        if (element == null) {
            throw new IllegalArgumentException("child element '" + childName + "' does not exist");
        }
        return element;
    }

    /**
     * Parses the {@link #XML_ELEMENT_BITMASKS} section of a types.xml file.
     * @param bitmasksElement the element to parse
     * @param errorViewCollector the error view collector for reporting errors
     */
    private void parseBitmasks(@NotNull final Element bitmasksElement, @NotNull final ErrorViewCollector errorViewCollector) {
        final Iterator<Element> bitmaskIterator = new NodeListIterator<Element>(bitmasksElement, XML_ELEMENT_BITMASK);
        while (bitmaskIterator.hasNext()) {
            final Element bitmaskElement = bitmaskIterator.next();

            final String name = bitmaskElement.getAttribute(XML_BITMASK_NAME);
            final AttributeBitmask attributeBitmask = parseBitmask(bitmaskElement, name, errorViewCollector);
            if (attributeBitmask != null) {
                archetypeTypeSet.addBitmask(name, attributeBitmask);
            }
        }
    }

    /**
     * Parses a {@link #XML_ELEMENT_BITMASK} section of a types.xml file.
     * @param bitmaskElement the element to parse
     * @param name the name of the bitmask element
     * @param errorViewCollector the error view collector for reporting errors
     * @return the parsed bitmask attribute or <code>null</code> if the entry is
     *         invalid
     */
    @Nullable
    private static AttributeBitmask parseBitmask(@NotNull final Element bitmaskElement, @NotNull final String name, @NotNull final ErrorViewCollector errorViewCollector) {
        final boolean isNamed = bitmaskElement.getAttribute(XML_BITMASK_IS_NAMED).equals("yes");
        final AttributeBitmask attributeBitmask = new AttributeBitmask(isNamed);

        final NodeListIterator<Element> entries = new NodeListIterator<Element>(bitmaskElement, XML_ELEMENT_BMENTRY);
        while (entries.hasNext()) {
            parseBmentry(entries.next(), errorViewCollector, name, isNamed, attributeBitmask);
        }
        if (attributeBitmask.getNumber() <= 0) {
            errorViewCollector.addWarning(ErrorViewCategory.TYPES_ENTRY_INVALID, name + ": no '" + XML_BMENTRY_BIT + "' entries");
            return null;
        }

        if (isNamed && !attributeBitmask.containsEncoding(0)) {
            errorViewCollector.addWarning(ErrorViewCategory.TYPES_ENTRY_INVALID, name + ": missing name for value 0");
            return null;
        }
        if (isNamed && !attributeBitmask.containsEncoding(attributeBitmask.getMaxValue())) {
            errorViewCollector.addWarning(ErrorViewCategory.TYPES_ENTRY_INVALID, name + ": missing name for value " + attributeBitmask.getMaxValue());
            return null;
        }

        return attributeBitmask;
    }

    /**
     * Parses a {@link #XML_ELEMENT_BMENTRY} section of a types.xml file.
     * @param errorViewCollector the error view collector for reporting errors
     * @param bmentryElement the "bmentry" section to parse
     * @param bitmaskName the name of the parent's "bitmask" section for error
     * messages
     * @param isNamed whether the bitmask attribute is named
     * @param attributeBitmask the bitmask attribute to update
     */
    private static void parseBmentry(@NotNull final Element bmentryElement, @NotNull final ErrorViewCollector errorViewCollector, @NotNull final String bitmaskName, final boolean isNamed, @NotNull final AttributeBitmask attributeBitmask) {
        final String bitAttribute = bmentryElement.getAttribute(XML_BMENTRY_BIT);

        final String valueAttribute = bmentryElement.getAttribute(XML_BMENTRY_VALUE);
        if (bitAttribute.length() != 0 && valueAttribute.length() != 0) {
            errorViewCollector.addWarning(ErrorViewCategory.TYPES_ENTRY_INVALID, bitmaskName + ": element contains both '" + XML_BMENTRY_BIT + "' and '" + XML_BMENTRY_VALUE + "' attributes");
            return;
        }

        final String name = bmentryElement.getAttribute(XML_BMENTRY_NAME);

        final int value;
        if (bitAttribute.length() == 0) {
            if (!isNamed) {
                errorViewCollector.addWarning(ErrorViewCategory.TYPES_ENTRY_INVALID, bitmaskName + ": '" + XML_BMENTRY_VALUE + "' attribute allowed only in named bitmasks");
                return;
            }
            try {
                value = Integer.parseInt(valueAttribute);
            } catch (final NumberFormatException ignored) {
                errorViewCollector.addWarning(ErrorViewCategory.TYPES_ENTRY_INVALID, bitmaskName + ": invalid '" + XML_BMENTRY_VALUE + "' value: " + valueAttribute);
                return;
            }
        } else {
            final int bitValue;
            try {
                bitValue = Integer.parseInt(bitAttribute);
            } catch (final NumberFormatException ignored) {
                errorViewCollector.addWarning(ErrorViewCategory.TYPES_ENTRY_INVALID, bitmaskName + ": invalid '" + XML_BMENTRY_BIT + "' value: " + bitAttribute);
                return;
            }
            if (bitValue < 0 || bitValue >= 32) {
                errorViewCollector.addWarning(ErrorViewCategory.TYPES_ENTRY_INVALID, bitmaskName + ": invalid '" + XML_BMENTRY_BIT + "' value: " + bitValue);
                return;
            }
            if (!attributeBitmask.addBitName(bitValue, name)) {
                errorViewCollector.addWarning(ErrorViewCategory.TYPES_ENTRY_INVALID, bitmaskName + ": duplicate '" + XML_BMENTRY_BIT + "' value: " + bitValue);
                return;
            }
            value = 1 << bitValue;
        }

        attributeBitmask.addName(name, value);

        final String encoding = bmentryElement.getAttribute(XML_BMENTRY_ENCODING);
        if (isNamed) {
            if (encoding.length() == 0) {
                errorViewCollector.addWarning(ErrorViewCategory.TYPES_ENTRY_INVALID, bitmaskName + ": missing '" + XML_BMENTRY_ENCODING + "' attribute");
                return;
            }
            attributeBitmask.addNamedValue(encoding, value);
        } else {
            if (encoding.length() != 0) {
                errorViewCollector.addWarning(ErrorViewCategory.TYPES_ENTRY_INVALID, bitmaskName + ": unused '" + XML_BMENTRY_ENCODING + "' attribute");
                //noinspection UnnecessaryReturnStatement
                return;
            }
        }
    }

    /**
     * Parses the {@link #XML_ELEMENT_LISTS} section of a types.xml file.
     * @param listsElement the element to parse
     * @param errorViewCollector the error view collector for reporting errors
     */
    private void parseLists(@NotNull final Element listsElement, @NotNull final ErrorViewCollector errorViewCollector) {
        final Iterator<Element> listIterator = new NodeListIterator<Element>(listsElement, XML_ELEMENT_LIST);
        while (listIterator.hasNext()) {
            final Element listElement = listIterator.next();
            final ArchetypeTypeList list = parseList(errorViewCollector, listElement);
            if (list.size() > 0) {
                final String name = listElement.getAttribute(XML_LIST_NAME);
                archetypeTypeSet.addList(name, list);
            }
        }
    }

    /**
     * Parses an {@link #XML_ELEMENT_LIST} section of a types.xml file.
     * @param errorViewCollector the error view collector for reporting errors
     * @param listElement the element to parse
     * @return the parsed archetype type list
     */
    @NotNull
    private static ArchetypeTypeList parseList(@NotNull final ErrorViewCollector errorViewCollector, @NotNull final Element listElement) {
        final ArchetypeTypeList list = new ArchetypeTypeList();
        final Iterator<Element> listentryIterator = new NodeListIterator<Element>(listElement, XML_ELEMENT_LISTENTRY);
        while (listentryIterator.hasNext()) {
            final Element listentryElement = listentryIterator.next();
            final String name = listentryElement.getAttribute(XML_LISTENTRY_NAME);
            final String valueString = listentryElement.getAttribute(XML_LISTENTRY_VALUE);
            final int value;
            try {
                value = Integer.valueOf(valueString);
            } catch (final NumberFormatException ignore) {
                errorViewCollector.addWarning(ErrorViewCategory.TYPES_ENTRY_INVALID, name + ": value '" + valueString + "' is not an integer.");
                continue;
            }
            try {
                list.add(value, name);
            } catch (final IllegalArgumentException ex) {
                errorViewCollector.addWarning(ErrorViewCategory.TYPES_ENTRY_INVALID, ex.getMessage() + ".");
            }
        }
        return list;
    }

    /**
     * Parses the {@link #XML_ELEMENT_IGNORELISTS} section of a types.xml file.
     * @param ignorelistsElement the element to parse
     * @param errorViewCollector the error view collector for reporting errors
     * @return the section's contents
     */
    @NotNull
    private static IgnorelistsDefinition parseIgnoreLists(@NotNull final Element ignorelistsElement, @NotNull final ErrorViewCollector errorViewCollector) {
        final Iterator<Element> ignoreListIterator = new NodeListIterator<Element>(ignorelistsElement, XML_ELEMENT_IGNORE_LIST);
        final IgnorelistsDefinition ignorelistsDefinition = new IgnorelistsDefinition();
        while (ignoreListIterator.hasNext()) {
            final Element ignoreListElement = ignoreListIterator.next();
            final String name = ignoreListElement.getAttribute(XML_IGNORE_LIST_NAME);
            if (ignorelistsDefinition.containsKey(name)) {
                errorViewCollector.addWarning(ErrorViewCategory.TYPES_ENTRY_INVALID, name + ": duplicate name");
            }

            final Iterator<Element> attributeIterator = new NodeListIterator<Element>(ignoreListElement, ArchetypeAttributeParser.XML_ELEMENT_ATTRIBUTE);
            while (attributeIterator.hasNext()) {
                final Element attributeElement = attributeIterator.next();
                final Attr archAttribute = attributeElement.getAttributeNode(ArchetypeAttributeParser.XML_ATTRIBUTE_ARCH);
                if (archAttribute != null) {
                    try {
                        ignorelistsDefinition.put(name, archAttribute.getValue());
                    } catch (final IllegalArgumentException ex) {
                        errorViewCollector.addWarning(ErrorViewCategory.TYPES_ENTRY_INVALID, name + ": " + ex.getMessage() + ".");
                    }
                } else {
                    errorViewCollector.addWarning(ErrorViewCategory.TYPES_ENTRY_INVALID, name + ": attribute missing '" + ArchetypeAttributeParser.XML_ATTRIBUTE_ARCH + "'.");
                }

                try {
                    rejectAttributes(attributeElement, ArchetypeAttributeParser.XML_ATTRIBUTE_TYPE, ArchetypeAttributeParser.XML_ATTRIBUTE_ARCH_BEGIN, ArchetypeAttributeParser.XML_ATTRIBUTE_ARCH_END, ArchetypeAttributeParser.XML_ATTRIBUTE_EDITOR, ArchetypeAttributeParser.XML_ATTRIBUTE_VALUE, ArchetypeAttributeParser.XML_ATTRIBUTE_MIN, ArchetypeAttributeParser.XML_ATTRIBUTE_MAX, ArchetypeAttributeParser.XML_ATTRIBUTE_CHECK_MIN, ArchetypeAttributeParser.XML_ATTRIBUTE_CHECK_MAX, ArchetypeAttributeParser.XML_ATTRIBUTE_LENGTH, ArchetypeAttributeParser.XML_ATTRIBUTE_TRUE, ArchetypeAttributeParser.XML_ATTRIBUTE_FALSE, ArchetypeAttributeParser.XML_ATTRIBUTE_MARKER);
                } catch (final SyntaxErrorException ex) {
                    errorViewCollector.addWarning(ErrorViewCategory.TYPES_ENTRY_INVALID, name + ": " + ex.getMessage() + ".");
                }
            }
        }
        return ignorelistsDefinition;
    }

    /**
     * Parses the {@link #XML_ELEMENT_DEFAULT_TYPE} element of a types.xml
     * file.
     * @param defaultTypeElement the element to parse
     * @param errorViewCollector the error view collector for reporting errors
     * @param ignorelistsDefinition the contents of the "ignorelists" section
     */
    private void parseDefaultType(@NotNull final Element defaultTypeElement, @NotNull final ErrorViewCollector errorViewCollector, @NotNull final IgnorelistsDefinition ignorelistsDefinition) {
        defaultArchetypeType = archetypeTypeParser.loadAttributeList(defaultTypeElement, errorViewCollector, null, archetypeTypeSet, ignorelistsDefinition, true);
    }

    /**
     * Parses an {@link #XML_ELEMENT_TYPE} element of a types.xml file.
     * @param typeElement the type element to parse
     * @param errorViewCollector the error view collector for reporting errors
     * @param ignorelistsDefinition the contents of the "ignorelists" section
     */
    private void parseTypes(@NotNull final Element typeElement, @NotNull final ErrorViewCollector errorViewCollector, @NotNull final IgnorelistsDefinition ignorelistsDefinition) {
        if (!typeElement.getAttribute(XML_TYPE_AVAILABLE).equals("no")) {
            archetypeTypeSet.addArchetypeType(archetypeTypeParser.loadAttributeList(typeElement, errorViewCollector, defaultArchetypeType, archetypeTypeSet, ignorelistsDefinition, false));
        }
    }

    /**
     * Makes sure an {@link Element} does not define attributes.
     * @param element the element to check
     * @param attributes the attributes to check for
     * @throws SyntaxErrorException if an attribute was found
     */
    private static void rejectAttributes(@NotNull final Element element, @NotNull final String... attributes) throws SyntaxErrorException {
        for (final String attribute : attributes) {
            if (element.hasAttribute(attribute)) {
                throw new SyntaxErrorException("unexpected attribute '" + attribute + "'");
            }
        }
    }

}
