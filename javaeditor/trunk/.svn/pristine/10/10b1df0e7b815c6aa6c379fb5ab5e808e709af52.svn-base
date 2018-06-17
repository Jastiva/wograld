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

package net.sf.gridarta.model.match;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import net.sf.gridarta.utils.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Class for some default {@link GameObjectMatcher GameObjectMatchers}.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 */
public class GameObjectMatcherParser {

    /**
     * The name of the "name" attribute within {@link #XML_ELEMENT_ATTRIB}
     * elements.
     */
    @NotNull
    private static final String XML_ATTRIB_ATTRIBUTE_NAME = "name";

    /**
     * The name of the "op" attribute within {@link #XML_ELEMENT_ATTRIB}
     * elements.
     */
    @NotNull
    private static final String XML_ATTRIB_ATTRIBUTE_OP = "op";

    /**
     * The name of the "type" attribute within {@link #XML_ELEMENT_ATTRIB}
     * elements.
     */
    @NotNull
    private static final String XML_ATTRIB_ATTRIBUTE_TYPE = "type";

    /**
     * The name of the "value" attribute within {@link #XML_ELEMENT_ATTRIB}
     * elements.
     */
    @NotNull
    private static final String XML_ATTRIB_ATTRIBUTE_VALUE = "value";

    /**
     * The name of the "useArchetype" attribute within {@link
     * #XML_ELEMENT_ATTRIB} elements.
     */
    @NotNull
    private static final String XML_ATTRIB_ATTRIBUTE_USEARCHETYPE = "useArchetype";

    /**
     * The name of the "id" attribute within matcher elements.
     */
    @NotNull
    private static final String XML_MATCHER_ATTRIBUTE_ID = "id";

    /**
     * The name of the "system" attribute within matcher elements.
     */
    @NotNull
    private static final String XML_MATCHER_ATTRIBUTE_SYSTEM = "system";

    /**
     * The name of the "Env" element within matcher elements.
     */
    @NotNull
    private static final String XML_MATCHER_ELEMENT_ENV = "Env";

    /**
     * The name of the "TypeNrs" element.
     */
    @NotNull
    private static final String XML_ELEMENT_TYPENRS = "TypeNrs";

    /**
     * The name of the "Attrib" element.
     */
    @NotNull
    private static final String XML_ELEMENT_ATTRIB = "Attrib";

    /**
     * The name of the "Or" element.
     */
    @NotNull
    private static final String XML_ELEMENT_OR = "Or";

    /**
     * The name of the "And" element.
     */
    @NotNull
    private static final String XML_ELEMENT_AND = "And";

    /**
     * The name of the "numbers" attribute within {@link #XML_ELEMENT_TYPENRS}
     * elements.
     */
    @NotNull
    private static final String XML_TYPENRS_ATTRIBUTE_NUMBERS = "numbers";

    /**
     * {@link XPath} to use.
     */
    @NotNull
    private final XPath xpath;

    /**
     * Creates a <code>GameObjectMatcherParser</code>.
     * @param xpath the <code>XPath</code> to use
     */
    public GameObjectMatcherParser(@NotNull final XPath xpath) {
        this.xpath = xpath;
    }

    /**
     * Creates a {@link NamedGameObjectMatcher} from XML.
     * @param el the XML node to create from
     * @param editType the edit type for the new matcher
     * @return the created matcher
     * @throws ParsingException if a parsing error occurs
     */
    @NotNull
    public NamedGameObjectMatcher parseMatcher(@NotNull final Element el, final int editType) throws ParsingException {
        final String currentLanguage = Locale.getDefault().getLanguage();
        final String localName = el.getLocalName();
        if (localName == null || !localName.equals("GameObjectMatcher")) {
            throw new ParsingException("wrong local element name: expected \"GameObjectMatcher\" but got \" + localName + \"");
        }
        final String id = el.getAttribute(XML_MATCHER_ATTRIBUTE_ID);
        String title = getLanguageTitle(el, currentLanguage);
        if (title == null || title.length() == 0) {
            title = getLanguageTitle(el, "en");
        }
        if (title == null || title.length() == 0) {
            title = id;
        }
        final boolean systemMatcher = el.getAttribute(XML_MATCHER_ATTRIBUTE_SYSTEM).equals("true");
        final Node content = xpathEvaluate(el, "*[last()]");
        assert content != null;

        final Node env = xpathEvaluate(el, XML_MATCHER_ELEMENT_ENV);
        @Nullable final GameObjectMatcher envGameObjectMatcher;
        if (env != null && env instanceof Element) {
            final Node envContent = xpathEvaluate(env, "*[1]");
            envGameObjectMatcher = envContent == null ? new AndGameObjectMatcher(Collections.<GameObjectMatcher>emptyList()) : createMatcher((Element) envContent);
        } else {
            envGameObjectMatcher = null;
        }

        assert content.getNodeType() == Node.ELEMENT_NODE;
        final GameObjectMatcher matcher = createMatcher((Element) content);
        return new NamedGameObjectMatcher(systemMatcher ? 0 : editType, id, title, systemMatcher, envGameObjectMatcher, matcher);
    }

    /**
     * Returns the title for a given language.
     * @param el the element to search
     * @param language the language to look up
     * @return the title
     */
    @Nullable
    private String getLanguageTitle(@NotNull final Node el, @NotNull final String language) {
        try {
            return (String) xpath.evaluate("title[lang('" + language + "')]", el, XPathConstants.STRING);
        } catch (final XPathExpressionException ex) {
            throw new AssertionError(ex);
        }
    }

    /**
     * Creates a {@link GameObjectMatcher} from XML.
     * @param el the XML node to create from
     * @return the created matcher
     * @throws ParsingException if a parsing error occurs
     */
    @NotNull
    private static GameObjectMatcher createMatcher(@NotNull final Element el) throws ParsingException {
        final String localName = el.getLocalName();
        if (localName == null) {
            // ignore
        } else if (localName.equals(XML_ELEMENT_TYPENRS)) {
            return createTypeNrsArchObjectMatcher(el);
        } else if (localName.equals(XML_ELEMENT_ATTRIB)) {
            return createAttributeArchObjectMatcher(el);
        } else if (localName.equals(XML_ELEMENT_OR)) {
            return createOrMatcher(el);
        } else if (localName.equals(XML_ELEMENT_AND)) {
            return createAndMatcher(el);
        }
        throw new ParsingException("expected element node name to be one of \"" + XML_ELEMENT_TYPENRS + "\", \"" + XML_ELEMENT_ATTRIB + "\", \"" + XML_ELEMENT_OR + "\", \"" + XML_ELEMENT_AND + "\".");
    }

    /**
     * Creates an {@link AndGameObjectMatcher}.
     * @param el <code>&lt;And/&gt;</code> xml element with matchers to and
     * @return the created matcher
     * @throws ParsingException if a parsing error occurs
     */
    @NotNull
    private static GameObjectMatcher createAndMatcher(@NotNull final Node el) throws ParsingException {
        return new AndGameObjectMatcher(getChildMatchers(el));
    }

    /**
     * Creates an {@link OrGameObjectMatcher}.
     * @param el <code>&lt;Or/&gt;</code> xml element with matchers to or
     * @return the created matcher
     * @throws ParsingException if a parsing error occurs
     */
    @NotNull
    private static GameObjectMatcher createOrMatcher(@NotNull final Node el) throws ParsingException {
        return new OrGameObjectMatcher(getChildMatchers(el));
    }

    /**
     * Gets the matchers that are found as children of an XML element. If the
     * children are {@link GameObjectMatcher GameObjectMatchers} that have child
     * matchers themselves, their elements are scanned recursively.
     * @param el the XML element to look for children
     * @return a <code>Collection</code> with <code>GameObjectMatcher</code>s
     *         that are the children of <code>el</code>
     * @throws ParsingException if a parsing error occurs
     */
    @NotNull
    private static Collection<GameObjectMatcher> getChildMatchers(@NotNull final Node el) throws ParsingException {
        final Collection<GameObjectMatcher> childMatchers = new ArrayList<GameObjectMatcher>();
        final NodeList childNodes = el.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            final Node childNode = childNodes.item(i);
            if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                childMatchers.add(createMatcher((Element) childNode));
            }
        }
        return childMatchers;
    }

    /**
     * Creates an {@link AttributeGameObjectMatcher}.
     * @param el <code>&lt;Attribute/&gt;</code> xml element
     * @return the created matcher
     * @throws ParsingException if the attribute matcher cannot be created
     */
    @NotNull
    private static GameObjectMatcher createAttributeArchObjectMatcher(@NotNull final Element el) throws ParsingException {
        final String type = el.getAttribute(XML_ATTRIB_ATTRIBUTE_TYPE);
        if (type.equals("string")) {
            return new StringAttributeGameObjectMatcher(el.getAttribute(XML_ATTRIB_ATTRIBUTE_NAME), Enum.valueOf(Operation.class, el.getAttribute(XML_ATTRIB_ATTRIBUTE_OP)), el.getAttribute(XML_ATTRIB_ATTRIBUTE_VALUE), Boolean.valueOf(el.getAttribute(XML_ATTRIB_ATTRIBUTE_USEARCHETYPE)));
        } else if (type.equals("int")) {
            return new IntAttributeGameObjectMatcher(el.getAttribute(XML_ATTRIB_ATTRIBUTE_NAME), Enum.valueOf(Operation.class, el.getAttribute(XML_ATTRIB_ATTRIBUTE_OP)), el.getAttribute(XML_ATTRIB_ATTRIBUTE_VALUE), Boolean.valueOf(el.getAttribute(XML_ATTRIB_ATTRIBUTE_USEARCHETYPE)));
        } else {
            throw new AssertionError("impossible value '" + type + "' for XML_ATTRIB_ATTRIBUTE_TYPE");
        }
    }

    /**
     * Creates a {@link TypeNrsGameObjectMatcher}.
     * @param el <code>&lt;TypeNrs/&gt;</code> xml element
     * @return the created matcher
     * @throws ParsingException if a parsing error occurs
     */
    @NotNull
    private static GameObjectMatcher createTypeNrsArchObjectMatcher(@NotNull final Element el) throws ParsingException {
        final CharSequence numbers = el.getAttribute(XML_TYPENRS_ATTRIBUTE_NUMBERS);
        final String[] typeNrs = StringUtils.PATTERN_WHITESPACE.split(numbers, 0);
        final int[] types = new int[typeNrs.length];
        for (int i = 0; i < types.length; i++) {
            try {
                types[i] = Integer.parseInt(typeNrs[i]);
            } catch (final NumberFormatException ignored) {
                //noinspection ThrowInsideCatchBlockWhichIgnoresCaughtException
                throw new ParsingException("invalid number: " + typeNrs[i]);
            }
        }
        return new TypeNrsGameObjectMatcher(types);
    }

    /**
     * Evaluates an XPath expression and returns the result {@link Node}.
     * @param el the element to evaluate against
     * @param xpathExpression the XPath expression to evaluate
     * @return the result node
     */
    @Nullable
    private Node xpathEvaluate(@NotNull final Node el, @NotNull final String xpathExpression) {
        try {
            return (Node) xpath.evaluate(xpathExpression, el, XPathConstants.NODE);
        } catch (final XPathExpressionException ex) {
            throw new AssertionError(ex);
        }
    }

}
