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

package net.sf.gridarta.model.filter;

import java.util.Map.Entry;
import org.jdom.Content;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Converts {@link FilterConfig} to and from XML representation.
 * @author tchize
 * @author Andreas Kirschbaum
 */
public class FilterParser {

    /**
     * The {@link Element} instance being converted.
     */
    @Nullable
    private Element element;

    /**
     * The {@link FilterConfigVisitor} for converting {@link FilterConfig}
     * instances to XML representation.
     */
    @NotNull
    private final FilterConfigVisitor toXMLFilterConfigVisitor = new FilterConfigVisitor() {

        @Override
        public void visit(@NotNull final NamedFilterConfig filterConfig) {
            final Element result = new Element("value");
            final Element enabled = new Element("enabled");
            enabled.addContent(Boolean.toString(filterConfig.isEnabled()));
            result.addContent(enabled);
            final Element inverted = new Element("inverted");
            inverted.addContent(Boolean.toString(filterConfig.isInverted()));
            result.addContent(inverted);
            for (final Entry<String, FilterConfig<?, ?>> entry : filterConfig.getEntries().entrySet()) {
                final String key = entry.getKey();
                final FilterConfig<?, ?> subFilterConfig = entry.getValue();
                final Content filterValue = toXML(subFilterConfig);
                final Element subFilter = new Element("subfilter");
                final Element fName = new Element("name");
                fName.addContent(key);
                subFilter.addContent(fName);
                subFilter.addContent(filterValue);
                result.addContent(subFilter);
            }
            element = result;
        }

        @Override
        public void visit(@NotNull final NamedGameObjectMatcherFilterConfig filterConfig) {
            final Element value = new Element("value");
            final Element enabled = new Element("enabled");
            enabled.addContent(Boolean.toString(filterConfig.isEnabled()));
            value.addContent(enabled);
            for (final String key : filterConfig.getProperties()) {
                final Element property = new Element("property");
                property.addContent(filterConfig.getProperty(key));
                value.addContent(property);
            }
            element = value;
        }

    };

    /**
     * The {@link FilterConfigVisitor} for converting {@link FilterConfig}
     * instances from XML representation.
     */
    @NotNull
    private final FilterConfigVisitor fromXMLFilterConfigVisitor = new FilterConfigVisitor() {

        @Override
        public void visit(@NotNull final NamedFilterConfig filterConfig) {
            filterConfig.getFilter().resetConfig(filterConfig);
            if (element == null) {
                return;
            }

            final Element value = element.getChild("value");
            if (value == null) {
                return;
            }

            final boolean enabled = Boolean.valueOf(value.getChildTextTrim("enabled"));
            filterConfig.setEnabled(enabled);
            filterConfig.setInverted(Boolean.valueOf(value.getChildTextTrim("inverted")));
            //Element does not use type parameters
            @SuppressWarnings("unchecked")
            final Iterable<Element> it = (Iterable<Element>) value.getChildren("subfilter");
            for (final Element filterElement : it) {
                final String name = filterElement.getChildTextTrim("name");
                final FilterConfig<?, ?> subFilterConfig = filterConfig.getConfig(name);
                if (subFilterConfig != null) {
                    fromXML(filterElement, subFilterConfig);
                }
            }
        }

        @Override
        public void visit(@NotNull final NamedGameObjectMatcherFilterConfig filterConfig) {
            if (element == null) {
                return;
            }

            final Element value = element.getChild("value");
            if (value == null) {
                return;
            }

            final boolean enabled = Boolean.valueOf(value.getChildTextTrim("enabled"));
            filterConfig.setEnabled(enabled);
            //Element does not use type parameters
            @SuppressWarnings("unchecked")
            final Iterable<Element> properties = (Iterable<Element>) value.getChildren("property");
            for (final Element property : properties) {
                final String pName = property.getChildTextTrim("name");
                final String pValue = property.getChildTextTrim("value");
                filterConfig.setProperty(pName, pValue);
            }
        }

    };

    /**
     * Export the filter configuration settings.
     * @param filterConfig the filter configuration to export
     * @return the exported settings
     */
    @NotNull
    public Content toXML(@NotNull final FilterConfig<?, ?> filterConfig) {
        final Element prevElement = element;
        try {
            element = null;
            filterConfig.accept(toXMLFilterConfigVisitor);
            assert element != null;
            return element;
        } finally {
            element = prevElement;
        }
    }

    /**
     * Import the filter configuration settings.
     * @param element the settings to import
     * @param filterConfig the filter configuration to modify
     */
    public void fromXML(@NotNull final Element element, @NotNull final FilterConfig<?, ?> filterConfig) {
        final Element prevElement = this.element;
        try {
            this.element = element;
            filterConfig.accept(fromXMLFilterConfigVisitor);
        } finally {
            this.element = prevElement;
        }
    }

}
