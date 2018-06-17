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

package net.sf.gridarta.plugin;

import java.util.Collection;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.plugin.parameter.NoSuchParameterException;
import net.sf.gridarta.plugin.parameter.PluginParameter;
import net.sf.gridarta.plugin.parameter.PluginParameterFactory;
import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.jdom.CDATA;
import org.jdom.Element;
import org.jdom.IllegalDataException;
import org.jetbrains.annotations.NotNull;

/**
 * Converter for {@link Plugin} instances to or from XML representation.
 * @author tchize
 * @author Andreas Kirschbaum
 */
public class PluginModelParser<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> {

    /**
     * Whether the plugin is in auto-boot mode.
     */
    @NotNull
    private static final String AUTO_BOOT = "autoboot";

    /**
     * Whether the plugin is a filter.
     */
    @NotNull
    private static final String FILTER = "filter";

    /**
     * Whether the plugin is a stand-alone plugin.
     */
    @NotNull
    private static final String BASH = "bash";

    /**
     * The Logger for printing log messages.
     */
    @NotNull
    private static final Category log = Logger.getLogger(PluginModelParser.class);

    /**
     * The {@link PluginParameterFactory} to use.
     */
    @NotNull
    private final PluginParameterFactory<G, A, R> pluginParameterFactory;

    /**
     * Creates a new instance.
     * @param pluginParameterFactory the plugin parameter factory to use
     */
    public PluginModelParser(@NotNull final PluginParameterFactory<G, A, R> pluginParameterFactory) {
        this.pluginParameterFactory = pluginParameterFactory;
    }

    /**
     * Creates a {@link Plugin} instance from XML representation.
     * @param node the XML representation
     * @return the plugin instance
     */
    @NotNull
    public Plugin<G, A, R> fromXML(@NotNull final Element node) {
        final Plugin<G, A, R> pluginModel = new Plugin<G, A, R>(node.getChildTextTrim("name"), pluginParameterFactory);
        pluginModel.setCode(node.getChildTextTrim("code"));
        boolean isAutoBoot = false;
        boolean isFilter = false;
        boolean isScript = false;
        final Element mode = node.getChild("mode");
        if (mode == null) {
            isScript = true;
        } else {
            //Element does not use type parameters
            @SuppressWarnings("unchecked")
            final Iterable<Element> modes = mode.getChildren();
            for (final Element modeChild : modes) {
                final boolean value = Boolean.valueOf(modeChild.getTextTrim());
                final String name = modeChild.getName();
                if (AUTO_BOOT.equalsIgnoreCase(name) && value) {
                    isAutoBoot = true;
                } else if (FILTER.equalsIgnoreCase(name) && value) {
                    isFilter = true;
                } else if (BASH.equalsIgnoreCase(name) && value) {
                    isScript = true;
                }
            }
        }
        pluginModel.setAutoBoot(isAutoBoot);
        pluginModel.setFilter(isFilter);
        pluginModel.setScript(isScript);
        //Element does not use type parameters
        @SuppressWarnings("unchecked")
        final Collection<Element> parameters = node.getChildren("parameter");
        if (parameters != null && !parameters.isEmpty()) {
            for (final Element parameter : parameters) {
                PluginParameter<G, A, R> pluginParameter;
                try {
                    pluginParameter = pluginParameterFactory.createParameter(parameter);
                } catch (final NoSuchParameterException ex) {
                    log.warn("Parameter type " + ex.getMessage() + " in plugin " + pluginModel + " is unknown");
                    pluginParameter = pluginParameterFactory.createStringParameter(parameter);
                }
                pluginModel.addParameter(pluginParameter);
            }
        }
        pluginModel.resetModified();
        return pluginModel;
    }

    /**
     * Converts a {@link Plugin} instance to XML representation.
     * @param plugin the plugin instance
     * @return the XML representation
     */
    @NotNull
    public Element toXML(@NotNull final Plugin<G, A, R> plugin) {
        final Element root = new Element("script");
        final Element name = new Element("name");
        final Element code = new Element("code");
        name.addContent(plugin.getName());
        try {
            code.addContent(new CDATA(plugin.getCode())); // protect code in xml!
        } catch (final IllegalDataException ignored) {
            //can't be converted to CDATA :(
            code.addContent(plugin.getCode());
        }
        root.addContent(name);
        root.addContent(code);
        final Element modes = new Element("mode");

        final Element autoBoot = new Element(AUTO_BOOT);
        autoBoot.addContent(Boolean.toString(plugin.isAutoBoot()));
        final Element bash = new Element(BASH);
        bash.addContent(Boolean.toString(plugin.isScript()));
        final Element filter = new Element(FILTER);
        filter.addContent(Boolean.toString(plugin.isFilter()));
        modes.addContent(autoBoot);
        modes.addContent(bash);
        modes.addContent(filter);

        root.addContent(modes);
        for (final PluginParameter<G, A, R> pluginParameter : plugin) {
            root.addContent(plugin.toXML(pluginParameter));
        }
        return root;
    }
}
