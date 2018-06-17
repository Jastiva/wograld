/*
 * Gridarta MMORPG map editor for Crossfire, Daimonin and similar games.
 * Copyright (C) 2000-2011 The Gridarta Developers.
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

package net.sf.gridarta.plugin.parameter;

import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.filter.FilterParser;
import net.sf.gridarta.model.filter.NamedFilterConfig;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.utils.NumberUtils;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Converts {@link PluginParameter PluginParameters} from or to XML encoding.
 * @author tchize
 * @author Andreas Kirschbaum
 */
public class PluginParameterCodec<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> {

    /**
     * A {@link PluginParameterVisitor} that returns XML representation.
     */
    @NotNull
    private final PluginParameterVisitor<G, A, R, Element> toXML = new PluginParameterVisitor<G, A, R, Element>() {

        /**
         * Converts a generic {@link PluginParameter} to XML encoding.
         * @param parameter the plugin parameter
         * @return the plugin parameter in XML encoding
         */
        @NotNull
        private Element toXML(@NotNull final PluginParameter<G, A, R> parameter) {
            final Element e = new Element("parameter");
            final Element n = new Element("name");
            final Element d = new Element("description");
            final Element t = new Element("type");
            n.addContent(parameter.getName());
            d.addContent(parameter.getDescription());
            t.addContent(parameter.getParameterType());
            e.addContent(n);
            e.addContent(d);
            e.addContent(t);
            return e;
        }

        @NotNull
        @Override
        public Element visit(@NotNull final ArchParameter<G, A, R> parameter) {
            final Element e = toXML(parameter);
            final Element v = new Element("value");
            final Archetype<G, A, R> archetype = parameter.getValue();
            if (archetype != null) {
                v.addContent(archetype.getArchetypeName());
            } else {
                v.addContent(parameter.getValueString());
            }
            e.addContent(v);
            return e;
        }

        @NotNull
        @Override
        public Element visit(@NotNull final BooleanParameter<G, A, R> parameter) {
            final Element e = toXML(parameter);
            final Element v = new Element("value");
            final Boolean value = parameter.getValue();
            v.addContent(value != null ? value.toString() : "");
            e.addContent(v);
            final Element yes = new Element("trueText");
            yes.addContent(parameter.getTrueText());
            final Element no = new Element("falseText");
            no.addContent(parameter.getFalseText());
            e.addContent(yes);
            e.addContent(no);
            return e;
        }

        @NotNull
        @Override
        public Element visit(@NotNull final DoubleParameter<G, A, R> parameter) {
            final Element e = toXML(parameter);
            final Element v = new Element("value");
            final Double value = parameter.getValue();
            v.addContent(value != null ? value.toString() : "");
            e.addContent(v);
            final Element min = new Element("minimum");
            final Element max = new Element("maximum");
            min.addContent(Double.toString(parameter.getMin()));
            max.addContent(Double.toString(parameter.getMax()));
            e.addContent(min);
            e.addContent(max);
            return e;
        }

        @NotNull
        @Override
        public Element visit(@NotNull final FilterParameter<G, A, R> parameter) {
            final Element e = toXML(parameter);
            e.addContent(new FilterParser().toXML(parameter.getValue()));
            return e;
        }

        @NotNull
        @Override
        public Element visit(@NotNull final IntegerParameter<G, A, R> parameter) {
            final Element e = toXML(parameter);
            final Element v = new Element("value");
            final Integer value = parameter.getValue();
            v.addContent(value != null ? value.toString() : "");
            e.addContent(v);
            final Element min = new Element("minimum");
            final Element max = new Element("maximum");
            min.addContent(Integer.toString(parameter.getMin()));
            max.addContent(Integer.toString(parameter.getMax()));
            e.addContent(min);
            e.addContent(max);
            return e;
        }

        @NotNull
        @Override
        public Element visit(@NotNull final MapParameter<G, A, R> parameter) {
            final Element e = toXML(parameter);
            final Element v = new Element("value");
            v.addContent((String) parameter.getValue());
            e.addContent(v);
            return e;
        }

        @NotNull
        @Override
        public Element visit(@NotNull final MapPathParameter<G, A, R> parameter) {
            final Element e = toXML(parameter);
            final Element s = new Element("value");
            final String value = parameter.getValue();
            s.addContent(value != null ? value : "");
            e.addContent(s);
            return e;
        }

        @NotNull
        @Override
        public Element visit(@NotNull final StringParameter<G, A, R> parameter) {
            final Element e = toXML(parameter);
            final Element s = new Element("value");
            final String value = parameter.getValue();
            s.addContent(value != null ? value : "");
            e.addContent(s);
            return e;
        }

    };

    /**
     * A {@link PluginParameterVisitor} that restores a {@link PluginParameter}
     * from XML representation.
     */
    @NotNull
    private final PluginParameterVisitor<G, A, R, PluginParameter<G, A, R>> fromXML = new PluginParameterVisitor<G, A, R, PluginParameter<G, A, R>>() {

        /**
         * Restores generic plugin parameter values.
         * @param parameter the plugin parameter being restored
         */
        public void fromXML(@NotNull final PluginParameter<G, A, R> parameter) {
            assert e != null;
            parameter.setName(e.getChildText("name"));
            assert e != null;
            parameter.setDescription(e.getChildText("description"));
        }

        @NotNull
        @Override
        public PluginParameter<G, A, R> visit(@NotNull final ArchParameter<G, A, R> parameter) {
            fromXML(parameter);
            assert e != null;
            parameter.setStringValue(e.getChildTextTrim("value"));
            return parameter;
        }

        @NotNull
        @Override
        public PluginParameter<G, A, R> visit(@NotNull final BooleanParameter<G, A, R> parameter) {
            fromXML(parameter);
            assert e != null;
            final String val = e.getChildText("value");
            parameter.setValue(Boolean.valueOf(val));
            assert e != null;
            parameter.setTrueText(e.getChildText("trueText"));
            assert e != null;
            parameter.setFalseText(e.getChildText("falseText"));
            return parameter;
        }

        @NotNull
        @Override
        public PluginParameter<G, A, R> visit(@NotNull final DoubleParameter<G, A, R> parameter) {
            fromXML(parameter);
            try {
                parameter.setMin(Double.parseDouble(e.getChildTextTrim("minimum")));
            } catch (final NumberFormatException ignored) {
            }
            try {
                parameter.setMax(Double.parseDouble(e.getChildTextTrim("maximum")));
            } catch (final NumberFormatException ignored) {
            }
            final String val = e.getChildText("value");
            parameter.setValue(NumberUtils.parseDouble(val));
            return parameter;
        }

        @NotNull
        @Override
        public PluginParameter<G, A, R> visit(@NotNull final FilterParameter<G, A, R> parameter) {
            fromXML(parameter);
            final NamedFilterConfig conf = parameter.getValue();
            assert e != null;
            assert e != null;
            new FilterParser().fromXML(e, conf);
            return parameter;
        }

        @NotNull
        @Override
        public PluginParameter<G, A, R> visit(@NotNull final IntegerParameter<G, A, R> parameter) {
            fromXML(parameter);
            final String val = e.getChildText("value");
            try {
                final Integer iVal = Integer.parseInt(val);
                parameter.setValue(iVal);
            } catch (final NumberFormatException ignored) {
                parameter.setValue(0);
            }
            try {
                parameter.setMin(Integer.parseInt(e.getChildTextTrim("minimum")));
            } catch (final NumberFormatException ignored) {
            }
            try {
                parameter.setMax(Integer.parseInt(e.getChildTextTrim("maximum")));
            } catch (final NumberFormatException ignored) {
            }
            return parameter;
        }

        @NotNull
        @Override
        public PluginParameter<G, A, R> visit(@NotNull final MapParameter<G, A, R> parameter) {
            fromXML(parameter);
            assert e != null;
            parameter.setStringValue(e.getChildTextTrim("value"));
            return parameter;
        }

        @NotNull
        @Override
        public PluginParameter<G, A, R> visit(@NotNull final MapPathParameter<G, A, R> parameter) {
            fromXML(parameter);
            assert e != null;
            final String val = e.getChildTextTrim("value");
            parameter.setValue(val);
            return parameter;
        }

        @NotNull
        @Override
        public PluginParameter<G, A, R> visit(@NotNull final StringParameter<G, A, R> parameter) {
            fromXML(parameter);
            assert e != null;
            final String val = e.getChildTextTrim("value");
            parameter.setValue(val);
            return parameter;
        }

    };

    /**
     * Holds the XML representation being decoded.
     */
    @Nullable
    private Element e;

    /**
     * Returns the XML representation of a {@link PluginParameter}.
     * @param parameter the plugin parameter to encode
     * @return the plugin parameter in XML repfresentation
     */
    @NotNull
    public Element toXML(@NotNull final PluginParameter<G, A, R> parameter) {
        return parameter.visit(toXML);
    }

    /**
     * Restores a {@link PluginParameter} from XML representation.
     * @param parameter the plugin parameter to restore
     * @param e the XML representation
     */
    public void fromXML(@NotNull final PluginParameter<G, A, R> parameter, @NotNull final Element e) {
        this.e = e;
        parameter.visit(fromXML);
        this.e = null;
    }

}
