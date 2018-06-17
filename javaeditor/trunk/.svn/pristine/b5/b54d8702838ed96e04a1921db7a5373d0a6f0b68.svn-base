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

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import net.sf.gridarta.model.errorview.ErrorViewCategory;
import net.sf.gridarta.model.errorview.ErrorViewCollector;
import net.sf.gridarta.utils.StringUtils;
import net.sf.japi.xml.NodeListIterator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Utility class for parsing {@link ArchetypeType} instances.
 * @author <a href="mailto:andi.vogl@gmx.net">Andreas Vogl</a>
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 * @author Andreas Kirschbaum
 */
public class ArchetypeTypeParser {

    /**
     * Attribute Element Name.
     */
    @NotNull
    private static final String XML_ELEMENT_ATTRIBUTE = "attribute";

    /**
     * Required Element Name.
     */
    @NotNull
    private static final String XML_ELEMENT_REQUIRED = "required";

    /**
     * Ignore Element Name.
     */
    @NotNull
    private static final String XML_ELEMENT_IGNORE = "ignore";

    /**
     * Ignore List Element Name.
     */
    @NotNull
    private static final String XML_ELEMENT_IGNORE_LIST = "ignore_list";

    /**
     * Import Type Element Name.
     */
    @NotNull
    private static final String XML_ELEMENT_IMPORT_TYPE = "import_type";

    /**
     * Section Element Name.
     */
    @NotNull
    private static final String XML_ELEMENT_SECTION = "section";

    /**
     * The "arch" attribute name of a {@link #XML_ELEMENT_ATTRIBUTE} element.
     */
    @NotNull
    private static final String XML_ATTRIBUTE_ARCH = "arch";

    /**
     * The "value" attribute name of a {@link #XML_ELEMENT_ATTRIBUTE} element.
     */
    @NotNull
    private static final String XML_ATTRIBUTE_VALUE = "value";

    /**
     * The "name" attribute name of a {@link #XML_ELEMENT_IGNORE_LIST} element.
     */
    @NotNull
    private static final String XML_IGNORE_LIST_NAME = "name";

    /**
     * The "name" attribute name of a {@link #XML_ELEMENT_IMPORT_TYPE} element.
     */
    @NotNull
    private static final String XML_IMPORT_TYPE_NAME = "name";

    /**
     * The "name" attribute name of a {@link #XML_ELEMENT_SECTION} element.
     */
    @NotNull
    private static final String XML_SECTION_NAME = "name";

    /**
     * Description Element Name.
     */
    @NotNull
    private static final String XML_DESCRIPTION = "description";

    /**
     * Use Element Name.
     */
    @NotNull
    private static final String XML_USE = "use";

    /**
     * An empty array of <code>int</code>s.
     */
    @NotNull
    public static final int[] EMPTY_INT_ARRAY = new int[0];

    /**
     * The parser to use.
     */
    @NotNull
    private final ArchetypeAttributeParser archetypeAttributeParser;

    /**
     * Creates a new instance.
     * @param archetypeAttributeParser the parser to use
     */
    public ArchetypeTypeParser(@NotNull final ArchetypeAttributeParser archetypeAttributeParser) {
        this.archetypeAttributeParser = archetypeAttributeParser;
    }

    /**
     * Parses an element which contains a list of {@link
     * ArchetypeAttributeParser#XML_ELEMENT_ATTRIBUTE} elements from a types.xml
     * file.
     * @param typeElement the xml 'type' element which is going to be parsed
     * @param errorViewCollector the error view collector for reporting errors
     * @param archetypeType the parent archetype type
     * @param archetypeTypeSet archetype type list
     * @param ignorelistsDefinition the ignore_lists contents
     * @param isDefaultType whether this element is the "default_type"
     * @return the archetype type or <code>null</code>
     */
    @NotNull
    public ArchetypeType loadAttributeList(@NotNull final Element typeElement, @NotNull final ErrorViewCollector errorViewCollector, @Nullable final ArchetypeType archetypeType, @NotNull final ArchetypeTypeSet archetypeTypeSet, @NotNull final IgnorelistsDefinition ignorelistsDefinition, final boolean isDefaultType) {
        final String typeName = parseTypeName(typeElement, isDefaultType);
        final int typeNo = parseTypeNo(typeElement, isDefaultType, typeName, errorViewCollector);
        final String display = typeElement.getAttribute(ArchetypeTypeSetParser.XML_TYPE_DISPLAY);
        final boolean map = parseMap(typeElement);
        final int[] inv = parseInv(typeElement, typeName, errorViewCollector);
        final boolean allowsAllInv = parseAllowsAllInv(typeElement);
        final Collection<String> ignoreTable = new HashSet<String>();
        final ArchetypeAttributesDefinition typeAttributes = isDefaultType ? new ArchetypeAttributesDefinition() : parseTypeAttributes(typeElement, typeName, errorViewCollector, ignoreTable, ignorelistsDefinition);

        final Collection<String> importTypes = new LinkedHashSet<String>();
        final Iterator<Element> importTypeIterator = new NodeListIterator<Element>(typeElement, XML_ELEMENT_IMPORT_TYPE);
        while (importTypeIterator.hasNext()) {
            final Element importTypeElement = importTypeIterator.next();
            final String importType = parseImportType(importTypeElement, typeName, errorViewCollector);
            if (importType != null && !importTypes.add(importType)) {
                errorViewCollector.addWarning(ErrorViewCategory.TYPES_ENTRY_INVALID, "type " + typeName + " has duplicate import for '" + importType + "'.");
            }
        }

        final ArchetypeAttributeSection attributeSection = new ArchetypeAttributeSection();
        final SectionNames sectionNames = new SectionNames();
        addAttributeList(typeElement, attributeSection, errorViewCollector, typeName, typeNo == -1 ? SectionNames.GENERAL_SECTION : SectionNames.SPECIAL_SECTION, sectionNames, archetypeTypeSet);
        if (archetypeType != null) {
            final Collection<String> autoIgnoreTable = new HashSet<String>();
            for (final ArchetypeAttribute attribute : attributeSection) {
                autoIgnoreTable.add(attribute.getArchetypeAttributeName());
            }

            if (archetypeType.hasAttribute()) {
                for (final String importType : importTypes) {
                    addImportList(importType, attributeSection, errorViewCollector, typeName, archetypeTypeSet, autoIgnoreTable);
                }
            }

            addDefaultList(archetypeType, attributeSection, autoIgnoreTable, ignoreTable);
        }
        attributeSection.setSectionNames(sectionNames);

        return new ArchetypeType(typeName, typeNo, display, map, inv, allowsAllInv, parseDescription(typeElement), parseUse(typeElement), sectionNames.getSectionNames(), attributeSection, typeAttributes);
    }

    /**
     * Adds all attributes of am {@link ArchetypeType} to an {@link
     * ArchetypeAttributeSection} that are not ignored.
     * @param archetypeType the archetype type
     * @param attributeSection the archetype attributes to add to
     * @param autoIgnoreTable the attributes to ignore
     * @param ignoreTable the attributes to ignore
     */
    private static void addDefaultList(@NotNull final Iterable<ArchetypeAttribute> archetypeType, @NotNull final ArchetypeAttributeSection attributeSection, @NotNull final Collection<String> autoIgnoreTable, @NotNull final Collection<String> ignoreTable) {
        for (final ArchetypeAttribute archetypeAttribute : archetypeType) {
            // add all attributes from the default_type which are not in the ignoreTable
            if (!ignoreTable.contains(archetypeAttribute.getArchetypeAttributeName()) && !autoIgnoreTable.contains(archetypeAttribute.getArchetypeAttributeName())) {
                attributeSection.add(archetypeAttribute.clone());
            }
        }
    }

    /**
     * Adds all imported attributes to an {@link ArchetypeAttributeSection}.
     * @param importName the name of the import list to import
     * @param attributeSection the archetype attributes to add to
     * @param errorViewCollector the error view collector for reporting errors
     * @param typeName the type name for error messages
     * @param archetypeTypeSet the archetype type set
     * @param autoIgnoreTable the attributes to ignore; imported attributes are
     * added
     */
    private static void addImportList(@NotNull final String importName, @NotNull final ArchetypeAttributeSection attributeSection, @NotNull final ErrorViewCollector errorViewCollector, @NotNull final String typeName, @NotNull final ArchetypeTypeSet archetypeTypeSet, @NotNull final Collection<String> autoIgnoreTable) {
        final Iterable<ArchetypeAttribute> importType = archetypeTypeSet.getArchetypeType(importName);
        if (importType == null) {
            errorViewCollector.addWarning(ErrorViewCategory.TYPES_ENTRY_INVALID, typeName + ": import type \"" + importName + "\" not found!");
            return;
        }

        for (final ArchetypeAttribute archetypeAttribute : importType) {
            final String attributeName = archetypeAttribute.getArchetypeAttributeName();
            if (autoIgnoreTable.contains(attributeName)) {
                continue;
            }

            if (archetypeAttribute.getSectionName().equals(SectionNames.GENERAL_SECTION)) {
                continue;
            }

            attributeSection.add(archetypeAttribute.clone());
            autoIgnoreTable.add(attributeName);
        }
    }

    /**
     * Parses the {@link #XML_ELEMENT_IMPORT_TYPE} of an {@link
     * ArchetypeTypeSetParser#XML_ELEMENT_TYPE} or {@link
     * ArchetypeTypeSetParser#XML_ELEMENT_DEFAULT_TYPE} {@link Element}.
     * @param importTypeElement the element
     * @param typeName the type name for error messages
     * @param errorViewCollector the error view collector for error messages
     * @return the import type name or <code>null</code> if no import_type is
     *         present or an error occurs
     */
    @Nullable
    private static String parseImportType(@NotNull final Element importTypeElement, @NotNull final String typeName, @NotNull final ErrorViewCollector errorViewCollector) {
        final Attr nameAttribute = importTypeElement.getAttributeNode(XML_IMPORT_TYPE_NAME);
        if (nameAttribute == null) {
            errorViewCollector.addWarning(ErrorViewCategory.TYPES_ENTRY_INVALID, "type " + typeName + " has " + XML_ELEMENT_IMPORT_TYPE + " element without '" + XML_IMPORT_TYPE_NAME + "'.");
            return null;
        }

        final String name = nameAttribute.getValue();
        if (name.isEmpty()) {
            errorViewCollector.addWarning(ErrorViewCategory.TYPES_ENTRY_INVALID, "type " + typeName + " has " + XML_ELEMENT_IMPORT_TYPE + " element with empty '" + XML_IMPORT_TYPE_NAME + "'.");
            return null;
        }

        return name;
    }

    /**
     * Parses the type name of an {@link ArchetypeTypeSetParser#XML_ELEMENT_TYPE}
     * or {@link ArchetypeTypeSetParser#XML_ELEMENT_DEFAULT_TYPE} {@link
     * Element}.
     * @param typeElement the element
     * @param isDefaultType whether the element is a "default_type"
     * @return the type name
     */
    @NotNull
    private static String parseTypeName(@NotNull final Element typeElement, final boolean isDefaultType) {
        return isDefaultType ? "default" : typeElement.getAttribute(ArchetypeTypeSetParser.XML_TYPE_NAME);
    }

    /**
     * Parses the type number of a "type" or "default_type" {@link Element}.
     * @param typeElement the element
     * @param isDefaultType whether the element is a "default_type"
     * @param typeName the type name for error messages
     * @param errorViewCollector the error view collector for error messages
     * @return the type number
     */
    private static int parseTypeNo(@NotNull final Element typeElement, final boolean isDefaultType, @NotNull final String typeName, @NotNull final ErrorViewCollector errorViewCollector) {
        if (isDefaultType) {
            return -1;
        }

        final String number = typeElement.getAttribute(ArchetypeTypeSetParser.XML_TYPE_NUMBER);
        try {
            return Integer.parseInt(number);
        } catch (final NumberFormatException ignored) {
            errorViewCollector.addWarning(ErrorViewCategory.TYPES_ENTRY_INVALID, "type " + typeName + " has invalid type number '" + number + "'.");
            return -1;
        }
    }

    /**
     * Parses the "map" attribute of a "type" {@link Element}.
     * @param typeElement the element
     * @return whether this archetype is allowed on maps
     */
    private static boolean parseMap(@NotNull final Element typeElement) {
        final String map = typeElement.getAttribute(ArchetypeTypeSetParser.XML_TYPE_MAP);
        return map.equals("yes");
    }

    /**
     * Parses the "inv" attribute of a "type" {@link Element}.
     * @param typeElement the element
     * @param typeName the type name for error messages
     * @param errorViewCollector the error view collector for error messages
     * @return the archetype types this game object is allowed in or
     *         <code>null</code> for no restrictions
     */
    @Nullable
    private static int[] parseInv(@NotNull final Element typeElement, @NotNull final String typeName, @NotNull final ErrorViewCollector errorViewCollector) {
        if (!typeElement.hasAttribute(ArchetypeTypeSetParser.XML_TYPE_INV)) {
            return null;
        }

        final String inv = typeElement.getAttribute(ArchetypeTypeSetParser.XML_TYPE_INV);
        if (inv.isEmpty()) {
            return EMPTY_INT_ARRAY;
        }
        if (inv.equals("*")) {
            return null;
        }

        final String[] types = StringUtils.PATTERN_COMMA.split(inv, -1);
        final int[] result = new int[types.length];
        for (int i = 0; i < types.length; i++) {
            try {
                result[i] = Integer.parseInt(types[i]);
            } catch (final NumberFormatException ignored) {
                errorViewCollector.addWarning(ErrorViewCategory.TYPES_ENTRY_INVALID, "type " + typeName + " has invalid inv specification '" + types[i] + "'.");
                result[i] = -1;
            }
        }
        return result;
    }

    /**
     * Parses the "allows_all_inv" attribute of a "type" {@link Element}.
     * @param typeElement the element
     * @return whether this archetype allows all inventory game objects
     */
    private static boolean parseAllowsAllInv(@NotNull final Element typeElement) {
        final String allowsAllInv = typeElement.getAttribute(ArchetypeTypeSetParser.XML_TYPE_ALLOWS_ALL_INV);
        return allowsAllInv.equals("yes");
    }

    /**
     * Parses the type attributes of a "type" or "default_type" {@link
     * Element}.
     * @param typeElement the element
     * @param typeName the type name for error messages
     * @param errorViewCollector the error view collector for error messages
     * @param ignoreTable returns the "ignore" attributes
     * @param ignorelistsDefinition the defined ignore lists
     * @return the type attributes
     */
    @NotNull
    private static ArchetypeAttributesDefinition parseTypeAttributes(@NotNull final Element typeElement, @NotNull final String typeName, @NotNull final ErrorViewCollector errorViewCollector, @NotNull final Collection<String> ignoreTable, @NotNull final IgnorelistsDefinition ignorelistsDefinition) {
        final ArchetypeAttributesDefinition typeAttributes = parseRequiredAttribute(typeElement, typeName, errorViewCollector);

        final Element ignoreElement = NodeListIterator.getFirstChild(typeElement, XML_ELEMENT_IGNORE);
        if (ignoreElement != null) {
            parseAttributeAttributes(ignoreElement, typeName, errorViewCollector, ignoreTable);
            parseIgnoreListAttribute(ignoreElement, typeName, errorViewCollector, ignoreTable, ignorelistsDefinition);
        }

        return typeAttributes;
    }

    /**
     * Parses the {@link #XML_ELEMENT_REQUIRED} child.
     * @param typeElement the root element
     * @param typeName the type name
     * @param errorViewCollector the error view collector for error messages
     * @return the type definitions
     */
    @NotNull
    private static ArchetypeAttributesDefinition parseRequiredAttribute(@NotNull final Element typeElement, @NotNull final String typeName, @NotNull final ErrorViewCollector errorViewCollector) {
        final ArchetypeAttributesDefinition attributes = new ArchetypeAttributesDefinition();
        final Element requiredElement = NodeListIterator.getFirstChild(typeElement, XML_ELEMENT_REQUIRED);
        if (requiredElement != null) {
            final Iterator<Element> attributeIterator = new NodeListIterator<Element>(requiredElement, XML_ELEMENT_ATTRIBUTE);
            while (attributeIterator.hasNext()) {
                final Element attributeElement = attributeIterator.next();

                final Attr archAttribute = attributeElement.getAttributeNode(XML_ATTRIBUTE_ARCH);
                if (archAttribute == null) {
                    errorViewCollector.addWarning(ErrorViewCategory.TYPES_ENTRY_INVALID, XML_ELEMENT_REQUIRED + ": element of type " + typeName + ": " + XML_ELEMENT_ATTRIBUTE + " missing '" + XML_ATTRIBUTE_ARCH + "'.");
                    continue;
                }
                final String attributeValue = archAttribute.getValue();
                if (attributeValue.isEmpty()) {
                    errorViewCollector.addWarning(ErrorViewCategory.TYPES_ENTRY_INVALID, XML_ELEMENT_REQUIRED + ": element of type " + typeName + ": " + XML_ELEMENT_ATTRIBUTE + " empty '" + XML_ATTRIBUTE_ARCH + "'.");
                    continue;
                }

                final Attr valueAttribute = attributeElement.getAttributeNode(XML_ATTRIBUTE_VALUE);
                if (valueAttribute == null) {
                    errorViewCollector.addWarning(ErrorViewCategory.TYPES_ENTRY_INVALID, XML_ELEMENT_REQUIRED + ": element of type " + typeName + ": " + XML_ELEMENT_ATTRIBUTE + " missing '" + XML_ATTRIBUTE_VALUE + "'.");
                    continue;
                }
                final String value = valueAttribute.getValue();
                if (value.isEmpty()) {
                    errorViewCollector.addWarning(ErrorViewCategory.TYPES_ENTRY_INVALID, XML_ELEMENT_REQUIRED + ": element of type " + typeName + ": " + XML_ELEMENT_ATTRIBUTE + " empty '" + XML_ATTRIBUTE_VALUE + "'.");
                    continue;
                }

                attributes.add(new ArchetypeAttributeDefinition(attributeValue, value));
            }
        }
        return attributes;
    }

    /**
     * Parses the {@link #XML_ELEMENT_ATTRIBUTE} child.
     * @param ignoreElement the ignore element to parse
     * @param typeName the type name for error messages
     * @param errorViewCollector the error view collector for error messages
     * @param ignoreTable the ignore table to add to
     */
    private static void parseAttributeAttributes(@NotNull final Element ignoreElement, @NotNull final String typeName, @NotNull final ErrorViewCollector errorViewCollector, @NotNull final Collection<String> ignoreTable) {
        final Iterator<Element> attributeIterator = new NodeListIterator<Element>(ignoreElement, XML_ELEMENT_ATTRIBUTE);
        while (attributeIterator.hasNext()) {
            final Element attributeElement = attributeIterator.next();

            final Attr archAttribute = attributeElement.getAttributeNode(XML_ATTRIBUTE_ARCH);
            if (archAttribute == null) {
                errorViewCollector.addWarning(ErrorViewCategory.TYPES_ENTRY_INVALID, XML_ELEMENT_IGNORE + ": section of type " + typeName + ": " + XML_ELEMENT_ATTRIBUTE + " missing '" + XML_ATTRIBUTE_ARCH + "'.");
                continue;
            }

            final String attributeValue = archAttribute.getValue();
            if (attributeValue.isEmpty()) {
                errorViewCollector.addWarning(ErrorViewCategory.TYPES_ENTRY_INVALID, XML_ELEMENT_IGNORE + ": section of type " + typeName + ": " + XML_ELEMENT_ATTRIBUTE + " empty '" + XML_ATTRIBUTE_ARCH + "'.");
                continue;
            }

            ignoreTable.add(attributeValue);
        }
    }

    /**
     * Parses the "ignore_list" children.
     * @param ignoreElement the ignore element to parse
     * @param typeName the type name for error messages
     * @param errorViewCollector the error view collector for error messages
     * @param ignoreTable the ignore table to add to
     * @param ignorelistsDefinition the ignore lists to use
     */
    private static void parseIgnoreListAttribute(@NotNull final Element ignoreElement, @NotNull final String typeName, @NotNull final ErrorViewCollector errorViewCollector, @NotNull final Collection<String> ignoreTable, @NotNull final IgnorelistsDefinition ignorelistsDefinition) {
        final Iterator<Element> ignoreListIterator = new NodeListIterator<Element>(ignoreElement, XML_ELEMENT_IGNORE_LIST);
        while (ignoreListIterator.hasNext()) {
            final Element ignoreListElement = ignoreListIterator.next();

            final Attr nameAttribute = ignoreListElement.getAttributeNode(XML_IGNORE_LIST_NAME);
            if (nameAttribute == null) {
                errorViewCollector.addWarning(ErrorViewCategory.TYPES_ENTRY_INVALID, XML_ELEMENT_IGNORE + ": section of type " + typeName + ": " + XML_ELEMENT_IGNORE_LIST + " missing '" + XML_IGNORE_LIST_NAME + "'.");
                continue;
            }

            final String name = nameAttribute.getValue();
            final Iterable<String> ignoreList = ignorelistsDefinition.get(name);
            if (ignoreList == null) {
                errorViewCollector.addWarning(ErrorViewCategory.TYPES_ENTRY_INVALID, XML_ELEMENT_IGNORE + ": section of type " + typeName + ": " + XML_ELEMENT_IGNORE_LIST + " with " + XML_IGNORE_LIST_NAME + " \"" + name + "\" is undefined.");
                continue;
            }

            for (final String ignoreItem : ignoreList) {
                ignoreTable.add(ignoreItem);
            }
        }
    }

    /**
     * Parses the {@link #XML_DESCRIPTION} child.
     * @param root the root element
     * @return the description or <code>null</code> if no description is
     *         present
     */
    @Nullable
    private static String parseDescription(@NotNull final Element root) {
        final Node elem = NodeListIterator.getFirstChild(root, XML_DESCRIPTION);
        return elem == null ? null : elem.getTextContent().trim();
    }

    /**
     * Parses the {@link #XML_USE} child.
     * @param root the root element
     * @return the use or <code>null</code> if no use is present
     */
    @Nullable
    private static String parseUse(@NotNull final Element root) {
        final Node elem = NodeListIterator.getFirstChild(root, XML_USE);
        return elem == null ? null : elem.getTextContent().trim();
    }

    /**
     * Parses the {@link #XML_ELEMENT_ATTRIBUTE} and {@link
     * #XML_ELEMENT_SECTION} children of a "type" or "default_type" element and
     * adds all found attributes to an {@link ArchetypeAttribute}.
     * @param typeElement the type element to parse
     * @param attributeSection the archetype attributes to add to
     * @param errorViewCollector the error view collector for reporting errors
     * @param typeName the type name of the element being parsed
     * @param defaultSectionName the name of the section to add attributes in
     * the default section
     * @param sectionNames the defined section names
     * @param archetypeTypeSet archetype type list
     */
    private void addAttributeList(@NotNull final Element typeElement, @NotNull final ArchetypeAttributeSection attributeSection, @NotNull final ErrorViewCollector errorViewCollector, @NotNull final String typeName, @NotNull final String defaultSectionName, @NotNull final SectionNames sectionNames, @NotNull final ArchetypeTypeSet archetypeTypeSet) {
        final Iterator<Element> childIterator = new NodeListIterator<Element>(typeElement, Node.ELEMENT_NODE);
        while (childIterator.hasNext()) {
            final Element childElement = childIterator.next();
            final String childName = childElement.getNodeName();

            if (childName.equals(XML_ELEMENT_ATTRIBUTE)) {
                parseAttribute(childElement, errorViewCollector, typeName, sectionNames, defaultSectionName, archetypeTypeSet, attributeSection);
            } else if (childName.equals(XML_ELEMENT_SECTION) && childElement.hasChildNodes()) {
                final Attr nameAttribute = childElement.getAttributeNode(XML_SECTION_NAME);
                if (nameAttribute == null) {
                    errorViewCollector.addWarning(ErrorViewCategory.TYPES_ENTRY_INVALID, "type " + typeName + " contains a " + XML_ELEMENT_SECTION + " missing '" + XML_SECTION_NAME + "'.");
                    continue;
                }

                final String sectionName = nameAttribute.getValue();
                if (sectionName.isEmpty()) {
                    errorViewCollector.addWarning(ErrorViewCategory.TYPES_ENTRY_INVALID, "type " + typeName + " contains a " + XML_ELEMENT_SECTION + " with empty '" + XML_SECTION_NAME + "'.");
                    continue;
                }

                sectionNames.defineSectionName(sectionName);

                final Iterator<Element> attributeIterator = new NodeListIterator<Element>(childElement, XML_ELEMENT_ATTRIBUTE);
                while (attributeIterator.hasNext()) {
                    parseAttribute(attributeIterator.next(), errorViewCollector, typeName, sectionNames, sectionName, archetypeTypeSet, attributeSection);
                }
            }
        }
    }

    /**
     * Parses an {@link #XML_ELEMENT_ATTRIBUTE} element.
     * @param attributeElement the attribute element to parse
     * @param errorViewCollector the error view collector for reporting errors
     * @param typeName the type name of the element being parsed
     * @param sectionNames the defined section names
     * @param sectionName the section name or <code>null</code> for top-level
     * attributes
     * @param archetypeTypeSet archetype type list
     * @param attributeSection the attribute will be added to this
     */
    private void parseAttribute(@NotNull final Element attributeElement, @NotNull final ErrorViewCollector errorViewCollector, @NotNull final String typeName, @NotNull final SectionNames sectionNames, @NotNull final String sectionName, @NotNull final ArchetypeTypeSet archetypeTypeSet, @NotNull final ArchetypeAttributeSection attributeSection) {
        final ArchetypeAttribute archetypeAttribute = parseAttribute(attributeElement, errorViewCollector, typeName, sectionName, archetypeTypeSet);
        if (archetypeAttribute == null) {
            return;
        }

        sectionNames.defineSectionName(archetypeAttribute.getSectionName());
        attributeSection.add(archetypeAttribute);
    }

    /**
     * Parses an XML attribute element. If parsing succeeds, the new {@link
     * ArchetypeAttribute} is added to the temporary linked list provided by the
     * parameters (see below). Assignment of sections to attributes also happens
     * in this method.
     * @param attributeElement the XML attribute element
     * @param errorViewCollector the error view collector for reporting errors
     * @param typeName the archetype type's name
     * @param sectionName name of the section or <code>null</code> for top-level
     * attribute definitions
     * @param archetypeTypeSet archetype type set
     * @return the parsed attribute or <code>null</code> if an error occurs
     */
    @Nullable
    private ArchetypeAttribute parseAttribute(@NotNull final Element attributeElement, @NotNull final ErrorViewCollector errorViewCollector, @NotNull final String typeName, @NotNull final String sectionName, @NotNull final ArchetypeTypeSet archetypeTypeSet) {
        try {
            return archetypeAttributeParser.load(attributeElement, errorViewCollector, archetypeTypeSet, typeName, sectionName);
        } catch (final MissingAttributeException ex) {
            errorViewCollector.addWarning(ErrorViewCategory.TYPES_ENTRY_INVALID, "type " + typeName + ": " + ex.getMessage());
            return null;
        }
    }

}
